//
//  SpeakerViewController.m
//  Partify
//
//  Created by Matt Malone on 25/10/2014.
//  Copyright (c) 2014 Apadmi Ltd. All rights reserved.
//

#import "SpeakerViewController.h"

@interface SpeakerViewController () <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIButton *goButton;
@property (weak, nonatomic) IBOutlet UITextField *eventNameField;
@property (weak, nonatomic) IBOutlet UIView *progressView;

@end

@implementation SpeakerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
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
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
        [self hideProgress];
    });
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
