//
//  DateComponent.m
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "DateComponent.h"

@implementation DateComponent

@synthesize format = _format;
@synthesize min = _min;
@synthesize max = _max;
@synthesize maxDate = _maxDate;
@synthesize minDate = _minDate;
@synthesize dateComponent = _dateComponent;

- (NSString *)getAnswerComponent {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = [self.format stringByReplacingOccurrencesOfString:@"mm" withString:@"MM"];
    NSString *answer = [dateFormatter stringFromDate:self.dateComponent.date];
    [super writeToXML:answer];
    return answer;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.dateComponent.frame = CGRectMake(20, self.view.frame.size.height/2-self.dateComponent.frame.size.height/2, self.view.frame.size.width-40, self.dateComponent.frame.size.height);
    self.dateComponent.datePickerMode = UIDatePickerModeDate;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = [self.format stringByReplacingOccurrencesOfString:@"mm" withString:@"MM"];
    self.minDate = [dateFormatter dateFromString:self.min];
    self.maxDate = [dateFormatter dateFromString:self.max];
    self.dateComponent.maximumDate = self.maxDate;
    self.dateComponent.minimumDate = self.minDate;
    if(self.defaultQuestion){
        [self.dateComponent setDate:[dateFormatter dateFromString:self.defaultQuestion]];
    }
    [self.view addSubview:self.dateComponent];
}

@end
