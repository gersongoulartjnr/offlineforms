//
//  ReconnectionViewController.m
//  tcc
//
//  Created by Marcela Tonon on 11/12/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "ReconnectionViewController.h"
#import "OAuthViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface ReconnectionViewController ()

@end

@implementation ReconnectionViewController


- (void) reconnecting {
    OAuthViewController *oauthController = [[OAuthViewController alloc] init];
    [self.navigationController pushViewController:oauthController animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //colocar imagem no background da view
    UIGraphicsBeginImageContext(self.view.frame.size);
    [[UIImage imageNamed:@"mobile_background.png"] drawInRect:self.view.bounds];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = @"Maritaca Mobile";
    self.navigationItem.hidesBackButton = YES;
    
    UIButton *buttonReconnect = [UIButton buttonWithType:UIButtonTypeCustom];
    buttonReconnect.backgroundColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:0.6];
    buttonReconnect.layer.borderColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1.0].CGColor;
    buttonReconnect.layer.borderWidth = 0.5f;
    buttonReconnect.layer.cornerRadius = 10.0f;    
    buttonReconnect.frame = CGRectMake(self.view.bounds.size.width/2-100, self.view.bounds.size.height/2-30-self.navigationController.navigationBar.bounds.size.height/2, 200, 60);
    [buttonReconnect setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [buttonReconnect setTitleColor:[UIColor blackColor] forState:UIControlStateHighlighted];
    [buttonReconnect setTitle:@"Login" forState:UIControlStateNormal];
    [buttonReconnect addTarget:self action:@selector(reconnecting) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:buttonReconnect];
}

@end
