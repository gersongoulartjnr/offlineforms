//
//  TextComponent.h
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@interface TextComponent : Question <UITextFieldDelegate>

@property (nonatomic) int size;
@property (nonatomic, strong) UITextField *textComponent;

/*private EditText field;
 
 @Element(required = false)
 private Integer size;
 
*/

@end
