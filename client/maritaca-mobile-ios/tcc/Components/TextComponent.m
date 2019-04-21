//
//  TextComponent.m
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "TextComponent.h"

@implementation TextComponent

@synthesize size = _size;
@synthesize textComponent = _textComponent;

- (NSString *)getAnswerComponent {
     NSString *answer = @"";
    if([self.textComponent.text isEqualToString:@""] || !self.textComponent.text) {
        answer = @"null";
    } else {
        answer = self.textComponent.text;
    }
    [super writeToXML:answer];
    return answer;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.textComponent.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, 200, 40);
    self.textComponent.borderStyle = UITextBorderStyleRoundedRect;
    self.textComponent.font = [UIFont systemFontOfSize:15];
    self.textComponent.autocorrectionType = UITextAutocorrectionTypeNo;
    self.textComponent.clearButtonMode = UITextFieldViewModeWhileEditing;
    self.textComponent.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    self.textComponent.text = self.defaultQuestion;
    self.textComponent.delegate = self;
    [self.view addSubview:self.textComponent];
}
     
@end
