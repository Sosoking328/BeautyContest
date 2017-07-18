//
//  Filter Options.swift
//  sosokan
//

import Foundation
import CoreLocation

func ==(left: FilterOptions?, right: FilterOptions?) -> Bool {
    return left?.category == right?.category
        && left?.state == right?.state
        && left?.city == right?.city
        && left?.property == right?.property
        && left?.selectedPrice == right?.selectedPrice
        && left?.selectedDistance == right?.selectedDistance
        && left?.currentLocation == right?.currentLocation
}

struct FilterOptions {
    var category: Category?
    var state: State?
    var city: City?
    var property: Property?
    var selectedPrice: Int?
    var selectedDistance: Int?
    var currentLocation: CLLocation?
    
    init(category: Category?,
         state: State?,
         city: City?,
         property: Property?,
         selectedPrice: Int?,
         selectedDistance: Int?,
         currentLocation: CLLocation?) {
        self.category = category
        self.city = city
        self.property = property
        self.selectedPrice = selectedPrice
        self.selectedDistance = selectedDistance
        self.currentLocation = currentLocation
    }
}

extension FilterOptions {
    
}

extension FilterOptions {
    static func defaultOptions() -> FilterOptions {
        return FilterOptions.init(category: nil,
                                  state: nil,
                                  city: nil,
                                  property: nil,
                                  selectedPrice: nil,
                                  selectedDistance: nil,
                                  currentLocation: nil)
    }
}
