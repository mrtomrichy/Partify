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
const NSString *ENDPOINT_PLAYLIST = @"Party/Playlist";
const NSString *ENDPOINT_VOTE = @"Party/Vote";
const NSString *ENDPOINT_JOIN = @"Party/Join";

@implementation ServerManager
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(requestFailureBlock) failureBlock
{
    // TODO Url encode the party name
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
//    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
//    [manager POST:[NSString stringWithFormat:@"%@/%@?partyName=%@", SERVER_ROOT, ENDPONT_CREATE, partyName] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        NSString *eventToken = responseObject[@"token"];
//        successBlock(partyName, eventToken);
//    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        failureBlock(error);
//    }];
}
- (void) voteForSong: (NSString *) songID withPartyID: (NSString *) partyID andSuccessBlock: (votePlaylistSuccessBlock)successBlock andFailureBlock:(requestFailureBlock) failureBlock
{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?songId=%@&partyId=%@", SERVER_ROOT, ENDPOINT_VOTE, [songID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],[partyID stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        successBlock();
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
@end
