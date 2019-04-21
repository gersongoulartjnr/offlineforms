//
//  RadioButtonComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "RadioButtonComponent.h"
#import "Option.h"

@implementation RadioButtonComponent 

- (NSString *)getAnswerComponent {
    bool checked = NO;
    NSString *checkedOption = @"";
    for (Option *option in self.options) {
        if(option.checked) {
            checked = YES;
            checkedOption = [checkedOption stringByAppendingString:[NSString stringWithFormat:@"{\"%d\":\"%@\"}", option.idOption, option.value]];
        }
    }
    if (!checked) {
        checkedOption = @"null";
    }
    [super writeToXML:checkedOption];
    return checkedOption;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    UITableView *tableView = [[UITableView alloc] initWithFrame:CGRectMake(20, self.label.frame.size.height + self.label.frame.origin.y + 20, super.view.bounds.size.width - 40, super.view.bounds.size.height - 20) style:UITableViewStyleGrouped];
    tableView.backgroundColor = [UIColor clearColor];
    tableView.opaque = NO;
    tableView.backgroundView = nil;
    tableView.allowsMultipleSelection = NO;
    [self.view addSubview: tableView];
    tableView.dataSource = self;
    tableView.delegate = self;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.options.count;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    Option *option = ((Option *)[self.options objectAtIndex:indexPath.row]);
    UITableViewCell *result = [tableView dequeueReusableCellWithIdentifier:[NSString stringWithFormat:@"%@", option.value]];
    if (nil == result) {
        result = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:[NSString stringWithFormat:@"%@",option.value]] autorelease];
    }
    if (option.checked) {
        result.accessoryType = UITableViewCellAccessoryCheckmark;
    }
    result.textLabel.text = option.text;
    result.textLabel.font = [UIFont systemFontOfSize:14];
    result.textLabel.backgroundColor = [UIColor clearColor];
    result.textLabel.numberOfLines = 0;
    result.selectionStyle = UITableViewCellSelectionStyleNone;
    return result;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    for (NSIndexPath *otherIndexPath in tableView.indexPathsForVisibleRows) {
        [tableView cellForRowAtIndexPath:otherIndexPath].accessoryType = UITableViewCellAccessoryNone;
    }
    cell.accessoryType = UITableViewCellAccessoryCheckmark;
    
    Option *option = ((Option *)[self.options objectAtIndex:indexPath.row]);
    for (Option *otherOption in self.options) {
        otherOption.checked = NO;
    }
    option.checked = YES;
}

@end
