//
//  MyAudioComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "MyAudioComponent.h"


@interface MyAudioComponent ()

@property (nonatomic, strong) UIButton *playButton;
@property (nonatomic, strong) UIButton *recordButton;
@property (nonatomic, strong) UIButton *stopButton;
@property (nonatomic, strong) UILabel *recordingLabel;
@property (nonatomic, strong) AVAudioRecorder *audioRecorder;
@property (nonatomic, strong) AVAudioPlayer *audioPlayer;

@end

@implementation MyAudioComponent

@synthesize playButton = _playButton;
@synthesize stopButton = _stopButton;
@synthesize recordButton = _recordButton;
@synthesize recordingLabel = _recordingLabel;
@synthesize audioPlayer = _audioPlayer;
@synthesize audioRecorder = _audioRecorder;

- (void) recordAudio { //SEL
    
    if(!self.audioRecorder){
        [self iniciateAudioRecord];
    }
    
    if (!self.audioRecorder.recording) {
        self.playButton.hidden = YES;
        self.recordingLabel.hidden = NO;
        self.stopButton.hidden = NO;
        self.stopButton.frame = CGRectMake(self.stopButton.frame.origin.x, self.recordButton.frame.origin.y, self.stopButton.frame.size.width, self.stopButton.frame.size.height);
        [self.audioRecorder record];
    }
}

- (void)stopAudio { //SEL
    self.stopButton.hidden = YES;
    self.playButton.hidden = NO;
    self.recordButton.hidden = NO;
    self.recordingLabel.hidden = YES;
    
    if (self.audioRecorder.recording) {
        [self.audioRecorder stop];
    } else if (self.audioPlayer.playing) {
        [self.audioPlayer stop];
    }
}

- (void) playAudio { //SEL
    if (!self.audioRecorder.recording) {
        self.stopButton.hidden = NO;
        self.stopButton.frame = CGRectMake(self.stopButton.frame.origin.x, self.playButton.frame.origin.y, self.stopButton.frame.size.width, self.stopButton.frame.size.height);
        self.recordButton.hidden = YES;
        
        if (self.audioPlayer) {
            [self.audioPlayer release];
        }
        NSError *error;
        
        self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:self.audioRecorder.url error:&error];
        self.audioPlayer.delegate = self;
        
        if (error) {
            NSLog(@"Error: %@", [error localizedDescription]);
        }
        else {
            [self.audioPlayer play];
        }
    }
}

- (void) createView {
    self.recordButton = [super getDefaultButtonAspect];
    [self.recordButton addTarget:self action:@selector(recordAudio) forControlEvents:UIControlEventTouchUpInside];
    [self.recordButton setTitle:@"Record Audio" forState:UIControlStateNormal];
    self.recordButton.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, 130, 40);
    [self.view addSubview:self.recordButton];
    
    self.recordingLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.recordButton.frame.origin.y + self.recordButton.frame.size.height + 20, 300, 40)];
    self.recordingLabel.backgroundColor = [UIColor clearColor];
    self.recordingLabel.text = @"Recording...";
    [self.view addSubview:self.recordingLabel];
    
    self.playButton = [super getDefaultButtonAspect];
    [self.playButton addTarget:self action:@selector(playAudio) forControlEvents:UIControlEventTouchUpInside];
    [self.playButton setTitle:@"Play Audio" forState:UIControlStateNormal];
    self.playButton.frame = CGRectMake(10, self.recordingLabel.frame.origin.y, 130, 40);
    [self.view addSubview:self.playButton];
    
    self.stopButton = [super getDefaultButtonAspect];
    [self.stopButton addTarget:self action:@selector(stopAudio) forControlEvents:UIControlEventTouchUpInside];
    [self.stopButton setTitle:@"Stop Audio" forState:UIControlStateNormal];
    self.stopButton.frame = CGRectMake(160, self.recordButton.frame.origin.y + self.recordButton.frame.size.height + 20, 130, 40);
    [self.view addSubview:self.stopButton];
    
}

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

- (void) iniciateAudioRecord {
    NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *docsDir = [dirPaths objectAtIndex:0];
    
    //pegando a data para formar o nome do arquivo
    NSString *date = [self getCurrentDate];
    NSString *soundFilePath = [docsDir stringByAppendingPathComponent:[NSString stringWithFormat:@"audio_%@.caf", date]];
    self.value = soundFilePath;
    NSURL *soundFileURL = [NSURL fileURLWithPath:soundFilePath];
    
    NSDictionary *recordSettings = [NSDictionary dictionaryWithObjectsAndKeys: [NSNumber numberWithInt:AVAudioQualityMin], AVEncoderAudioQualityKey, [NSNumber numberWithInt:16], AVEncoderBitRateKey, [NSNumber numberWithInt: 2], AVNumberOfChannelsKey, [NSNumber numberWithFloat:44100.0], AVSampleRateKey, nil];
    
    NSError *error = nil;
    
    self.audioRecorder = [[AVAudioRecorder alloc] initWithURL:soundFileURL settings:recordSettings error:&error];
    
    if (error) {
        NSLog(@"error: %@", [error localizedDescription]);
    } else {
        [self.audioRecorder prepareToRecord];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createView];
    
    self.playButton.hidden = YES;
    self.stopButton.hidden = YES;
    self.recordingLabel.hidden = YES;
}

-(void)audioPlayerDidFinishPlaying: (AVAudioPlayer *)player successfully:(BOOL)flag {
    self.recordButton.hidden = NO;
    self.stopButton.hidden = YES;
}

-(void)audioPlayerDecodeErrorDidOccur: (AVAudioPlayer *)player error:(NSError *)error {
    NSLog(@"Decode Error occurred");
}

-(void)audioRecorderDidFinishRecording: (AVAudioRecorder *)recorder successfully:(BOOL)flag {
    if(flag) NSLog(@"SUCCESS");
    else NSLog(@"NAO GRAVOU");
}

-(void)audioRecorderEncodeErrorDidOccur: (AVAudioRecorder *)recorder error:(NSError *)error {
    NSLog(@"Encode Error occurred");
}

- (void)viewDidUnload {
    self.audioPlayer = nil;
    self.audioRecorder = nil;
    self.stopButton = nil;
    self.recordButton = nil;
    self.playButton = nil;
}

- (void)dealloc {
    [self.audioPlayer release];
    [self.audioRecorder release];
    [self.stopButton release];
    [self.playButton release];
    [self.recordButton release];
    [super dealloc];
}

@end
