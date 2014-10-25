//
//  SpeakerViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SpeakerViewController.h"
#import "AppDelegate.h"
#import "ServerManager.h"

@interface SpeakerViewController () <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIButton *goButton;
@property (weak, nonatomic) IBOutlet UITextField *eventNameField;
@property (weak, nonatomic) IBOutlet UIView *progressView;
@property (weak, nonatomic) AppDelegate *appD;
@property (weak, nonatomic) ServerManager *serverManager;
@end

@implementation SpeakerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.appD = [UIApplication sharedApplication].delegate;
    self.serverManager = self.appD.serverManager;
    
    [self.eventNameField addTarget:self
                            action:@selector(textFieldDidChange:)
                  forControlEvents:UIControlEventEditingChanged];
}

- (void) textFieldDidChange: (UITextField *) textField
{
    self.goButton.enabled = textField.text.length > 0;
}

- (IBAction)goPressed:(id)sender
{
    [self showProgress];
    
    NSString *partyName = self.eventNameField.text;
    [self.serverManager createPartyWithName:partyName andSuccessBlock:^(NSString *partyName, NSString *partyID) {
        
    } andFailureBlock:^(NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [[[UIAlertView alloc] initWithTitle:@"Error" message:[error description] delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        });
    }];
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
