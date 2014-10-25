//
//  AppDelegate.h
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SpotifyManager;
@class ServerManager;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (readonly, nonatomic) SpotifyManager *spotifyManager;
@property (readonly, nonatomic) ServerManager *serverManager;
@end

