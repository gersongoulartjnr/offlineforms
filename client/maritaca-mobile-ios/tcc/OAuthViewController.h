//
//  OAuthViewController.h
//  tcc
//
//  Created by Marcela Tonon on 04/12/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OAuthViewController : UIViewController <UIWebViewDelegate, NSURLConnectionDataDelegate>

- (void) URLFromGetMethod:(NSURL *)url;
@property (strong, nonatomic) NSDictionary *dictionaryToken;
@property (strong, nonatomic) NSString *formID;

@end
