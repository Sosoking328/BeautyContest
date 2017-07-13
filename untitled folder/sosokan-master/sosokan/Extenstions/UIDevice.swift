//
//  UIDevice.swift
//  sosokan
//
//  Created by An Phan on 6/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

extension UIDevice {
    enum DeviceType {
        case iPhone35
        case iPhone40
        case iPhone47
        case iPhone55
        case iPad
        case TV
        
        var isPhone: Bool {
            return [ .iPhone35, .iPhone40, .iPhone47, .iPhone55 ].contains(self)
        }
    }
    
    var deviceType: DeviceType? {
        switch UIDevice.current.userInterfaceIdiom {
        case .tv:
            return .TV
            
        case .pad:
            return .iPad
            
        case .phone:
            let screenSize = UIScreen.main.bounds.size
            let height = max(screenSize.width, screenSize.height)
            
            switch height {
            case 480:
                return .iPhone35
            case 568:
                return .iPhone40
            case 667:
                return .iPhone47
            case 736:
                return .iPhone55
            default:
                return nil
            }
            
        case .unspecified:
            return nil
        default:
            return nil
        }
    }
}
