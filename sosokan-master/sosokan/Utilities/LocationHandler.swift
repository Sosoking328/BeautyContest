//
//  LocationHandler.swift
//  sosokan

import Foundation
import CoreLocation

/**
* Talks with the built in GPS.
*/

protocol LocationHandlerDelegate {
    /**
        Called when location data is received.
        :param: location location that was acquired
    */
    func locationDetected(_ location: CLLocation)
    /**
        Called when location cannot be found.
        :param: error error which occured while getting locations.
    */
    func locationDiscoveryFailed(_ error: NSError)
}

class LocationHandler: NSObject, CLLocationManagerDelegate {
    
    /// delegate object
    var delegate: LocationHandlerDelegate?
    
    let locationManager: CLLocationManager = {
        var manager = CLLocationManager()
        manager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        manager.distanceFilter = 1.0
        return manager
    }()
    
    override init() {
        super.init()
        locationManager.delegate = self
    }
    
    /**
    * Tells the location manager to start updating the user's location
    */
    func startLocationUpdate() {
        if locationManager.responds(to: #selector(CLLocationManager.requestWhenInUseAuthorization)) {
            locationManager.requestWhenInUseAuthorization()
        }
        locationManager.startUpdatingLocation()
    }
    
    /**
    * Stops the location manager to update the location
    */
    func stopLocationUpdate() {
        locationManager.stopUpdatingLocation()
    }
    
    
    // MARK - location & map methods
    /**
    * Tells the delegate that the authorization status for the application changed.
    * @param manager The location manager object reporting the event.
    * @param status The new authorization status for the application.
    */
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == CLAuthorizationStatus.denied || status == CLAuthorizationStatus.restricted {
            NotificationCenter.default.post(name: Notification.Name(rawValue: "Location_Tracking_Disabled"), object: nil)
            stopLocationUpdate()
        }
        else if status == CLAuthorizationStatus.authorizedAlways {
            startLocationUpdate()
        }
    }
    
    /**
    * Tells the delegate that the location manager was unable to retrieve a location value.
    * @param manager The location manager object that was unable to retrieve the location.
    * @param error The error object containing the reason the location or heading could not be retrieved.
    */
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        stopLocationUpdate()
        delegate?.locationDiscoveryFailed(error as NSError)
    }
    
    /**
    * Tells the delegate that new location data is available.
    * @param manager The location manager object that generated the update event.
    * @param locations An array of CLLocation objects containing the location data. This array always contains at least one object representing the current location.
    */
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        stopLocationUpdate()
        delegate?.locationDetected(locations[0])
    }
}
