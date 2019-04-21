//
//  DrawViewController.h
//  tcc
//
//  Created by Marcela Tonon on 02/01/14.
//  Copyright (c) 2014 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DrawComponent.h"

@interface DrawViewController : UIViewController <UIAlertViewDelegate>

@property (nonatomic, strong) DrawComponent *backViewController;
@property (nonatomic) int delta_iOS7;
@end
