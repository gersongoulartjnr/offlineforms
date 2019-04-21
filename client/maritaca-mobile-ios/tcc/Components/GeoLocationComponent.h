//
//  GeoLocationComponent.h
//  tcc
//
//  Created by Marcela Tonon on 10/10/13.
//  Copyright (c) 2013 Marcela Tonon. All rights reserved.
//

#import "Question.h"
#import <CoreLocation/CoreLocation.h>

@interface GeoLocationComponent : Question <CLLocationManagerDelegate>

@property (nonatomic,strong) CLLocationManager *locationManager;
@property (nonatomic, strong) UILabel *latitudelabel;
@property (nonatomic, strong) UILabel *longitudelabel;
@property (nonatomic) CGFloat latitude;
@property (nonatomic) CGFloat longitude;
@property (nonatomic) bool isGpsEnabled;


/*private TextView txtLatitude;
 private TextView txtLongitude;
 
 private Location location;
 private Double latitude;
 private Double longitude;
 private boolean isNetworkEnabled;
 private boolean isGpsEnabled;
 private static final long MIN_DISTANCE = 1;
 private static final long MIN_TIME = 60000;
 */
@end
