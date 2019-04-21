//
//  SliderComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "SliderComponent.h"

@interface SliderComponent ()

@property (nonatomic, strong) UILabel *valueSliderLabel;

@end

@implementation SliderComponent


@synthesize maxValue = _maxValue;
@synthesize sliderComponent = _sliderComponent;
@synthesize valueSliderLabel = _valueSliderLabel;

- (NSString *)getAnswerComponent {
    NSString *answer = [NSString stringWithFormat:@"%.0f", self.sliderComponent.value];
    [super writeToXML:answer];
    return answer;
}

- (int) maxValue {
    if(!_maxValue) {
        _maxValue = 100;
    }
    return _maxValue;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.sliderComponent.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, self.view.frame.size.width-20, self.sliderComponent.frame.size.height);
    self.sliderComponent.maximumValue = self.maxValue;
    
    self.valueSliderLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.sliderComponent.frame.origin.y + self.sliderComponent.frame.size.height + 20, 300, 40)];
    self.valueSliderLabel.backgroundColor = [UIColor clearColor];
    
    [self.view addSubview:self.valueSliderLabel];
    if(self.defaultQuestion && [self.defaultQuestion integerValue]) {
        self.sliderComponent.value = [self.defaultQuestion integerValue];
    }
    self.valueSliderLabel.text = [NSString stringWithFormat:@"%.0f%%", self.sliderComponent.value];
    [self.sliderComponent addTarget:self action:@selector(slider:) forControlEvents:UIControlEventAllTouchEvents];
    [self.view addSubview:self.sliderComponent];
}

- (IBAction)slider:(UISlider *)sender {
    self.valueSliderLabel.text = [NSString stringWithFormat:@"%.0f%%", [sender value]];
    self.sliderComponent.value = [sender value];
    //float value = [sender value];
    //self.debugLabel.text = [NSString stringWithFormat:@"Value: %f",value];
}

@end
