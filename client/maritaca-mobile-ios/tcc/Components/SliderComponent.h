//
//  SliderComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface SliderComponent : Question

@property (nonatomic) int maxValue;
@property (nonatomic, strong) UISlider *sliderComponent;

/*
 @Attribute(name = "max", required = false)
 private Integer maxValue = 100;
 
 private TextView text;
 private final int seekBarId = 1;
*/

@end
