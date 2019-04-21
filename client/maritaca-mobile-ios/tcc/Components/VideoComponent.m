//
//  VideoComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "VideoComponent.h"
#import "ImageMediaViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>

@implementation VideoComponent

- (NSString *)getAnswerComponent {
    NSString *answer = @"null";
    [super writeToXML:answer];
    return answer;
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

    UIButton *buttonCaptureVideo = [super getDefaultButtonAspect];
    buttonCaptureVideo.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, buttonCaptureVideo.frame.size.width, buttonCaptureVideo.frame.size.height);
    [buttonCaptureVideo addTarget:self action:@selector(captureVideo) forControlEvents:UIControlEventTouchUpInside];
    [buttonCaptureVideo setTitle:@"Capture Video" forState:UIControlStateNormal];
    
    //self.pictureComponent.frame = CGRectMake(10, 120, self.view.frame.size.width-20, self.view.frame.size.height-210);
    //self.pictureComponent.backgroundColor = [UIColor purpleColor];
    //self.pictureComponent.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:buttonCaptureVideo];

}

- (void) captureVideo {
    ImageMediaViewController *imageController = [[ImageMediaViewController alloc] init];
    [imageController startCameraControllerFromViewController:self usingDelegate:(id<UIImagePickerControllerDelegate, UINavigationControllerDelegate>)self withMediaTypes:[[NSArray alloc] initWithObjects: (NSString *) kUTTypeMovie, nil]];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    NSString *moviePath = [[info objectForKey: UIImagePickerControllerMediaURL] path];
    if (UIVideoAtPathIsCompatibleWithSavedPhotosAlbum (moviePath)) {
        self.value = moviePath;
        UISaveVideoAtPathToSavedPhotosAlbum(moviePath, nil, nil, nil);
    }
    [picker dismissViewControllerAnimated:YES completion:nil];
    [picker release];
}


@end
