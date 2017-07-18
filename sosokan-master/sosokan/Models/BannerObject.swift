//
//  BannerObject.swift
//  sosokan
//
//  Created by An Phan on 12/1/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation
import SwiftyJSON
import CoreLocation

struct BannerObject {
    let categoryId: String
    let regionCode: String
    fileprivate let imageURLString: String
    var imageURL: Foundation.URL? {
        get {
            return Foundation.URL.init(string: self.imageURLString)
        }
    }
    fileprivate let URLString: String
    var URL: Foundation.URL? {
        get {
            return Foundation.URL.init(string: self.URLString)
        }
    }
    let address: String?
    fileprivate let latitude: Double?
    fileprivate let longitude: Double?
    var location: CLLocation? {
        if let latitude = self.latitude, let longitude = self.longitude {
            return CLLocation.init(latitude: latitude, longitude: longitude)
        }
        else {
            return nil
        }
    }
    fileprivate let createdAtString: String?
    var createdAt: Date {
        get {
            if let createdAtString = self.createdAtString {
                return createdAtString.toTime(withFormat: "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timeZone: "UTC") as Date? ?? Date.init()
            }
            else {
                return Date.init()
            }
        }
    }
    
    init(categoryId: String, regionCode: String, imageURLString: String, URLString: String, address: String?, latitude: Double?, longitude: Double?, createdAtString: String?) {
        self.categoryId = categoryId
        self.regionCode = regionCode
        self.imageURLString = imageURLString
        self.URLString = URLString
        self.address = address
        self.latitude = latitude
        self.longitude = longitude
        self.createdAtString = createdAtString
    }
    
    init?(json: JSON) {
        guard let categoryId = json["categoryId"].string else { return nil }
        guard let regionCode = json["language"].string else { return nil }
        guard let imageURLString = json["image"].string else { return nil }
        guard let URLString = json["url"].string else { return nil }
        
        let address = json["address"].stringValue
        let latitude = json["latitude"].doubleValue
        let longitude = json["longitude"].doubleValue
        let createdAtString = json["created"].stringValue
        
        self.init(categoryId: categoryId, regionCode: regionCode, imageURLString: imageURLString, URLString: URLString, address: address, latitude: latitude, longitude: longitude, createdAtString: createdAtString)
    }
}
