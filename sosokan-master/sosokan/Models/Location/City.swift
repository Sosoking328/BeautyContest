//
//  City.swift
//  sosokan
//

import Foundation
import SwiftyJSON
import CoreLocation

func ==(left: City?, right: City?) -> Bool {
    return left?.name == right?.name
        && left?.latitude == right?.latitude
        && left?.longitude == right?.longitude
        && left?.stateName == right?.stateName
        && left?.stateCode == right?.stateCode
}

struct City {
    var name: String
    var latitude: Double?
    var longitude: Double?
    var stateName: String?
    var stateCode: String?
    
    init(name: String,
         latitude: Double?,
         longitude: Double?,
         stateName: String?,
         stateCode: String?) {
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.stateName = stateName
        self.stateCode = stateCode
    }
    
    init?(json: JSON) {
        if let name = json["city"].string, !name.isEmpty {
            self.name = name
            self.latitude = json["latitude"].double
            self.longitude = json["longitude"].double
            self.stateName = json["state"].string
        }
        else {
            return nil
        }
    }
}

extension City {
    var location: CLLocation? {
        if let longitude = self.longitude,
            let latitude = self.latitude {
            return CLLocation.init(latitude: latitude, longitude: longitude)
        }
        return nil
    }
}

extension City {
    static func availableCities(_ language: SupportedLanguage) -> [City] {
        if language == .english {
            if let path = Bundle.main.path(forResource: "us_cities", ofType: "json"),
                let data = try? Data.init(contentsOf: URL(fileURLWithPath: path)) {
                let json = JSON.init(data: data)
                let cities = json.array?.flatMap({ City.init(json: $0) }) ?? []
                return cities
            }
            return []
        }
        return []
    }
}
