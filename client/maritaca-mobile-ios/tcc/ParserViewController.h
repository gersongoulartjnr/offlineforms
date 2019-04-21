//
//  ParserViewController.h
//  tcc
//
//  Created by Marcela Tonon on 17/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ParserViewController : UIViewController

+ (NSMutableArray *) parseData:(NSData *) data;
    
@end
