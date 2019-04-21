//
//  NumberComponent.h
//  tcc
//
//  Created by Marcela Tonon on 17/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"

@interface NumberComponent : Question <UITextFieldDelegate>

@property (nonatomic, strong) UITextField *numberComponent;
@property (nonatomic) int min;
@property (nonatomic) int max;

/*@Attribute(required = false)
private Integer min;

@Attribute(required = false)
private Integer max;
*/

@end
