//
//  DrawViewController.m
//  tcc
//
//  Created by Marcela Tonon on 02/01/14.
//  Copyright (c) 2014 Marcela Tonon. All rights reserved.
//

#import "DrawViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface DrawViewController () {
    CGPoint lastPoint;
    CGFloat red;
    CGFloat oldRed;
    CGFloat green;
    CGFloat oldGreen;
    CGFloat blue;
    CGFloat oldBlue;
    CGFloat brush;
    CGFloat opacity;
    bool mouseSwiped;
    bool tagMudouCor;
}

@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIView *slidersView;
@property (nonatomic, strong) UIView *colorView;
@end

@implementation DrawViewController

@synthesize imageView = _imageView;
@synthesize backViewController = _backViewController;
@synthesize slidersView = _slidersView;
@synthesize colorView = _colorView;
@synthesize delta_iOS7 = _delta_iOS7;

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

- (void) saveDrawing {
    UIGraphicsBeginImageContextWithOptions(self.imageView.bounds.size,NO,0.0);
    [self.imageView.image drawInRect:CGRectMake(0, 0, self.imageView.bounds.size.width, self.imageView.bounds.size.height)];

    UIImage *saveImage = UIGraphicsGetImageFromCurrentImageContext();
    //salvando na pasta documents
    NSString *date = [self getCurrentDate];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *imagePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"picture_%@.png",date]];
    //extracting image from the picker and saving it
    NSData *webData = UIImagePNGRepresentation(saveImage);
    [webData writeToFile:imagePath atomically:YES];

    UIGraphicsEndImageContext();
    UIImageWriteToSavedPhotosAlbum(saveImage, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
    self.backViewController.drawComponent.image = saveImage;
    self.backViewController.value = imagePath;
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) cleanDrawing {
    self.imageView.image = nil;
    [self paintBackground];
}

- (void) changePincelColor {
    red = oldRed;
    green = oldGreen;
    blue = oldBlue;
    brush = 5.0;
    self.slidersView.hidden = NO;
}

- (void) eraseDrawing {
    oldRed = red;
    oldGreen = green;
    oldBlue = blue;
    red = 1.0;
    green = 1.0;
    blue = 1.0;
    brush = 10.0;
}

- (void) cancelDrawing {
    [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"Are you sure you want to cancel your drawing?" delegate:self cancelButtonTitle:nil otherButtonTitles:@"Yes", @"No", nil] show];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    mouseSwiped = NO;
    UITouch *touch = [touches anyObject];
    lastPoint = [touch locationInView:self.imageView];
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    
    mouseSwiped = YES;
    UITouch *touch = [touches anyObject];
    CGPoint currentPoint = [touch locationInView:self.imageView];
    
    UIGraphicsBeginImageContext(self.imageView.bounds.size);
    [self.imageView.image drawInRect:CGRectMake(0, 0, self.imageView.bounds.size.width, self.imageView.bounds.size.height)];
    CGContextMoveToPoint(UIGraphicsGetCurrentContext(), lastPoint.x, lastPoint.y);
    CGContextAddLineToPoint(UIGraphicsGetCurrentContext(), currentPoint.x, currentPoint.y);
    CGContextSetLineCap(UIGraphicsGetCurrentContext(), kCGLineCapRound);
    CGContextSetLineWidth(UIGraphicsGetCurrentContext(), brush );
    CGContextSetRGBStrokeColor(UIGraphicsGetCurrentContext(), red, green, blue, 1.0);
    CGContextSetBlendMode(UIGraphicsGetCurrentContext(),kCGBlendModeNormal);
    CGContextStrokePath(UIGraphicsGetCurrentContext());
    self.imageView.image = UIGraphicsGetImageFromCurrentImageContext();
    [self.imageView setAlpha:opacity];
    UIGraphicsEndImageContext();
    
    lastPoint = currentPoint;
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    
    if(!mouseSwiped) {
        UIGraphicsBeginImageContext(self.imageView.bounds.size);
        [self.imageView.image drawInRect:CGRectMake(0, 0, self.imageView.bounds.size.width, self.imageView.bounds.size.height)];
        CGContextSetLineCap(UIGraphicsGetCurrentContext(), kCGLineCapRound);
        CGContextSetLineWidth(UIGraphicsGetCurrentContext(), brush);
        CGContextSetRGBStrokeColor(UIGraphicsGetCurrentContext(), red, green, blue, opacity);
        CGContextMoveToPoint(UIGraphicsGetCurrentContext(), lastPoint.x, lastPoint.y);
        CGContextAddLineToPoint(UIGraphicsGetCurrentContext(), lastPoint.x, lastPoint.y);
        CGContextStrokePath(UIGraphicsGetCurrentContext());
        CGContextFlush(UIGraphicsGetCurrentContext());
        self.imageView.image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    }
}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    // Was there an error?
    if (error != NULL) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Image could not be saved.Please try again."  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Close", nil];
        [alert show];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success" message:@"Image was successfully saved in photoalbum."  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
        [alert show];
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
    if([title isEqualToString:@"Yes"]) {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void) hideViewColors {
    oldRed = red;
    oldGreen = green;
    oldBlue = blue;
    self.slidersView.hidden = YES;
}

- (void) createViewColors {
    self.slidersView = [[UIView alloc] initWithFrame:CGRectMake(0, self.delta_iOS7 + 0, self.view.frame.size.width, 160)];
    self.slidersView.backgroundColor = [UIColor whiteColor];
    self.slidersView.layer.borderWidth = 15.0;
    self.slidersView.layer.borderColor = [[UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1] CGColor]; //verde do maritaca
    
    UISlider *redSlider = [[UISlider alloc] initWithFrame:CGRectMake(80,20, 150, 40)];
    [redSlider setMaximumValue:255];
    redSlider.tag = 0;
    redSlider.minimumTrackTintColor = [UIColor whiteColor];
    redSlider.maximumTrackTintColor = [UIColor whiteColor];
    redSlider.thumbTintColor = [UIColor blackColor];
    [redSlider addTarget:self action:@selector(sliderValue:) forControlEvents:UIControlEventAllTouchEvents];
    
    UISlider *greenSlider = [[UISlider alloc] initWithFrame:CGRectMake(80, 60, 150, 40)];
    [greenSlider setMaximumValue:255];
    greenSlider.tag = 1;
    greenSlider.minimumTrackTintColor = [UIColor whiteColor];
    greenSlider.maximumTrackTintColor = [UIColor whiteColor];
    greenSlider.thumbTintColor = [UIColor blackColor];
    [greenSlider addTarget:self action:@selector(sliderValue:) forControlEvents:UIControlEventAllTouchEvents];
    
    UISlider *blueSlider = [[UISlider alloc] initWithFrame:CGRectMake(80, 100, 150, 40)];
    [blueSlider setMaximumValue:255];
    blueSlider.tag = 2;
    blueSlider.minimumTrackTintColor = [UIColor whiteColor];
    blueSlider.maximumTrackTintColor = [UIColor whiteColor];
    blueSlider.thumbTintColor = [UIColor blackColor];
    [blueSlider addTarget:self action:@selector(sliderValue:) forControlEvents:UIControlEventAllTouchEvents];
    
    
    UILabel *labelRed = [[UILabel alloc] initWithFrame:CGRectMake(30, 20, 50, 40)];
    labelRed.font = [UIFont systemFontOfSize:15];
    labelRed.text = @"Red:";
   
    UILabel *labelGreen = [[UILabel alloc] initWithFrame:CGRectMake(30, 60, 50, 40)];
    labelGreen.font = [UIFont systemFontOfSize:15];
    labelGreen.text = @"Green:";

    UILabel *labelBlue = [[UILabel alloc] initWithFrame:CGRectMake(30, 100, 50, 40)];
    labelBlue.font = [UIFont systemFontOfSize:15];
    labelBlue.text = @"Blue:";

    self.colorView = [[UIView alloc] initWithFrame:CGRectMake(250, 30, 40, 40)];
    self.colorView.backgroundColor =[UIColor colorWithRed:red green:green blue:blue alpha:1];

    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(250,90,40,40);
    button.backgroundColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:0.6];
    button.layer.borderColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:0.6].CGColor;
    button.layer.borderWidth = 0.5f;
    button.layer.cornerRadius = 10.0f;
    button.titleLabel.font = [UIFont systemFontOfSize:16];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:UIControlStateHighlighted];
    [button setTitle:@"Ok" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(hideViewColors) forControlEvents:UIControlEventTouchUpInside];
    
    self.slidersView.hidden = YES;
    [self.slidersView addSubview:redSlider];
    [self.slidersView addSubview:greenSlider];
    [self.slidersView addSubview:blueSlider];
    [self.slidersView addSubview:labelRed];
    [self.slidersView addSubview:labelGreen];
    [self.slidersView addSubview:labelBlue];
    [self.slidersView addSubview:button];
    [self.slidersView addSubview:self.colorView];
    [self.view addSubview:self.slidersView];
}

- (IBAction)sliderValue:(UISlider *)sender {
    if(sender.tag == 0) {
        red = sender.value / 255.0;
        sender.thumbTintColor = [UIColor colorWithRed:red green:0 blue:0 alpha:1];
    } else if(sender.tag == 1) {
        green = sender.value / 255.0;
        sender.thumbTintColor = [UIColor colorWithRed:0 green:green blue:0 alpha:1];
    } else if(sender.tag == 2) {
        blue = sender.value / 255.0;
        sender.thumbTintColor = [UIColor colorWithRed:0 green:0 blue:blue alpha:1];
    }
    self.colorView.backgroundColor = [UIColor colorWithRed:red green:green blue:blue alpha:1];
}

- (void) paintBackground {
    //coloca fundo branco na imagem
    CGSize size = CGSizeMake(self.imageView.frame.size.width, self.imageView.frame.size.height- [[UIApplication sharedApplication] statusBarFrame].size.height);
    UIGraphicsBeginImageContext(size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextMoveToPoint(context, 0, 0);
    CGContextAddLineToPoint(context, 0, size.height);
    CGContextAddLineToPoint(context, size.width, size.height);
    CGContextAddLineToPoint(context, size.width, 0);
    CGContextAddLineToPoint(context, 0, 0);
    CGContextSetFillColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextFillPath(context);
    self.imageView.image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationItem.hidesBackButton = YES;
    self.navigationController.navigationBarHidden = NO;

    UIImage *saveImage = [UIImage imageNamed:@"save.png"];
    UIButton *btSave = [UIButton buttonWithType:UIButtonTypeCustom];
    btSave.frame = CGRectMake(0, 0, saveImage.size.width, saveImage.size.height );
    [btSave setImage:saveImage forState:UIControlStateNormal];
    [btSave addTarget:self action:@selector(saveDrawing) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnSave = [[UIBarButtonItem alloc] initWithCustomView:btSave];
    
    UIImage *cancelImage = [UIImage imageNamed:@"ic_menu_cancel_over.png"];
    UIButton *btCancel = [UIButton buttonWithType:UIButtonTypeCustom];
    btCancel.frame = CGRectMake(0, 0, saveImage.size.width, saveImage.size.height );
    [btCancel setImage:cancelImage forState:UIControlStateNormal];
    [btCancel setImage:[UIImage imageNamed:@"ic_menu_cancel.png"] forState:UIControlStateHighlighted];
    [btCancel addTarget:self action:@selector(cancelDrawing) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnCancel = [[UIBarButtonItem alloc] initWithCustomView:btCancel];
    
    UIImage *maritacaImage = [UIImage imageNamed:@"form_logo.png"];
    UIButton *btMaritaca = [UIButton buttonWithType:UIButtonTypeCustom];
    btMaritaca.frame = CGRectMake(0, 0, saveImage.size.width, saveImage.size.height );
    [btMaritaca setImage:maritacaImage forState:UIControlStateNormal];
    [btMaritaca setImage:maritacaImage forState:UIControlStateHighlighted];
    UIBarButtonItem *btnMaritaca = [[UIBarButtonItem alloc] initWithCustomView:btMaritaca];

    UIImage *cleanImage = [UIImage imageNamed:@"clear.png"];
    UIButton *btClean = [UIButton buttonWithType:UIButtonTypeCustom];
    btClean.frame = CGRectMake(0, 0, cleanImage.size.width, cleanImage.size.height );
    [btClean setImage:cleanImage forState:UIControlStateNormal];
    [btClean addTarget:self action:@selector(cleanDrawing) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnClean = [[UIBarButtonItem alloc] initWithCustomView:btClean];

    UIImage *pincelImage = [UIImage imageNamed:@"pincel.png"];
    UIButton *btPincel = [UIButton buttonWithType:UIButtonTypeCustom];
    btPincel.frame = CGRectMake(0, 0, pincelImage.size.width, pincelImage.size.height );
    [btPincel setImage:pincelImage forState:UIControlStateNormal];
    [btPincel addTarget:self action:@selector(changePincelColor) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnPincel = [[UIBarButtonItem alloc] initWithCustomView:btPincel];

    UIImage *eraserImage = [UIImage imageNamed:@"eraser.png"];
    UIButton *btEraser = [UIButton buttonWithType:UIButtonTypeCustom];
    btEraser.frame = CGRectMake(0, 0, eraserImage.size.width, eraserImage.size.height);
    [btEraser setImage:eraserImage forState:UIControlStateNormal];
    [btEraser addTarget:self action:@selector(eraseDrawing) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnEraser = [[UIBarButtonItem alloc] initWithCustomView:btEraser];

    CGFloat separator = 20.0f;
    UIBarButtonItem *fixedItem1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:self action:nil];
    UIBarButtonItem *fixedItem2 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:self action:nil];
    UIBarButtonItem *fixedItem3 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:self action:nil];
    UIBarButtonItem *fixedItem4 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:self action:nil];
    fixedItem1.width = separator;
    fixedItem2.width = separator;
    fixedItem3.width = separator;
    fixedItem4.width = separator;
    
    [self.navigationItem setLeftBarButtonItem:btnMaritaca];
    [self.navigationItem setRightBarButtonItems:[NSArray arrayWithObjects: btnCancel, fixedItem4, btnSave, fixedItem1, btnClean, fixedItem2, btnPincel, fixedItem3, btnEraser, nil]];
    
    self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    self.imageView.backgroundColor = [UIColor whiteColor];
    
    [self paintBackground];
    
    [self.view addSubview:self.imageView];
    [self createViewColors];
    
    red = 0.0/255.0;
    green = 0.0/255.0;
    blue = 0.0/255.0;
    oldRed = 0.0/255.0;
    oldGreen = 0.0/255.0;
    oldBlue = 0.0/255.0;
    brush = 5.0;
    opacity = 1.0;
    tagMudouCor = NO;

}


@end
