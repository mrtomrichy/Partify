//
//  ServerManager.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "ServerManager.h"
#import <AFNetworking/AFNetworking.h>


const NSString *SERVER_ROOT = @"http://partify.harborapp.com";
const NSString *ENDPONT_CREATE = @"create";

@implementation ServerManager
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(createPartyFailureBlock) failureBlock
{
   dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
       successBlock(partyName,@"123456");
   });
    
    return;
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"partyname": partyName};
    [manager POST:[NSString stringWithFormat:@"%@/%@", SERVER_ROOT, ENDPONT_CREATE] parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *eventToken = responseObject[@"token"];
        successBlock(partyName, eventToken);

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        failureBlock(error);
    }];
}

@end
