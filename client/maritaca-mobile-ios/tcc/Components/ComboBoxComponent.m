//
//  ComboBoxComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "ComboBoxComponent.h"
#import "Option.h"
#import <QuartzCore/QuartzCore.h>

@implementation ComboBoxComponent

@synthesize comboboxComponent = _comboboxComponent;

- (NSString *)getAnswerComponent {
    int indice = [self.comboboxComponent selectedRowInComponent:0];
    Option *option = (Option *)[self.options objectAtIndex:indice];
    NSString *checkedOption = [NSString stringWithFormat:@"{\"%d\":\"%@\"}", option.idOption, option.value];
    [super writeToXML:checkedOption];
    return checkedOption;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    Option *choice = nil;
    for (Option *option in self.options) {
        if(option.checked) {
            choice = option;
            break;
        }
    }

    self.comboboxComponent = [[UIPickerView alloc] init];
    self.comboboxComponent.dataSource = self;
    self.comboboxComponent.delegate = self;
    self.comboboxComponent.showsSelectionIndicator = YES;
    self.comboboxComponent.frame = CGRectMake(20, self.view.frame.size.height/2-self.comboboxComponent.frame.size.height/2, self.view.frame.size.width-40, self.comboboxComponent.frame.size.height);
    [self.comboboxComponent selectRow:choice.idOption inComponent:0 animated:NO];
    [self.view addSubview:self.comboboxComponent];
        
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.options.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    Option *option = ((Option *)[self.options objectAtIndex:row]);
    for (Option *otherOption in self.options) {
        otherOption.checked = NO;
    }
    option.checked = YES;
    return option.text;
}

@end
