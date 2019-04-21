//
//  OAuthViewController.m
//  tcc
//
//  Created by Marcela Tonon on 04/12/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "OAuthViewController.h"
#import "MenuViewController.h"
#import "ReconnectionViewController.h"

@interface OAuthViewController ()

@property (strong, nonatomic) UIWebView *webView;
@property (strong, nonatomic) UIView *viewLoading;
@property (strong, nonatomic) NSMutableData *responseData;

@end

@implementation OAuthViewController

@synthesize webView = _webView;
@synthesize dictionaryToken = _dictionaryToken;
@synthesize responseData = _responseData;
@synthesize viewLoading = _viewLoading;
@synthesize formID = _formID;

- (NSString *)formID {
    if(!_formID) {
        _formID = @"33351b40-4c06-11e3-b0f6-080027ad6d3a";
    }
    return _formID;
}

- (void) URLFromGetMethod:(NSURL *)url {
    NSString *bodyData = [NSString stringWithFormat:@"grant_type=authorization_code&client_id=maritacamobile&client_secret=maritacasecret&redirect_uri=maritacamobile%%3A%%2F%%2F&%@&response_type=token", url.query];
    
    NSMutableURLRequest *postRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://maritaca.unifesp.br:8080/maritaca/oauth/accessTokenRequest"]];
        
    //NSString *s = (NSString *)CFURLCreateStringByAddingPercentEscapes(NULL, (CFStringRef)unencodedString,NULL, (CFStringRef)@"!*'\"();:@&=+$,/?%#[]% ",kCFStringEncodingUTF8);
    
    [postRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [postRequest setHTTPMethod:@"POST"];
    NSData *requestBodyData = [bodyData dataUsingEncoding:NSUTF8StringEncoding];
    [postRequest setHTTPBody:requestBodyData];
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:postRequest delegate:self];
    [connection start];
}

#pragma mark Connection

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    self.responseData = [[NSMutableData alloc] init];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [self.responseData appendData:data];
}


- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse*)cachedResponse {
    return nil;
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSLog(@"connectionDidFinishLoading");
    NSError *error = nil;
    self.dictionaryToken = self.responseData ? [NSJSONSerialization JSONObjectWithData:self.responseData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
    [connection cancel];
    [self pushDashboard];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    NSLog(@"connection didFailWithError = %@", error);
}


#pragma mark WebView

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    NSLog(@"webViewDidFinishLoad");
    self.navigationController.navigationBarHidden = YES;
    [self.viewLoading removeFromSuperview];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    NSLog(@"WebView didFailLoadWithError error = %@", error);
    NSLog(@"code = %d",  error.code);
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:[NSString stringWithFormat:@"%@ Please, try again later.", error.localizedDescription] delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    //erro Code=-1009 "The Internet connection appears to be offline."
    //erro Code=-1001 "The request timed out."
    [alert show];
    [alert release];
    ReconnectionViewController *reconnectionController = [[ReconnectionViewController alloc] init];
    [self.navigationController pushViewController:reconnectionController animated:YES];
}


- (void) pushDashboard {
    [self.webView removeFromSuperview];
    self.webView = nil;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard_iPhone" bundle: nil];
    UIViewController *destinationController = [storyboard instantiateViewControllerWithIdentifier:@"DashboardController"];
    [self.navigationController pushViewController:destinationController animated:YES];
    [self.navigationController setViewControllers:[NSArray arrayWithObject:destinationController] animated:NO];
}

- (void)viewDidLoad {
    if(self.dictionaryToken) {
        [self pushDashboard];
        return;
    }
    [self pushDashboard];
    return;
    [super viewDidLoad];
    
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.title = @"Maritaca Mobile";
    self.navigationController.navigationBar.tintColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1]; //verde do maritaca

    //self.webView = [[UIWebView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    self.webView = [[UIWebView alloc] initWithFrame:self.view.bounds];
    self.webView.delegate = self;
    self.webView.contentMode = UIViewContentModeScaleAspectFit;
    self.webView.scalesPageToFit = NO;
    [self.view addSubview:self.webView];
    
    //colocar imagem no background da view
    self.viewLoading = [[UIView alloc] initWithFrame:self.view.bounds];
    UIGraphicsBeginImageContext(self.view.bounds.size);
    [[UIImage imageNamed:@"mobile_background.png"] drawInRect:self.view.bounds];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.viewLoading.backgroundColor = [UIColor colorWithPatternImage:image];
    UILabel *labelLoading = [[UILabel alloc] initWithFrame:CGRectMake(self.view.bounds.size.width/2-50, self.view.bounds.size.height/2-20-self.navigationController.navigationBar.bounds.size.height/2, 100, 40)];
    
    labelLoading.text = @"Loading...";
    labelLoading.textAlignment = NSTextAlignmentCenter;
    labelLoading.backgroundColor = [UIColor clearColor];
    [self.viewLoading addSubview:labelLoading];
    [self.view addSubview:self.viewLoading];
    
    NSURL *url = [NSURL URLWithString:@"http://maritaca.unifesp.br:8080/maritaca/oauth/authorizationRequest?redirect_uri=maritacamobile://&response_type=code&client_id=maritacamobile&client_secret=maritacasecret&form_id=33351b40-4c06-11e3-b0f6-080027ad6d3a"];
    //NSURL *url = [NSURL URLWithString:@"http://www.google.com"];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:5];
    [request setHTTPMethod: @"GET"];
    [self.webView loadRequest:request];
}

@end
