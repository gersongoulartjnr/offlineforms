//
//  AnswersViewController.m
//  tcc
//
//  Created by Marcela Tonon on 22/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "AnswersViewController.h"
#import "SMXMLDocument.h"

@interface AnswersViewController ()
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *elements;
@end

@implementation AnswersViewController

@synthesize tableView = _tableView;

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    SMXMLElement *element = (SMXMLElement *) [[self.elements objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    UITableViewCell *result = [tableView dequeueReusableCellWithIdentifier:[NSString stringWithFormat:@"%@", [element childNamed:@"value"].value]];
    if (!result) {
        result = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:[NSString stringWithFormat:@"%@",[element childNamed:@"value"].value]] autorelease];
    }
    result.detailTextLabel.text = [element childNamed:@"value"].value;
    result.detailTextLabel.font = [UIFont systemFontOfSize:14];
    result.detailTextLabel.numberOfLines = 0;
    
    result.textLabel.text = [element attributeNamed:@"type"];
    result.textLabel.font = [UIFont systemFontOfSize:14];
    result.textLabel.backgroundColor = [UIColor clearColor];
    result.textLabel.numberOfLines = 0;
    
    result.selectionStyle = UITableViewCellSelectionStyleNone;
    return result;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString * cellText = [[[self.elements objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] childNamed:@"value"].value;
    if(!cellText) cellText = @"1";
    UIFont *cellFont = [UIFont systemFontOfSize:14];
    CGSize constraintSize = CGSizeMake(self.view.frame.size.width-100, MAXFLOAT);
    CGSize labelSize = [cellText sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
    return labelSize.height + 10;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [[self.elements objectAtIndex:section] count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [NSString stringWithFormat:@"Answer %d", section+1];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.elements.count;
}

- (void) readXMLFile {
    NSError *error;
    NSString *path = [NSTemporaryDirectory() stringByAppendingString:@"upload.xml"];
    NSData *xmlData = [[NSMutableData alloc] initWithContentsOfFile:path];
    SMXMLDocument *document = [SMXMLDocument documentWithData:xmlData error:&error];
    
    if (error) {
        NSLog(@"Error while parsing the document: %@", error);
        return;
    }
    
    SMXMLElement *form = document.root;
    for (SMXMLElement *answer in [[form lastChild] children]) {
        NSMutableArray *answerArray = [[NSMutableArray alloc] init];
        for (SMXMLElement *question in [answer children]) {
            [answerArray addObject:question];
        }
        [self.elements addObject:answerArray];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor colorWithRed:0.7725 green:0.9804 blue:0.6902 alpha:1];
    self.navigationItem.title = @"Answers";
        
    //colocar imagem no background da view
    UIGraphicsBeginImageContext(self.view.frame.size);
    [[UIImage imageNamed:@"mobile_background.png"] drawInRect:self.view.bounds];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    
    CGRect rect = CGRectMake(self.view.bounds.origin.x, self.view.bounds.origin.y, self.view.bounds.size.width, self.view.bounds.size.height - [[UIApplication sharedApplication] statusBarFrame].size.height);
    self.tableView = [[UITableView alloc] initWithFrame:rect style:UITableViewStyleGrouped];
    self.tableView.backgroundColor = [UIColor clearColor];
    self.tableView.opaque = NO;
    self.tableView.backgroundView = nil;
    [self.view addSubview: self.tableView];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;

    self.elements = [[NSMutableArray alloc] init];
    
    [self readXMLFile];
}

- (void)dealloc {
    [super dealloc];
}

@end
