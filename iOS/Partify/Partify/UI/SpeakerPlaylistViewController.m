//
//  SpearkerPlaylistViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SpeakerPlaylistViewController.h"
#import "PlaylistProvider.h"
#import "PlaylistProvider.h"
#import "AppDelegate.h"
#import "ServerManager.h"
#import "PartyManager.h"

@interface SpeakerPlaylistViewController ()
@property (weak, nonatomic) IBOutlet UITableView *playlistTableView;
@property (nonatomic, strong) PlaylistProvider *playlistProvider;
@property (nonatomic, weak) AppDelegate *appD;
@property (nonatomic, weak) ServerManager *serverManager;
@property (nonatomic, weak) PartyManager *partyManager;
@property (nonatomic, strong) NSTimer *timer;
@end

@implementation SpeakerPlaylistViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.appD = [UIApplication sharedApplication].delegate;
    self.serverManager = self.appD.serverManager;
    self.partyManager = self.appD.partyManager;
    
    self.playlistProvider = [PlaylistProvider new];
    self.playlistTableView.delegate = self.playlistProvider;
    self.playlistTableView.dataSource = self.playlistProvider;
    
    self.timer = [NSTimer scheduledTimerWithTimeInterval:5.0f target:self selector:@selector(updateFired:) userInfo:nil repeats:YES];
    
    [self loadPlaylist];
    
}

- (void) updateFired: (NSTimer *) timer
{
    [self loadPlaylist];
}

- (void) loadPlaylist
{
    [self.serverManager updatePlaylistWithPartyID:self.partyManager.partyID andSuccessBlock:^(NSArray *newPlaylist) {
        self.partyManager.playlist = newPlaylist;
        self.playlistProvider.playlist = newPlaylist;
        [self.playlistTableView     reloadData];
    } andFailureBlock:^(NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"Error" message:[error description] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
    }];
}

- (IBAction)closePressed:(id)sender {
    [self.timer invalidate];
    [self.presentingViewController dismissViewControllerAnimated:YES completion:NULL];
}


@end
