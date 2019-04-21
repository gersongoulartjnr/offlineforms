//
//  RadioButtonComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface RadioButtonComponent : Question <UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) NSMutableArray *options;
/*
 
 private RadioGroup radioGroup;
 private int idWrapContent = RadioGroup.LayoutParams.WRAP_CONTENT;
 
 @ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
 private List<Option> options;
 */

@end
