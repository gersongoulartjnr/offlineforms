//
//  PictureComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "PictureComponent.h"
#import "ImageMediaViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>

@implementation PictureComponent

@synthesize picture = _picture;
@synthesize pictureComponent = _pictureComponent;

- (void) takePicture {
    ImageMediaViewController *imageController = [[ImageMediaViewController alloc] init];
    [imageController startCameraControllerFromViewController:self usingDelegate:(id<UIImagePickerControllerDelegate, UINavigationControllerDelegate>)self withMediaTypes:[[NSArray alloc] initWithObjects: (NSString *) kUTTypeImage, nil]];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    NSLog(@"shuashuhuas");
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:nil];
    UIImage *editedImage = (UIImage *) [info objectForKey:UIImagePickerControllerEditedImage];
    UIImage *originalImage = (UIImage *) [info objectForKey:UIImagePickerControllerOriginalImage];
    UIImage *imageToSave = nil;
    
    if (editedImage) {
        imageToSave = editedImage;
    } else {
        imageToSave = originalImage;
    }
    self.picture = imageToSave;
    self.pictureComponent.image = self.picture;
    
    //salvando na pasta documents
    NSString *date = [self getCurrentDate];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *imagePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"picture_%@.png",date]];
    //extracting image from the picker and saving it
    NSData *webData = UIImagePNGRepresentation(imageToSave);
    [webData writeToFile:imagePath atomically:YES];
    self.value = imagePath;
    
    UIImageWriteToSavedPhotosAlbum(imageToSave, nil, nil , nil);
    
    [picker release];
}

- (NSString *) getCurrentDate {
    NSDate* date = [NSDate date];
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    NSTimeZone *destinationTimeZone = [NSTimeZone systemTimeZone];
    formatter.timeZone = destinationTimeZone;
    [formatter setDateStyle:NSDateFormatterLongStyle];
    [formatter setDateFormat:@"MM/dd/yyyy hh:mm:ss"];
    NSString* dateString = [formatter stringFromDate:date];
    dateString = [dateString stringByReplacingOccurrencesOfString:@"/" withString:@"-"];
    dateString = [dateString stringByReplacingOccurrencesOfString:@":" withString:@"."];
    dateString = [dateString stringByReplacingOccurrencesOfString:@" " withString:@"_"];
    return dateString;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIButton *buttonTakePicture = [super getDefaultButtonAspect];
    [buttonTakePicture addTarget:self action:@selector(takePicture) forControlEvents:UIControlEventTouchUpInside];
    [buttonTakePicture setTitle:@"Take a Picture" forState:UIControlStateNormal];
    buttonTakePicture.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, buttonTakePicture.frame.size.width-50, buttonTakePicture.frame.size.height);

    self.pictureComponent.frame = CGRectMake(10, buttonTakePicture.frame.origin.y + buttonTakePicture.frame.size.height + 20, self.view.frame.size.width-20, self.view.frame.size.height-210);
    self.pictureComponent.backgroundColor = [UIColor clearColor];
    self.pictureComponent.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:buttonTakePicture];
    [self.view addSubview:self.pictureComponent];    
}

@end
