//
//  BarCodeComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "BarCodeComponent.h"

@interface BarCodeComponent ()

@property (nonatomic, strong) UIButton *scanButton;

@end

@implementation BarCodeComponent

@synthesize scanButton = _scanButton;
@synthesize barCodeComponent = _barCodeComponent;

- (NSString *)getAnswerComponent {
    NSString *answer = @"";
    if ([self.barCodeComponent.text isEqualToString:@""] || !self.barCodeComponent.text) {
        answer = @"null";
    } else {
        answer = self.barCodeComponent.text;
    }
    [super writeToXML:answer];
    return answer;
}

- (void) scan {
    // ADD: present a barcode reader that scans from the camera feed
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    ZBarImageScanner *scanner = reader.scanner;
    // TODO: (optional) additional reader configuration here
    
    // EXAMPLE: disable rarely used I2/5 to improve performance
    [scanner setSymbology: ZBAR_I25 config: ZBAR_CFG_ENABLE to:0];
    
    // present and release the controller
    [self presentViewController:reader animated:YES completion:nil];
    //[reader release];
}
- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info {
    // ADD: get the decode results
    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        // EXAMPLE: just grab the first barcode
        break;
    // EXAMPLE: do something useful with the barcode data
    self.barCodeComponent.text = symbol.data;
    
    // ADD: dismiss the controller (NB dismiss from the *reader*!)
    [reader dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.scanButton = [super getDefaultButtonAspect];
    self.scanButton.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, self.scanButton.frame.size.width, self.scanButton.frame.size.height);
    [self.scanButton setTitle:@"Scan" forState:UIControlStateNormal];
    [self.scanButton addTarget:self action:@selector(scan) forControlEvents:UIControlEventTouchUpInside];
    
    self.barCodeComponent.frame = CGRectMake(20, self.scanButton.frame.origin.y + self.scanButton.frame.size.height + 20, self.view.bounds.size.width-40, 200);
    self.barCodeComponent.font = [UIFont systemFontOfSize:15];
    self.barCodeComponent.autocorrectionType = UITextAutocorrectionTypeNo;
    self.barCodeComponent.keyboardType = UIKeyboardTypeDefault;
    self.barCodeComponent.backgroundColor = [UIColor clearColor];
    self.barCodeComponent.editable = NO;
    self.barCodeComponent.dataDetectorTypes = UIDataDetectorTypeLink;
    
    [self.view addSubview:self.scanButton];
    [self.view addSubview:self.barCodeComponent];
    
}

@end
