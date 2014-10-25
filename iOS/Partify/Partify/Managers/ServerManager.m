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
const NSString *ENDPONT_CREATE = @"Party/Create";

@implementation ServerManager
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(createPartyFailureBlock) failureBlock
{
    // TODO Url encode the party name
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:[NSString stringWithFormat:@"%@/%@?partyName=%@", SERVER_ROOT, ENDPONT_CREATE, partyName] parameters:NULL success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *eventToken = responseObject[@"token"];
        successBlock(partyName, eventToken);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}
@end
