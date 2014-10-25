//
//  AppDelegate.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "AppDelegate.h"
#import "SpotifyManager.h"
#import "ServerManager.h"
#import "PartyManager.h"

@interface AppDelegate ()
@property (strong, nonatomic) SpotifyManager *spotifyManager;
@property (strong, nonatomic) ServerManager *serverManager;
@property (strong, nonatomic) PartyManager *partyManager;

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    self.spotifyManager = [SpotifyManager new];
    self.serverManager = [ServerManager new];
    self.partyManager = [PartyManager new];
    return YES;
}


// Handle auth callback
-(BOOL)application:(UIApplication *)application
           openURL:(NSURL *)url
 sourceApplication:(NSString *)sourceApplication
        annotation:(id)annotation {
    
    
    return [self.spotifyManager handleURL:url];
}

- (void) switchToSpeakerPlayback
{
    if ([NSThread isMainThread])
    {
        UIStoryboard *storyBoard = self.window.rootViewController.storyboard;
        UIViewController *vc = [storyBoard instantiateViewControllerWithIdentifier:@"speakerPlayback"];
        self.window.rootViewController = vc;
    }
    else {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self switchToSpeakerPlayback];
        });
    }
    
}
- (void) switchToVoterPlayback
{
    if ([NSThread isMainThread])
    {
        UIStoryboard *storyBoard = self.window.rootViewController.storyboard;
        UIViewController *vc = [storyBoard instantiateViewControllerWithIdentifier:@"voterPlayback"];
        self.window.rootViewController = vc;
    }
    else {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self switchToVoterPlayback];
        });
    }

}
@end
