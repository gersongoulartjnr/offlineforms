//
//  CustomButton.h
//  ProjetoFPG
//
//  Created by EPR Development on 01/10/12.
//  Copyright (c) 2012 Embraer. All rights reserved.
//

#import <UIKit/UIKit.h>

@class CAGradientLayer;

@interface CustomButton : UIButton {
@private
	UIColor* _gradientStartColor;
	UIColor* _gradientEndColor;
	CAGradientLayer* _gradientLayer;
    CAGradientLayer* _glossyLayer;
}

@property (nonatomic, retain) UIColor* gradientStartColor;
@property (nonatomic, retain) UIColor* gradientEndColor;
- (void) setMyCornerRadius:(CGFloat)cr;
- (void) setMyBorder:(CGFloat)border withColor:(UIColor *)color;
- (void)drawRect:(CGRect)rect;

@end