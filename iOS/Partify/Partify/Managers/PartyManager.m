//
//  PartyManager.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "PartyManager.h"
#import "ServerManager.h"
#import "AppDelegate.h"

@interface PartyManager ()
@property (nonatomic, weak) ServerManager *serverManager;
@property (nonatomic, weak) AppDelegate *appD;
@end

@implementation PartyManager

- (instancetype) init
{
    if ((self = [super init]))
    {
        _appD = [UIApplication sharedApplication].delegate;
        _serverManager = _appD.serverManager;
    }
    return self;
}

- (void) updatePlaylist
{
    
}

@end
