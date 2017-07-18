//
//  PImage.swift
//  sosokan
//

import Foundation
import SwiftyJSON

extension Keys {
    struct PImage {
        static let id = "id"
        static let URL = "image"
        static let width = "width"
        static let height = "height"
    }
}

struct PImage {
    var id: Int
    var URL: String
    var width: Double
    var height: Double
    
    init(id: Int,
         URL: String,
         width: Double,
         height: Double) {
        self.id = id
        self.URL = URL
        self.width = width
        self.height = height
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.PImage.id].int,
            let URL = json[Keys.PImage.URL].string, !URL.isEmpty,
            let width = json[Keys.PImage.width].int, width != 0,
            let height = json[Keys.PImage.height].int, height != 0
            else {
                return nil
        }
        self.id = id
        self.URL = URL
        self.width = Double.init(width)
        self.height = Double.init(height)
    }
}

extension PImage {
    var absoluteURL: Foundation.URL? {
        return Foundation.URL.init(string: self.URL)
    }
}

