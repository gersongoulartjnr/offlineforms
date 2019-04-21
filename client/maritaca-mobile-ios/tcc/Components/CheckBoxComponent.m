//
//  CheckBoxComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "CheckBoxComponent.h"
#import "Option.h"

@implementation CheckBoxComponent

@synthesize checkboxComponent = _checkboxComponent;

- (NSString *)getAnswerComponent {
    bool checked = NO;
    NSString *checkedOptions = @"{";
    for (Option *option in self.options) {
        if(option.checked) {
            checked = YES;
            checkedOptions = [checkedOptions stringByAppendingString:[NSString stringWithFormat:@"\"%d\":\"%@\",", option.idOption, option.value]];
        }
    }
    checkedOptions = [checkedOptions substringToIndex:checkedOptions.length-1];
    checkedOptions = [checkedOptions stringByAppendingString:@"}"];
    if (!checked) {
        checkedOptions = @"null";
    }
    [super writeToXML:checkedOptions];
    return checkedOptions;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.checkboxComponent = [[UITableView alloc] initWithFrame:CGRectMake(20, self.label.frame.origin.y + self.label.frame.size.height + 20, super.view.frame.size.width - self.label.frame.origin.x-40, super.view.frame.size.height - 20) style:UITableViewStyleGrouped];
    self.checkboxComponent.backgroundColor = [UIColor clearColor];
    self.checkboxComponent.opaque = NO;
    self.checkboxComponent.backgroundView = nil;
    [self.view addSubview: self.checkboxComponent];
    self.checkboxComponent.dataSource = self;
    self.checkboxComponent.delegate = self;
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
    Option *option = ((Option *)[self.options objectAtIndex:indexPath.row]);
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark) {
        cell.accessoryType = UITableViewCellAccessoryNone;
        option.checked = NO;
    } else {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        option.checked = YES;
    }
}

@end
