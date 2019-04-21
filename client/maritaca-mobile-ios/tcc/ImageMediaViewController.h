//
//  ImageMediaViewController.h
//  tcc
//
//  Created by Marcela Tonon on 16/11/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Question.h"

@interface ImageMediaViewController : UIViewController <UIImagePickerControllerDelegate>

@property (nonatomic, strong) UIImage *image;

- (void) startCameraControllerFromViewController: (UIViewController *) controller usingDelegate: (id <UIImagePickerControllerDelegate,UINavigationControllerDelegate>) delegate withMediaTypes:(NSArray *)mediaTypes;


@end
