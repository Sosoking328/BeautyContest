//
//  Double.swift
//  sosokan
//
//  Created by An Phan on 7/16/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation

extension Double {
    func toNSDate() -> Date {
        return Date.init(timeIntervalSince1970: self)
    }
    
    /// Rounds the double to decimal places value
    func roundTo(places:Int) -> Double {
        let divisor = pow(10.0, Double(places))
        return (self * divisor).rounded() / divisor
    }
}
