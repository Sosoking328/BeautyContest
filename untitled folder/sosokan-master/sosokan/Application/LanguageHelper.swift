//
//  LanguageHelper.swift
//  sosokan
//
//  Created by An Phan on 12/1/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class LanguageHelper: NSObject {
    static let sharedInstance: LanguageHelper = LanguageHelper.init()
    
    func getRegionCode() -> String {
        if IS_ENGLISH {
            return "en"
        }
        else {
            return "zh-hans"
        }
    }
}
