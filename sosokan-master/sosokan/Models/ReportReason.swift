//
//  ReportReason.swift
//  sosokan
//

import Foundation
import SwiftyJSON

extension Keys {
    struct ReportReason {
        static let id: String = "id"
        static let reason: String = "reason"
    }
}

func ==(left: ReportReason?, right: ReportReason?) -> Bool {
    return left?.id == right?.id
        && left?.reason == right?.reason
}

struct ReportReason {
    var id: Int
    var reason: String
    
    init(id: Int, reason: String) {
        self.id = id
        self.reason = reason
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.ReportReason.id].int,
            let reason = json[Keys.ReportReason.reason].string
            else { return nil }
        self.id = id
        self.reason = reason
    }
}
