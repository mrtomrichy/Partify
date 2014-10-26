//
//  PlaylistProvider.m
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "PlaylistProvider.h"

@interface PlaylistProvider ()

@end

@implementation PlaylistProvider
- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"rrererere"];
    cell.textLabel.text = self.playlist[indexPath.row];
    return cell;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.playlist.count;
}
@end
