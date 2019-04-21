//
//  BarCodeComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"
#import "ZBarSDK.h"

@interface BarCodeComponent : Question <ZBarReaderDelegate>

@property (nonatomic, strong) UITextView *barCodeComponent;

@end
