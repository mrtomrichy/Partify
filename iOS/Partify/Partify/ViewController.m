//
//  ViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "ViewController.h"
#import "SpotifyManager.h"

@interface ViewController ()
@property (nonatomic, strong) SpotifyManager *spotifyManager;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.spotifyManager = [SpotifyManager new];
}
- (IBAction)loginPressed:(id)sender {
    [self.spotifyManager doAuth];
}

- (IBAction)playPressed:(id)sender {
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
