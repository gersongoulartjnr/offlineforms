//
//  DateComponent.h
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@interface DateComponent : Question

@property (nonatomic, strong) NSString *format;
@property (nonatomic, strong) NSString *min;
@property (nonatomic, strong) NSString *max;
@property (nonatomic, strong) UIDatePicker *dateComponent;
@property (nonatomic, strong) NSDate *maxDate;
@property (nonatomic, strong) NSDate *minDate;


@end

/*
 @Element(name = "format", required = true)
 private String format;
 
 @Attribute(name = "min", required = false)
 private String min;
 
 @Attribute(name = "max", required = false)
 private String max;
 
 private Date maxDate;
 private Date minDate;
 
 private DatePicker field;
*/