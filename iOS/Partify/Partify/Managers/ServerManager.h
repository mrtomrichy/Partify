//
//  ServerManager.h
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^createPartySuccessBlock)(NSString *partyName, NSString *partyID);
typedef void(^createPartyFailureBlock)(NSError *error);

@interface ServerManager : NSObject
- (void) createPartyWithName: (NSString *) partyName andSuccessBlock: (createPartySuccessBlock) successBlock andFailureBlock:(createPartyFailureBlock) failureBlock;
@end
