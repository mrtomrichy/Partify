//
//  SearchResultsProvider.h
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void(^selectedItemBlock)(NSDictionary *item);

@interface SearchResultsProvider : NSObject <UITableViewDataSource, UITableViewDelegate>
@property (nonatomic, strong) NSArray *results;
@property (copy) selectedItemBlock selectedItemBlock;
@end
