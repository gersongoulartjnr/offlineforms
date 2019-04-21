//
//  DecimalComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "DecimalComponent.h"


@interface DecimalComponent ()
@property (nonatomic) bool isNumber;
@property (nonatomic, strong) UILabel *warningLabel;
@end

@implementation DecimalComponent

@synthesize decimalComponent = _decimalComponent;
@synthesize min = _min;
@synthesize max = _max;
@synthesize warningLabel = _warningLabel;
@synthesize isNumber = _isNumber;

- (NSString *)getAnswerComponent {
    NSString *answer = @"";
    if([self.decimalComponent.text isEqualToString:@""] || !self.decimalComponent.text) {
        answer = @"null";
    } else {
        answer = self.decimalComponent.text;
    }
    [super writeToXML:answer];
    return answer;
}

- (void) checkNumber {
    self.isNumber = [super checkNumber:self.decimalComponent.text];
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
    self.decimalComponent.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, 200, 40);
    self.decimalComponent.borderStyle = UITextBorderStyleRoundedRect;
    self.decimalComponent.font = [UIFont systemFontOfSize:15];
    self.decimalComponent.autocorrectionType = UITextAutocorrectionTypeNo;
    self.decimalComponent.delegate = self;
    self.decimalComponent.clearButtonMode = UITextFieldViewModeWhileEditing;
    self.decimalComponent.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    self.decimalComponent.text = self.defaultQuestion;
    self.decimalComponent.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    [self.decimalComponent addTarget:self action:@selector(checkNumber) forControlEvents:UIControlEventEditingChanged];

    self.isNumber = YES;
    
    self.warningLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.decimalComponent.frame.origin.y + self.decimalComponent.frame.size.height + 20, 300, 40)];
    self.warningLabel.backgroundColor = [UIColor clearColor];
    self.warningLabel.font = [UIFont systemFontOfSize:13];
    
    [self.view addSubview:self.decimalComponent];
    [self.view addSubview:self.warningLabel];
}

@end
