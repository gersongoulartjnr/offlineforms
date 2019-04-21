//
//  MenuViewController.h
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CustomButton.h"

@interface MenuViewController : UIViewController

@property (retain, nonatomic) IBOutlet CustomButton *btnCollect;
@property (retain, nonatomic) IBOutlet CustomButton *btnAnswers;
@property (retain, nonatomic) IBOutlet CustomButton *btnSend;
@property (retain, nonatomic) IBOutlet CustomButton *btnTellaFriend;
@property (retain, nonatomic) IBOutlet CustomButton *btnAbout;
@property (retain, nonatomic) IBOutlet CustomButton *btnSettings;

@end
