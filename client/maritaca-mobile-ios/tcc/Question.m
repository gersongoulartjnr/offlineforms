//
//  Question.m
//  tcc
//
//  Created by Marcela Tonon on 11/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"
#import "AnswersViewController.h"
#import "OAuthViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "SMXMLDocument.h"

@interface Question ()
@property (nonatomic, strong) NSMutableArray *answerElements;
@property (nonatomic, strong) UIProgressView *progressView;
@end

@implementation Question

@synthesize name = _name;
@synthesize idQuestion = _idQuestion;
@synthesize next = _next;
@synthesize previous = _previous;
@synthesize help = _help;
@synthesize defaultQuestion = _defaultQuestion;
@synthesize required = _required;
@synthesize label = _label;
@synthesize value = _value;
@synthesize nextQuestion = _nextQuestion;
@synthesize answerElements = _answerElements;
@synthesize delta_iOS7 = _delta_iOS7;
@synthesize progressView = _progressView;

static NSString * const kFormID = @"33351b40-4c06-11e3-b0f6-080027ad6d3a";
static NSString * const kTimestamp = @"17838368723";

- (int)delta_iOS7 {
    if(!_delta_iOS7) {
        _delta_iOS7 = 0;
        if([[[UIDevice currentDevice] systemVersion] floatValue] > 6.1) {
            self.delta_iOS7 = 64;
        }
    }
    return _delta_iOS7;
}

- (UIButton *) getDefaultButtonAspect {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.backgroundColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:0.6];
    button.layer.borderColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1.0].CGColor;
    button.layer.borderWidth = 0.5f;
    button.layer.cornerRadius = 10.0f;
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:UIControlStateHighlighted];
    button.titleLabel.font = [UIFont systemFontOfSize:16];
    button.frame = CGRectMake(0,0,200,40);
    return button;
}

- (bool) checkNumber:(NSString *) number {
    if(number.length != 0) {
        for(int i=0; i < number.length; i++) {
            unichar character = [number characterAtIndex:i];
            if(character == 46) { //46 = "."
                if(([[number componentsSeparatedByString:@"."] count]-1) != 1) {
                    return NO;
                }
            }
            else if (character < 48 || character > 57) { //48 = "0" e 57 = "9"
                return NO;
                break;
            }
        }
    }
    return YES;
}

- (void) inicializeXMLAnswer {
    NSString *filePath = [NSTemporaryDirectory() stringByAppendingString:@"upload.xml"];
    bool fileExists =[[NSFileManager defaultManager] fileExistsAtPath:filePath];
    if(!fileExists) {
        NSString *content = @"<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";
        content = [content stringByAppendingString:@"<collecteddata>"];
        content = [content stringByAppendingString:[NSString stringWithFormat:@"<formId>%@</formId>", kFormID]];
        content = [content stringByAppendingString:@"<answers></answers>"];
        content = [content stringByAppendingString:@"</collecteddata>"];
        [[NSFileManager defaultManager] createFileAtPath:filePath contents:[content dataUsingEncoding:NSUTF8StringEncoding] attributes:nil];
    }    
}

- (void) writeToXML:(NSString *)answer {
    NSLog(@"Answer = %@", answer);
    SMXMLElement *question = [[SMXMLElement alloc] init];
    question.name = @"question";
    
    NSDictionary *dic = [[NSDictionary alloc] initWithObjects:[NSArray arrayWithObjects:[NSString stringWithFormat:@"%d", self.idQuestion], self.name, nil] forKeys:[NSArray arrayWithObjects:@"id", @"type", nil]];
    question.attributes = dic;
    
    SMXMLElement *value = [[SMXMLElement alloc] init];
    value.name = @"value";
    if([answer isEqualToString:@"null"]) {
        value.value = @"";
    } else {
        value.value = answer;
    }
    
    [question setChildren:[NSArray arrayWithObject:value]];
    [self.answerElements addObject:question];
}

- (void) closeXMLAnswers {
    SMXMLElement *answerElement = [[SMXMLElement alloc] init];
    answerElement.name = @"answer";
    
    NSDictionary *dic = [[NSDictionary alloc] initWithObjects:[NSArray arrayWithObjects:kTimestamp, nil] forKeys:[NSArray arrayWithObjects:@"timestamp", nil]];
    answerElement.attributes = dic;
    [answerElement setChildren:self.answerElements];
    
    NSString *filePath = [NSTemporaryDirectory() stringByAppendingString:@"upload.xml"];
    NSFileHandle *filehandlereader = [NSFileHandle fileHandleForReadingAtPath:filePath];
    [filehandlereader seekToFileOffset:[filehandlereader seekToEndOfFile]-26];
    NSData *data = [filehandlereader readDataToEndOfFile];
    
    NSFileHandle *filehandle = [NSFileHandle fileHandleForWritingAtPath:filePath];
    [filehandle seekToFileOffset:[filehandle seekToEndOfFile]-26];
    [self writeElement:answerElement toXMLFileUsingFileHandle:filehandle];
    [filehandle seekToEndOfFile];
    [filehandle writeData:data];
    [filehandle closeFile];
}

- (void)writeElement:(SMXMLElement *)element toXMLFileUsingFileHandle:(NSFileHandle *)filehandle {
    NSString *content = [NSString stringWithFormat:@"<%@", element.name];
    NSArray *atttributeKeys = [element.attributes allKeys];
    NSArray *atttributeValues = [element.attributes allValues];
    for (int i=0; i < atttributeKeys.count; i++) {
        content = [content stringByAppendingString:[NSString stringWithFormat:@" %@=\"%@\"", [atttributeKeys objectAtIndex:i], [atttributeValues objectAtIndex:i]]];
    }
    content = [content stringByAppendingString:@">"];
    [filehandle writeData:[content dataUsingEncoding:NSUTF8StringEncoding]];
    
    for (SMXMLElement *child in [element children]) {
        [self writeElement:child toXMLFileUsingFileHandle:filehandle];
    }
    if(element.value) {
        content = [NSString stringWithFormat:@"%@</%@>", element.value, element.name];
    } else {
        content = [NSString stringWithFormat:@"</%@>", element.name];
    }
    [filehandle writeData:[content dataUsingEncoding:NSUTF8StringEncoding]];
}

- (NSString *) getAnswerComponent {
    [self writeToXML:self.value];
    return self.value;
}

- (bool)validatedAnswer {
    if([[self getAnswerComponent] isEqualToString:@"null"] && self.required) {
        [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"This question is required. Please, answer it!" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
        return NO;
    } else {
        return YES;
    }
}

- (void) backToPreviousQuestion {
    [self.answerElements removeLastObject];
    self.nextQuestion.answerElements = self.answerElements;
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) goToNextQuestion {
    if([self validatedAnswer]) {
        self.nextQuestion.answerElements = self.answerElements;
        self.nextQuestion.statusProgressView = self.progressView.progress;
        self.nextQuestion.numberOfQuestions = self.numberOfQuestions;
        [self.navigationController pushViewController:self.nextQuestion animated:YES];
    }
}

- (void) doneAnswers {
    if([self validatedAnswer]) {
        [[[UIAlertView alloc] initWithTitle:@"Confirmation" message:@"Do you want to save the data?" delegate:self cancelButtonTitle:nil otherButtonTitles:@"Save", @"Cancel", nil] show];
    }
}

- (void) cancelCollect {
    [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"Are you sure you want to cancel this collect?" delegate:self cancelButtonTitle:nil otherButtonTitles:@"Yes", @"No", nil] show];
}

-(void) showHelp {
    NSString *help = @"";
    if([self.help isEqualToString:@""] || !self.help) {
        help = @"Help is not available.";
    } else {
        help = self.help;
    }
    UIActionSheet *showHelp = [[UIActionSheet alloc] initWithTitle:help delegate:self cancelButtonTitle:@"Thanks" destructiveButtonTitle:nil otherButtonTitles:nil];
	[showHelp showInView:self.view];
	[showHelp release];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if([alertView.title isEqualToString:@"Warning"]) {
        NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
        if([title isEqualToString:@"Yes"]) {
            for (SMXMLElement *question in self.answerElements) {
                if(([[question attributeNamed:@"type"] isEqualToString:@"audio"]   ||
                   [[question attributeNamed:@"type"] isEqualToString:@"picture"] ||
                   [[question attributeNamed:@"type"] isEqualToString:@"video"]   ||
                   [[question attributeNamed:@"type"] isEqualToString:@"draw"]) &&
                   [question childNamed:@"value"].value && ![[question childNamed:@"value"].value isEqualToString:@""]){
                    NSLog(@"entrou no if com name = %@", [question attributeNamed:@"type"]);
                    NSLog(@"Removendo arquivo %@...", [question childNamed:@"value"].value);
                    NSError *error = nil;
                    [[NSFileManager defaultManager] removeItemAtPath:[question childNamed:@"value"].value error:&error];
                }
            }
            if(([self.name isEqualToString:@"audio"]   ||
               [self.name isEqualToString:@"picture"] ||
               [self.name isEqualToString:@"video"]   ||
               [self.name isEqualToString:@"draw"]) &&
               ![self.value isEqualToString:@""]){
                NSLog(@"entrou no if com name = %@", self.name);
                NSLog(@"Removendo arquivo %@...", self.value);
                NSError *error = nil;
                [[NSFileManager defaultManager] removeItemAtPath:self.value error:&error];
            }
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    } else if([alertView.title isEqualToString:@"Confirmation"]) {
        NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
        if([title isEqualToString:@"Save"]) {
            [self closeXMLAnswers];
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}

- (UIButton *) buttonWithImages: (NSArray *)images {
    UIImage *image = [UIImage imageNamed:[images objectAtIndex:0]];
    UIImage *imageOver = [UIImage imageNamed:[images objectAtIndex:1]];
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    [btn setImage:image forState:UIControlStateNormal];
    [btn setImage:imageOver forState:UIControlStateHighlighted];
    btn.frame = CGRectMake(0, 0, image.size.width, image.size.height);
    [self.view addSubview:btn];
    return btn;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationController.title = self.title;
    
    if(self.idQuestion == 1) {
        [self inicializeXMLAnswer];
        self.answerElements = [[NSMutableArray alloc] init];
    }
    
    //colocar imagem no background da view
    UIGraphicsBeginImageContext(self.view.frame.size);
    [[UIImage imageNamed:@"mobile_background.png"] drawInRect:self.view.bounds];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    //ajusta tamanho label
    CGSize size = [self.label.text sizeWithFont:self.label.font];
    self.label.numberOfLines = round((double)size.width / (self.view.frame.size.width-10)) + 1;
    NSLog(@"number of lines: %d", self.label.numberOfLines);
    self.label.frame = CGRectMake(10, self.delta_iOS7 + 15, self.view.frame.size.width-10, 25*self.label.numberOfLines);
    self.label.backgroundColor = [UIColor clearColor];
        
    self.value = @"null";
    
    self.progressView = [[UIProgressView alloc] initWithFrame:CGRectMake(0, self.delta_iOS7 - [[UIApplication sharedApplication] statusBarFrame].size.height + self.view.frame.origin.y, self.view.frame.size.width, 50)];
    self.progressView.progressTintColor = [UIColor colorWithRed:0.05098 green:0.29804 blue:0.05490 alpha:1];
    self.progressView.progress = self.statusProgressView;
    self.progressView.progress += (float)1/self.numberOfQuestions;    
    [self.view addSubview:self.label];
    [self.view addSubview:self.progressView];
    
    self.navigationItem.hidesBackButton = YES;

    if(self.idQuestion != 1) {
        UIButton *btnPrevious = [self buttonWithImages:[NSArray arrayWithObjects:@"ic_menu_previous.png", @"ic_menu_previous_over.png", nil]];
        btnPrevious.frame = CGRectMake(0, self.delta_iOS7 + self.view.frame.size.height/2-btnPrevious.frame.size.height/2, btnPrevious.frame.size.width, btnPrevious.frame.size.height);
        [btnPrevious addTarget:self action:@selector(backToPreviousQuestion) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:btnPrevious];
     }

    UIButton *btnHelp = [self buttonWithImages:[NSArray arrayWithObjects:@"ic_menu_help.png", @"ic_menu_help_over.png", nil]];
    [btnHelp addTarget:self action:@selector(showHelp) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnHelpBar = [[UIBarButtonItem alloc] initWithCustomView:btnHelp];
    
    UIButton *btnMaritaca = [self buttonWithImages:[NSArray arrayWithObjects:@"form_logo.png", @"form_logo.png", nil]];
    btnMaritaca.frame = CGRectMake(0, 0, 40, 40);
    UIBarButtonItem *btnMaritacaBar = [[UIBarButtonItem alloc] initWithCustomView:btnMaritaca];
    self.navigationItem.leftBarButtonItem = btnMaritacaBar;
    
    UIButton *btnCancel = [self buttonWithImages:[NSArray arrayWithObjects:@"ic_menu_cancel.png", @"ic_menu_cancel_over.png", nil]];
    [btnCancel addTarget:self action:@selector(cancelCollect) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *btnCancelBar = [[UIBarButtonItem alloc] initWithCustomView:btnCancel];
    
     if(self.next != -1) {
         UIButton *btnNext = [self buttonWithImages:[NSArray arrayWithObjects:@"ic_menu_next.png", @"ic_menu_next_over.png", nil]];
         btnNext.frame = CGRectMake(self.view.frame.size.width-btnNext.frame.size.width, self.delta_iOS7 + self.view.frame.size.height/2-btnNext.frame.size.height/2, btnNext.frame.size.width, btnNext.frame.size.height);
         [btnNext addTarget:self action:@selector(goToNextQuestion) forControlEvents:UIControlEventTouchUpInside];
         [self.view addSubview:btnNext];
         self.navigationItem.rightBarButtonItems = [NSArray arrayWithObjects:btnCancelBar, btnHelpBar, nil];
     } else {
         UIBarButtonItem *btnDone = [[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStylePlain target:self action:@selector(doneAnswers)];
         [self.navigationItem setRightBarButtonItems:[NSArray arrayWithObjects:btnDone, btnCancelBar, btnHelpBar, nil]];
     }

}
@end
