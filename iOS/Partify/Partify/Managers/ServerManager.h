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
typedef void (^votePlaylistSuccessBlock)(NSArray * newPlaylist);
typedef void (^joinSuccessBlock)(NSString * userToken);
typedef void (^searchSuccessBlock)(NSArray * results);
typedef void (^addSongSuccessBlock)();
typedef void (^songDetailsSuccessBlock)(NSDictionary *songDetail);

@interface ServerManager : NSObject
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(requestFailureBlock) failureBlock;

- (void) updatePlaylistWithPartyID: (NSString *) partyID andSuccessBlock: (updatePlaylistSuccessBlock) successBlock andFailureBlock: (requestFailureBlock) failureBlock;

- (void) voteForSong: (NSString *) songID withPartyID: (NSString *) partyID andSuccessBlock: (votePlaylistSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock;

- (void) joinParty: (NSString *) partyID withSuccessBlock: (joinSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock;

- (void) searchForString: (NSString *)searchString withSuccessBlock:(searchSuccessBlock)successBlock andFailureBlock:(requestFailureBlock)failureBlock;

- (void) addSong: (NSString *) songID andPartyID: (NSString *) partyID andSuccessBlock: (addSongSuccessBlock) successBlock andFailure: (requestFailureBlock) failureBlock;

- (void) lookupSongID: (NSString *) songID withSuccessBlock: (songDetailsSuccessBlock) successBlock andFailureBlock: (requestFailureBlock) failureBlock;
@end
