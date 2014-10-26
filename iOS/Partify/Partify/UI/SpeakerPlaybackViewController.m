//
//  SpeakerPlaybackViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SpeakerPlaybackViewController.h"
#import <Spotify/Spotify.h>
#import "SpotifyManager.h"
#import "AppDelegate.h"
#import "PartyManager.h"
#import "ServerManager.h"

@interface SpeakerPlaybackViewController () <SPTAudioStreamingPlaybackDelegate>
@property (weak, nonatomic) IBOutlet UILabel *partyCodeLabel;
@property (weak, nonatomic) IBOutlet UIImageView *coverArtView;
@property (nonatomic, weak) SpotifyManager *spotifyManager;
@property (nonatomic, weak) AppDelegate *appD;
@property (nonatomic, weak) ServerManager * serverManager;
@property (weak, nonatomic) IBOutlet UIButton *playButton;
@property (weak, nonatomic) IBOutlet UISlider *seekBar;
@property (nonatomic, strong) SPTAudioStreamingController *player;
@property (nonatomic, strong) NSTimer *seekTimer;
@property (nonatomic) BOOL seekInProgress;
@property (nonatomic, weak) PartyManager *partyManager;
@property (nonatomic, strong) NSTimer *pollingTimer;
@property (nonatomic, strong) NSString *currentTrack;
@end

@implementation SpeakerPlaybackViewController


- (void) viewDidLoad
{
    [super viewDidLoad];
    self.appD = [UIApplication sharedApplication].delegate;
    self.spotifyManager = self.appD.spotifyManager;
    self.serverManager = self.appD.serverManager;
    self.partyManager = self.appD.partyManager;
    self.partyCodeLabel.text = [NSString stringWithFormat:@"%@-%@", self.partyManager.partyID, self.partyManager.partyName];
    
    
    self.pollingTimer = [NSTimer scheduledTimerWithTimeInterval:5.0f target:self selector:@selector(pollTimerFired:) userInfo:NULL repeats:YES];
    //[self playTrack];
}

- (void) pollTimerFired: (NSTimer *) timer
{
    NSArray *playlist = self.partyManager.playlist;
    if (playlist.count > 0 && self.currentTrack == nil) {
        [self playNextTrack];
    }
    
    [self updatePlaylist];
}

- (void) playNextTrack
{
    NSArray *playlist = self.partyManager.playlist;
    NSInteger currentIdx = [playlist indexOfObject:self.currentTrack];
    NSInteger nextIdx = currentIdx != NSNotFound ? currentIdx + 1 : 0;
    NSString *nextTrack = playlist[nextIdx];
    if (nextTrack)
    {
        self.currentTrack = nextTrack;
        [self playTrackWithID:nextTrack];
    }
}

- (void) updatePlaylist
{
    [self.serverManager updatePlaylistWithPartyID:self.partyManager.partyID andSuccessBlock:^(NSArray *newPlaylist) {
        self.partyManager.playlist = newPlaylist;
    } andFailureBlock:nil];
}

- (IBAction)playPressed:(id)sender {
    [self.player setIsPlaying:!self.player.isPlaying callback:NULL];
}

- (IBAction)seekStarted:(id)sender {
    self.seekInProgress = YES;
    [self.player setIsPlaying:NO callback:NULL];

}

- (IBAction)seekEnded:(id)sender {
    self.seekInProgress = NO;
    UISlider *seekBar = sender;
    NSTimeInterval trackLength = [self.player.currentTrackMetadata[SPTAudioStreamingMetadataTrackDuration] doubleValue];
    
    NSTimeInterval newTimeValue = trackLength * seekBar.value;
    [self.player seekToOffset:newTimeValue callback:NULL];
    [self.player setIsPlaying:YES callback:NULL];

}

- (IBAction)seekBarDidSeek:(id)sender {
    
}

- (void) seekTimerFired: (NSTimer *) timer
{
    if (self.seekInProgress) {
        return;
    }
    NSTimeInterval trackLength = [self.player.currentTrackMetadata[SPTAudioStreamingMetadataTrackDuration] doubleValue];
    double newSeekValue = self.player.currentPlaybackPosition / trackLength;
    
    self.seekBar.value = newSeekValue;
}

-(void)playTrackWithID: (NSString *)trackID {
    
    if (self.player == nil) {
        self.player = [SPTAudioStreamingController new];
        self.player.playbackDelegate = self;
    }
    
    [self.player loginWithSession:self.spotifyManager.currentSession callback:^(NSError *error) {
        
        if (error != nil) {
            NSLog(@"*** Enabling playback got error: %@", error);
            return;
        }
        
        [SPTRequest requestItemAtURI:[NSURL URLWithString:[@"spotify:track:" stringByAppendingString:trackID]]
                         withSession:self.spotifyManager.currentSession
                            callback:^(NSError *error, id object) {
                                
                                if (error != nil) {
                                    NSLog(@"*** Album lookup got error %@", error);
                                    return;
                                }
                                
                                [self.player playTrackProvider:(id <SPTTrackProvider>)object callback:nil];
                                
                            }];
    }];
}

#pragma mark - Track Player Delegates
-(void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didChangePlaybackStatus:(BOOL)isPlaying
{
    [self.playButton setTitle:(isPlaying ? @"Pause" : @"Play") forState:UIControlStateNormal];
    
    if (isPlaying) {
        self.seekTimer = [NSTimer scheduledTimerWithTimeInterval:0.1f target:self selector:@selector(seekTimerFired:) userInfo:NULL repeats:YES];
    }
    else {
        if (self.player.currentPlaybackPosition == [self.player.currentTrackMetadata[SPTAudioStreamingMetadataTrackDuration] doubleValue]) {
            [self playNextTrack];
        }
        [self.seekTimer invalidate];
    }
}
- (void)audioStreaming:(SPTAudioStreamingController *)audioStreaming didReceiveMessage:(NSString *)message {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Message from Spotify"
                                                        message:message
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
    [alertView show];
}

- (void) audioStreaming:(SPTAudioStreamingController *)audioStreaming didChangeToTrack:(NSDictionary *)trackMetadata {

}
@end
