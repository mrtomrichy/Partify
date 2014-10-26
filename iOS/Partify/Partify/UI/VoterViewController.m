//
//  VoterViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "VoterViewController.h"
#import "AppDelegate.h"
#import "ServerManager.h"
#import "PartyManager.h"

@interface VoterViewController () <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *codeField;
@property (weak, nonatomic) IBOutlet UIButton *goButton;
@property (weak, nonatomic) AppDelegate *appD;
@property (weak, nonatomic) ServerManager *serverManager;
@property (weak, nonatomic) IBOutlet UIView *progressView;
@property (weak, nonatomic) PartyManager *partyManager;

@end

@implementation VoterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.appD = [UIApplication sharedApplication].delegate;
    self.serverManager = self.appD.serverManager;
    self.partyManager = self.appD.partyManager;
    
    [self.codeField addTarget:self
                            action:@selector(textFieldDidChange:)
                  forControlEvents:UIControlEventEditingChanged];
}

- (void) textFieldDidChange: (UITextField *) textField
{
    self.goButton.enabled = textField.text.length > 0;
}

- (IBAction)goButtonPressed:(id)sender {
    [self showProgress];
    NSString *codeName = self.codeField.text;
    [self.serverManager joinParty:codeName withSuccessBlock:^(NSString *userToken) {
        self.partyManager.partyID = codeName;
        [self.appD switchToVoterPlayback];
    } andFailureBlock:^(NSError *error) {
        [self hideProgress];
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"Error" message:[error description] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
    } ];
     

}

- (void) showProgress
{
    self.progressView.alpha = 0.0f;
    self.progressView.hidden = NO;
    [UIView animateWithDuration:0.3f animations:^{
        self.progressView.alpha = 0.7f;
    }];
}

- (void) hideProgress
{
    [UIView animateWithDuration:0.3f animations:^{
        self.progressView.alpha = 0.0f;
    } completion:^(BOOL finished) {
        self.progressView.hidden = YES;
    }];
}
@end
