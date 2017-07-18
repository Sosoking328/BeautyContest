//
//  ReportMenuItem.swift
//  sosokan
//
//  Created by An Phan on 1/11/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit

enum ReportMenuItemType {
    case select
    case text
}

func ==(left: ReportMenuItem?, right: ReportMenuItem?) -> Bool {
    return left?.reason == right?.reason
        && left?.title == right?.title
        && left?.type == right?.type
}

struct ReportMenuItem {
    var reason: ReportReason?
    var title: String
    var type: ReportMenuItemType
    
    init(reason: ReportReason?, title: String, type: ReportMenuItemType) {
        self.reason = reason
        self.title = title
        self.type = type
    }
}
