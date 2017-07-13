//
//  ViewType.swift
//  sosokan
//

import Foundation

enum ViewType {
    case grid
    case list
}

extension ViewType {
    var numberOfColumns: Int {
        switch self {
        case .grid:
            return 2
        case .list:
            return 1
        }
    }
    
    var defaultBarButtonIcon: UIImage {
        switch self {
        case .grid:
            return AppIcons.viewByGridIcon
        case .list:
            return AppIcons.viewByListIcon
        }
    }
}

extension ViewType {
    static func defaultViewType() -> ViewType {
        return .grid
    }
}
