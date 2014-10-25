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
@property (nonatomic, strong) SpotifyManager *spotifyManager;
@property (nonatomic, weak) AppDelegate *appD;
@property (nonatomic, strong) SPTAudioStreamingController *player;
@end

@implementation SpeakerPlaybackViewController


- (void) viewDidLoad
{
    [super viewDidLoad];
    
    self.appD = [UIApplication sharedApplication].delegate;
    self.spotifyManager = self.appD.spotifyManager;
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
        
        [SPTRequest requestItemAtURI:[NSURL URLWithString:@"spotify:album:2Z51EnLF4Nps4LmulSQPnn"]
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
