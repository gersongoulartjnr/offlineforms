//
//  UploadViewController.m
//  tcc
//
//  Created by Marcela Tonon on 10/12/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "UploadViewController.h"
#import "OAuthViewController.h"

@interface UploadViewController ()

@end

@implementation UploadViewController

- (void) sendDataToServer {
    [self uploadXML];
}

- (void) uploadXML {
    
    //UIViewController *oauth = ((UINavigationController *)[[[UIApplication sharedApplication] keyWindow] rootViewController]).topViewController;
    //NSMutableURLRequest *postRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maritaca.unifesp.br:8080/maritaca/ws/answer/save/?oauth_token=%@", [((OAuthViewController *)oauth).dictionaryToken valueForKey:@"access_token"]]]];
    NSMutableURLRequest *postRequest = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://maritaca.unifesp.br:8080/maritaca/ws/answer/save/?oauth_token=346546886545435"]];
    
    NSString *path = [NSTemporaryDirectory() stringByAppendingString:@"upload.xml"];
    NSData *xmlData = [[NSMutableData alloc] initWithContentsOfFile:path];
    NSString *stringXML = [[NSString alloc] initWithData:xmlData encoding:NSASCIIStringEncoding];

    [postRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [postRequest setHTTPMethod:@"POST"];
    NSData *requestBodyData = [stringXML dataUsingEncoding:NSUTF8StringEncoding];
    [postRequest setHTTPBody:requestBodyData];
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:postRequest delegate:self];
    [connection start];
    
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSLog(@"connectionDidFinishLoading");
}

- (void)connection:(NSURLConnection *)connection didCancelAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
    NSLog(@"didCancelAuthenticationChallenge");
}
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    NSLog(@"data = %@", data);
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    NSLog(@"response = %@", response);
}
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    NSLog(@"erro upload = %@", error);
}

@end
