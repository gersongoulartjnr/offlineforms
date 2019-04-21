//
//  CustomButton.m
//  ProjetoFPG
//
//  Created by EPR Development on 01/10/12.
//  Copyright (c) 2012 Embraer. All rights reserved.
//

#import "CustomButton.h"
#import <QuartzCore/QuartzCore.h>

@implementation CustomButton

@synthesize gradientStartColor = _gradientStartColor;
@synthesize gradientEndColor = _gradientEndColor;

- (void) setMyCornerRadius:(CGFloat)cr {
    self.layer.cornerRadius = cr;
}

- (void) setMyBorder:(CGFloat)border withColor:(UIColor *)color {
    self.layer.borderWidth = border;
    self.layer.borderColor = color.CGColor;
}

-(void)awakeFromNib {
    _gradientLayer = [[CAGradientLayer alloc] init];
    _gradientLayer.bounds = self.bounds;
    _gradientLayer.position = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
    
    [self.layer insertSublayer:_gradientLayer atIndex:0];
    
	self.layer.cornerRadius = 5.0f;
	self.layer.masksToBounds = YES;
	self.layer.borderWidth = 1.0f; 
    self.layer.borderColor = [UIColor grayColor].CGColor;
  /*  _glossyLayer = [[CAGradientLayer alloc] init];
	_glossyLayer.bounds = CGRectMake(0, 0, self.bounds.size.width, self.bounds.size.height/2);
    _glossyLayer.position = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/4);
    [self.layer addSublayer:_glossyLayer];
   */
    
}


// draw gradient layer

- (void)drawRect:(CGRect)rect {
    if (_gradientStartColor && _gradientEndColor)
    {
        [_gradientLayer setColors:
		 [NSArray arrayWithObjects: (id)[_gradientStartColor CGColor]
		  , (id)[_gradientEndColor CGColor], nil]];
    }
    
 /*   [_glossyLayer setColors:
	 [NSArray arrayWithObjects: 
	  (id)[[UIColor colorWithRed:1.0f green:1.0f blue:1.0f alpha:0.99f] CGColor]
	  , (id)[[UIColor colorWithRed:1.0f green:1.0f blue:1.0f alpha:0.2f] CGColor], nil]];
    */
    [super drawRect:rect];
}


- (void)dealloc {
	[_gradientEndColor release];
	[_gradientStartColor release];
	[_gradientLayer release];
    [super dealloc];
}

@end