//
//  GeoLocationComponent.m
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "GeoLocationComponent.h"
#import <CoreLocation/CoreLocation.h>

@interface GeoLocationComponent ()

@property (nonatomic, strong) UIButton *getLocationButton;

@end


@implementation GeoLocationComponent

@synthesize getLocationButton = _getLocationButton;
@synthesize latitudelabel = _latitudelabel;
@synthesize longitudelabel = _longitudelabel;
@synthesize locationManager = _locationManager;
@synthesize latitude = _latitude;
@synthesize longitude = _longitude;
@synthesize isGpsEnabled = _isGpsEnabled;

- (NSString *)getAnswerComponent {
    NSString *location = @"";
    if(!self.latitude || !self.longitude) {
        location = @"null";
    } else {
        location = [NSString stringWithFormat:@"{%f;%f}", self.latitude, self.longitude];
    }
    [super writeToXML:location];
    return location;
}

- (void) getCurrentLocation {
    self.isGpsEnabled = [CLLocationManager locationServicesEnabled];
    if(self.isGpsEnabled) {
        self.locationManager.delegate = self;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
        self.locationManager.distanceFilter = kCLDistanceFilterNone;
        self.latitudelabel.text = @"Latitude:";
        self.longitudelabel.text = @"Longitude:";
        [self.locationManager startUpdatingLocation];
    } else {
        [[[[UIAlertView alloc] initWithTitle:@"Warning" message:@"Location services are disabled. Please, open Location Services in Settings to enable it." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] autorelease] show];
        self.latitudelabel.numberOfLines = 4;
        self.latitudelabel.text = @"Can't get location.";
        self.longitudelabel.text = @"";
    }
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    CLLocation *location = [locations lastObject];
    self.latitude = location.coordinate.latitude;
    self.longitude = location.coordinate.longitude;
    self.latitudelabel.text = [NSString stringWithFormat:@"Latitude: %f", self.latitude];
    self.longitudelabel.text = [NSString stringWithFormat:@"Longitude: %f", self.longitude];
    [self.locationManager stopUpdatingLocation];
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"erro = %@", error);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.getLocationButton = [super getDefaultButtonAspect];
    self.getLocationButton.frame = CGRectMake(10, self.label.frame.origin.y + self.label.frame.size.height + 20, self.getLocationButton.frame.size.width, self.getLocationButton.frame.size.height);
    [self.getLocationButton addTarget:self action:@selector(getCurrentLocation) forControlEvents:UIControlEventTouchUpInside];
    [self.getLocationButton setTitle:@"Get Location" forState:UIControlStateNormal];

    self.latitudelabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.getLocationButton.frame.origin.y + self.getLocationButton.frame.size.height + 20, 200, 25)];
    self.latitudelabel.backgroundColor = [UIColor clearColor];
    self.longitudelabel = [[UILabel alloc] initWithFrame:CGRectMake(10, self.latitudelabel.frame.origin.y + self.latitudelabel.frame.size.height, 200, 25)];
    self.longitudelabel.backgroundColor = [UIColor clearColor];
    
    [self.view addSubview:self.latitudelabel];
    [self.view addSubview:self.longitudelabel];
    [self.view addSubview:self.getLocationButton];
}

@end
