//
//  CheckBoxComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface CheckBoxComponent : Question <UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) NSMutableArray *options;
@property (nonatomic, strong) UITableView *checkboxComponent;

/*private ListView listView;
 private CheckBox checkBox;
 private ArrayAdapter<Option> listAdapter;
 
 @ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
 private List<Option> options;
 
 private Option option;*/

@end
