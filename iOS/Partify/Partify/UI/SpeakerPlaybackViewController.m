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


@interface SpeakerPlaybackViewController () <SPTAudioStreamingPlaybackDelegate>
@property (weak, nonatomic) IBOutlet UILabel *partyCodeLabel;
@property (weak, nonatomic) IBOutlet UIImageView *coverArtView;
@property (nonatomic, strong) SpotifyManager *spotifyManager;
@property (nonatomic, weak) AppDelegate *appD;
@property (weak, nonatomic) IBOutlet UIButton *playButton;
@property (weak, nonatomic) IBOutlet UISlider *seekBar;
@property (nonatomic, strong) SPTAudioStreamingController *player;
@property (nonatomic, strong) NSTimer *seekTimer;
@property (nonatomic) BOOL seekInProgress;
@end

@implementation SpeakerPlaybackViewController


- (void) viewDidLoad
{
    [super viewDidLoad];
    self.appD = [UIApplication sharedApplication].delegate;
    self.spotifyManager = self.appD.spotifyManager;
    [self playTrack];
}
- (IBAction)playPressed:(id)sender {
    [self.player setIsPlaying:!self.player.isPlaying callback:NULL];
}

- (IBAction)seekStarted:(id)sender {
    self.seekInProgress = YES;
}

- (IBAction)seekEnded:(id)sender {
    self.seekInProgress = NO;
}

- (IBAction)seekBarDidSeek:(id)sender {
    UISlider *seekBar = sender;
    NSTimeInterval trackLength = [self.player.currentTrackMetadata[SPTAudioStreamingMetadataTrackDuration] doubleValue];
    
    NSTimeInterval newTimeValue = trackLength * seekBar.value;
    [self.player seekToOffset:newTimeValue callback:NULL];
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

-(void)playTrack {
    
    if (self.player == nil) {
        self.player = [SPTAudioStreamingController new];
        self.player.playbackDelegate = self;
    }
    
    [self.player loginWithSession:self.spotifyManager.currentSession callback:^(NSError *error) {
        
        if (error != nil) {
            NSLog(@"*** Enabling playback got error: %@", error);
            return;
        }
        
        [SPTRequest requestItemAtURI:[NSURL URLWithString:@"spotify:track:1Vv0MPcooEoQzVZYfKMgKW"]
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
    NSLog(@"Change");
}
@end
