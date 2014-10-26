//
//  PlaylistProvider.h
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface PlaylistProvider : NSObject  <UITableViewDelegate,UITableViewDataSource>
@property (nonatomic, strong) NSArray *playlist;
@end
