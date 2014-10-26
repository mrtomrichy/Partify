//
//  SearchViewController.m
//  Partify
//
//  Created by Matt Malone on 26/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SearchViewController.h"
#import "AppDelegate.h"
#import "ServerManager.h"
#import "SearchResultsProvider.h"
#import "PartyManager.h"

@interface SearchViewController () <UISearchBarDelegate>
@property (weak, nonatomic) IBOutlet UITableView *resultsTableView;
@property (weak, nonatomic) IBOutlet UITextField *searchField;
@property (weak, nonatomic) AppDelegate *appD;
@property (weak, nonatomic) ServerManager *serverManager;
@property (weak, nonatomic) PartyManager *partyManager;

@property (strong, nonatomic) SearchResultsProvider *searchProvider;

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.appD = [UIApplication sharedApplication].delegate;
    self.serverManager = self.appD.serverManager;
    self.searchProvider = [SearchResultsProvider new];
    self.partyManager = self.appD.partyManager;
    __weak SearchViewController * wSelf = self;
    self.searchProvider.selectedItemBlock = ^(NSDictionary * item) {
      [wSelf.serverManager addSong:item[@"id"] andPartyID:wSelf.partyManager.partyID andSuccessBlock:^{
          [wSelf.presentingViewController dismissViewControllerAnimated:YES completion:NULL];
      } andFailure:^(NSError *error) {
          
      }];
    };
    self.resultsTableView.delegate = self.searchProvider;
    self.resultsTableView.dataSource = self.searchProvider;
}

- (IBAction)searchPressed:(id)sender {
    NSString *searchQuery = self.searchField.text;
    [self.serverManager searchForString:searchQuery withSuccessBlock:^(NSArray *results) {
        self.searchProvider.results = results;
        [self.resultsTableView reloadData];
        
    } andFailureBlock:^(NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"Error" message:[error description] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
    }];
}
- (IBAction)closePressed:(id)sender {
    [self.presentingViewController dismissViewControllerAnimated:YES completion:NULL];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
