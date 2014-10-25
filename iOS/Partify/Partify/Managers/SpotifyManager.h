//
//  SpotifyManager.h
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SpotifyManager : NSObject
- (void) doAuth;
- (BOOL) handleURL: (NSURL *)url;
@end
