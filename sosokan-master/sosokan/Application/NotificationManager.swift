//
//  NotificationManager.swift
//  sosokan
//
//  Created by An Phan on 7/17/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation
import Alamofire

enum NotificationType: String {
    case Message
    case Following
    case Subscription
    
    var identifier: String {
        switch self {
        case .Message:
            return "message"
        case .Following:
            return "following"
        case .Subscription:
            return "subscription"
        }
    }
}
