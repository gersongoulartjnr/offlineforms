//
//  MoneyComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "MoneyComponent.h"

@interface MoneyComponent ()
@property (nonatomic) bool isNumber;
@property (nonatomic, strong) UILabel *warningLabel;
@end


@implementation MoneyComponent

@synthesize moneyComponent = _moneyComponent;
@synthesize min = _min;
@synthesize max = _max;
@synthesize currency = _currency;
@synthesize warningLabel = _warningLabel;
@synthesize isNumber = _isNumber;

- (NSString *)getAnswerComponent {
    NSString *answer = @"";
    if([self.moneyComponent.text isEqualToString:@""] || !self.moneyComponent.text) {
        answer = @"null";
    } else {
        answer = self.moneyComponent.text;
    }
    [super writeToXML:answer];
    return answer;
}

- (void) checkNumber {
    self.isNumber = [super checkNumber:self.moneyComponent.text];
    if(!self.isNumber) {
        self.warningLabel.text = @"It is not a valid number.";
    } else {
        self.warningLabel.text = @"";
    }
}

- (bool) validatedAnswer {
    bool valid = NO;
    if(!self.isNumber) {
        [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"You can't go to next question while the number is invalid." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
            valid = NO;
    } else {
        valid = YES;
        [super validatedAnswer];
    }
    return valid;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.moneyComponent.frame = CGRectMake(51, self.label.frame.origin.y + self.label.frame.size.height + 20, 200, 40);
    self.moneyComponent.borderStyle = UITextBorderStyleRoundedRect;
    self.moneyComponent.font = [UIFont systemFontOfSize:15];
    self.moneyComponent.autocorrectionType = UITextAutocorrectionTypeNo;
    self.moneyComponent.clearButtonMode = UITextFieldViewModeWhileEditing;
    self.moneyComponent.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    self.moneyComponent.text = self.defaultQuestion;
    self.moneyComponent.delegate = self;
    self.moneyComponent.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    [self.moneyComponent addTarget:self action:@selector(checkNumber) forControlEvents:UIControlEventEditingChanged];
    
    UILabel *labelCurrency = [[UILabel alloc] initWithFrame:CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, 40, 40)];
    labelCurrency.backgroundColor = [UIColor clearColor];
    labelCurrency.text = self.currency;

    self.isNumber = YES;
    
    self.warningLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.moneyComponent.frame.origin.y + self.moneyComponent.frame.size.height + 20, 300, 40)];
    self.warningLabel.backgroundColor = [UIColor clearColor];
    self.warningLabel.font = [UIFont systemFontOfSize:13];

    [self.view addSubview:labelCurrency];
    [self.view addSubview:self.moneyComponent];
    [self.view addSubview:self.warningLabel];
}

@end
