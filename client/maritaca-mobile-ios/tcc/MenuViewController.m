//
//  MenuViewController.m
//  tcc
//
//  Created by Marcela Tonon on 09/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "MenuViewController.h"
#import "ParserViewController.h"
#import "UploadViewController.h"
#import "AnswersViewController.h"
#import "Question.h"

@interface MenuViewController ()

@property (strong, nonatomic) Question *firstQuestion;

@end

@implementation MenuViewController

@synthesize btnCollect = _btnCollect;
@synthesize firstQuestion = _firstQuestion;

- (IBAction)btnCollect:(UIButton *)sender {
    [self.navigationController pushViewController:self.firstQuestion animated:YES];
    [self.firstQuestion release];
}

- (IBAction)btnAnswers:(UIButton *)sender {
    NSString *path = [NSTemporaryDirectory() stringByAppendingString:@"upload.xml"];
    bool fileExists =[[NSFileManager defaultManager] fileExistsAtPath:path];
    if(!fileExists) {
        [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"There is no data to view."  delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
    } else {
        AnswersViewController *avc = [[AnswersViewController alloc] init];
        [self.navigationController pushViewController:avc animated:YES];
    }
}

- (IBAction)btnSendData:(UIButton *)sender {
    UploadViewController *uvc = [[UploadViewController alloc] init];
    
/*    UIView *sendView = [[UIView alloc] initWithFrame:self.view.bounds];
    self.view.backgroundColor = [UIColor whiteColor];
    sendView.alpha = 1;
    [self.view addSubview:sendView];*/
    
    [uvc sendDataToServer];
}

- (IBAction)btnTellAFriend:(UIButton *)sender {
    NSString *body = @"mailto:?subject=Do you know Maritaca?&body=Hi!\n\nDo you know Maritaca?\n\nTry to visit its website: http://www.maritaca.unifesp.br.\n\n\nBest regards";
    NSString *url = [body stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    [[UIApplication sharedApplication]  openURL:[NSURL URLWithString: url]];
}

- (IBAction)btnSettings:(UIButton *)sender {
}


- (void) parseXML {
    NSString *path = [[[NSBundle mainBundle] bundlePath] stringByAppendingString:@"/teste.xml"];
    NSData *xmlData = [[NSMutableData alloc] initWithContentsOfFile:path];
    
    NSMutableArray *objetos = [ParserViewController parseData:xmlData];
    for(Question *question in objetos) {
        if(question.idQuestion == 1) {
            self.firstQuestion = question;
            break;
        }
    }
    self.title = objetos.lastObject;
    [objetos removeLastObject];
    self.firstQuestion.numberOfQuestions = objetos.count;
}

- (void)viewDidAppear:(BOOL)animated {
    self.btnCollect.enabled = NO;
    [self parseXML];
    self.btnCollect.enabled = YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.btnCollect setMyBorder:0.0 withColor:nil];
    [self.btnAnswers setMyBorder:0.0 withColor:nil];
    [self.btnAbout setMyBorder:0.0 withColor:nil];
    [self.btnSend setMyBorder:0.0 withColor:nil];
    [self.btnSettings setMyBorder:0.0 withColor:nil];
    [self.btnTellaFriend setMyBorder:0.0 withColor:nil];

    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.hidesBackButton = YES;
    self.navigationController.navigationBar.tintColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1]; //verde do maritaca

    UIButton *btnMaritaca = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnMaritaca setImage:[UIImage imageNamed:@"form_logo.png"] forState:UIControlStateNormal];
    [btnMaritaca setImage:[UIImage imageNamed:@"form_logo.png"] forState:UIControlStateHighlighted];
    btnMaritaca.frame = CGRectMake(0, 0, 40, 40);
    UIBarButtonItem *btnMaritacaBar = [[UIBarButtonItem alloc] initWithCustomView:btnMaritaca];
    self.navigationItem.leftBarButtonItem = btnMaritacaBar;
    
    //colocar imagem no background da view
    UIGraphicsBeginImageContext(self.view.frame.size);
    [[UIImage imageNamed:@"mobile_background.png"] drawInRect:self.view.bounds];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    
    self.btnCollect.enabled = NO;
}

- (void)dealloc {
    [_btnCollect release];
    [_btnAnswers release];
    [_btnSend release];
    [_btnTellaFriend release];
    [_btnAbout release];
    [_btnSettings release];
    [super dealloc];
}

@end
