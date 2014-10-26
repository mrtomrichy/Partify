//
//  SearchResultsProvider.m
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SearchResultsProvider.h"
#import "AppDelegate.h"
#import "ServerManager.h"

@interface SearchResultsProvider ()
@property (nonatomic, weak) AppDelegate *appD;
@property (nonatomic, weak) ServerManager *serverManager;
@end

@implementation SearchResultsProvider

- (instancetype) init
{
    if ((self = [super init]))
    {
        _appD = [UIApplication sharedApplication].delegate;
        _serverManager = _appD.serverManager;
    }
    return self;
}
- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"rrererere"];
    cell.backgroundColor = [UIColor colorWithRed:82.0f / 255.0f green:82.0f / 255.0f blue:82.0f / 255.0f alpha:1.0f];
    
    [self.serverManager lookupSongID:self.results[indexPath.row][@"id"] withSuccessBlock:^(NSDictionary *songDetail) {
        cell.textLabel.text = songDetail[@"name"];
        cell.detailTextLabel.text = songDetail[@"artists"][0][@"name"];
    } andFailureBlock:NULL];
    
    
    return cell;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.results.count;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.selectedItemBlock) {
        self.selectedItemBlock(self.results[indexPath.row]);
    }
}
@end
