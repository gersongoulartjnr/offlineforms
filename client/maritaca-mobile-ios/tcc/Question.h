//
//  Question.h
//  tcc
//
//  Created by Marcela Tonon on 11/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Question : UIViewController <UIAlertViewDelegate, UIActionSheetDelegate>

@property (nonatomic, strong) NSString *name;
@property (nonatomic) int idQuestion;
@property (nonatomic) int next;
@property (nonatomic) int previous;
@property (nonatomic, strong) NSString *help;
@property (nonatomic, strong) NSString *defaultQuestion;
@property (nonatomic) bool required;
@property (nonatomic, strong) NSString *value;
@property (nonatomic, strong) UILabel *label;
@property (nonatomic, strong) Question *nextQuestion;
@property (nonatomic) int delta_iOS7;
@property (nonatomic) int numberOfQuestions;
@property (nonatomic) float statusProgressView;

- (UIButton *) getDefaultButtonAspect;
- (bool) checkNumber:(NSString *)number;
- (NSString *) getAnswerComponent;
- (bool) validatedAnswer;
- (void) writeToXML:(NSString *)answer;

@end


/*@Attribute(required = true)
 protected Integer id;
 
 @Attribute(required = false)
 protected Integer next;
 
 @Attribute(required = false)
 protected Integer previous;
 
 @Element(required = false)
 protected String help;
 
 @Element(name = "default", required = false)
 protected String _default;
 
 @Attribute(required = false)
 protected Boolean required;
 
 @Element
 protected String label;
 
 protected Object value;
 
 
 
 
 // Abstract methods
public abstract ComponentType getComponentType();

public abstract Object getValue();

public abstract View getLayout(ControllerActivity activity);

public abstract boolean validate();

public abstract void save(View answer);

public List<Comparison> getComparisons() {
    return comparisons;
}

public void setComparisons(List<Comparison> comparisons) {
    this.comparisons = comparisons;
}


 */