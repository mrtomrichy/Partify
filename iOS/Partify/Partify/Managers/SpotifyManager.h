//
//  SpotifyManager.h
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@class SPTSession;

@interface SpotifyManager : NSObject
@property (nonatomic, strong) SPTSession *currentSession;
- (void) doAuth;
- (BOOL) handleURL: (NSURL *)url;
@end
