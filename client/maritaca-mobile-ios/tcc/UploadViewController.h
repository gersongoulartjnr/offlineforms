//
//  UploadViewController.h
//  tcc
//
//  Created by Marcela Tonon on 10/12/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UploadViewController : UIViewController <NSURLConnectionDataDelegate>

- (void) sendDataToServer;

@end
