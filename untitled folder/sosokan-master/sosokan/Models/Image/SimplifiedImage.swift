//
//  SimplifiedImageb.swift
//  sosokan
//

import UIKit
import SwiftyJSON

extension Keys {
    struct SimplifiedImage {
        static let URL = "url"
        static let width = "width"
        static let height = "height"
    }
}

class SimplifiedImage: NSObject {
    var URL: String
    var width: Double
    var height: Double
    
    init(URL: String,
        width: Double,
        height: Double) {
        self.URL = URL
        self.width = width
        self.height = height
        
        super.init()
    }
    
    init?(json: JSON) {
        guard let URL = json[Keys.SimplifiedImage.URL].string, !URL.isEmpty,
            let widthString = json[Keys.SimplifiedImage.width].string, !widthString.isEmpty,
            let width = Double.init(widthString), width != 0,
            let heightString = json[Keys.SimplifiedImage.height].string, !heightString.isEmpty,
            let height = Double.init(heightString), height != 0
            else {
                return nil
        }
        self.URL = URL
        self.width = width
        self.height = height
        
        super.init()
    }
}

extension SimplifiedImage {
    var absoluteURL: Foundation.URL? {
        return Foundation.URL.init(string: self.URL)
    }
}
