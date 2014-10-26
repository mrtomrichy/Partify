//
//  VoterPlaybackViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "VoterPlaybackViewController.h"
#import "PlaylistProvider.h"
#import "AppDelegate.h"
#import "ServerManager.h"
#import "PartyManager.h"

@interface VoterPlaybackViewController ()
@property (weak, nonatomic) IBOutlet UITableView *playlistTable;
@property (nonatomic, strong) PlaylistProvider *playlistProvider;
@property (nonatomic, weak) AppDelegate *appD;
@property (nonatomic, weak) ServerManager *serverManager;
@property (nonatomic, weak) PartyManager *partyManager;
@end

@implementation VoterPlaybackViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.appD = [UIApplication sharedApplication].delegate;
    self.serverManager = self.appD.serverManager;
    self.partyManager = self.appD.partyManager;
    
    self.playlistProvider = [PlaylistProvider new];
    self.playlistTable.delegate = self.playlistProvider;
    self.playlistTable.dataSource = self.playlistProvider;
    
    __weak VoterPlaybackViewController *wSelf = self;
    self.playlistProvider.playlistItemSelectedBlock = ^(NSString * item)
    {
        [wSelf.serverManager voteForSong:item withPartyID:wSelf.partyManager.partyID andSuccessBlock:^(NSArray *fleh){
            wSelf.playlistProvider.playlist = fleh;
            [wSelf.playlistTable reloadData];
            //[wSelf reload];
        } andFailureBlock:^(NSError *error) {
            
        }];
    };
    
}

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self reload];
}

- (void) reload
{
    [self.serverManager updatePlaylistWithPartyID:self.partyManager.partyID andSuccessBlock:^(NSArray *newPlaylist) {
        self.playlistProvider.playlist = newPlaylist;
        self.partyManager.playlist = newPlaylist;
        [self.playlistTable reloadData];
    } andFailureBlock:^(NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"Error" message:[error description] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
    }];
}

@end
