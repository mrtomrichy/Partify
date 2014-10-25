//
//  SpotifyManager.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SpotifyManager.h"
#import <Spotify/Spotify.h>

// Constants
static NSString * const kClientId = @"ae0aa20d4b694ca38dacb56c2c2298c4";
static NSString * const kCallbackURL = @"partify://";
static NSString * const kSessionUserDefaultsKey = @"spotifysessionkey";

@interface SpotifyManager ()
@property (nonatomic, strong) SPTSession *session;
@property (nonatomic, strong) SPTAudioStreamingController *player;
@end

@implementation SpotifyManager

- (void) doAuth
{
    SPTAuth *auth = [SPTAuth defaultInstance];
    
    NSURL *loginURL = loginURL = [auth loginURLForClientId:kClientId
                         declaredRedirectURL:[NSURL URLWithString:kCallbackURL]
                                      scopes:@[SPTAuthStreamingScope]
                            withResponseType:@"token"];
    
    [[UIApplication sharedApplication] openURL:loginURL];

}

- (BOOL) handleURL:(NSURL *)url
{
    // Ask SPTAuth if the URL given is a Spotify authentication callback
    if ([[SPTAuth defaultInstance] canHandleURL:url withDeclaredRedirectURL:[NSURL URLWithString:kCallbackURL]]) {
        
        // Call the token swap service to get a logged in session
        
        [[SPTAuth defaultInstance]
         handleAuthCallbackWithTriggeredAuthURL:url
         callback:^(NSError *error, SPTSession *session) {
             
             if (error != nil) {
                 NSLog(@"*** Auth error: %@", error);
                 return;
             }
             
             NSData *sessionData = [NSKeyedArchiver archivedDataWithRootObject:session];
             [[NSUserDefaults standardUserDefaults] setObject:sessionData
                                                       forKey:kSessionUserDefaultsKey];
         }];
        return YES;
    }
    return NO;
}
@end
