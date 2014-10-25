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

@interface AppDelegate ()
@property (strong, nonatomic) SpotifyManager *spotifyManager;
@property (strong, nonatomic) ServerManager *serverManager;
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    self.spotifyManager = [SpotifyManager new];
    self.serverManager = [ServerManager new];
    return YES;
}


// Handle auth callback
-(BOOL)application:(UIApplication *)application
           openURL:(NSURL *)url
 sourceApplication:(NSString *)sourceApplication
        annotation:(id)annotation {
    
    
    return [self.spotifyManager handleURL:url];
}

@end
