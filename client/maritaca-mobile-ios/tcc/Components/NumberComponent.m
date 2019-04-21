//
//  NumberComponent.m
//  tcc
//
//  Created by Marcela Tonon on 17/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "NumberComponent.h"

@interface NumberComponent ()
@property (nonatomic) bool isNumber;
@property (nonatomic, strong) UILabel *warningLabel;
@end

@implementation NumberComponent

@synthesize numberComponent = _numberComponent;
@synthesize warningLabel = _warningLabel;
@synthesize isNumber = _isNumber;
@synthesize min = _min;
@synthesize max = _max;

- (NSString *)getAnswerComponent {
    NSString *answer = @"";
    if([self.numberComponent.text isEqualToString:@""] || !self.numberComponent.text) {
        answer = @"null";
    } else {
        answer = self.numberComponent.text;
    }
    [super writeToXML:answer];
    return answer;
}


- (bool) validatedAnswer {
    bool valid = NO;
    if(!self.isNumber) {
        [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"You can't go to next question while the number is invalid." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
        valid = NO;
    } else {
        valid = [super validatedAnswer];
    }
    return valid;
}

- (void) checkNumber {
    self.isNumber = [super checkNumber:self.numberComponent.text];
    if(!self.isNumber) {
        self.warningLabel.text = @"It is not a valid number.";
    } else {
        self.warningLabel.text = @"";
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.numberComponent.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, 200, 40);
    self.numberComponent.borderStyle = UITextBorderStyleRoundedRect;
    self.numberComponent.font = [UIFont systemFontOfSize:15];
    self.numberComponent.autocorrectionType = UITextAutocorrectionTypeNo;
    self.numberComponent.clearButtonMode = UITextFieldViewModeWhileEditing;
    self.numberComponent.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    self.numberComponent.text = self.defaultQuestion;
    self.numberComponent.delegate = self;
    self.numberComponent.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    self.isNumber = YES;
    
    self.warningLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.numberComponent.frame.origin.y + self.numberComponent.frame.size.height + 20, 300, 40)];
    self.warningLabel.backgroundColor = [UIColor clearColor];
    self.warningLabel.font = [UIFont systemFontOfSize:13];

    [self.numberComponent addTarget:self action:@selector(checkNumber) forControlEvents:UIControlEventEditingChanged];
    [self.view addSubview:self.numberComponent];
    [self.view addSubview:self.warningLabel];
}

@end
