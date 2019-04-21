//
//  MoneyComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface MoneyComponent : Question <UITextFieldDelegate>

@property (nonatomic, strong) UITextField *moneyComponent;
@property (nonatomic, strong) NSNumber *min;
@property (nonatomic, strong) NSNumber *max;
@property (nonatomic, strong) NSString *currency;

@end
/*
 private static final int FIELD_SIZE = 80;
 
 @Attribute(required = false)
 private BigDecimal min;
 
 @Attribute(required = false)
 private BigDecimal max;
 
 @Element(required = false)
 private String currency;
 
 private EditText field;

 */

