//
//  SearchResultsProvider.m
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SearchResultsProvider.h"

@implementation SearchResultsProvider
- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"rrererere"];
    cell.textLabel.text = self.results[indexPath.row][@"name"];
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
