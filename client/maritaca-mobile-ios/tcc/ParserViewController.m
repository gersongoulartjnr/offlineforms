//
//  ParserViewController.m
//  tcc
//
//  Created by Marcela Tonon on 17/08/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "ParserViewController.h"
#import "SMXMLDocument.h"
#import "TextComponent.h"
#import "NumberComponent.h"
#import "DateComponent.h"
#import "DecimalComponent.h"
#import "CheckBoxComponent.h"
#import "ComboBoxComponent.h"
#import "RadioButtonComponent.h"
#import "BarCodeComponent.h"
#import "MoneyComponent.h"
#import "SliderComponent.h"
#import "PictureComponent.h"
#import "MyAudioComponent.h"
#import "VideoComponent.h"
#import "GeoLocationComponent.h"
#import "DrawComponent.h"
#import "Option.h"

@interface ParserViewController ()

@end

@implementation ParserViewController

+ (NSMutableArray *) parseData:(NSData *) data {
    NSError *error;
    SMXMLDocument *document = [SMXMLDocument documentWithData:data error:&error];
    
    if (error) {
        NSLog(@"Error while parsing the document: %@", error);
        return nil;
    }
    
    NSMutableArray *questions = [[NSMutableArray alloc] init];
    SMXMLElement *form = document.root;
    for (SMXMLElement *question in [[form lastChild] children]) {
        Question *newComponent = nil;
        //procura qual tipo de objeto que eh
        
//------TIPO TEXTO
        if ([[question name] isEqualToString:@"text"]) {
            newComponent = [[TextComponent alloc] init];
            ((TextComponent *)newComponent).textComponent = [[UITextField alloc] init];
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"size"]) { //propriedade
                    ((TextComponent *)newComponent).size = [child.value integerValue];
                }
            }
//------TIPO NUMERO
        } else if ([[question name] isEqualToString:@"number"]) {
            newComponent = [[NumberComponent alloc] init];
            ((NumberComponent *)newComponent).numberComponent = [[UITextField alloc] init];
            ((NumberComponent *)newComponent).min = [[question.attributes valueForKey:@"min"] integerValue];
            ((NumberComponent *)newComponent).max = [[question.attributes valueForKey:@"max"] integerValue];
//------TIPO DATE
        } else if ([[question name] isEqualToString:@"date"]) {
            newComponent = [[DateComponent alloc] init];
            ((DateComponent *)newComponent).dateComponent = [[UIDatePicker alloc] init];
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"format"]) { //propriedade
                    ((DateComponent *)newComponent).format = child.value;
                }
            }
            ((DateComponent *)newComponent).min = [question.attributes valueForKey:@"min"];
            ((DateComponent *)newComponent).max = [question.attributes valueForKey:@"max"];
//------TIPO DECIMAL
        } else if ([[question name] isEqualToString:@"decimal"]) {
            newComponent = [[DecimalComponent alloc] init];
            ((DecimalComponent *)newComponent).decimalComponent = [[UITextField alloc] init];
            ((DecimalComponent *)newComponent).min = [NSNumber numberWithDouble:[[question.attributes valueForKey:@"min"] doubleValue]];
            ((DecimalComponent *)newComponent).max = [NSNumber numberWithDouble:[[question.attributes valueForKey:@"max"] doubleValue]];
//------TIPO MONEY
        } else if ([[question name] isEqualToString:@"money"]) {
            newComponent = [[MoneyComponent alloc] init];
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"currency"]) { //propriedade
                    ((MoneyComponent *)newComponent).currency = child.value;
                }
            }
            ((MoneyComponent *)newComponent).moneyComponent = [[UITextField alloc] init];
            ((MoneyComponent *)newComponent).min = [NSNumber numberWithDouble:[[question.attributes valueForKey:@"min"] doubleValue]];
            ((MoneyComponent *)newComponent).max = [NSNumber numberWithDouble:[[question.attributes valueForKey:@"max"] doubleValue]];
//------TIPO SLIDER
        } else if ([[question name] isEqualToString:@"slider"]) {
            newComponent = [[SliderComponent alloc] init];
            ((SliderComponent *)newComponent).sliderComponent = [[UISlider alloc] init];
            ((SliderComponent *)newComponent).maxValue = [[question.attributes valueForKey:@"max"] integerValue];
//------TIPO PICTURE
        } else if ([[question name] isEqualToString:@"picture"]) {
            newComponent = [[PictureComponent alloc] init];
            ((PictureComponent *)newComponent).pictureComponent = [[UIImageView alloc] init];
//------TIPO AUDIO
        } else if ([[question name] isEqualToString:@"audio"]) {
            newComponent = [[MyAudioComponent alloc] init];
//------TIPO VIDEO
        } else if ([[question name] isEqualToString:@"video"]) {
            newComponent = [[VideoComponent alloc] init];
//------TIPO GEOLOCATION
        } else if ([[question name] isEqualToString:@"geolocation"]) {
            newComponent = [[GeoLocationComponent alloc] init];
            ((GeoLocationComponent *)newComponent).locationManager = [[CLLocationManager alloc] init];
//------TIPO BAR CODE
        } else if ([[question name] isEqualToString:@"barcode"]) {
            newComponent = [[BarCodeComponent alloc] init];
            ((BarCodeComponent *)newComponent).barCodeComponent = [[UITextView alloc] init];
            ((BarCodeComponent *)newComponent).barCodeComponent.text = @"";
//------TIPO DRAW
        } else if ([[question name] isEqualToString:@"draw"]) {
            newComponent = [[DrawComponent  alloc] init];
            ((DrawComponent *)newComponent).drawComponent = [[UIImageView alloc] init];
///------TIPO CHECKBOX
        } else if ([[question name] isEqualToString:@"checkbox"]) {
            newComponent = [[CheckBoxComponent alloc] init];
            ((CheckBoxComponent *)newComponent).options = [[NSMutableArray alloc] init];
            int idOption = 0;
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"option"]) { //propriedade
                    Option *option = [[Option alloc] init];
                    option.value = [child.attributes valueForKey:@"value"];
                    option.checked = ([[child.attributes valueForKey:@"checked"] isEqualToString:@"true"]) ? YES : NO;
                    option.text = child.value;
                    option.idOption = idOption;
                    idOption++;
                    [((CheckBoxComponent *)newComponent).options addObject:option];
                }
            }
///------TIPO COMBOBOX
        } else if ([[question name] isEqualToString:@"combobox"]) {
            newComponent = [[ComboBoxComponent alloc] init];
            ((ComboBoxComponent *)newComponent).options = [[NSMutableArray alloc] init];
            int idOption = 0;
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"option"]) { //propriedade
                    Option *option = [[Option alloc] init];
                    option.value = [child.attributes valueForKey:@"value"];
                    option.checked = ([[child.attributes valueForKey:@"checked"] isEqualToString:@"true"]) ? YES : NO;
                    option.text = child.value;
                    option.idOption = idOption;
                    idOption++;
                    option.idOption = ((ComboBoxComponent *)newComponent).options.count;
                    [((ComboBoxComponent *)newComponent).options addObject:option];
                }
            }
///------TIPO RADIO BUTTON
        } else if ([[question name] isEqualToString:@"radio"]) {
            newComponent = [[RadioButtonComponent alloc] init];
            ((RadioButtonComponent *)newComponent).options = [[NSMutableArray alloc] init];
            int idOption = 0;
            for (SMXMLElement *child in [question children]) {
                if ([child.name isEqualToString:@"option"]) { //propriedade
                    Option *option = [[Option alloc] init];
                    option.value = [child.attributes valueForKey:@"value"];
                    option.checked = ([[child.attributes valueForKey:@"checked"] isEqualToString:@"true"]) ? YES : NO;
                    option.text = child.value;
                    option.idOption = idOption;
                    idOption++;
                    [((RadioButtonComponent *)newComponent).options addObject:option];
                }
            }
        }

        //propriedades que sao iguais para toda questao
        for (SMXMLElement *child in [question children]) {
            if ([child.name isEqualToString:@"label"]) {
                newComponent.label = [[UILabel alloc] init];
                newComponent.label.text = child.value;
            } else if ([child.name isEqualToString:@"help"]) {
                newComponent.help = child.value;
            } else if ([child.name isEqualToString:@"default"]) {
                newComponent.defaultQuestion = child.value;
            }
        }
        //tipo de questao
        newComponent.name = question.name;
        //atributos iguais para todos
        newComponent.title = [form firstChild].value;
        newComponent.idQuestion = [[question.attributes valueForKey:@"id"] integerValue];
        newComponent.next = [[question.attributes valueForKey:@"next"] integerValue];
        newComponent.required = ([[question.attributes valueForKey:@"required"] isEqualToString:@"true"]) ? YES : NO;
        [questions addObject:newComponent];
        [newComponent release];
    }
    
    for (int i=0; i<questions.count; i++) {
        for (Question *question in questions) {
            if (question.idQuestion == [[questions objectAtIndex:i] next]) {
                ((Question *)[questions objectAtIndex:i]).nextQuestion = question;
            }
        }
    }
    
    //pega o nome do formulario. Este nome eh excluido do array de questoes logo depois de setado no title no navigation controller
    [questions addObject:[form firstChild].value];
    return questions;
}


@end
