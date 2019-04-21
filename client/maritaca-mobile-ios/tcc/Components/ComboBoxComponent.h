//
//  ComboBoxComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface ComboBoxComponent : Question <UIPickerViewDataSource, UIPickerViewDelegate>

@property (nonatomic, strong) NSMutableArray *options;
@property (nonatomic, strong) UIPickerView *comboboxComponent;

/*private static final long serialVersionUID = 1L;
 private static String CHOOSE_ONE = "";
 private Spinner spinner;
 
 @ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
 private List<Option> options;
 */

@end
