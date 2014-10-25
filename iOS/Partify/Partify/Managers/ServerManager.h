//
//  ServerManager.h
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^createPartySuccessBlock)(NSString *partyName, NSString *partyID);
typedef void(^requestFailureBlock)(NSError *error);

typedef void (^updatePlaylistSuccessBlock)(NSArray *newPlaylist);
typedef void (^votePlaylistSuccessBlock)();
typedef void (^joinSuccessBlock)(NSString * userToken);

@interface ServerManager : NSObject
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(requestFailureBlock) failureBlock;

- (void) updatePlaylistWithPartyID: (NSString *) partyID andSuccessBlock: (updatePlaylistSuccessBlock) successBlock andFailureBlock: (requestFailureBlock) failureBlock;

- (void) voteForSong: (NSString *) songID withPartyID: (NSString *) partyID andSuccessBlock: (votePlaylistSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock;

- (void) joinParty: (NSString *) partyID withSuccessBlock: (joinSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock;
@end
