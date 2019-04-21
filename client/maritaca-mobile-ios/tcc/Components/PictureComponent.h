//
//  PictureComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"

@interface PictureComponent : Question <UIImagePickerControllerDelegate>

@property (nonatomic, strong) UIImage *picture;
@property (nonatomic, strong) UIImageView *pictureComponent;
/*private File picture;
 */

@end
