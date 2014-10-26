//
//  ServerManager.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "ServerManager.h"
#import <AFNetworking/AFNetworking.h>


const NSString *SERVER_ROOT = @"http://partify.apphb.com/api/";
const NSString *ENDPOINT_CREATE = @"Party/Create";
const NSString *ENDPOINT_PLAYLIST = @"Party/GetPlaylist";
const NSString *ENDPOINT_VOTE = @"Party/Vote";
const NSString *ENDPOINT_JOIN = @"Party/Join";
const NSString *ENDPOINT_ADD = @"Party/Add";
const NSString *ENDPOINT_DETAILS = @"https://api.spotify.com/v1/tracks/";
NSString * const ENDPOINT_SEARCH = @"https://api.spotify.com/v1/search?q=%@&type=track";

@implementation ServerManager
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?partyName=%@", SERVER_ROOT, ENDPOINT_CREATE, [partyName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *eventToken = responseObject[@"PartyCode"];
        successBlock(partyName, eventToken);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

- (void) updatePlaylistWithPartyID: (NSString *) partyID andSuccessBlock: (updatePlaylistSuccessBlock) successBlock andFailureBlock: (requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager GET:[NSString stringWithFormat:@"%@/%@?partyCodePlaylist=%@", SERVER_ROOT, ENDPOINT_PLAYLIST, partyID] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if (((NSDictionary *)responseObject).count == 0) {
            successBlock(@[]);
        }
        else {
            successBlock(responseObject[@"SpotifyIds"]);
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}
- (void) voteForSong: (NSString *) songID withPartyID: (NSString *) partyID andSuccessBlock: (votePlaylistSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?songId=%@&partyCode=%@", SERVER_ROOT, ENDPOINT_VOTE, [songID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],[partyID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock(responseObject[@"SongIds"]);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

- (void) joinParty: (NSString *) partyID withSuccessBlock: (joinSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?partyCode=%@", SERVER_ROOT, ENDPOINT_JOIN, [partyID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock(responseObject[@"AttendeeId"]);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

- (void) searchForString: (NSString *)searchString withSuccessBlock:(searchSuccessBlock)successBlock andFailureBlock:(requestFailureBlock)failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager GET:[NSString stringWithFormat:ENDPOINT_SEARCH, [searchString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock(responseObject[@"tracks"][@"items"]);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

- (void) addSong: (NSString *) songID andPartyID: (NSString *) partyID andSuccessBlock: (addSongSuccessBlock) successBlock andFailure: (requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?songId=%@&partyId=%@", SERVER_ROOT, ENDPOINT_ADD, songID, partyID] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock();
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

- (void) lookupSongID: (NSString *) songID withSuccessBlock: (songDetailsSuccessBlock) successBlock andFailureBlock: (requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager GET:[ENDPOINT_DETAILS stringByAppendingString:songID] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock(responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}
@end
