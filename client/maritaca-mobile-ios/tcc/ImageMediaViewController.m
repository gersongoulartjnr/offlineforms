//
//  ImageMediaViewController.m
//  tcc
//
//  Created by Marcela Tonon on 16/11/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "ImageMediaViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>

@interface ImageMediaViewController ()


@end

@implementation ImageMediaViewController

@synthesize image = _myImage;


- (void) startCameraControllerFromViewController: (UIViewController *) controller usingDelegate: (id <UIImagePickerControllerDelegate,UINavigationControllerDelegate>) delegate withMediaTypes:(NSArray *)mediaTypes {

    if (([UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypeCamera] == NO) || (delegate == nil) || (controller == nil)) return;
    
    UIImagePickerController *cameraUI = [[UIImagePickerController alloc] init];
    cameraUI.sourceType = UIImagePickerControllerSourceTypeCamera;
    cameraUI.mediaTypes = mediaTypes;
    cameraUI.allowsEditing = NO;
    cameraUI.delegate = delegate;
    [controller presentViewController:cameraUI animated:YES completion:nil];
}



@end