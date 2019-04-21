//
//  DrawComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "DrawComponent.h"
#import "DrawViewController.h"

@interface DrawComponent ()

@property (nonatomic, strong) UIButton *drawButton;
@property (nonatomic, strong) UIImage *drawPicture;

@end

@implementation DrawComponent

@synthesize drawButton = _drawButton;
@synthesize drawComponent = _drawComponent;
@synthesize drawPicture = _drawPicture;

- (void) openDrawViewController {
    DrawViewController *viewController = [[DrawViewController alloc] init];
    viewController.backViewController = self;
    viewController.delta_iOS7 = self.delta_iOS7;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.drawButton = [super getDefaultButtonAspect];
    self.drawButton.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, self.drawButton.frame.size.width, self.drawButton.frame.size.height);
    [self.drawButton setTitle:@"Click to Draw" forState:UIControlStateNormal];
    [self.drawButton addTarget:self action:@selector(openDrawViewController) forControlEvents:UIControlEventTouchUpInside];
    
    self.drawComponent.frame = CGRectMake(20, self.drawButton.frame.origin.y + self.drawButton.frame.size.height + 20, self.view.frame.size.width-40, self.view.frame.size.height-210);
    self.drawComponent.backgroundColor = [UIColor clearColor];
    self.drawComponent.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:self.drawButton];
    [self.view addSubview:self.drawComponent];
}

@end
