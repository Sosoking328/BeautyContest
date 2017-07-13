//
//  CategoryMenu.swift
//  sosokan
//

import UIKit
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}


enum CategoryMenuType {
    case expandable
    case selectable
}

class CategoryMenu: NSObject {
    var category: Category?
    var title: String
    var number: Int
    var level: Int
    var iconURL: String?
    var icon: UIImage?
    var type: CategoryMenuType
    
    init(category: Category, level: Int) {
        self.category = category
        self.title = category.localizeName
        self.number = category.localizeNumberOfPosts
        self.level = level
        self.iconURL = category.localizeIconURL
        if let iconURL = self.iconURL {
            self.icon = DataManager.shared.getCacheImage(iconURL)
        }
        self.type = DataManager.shared.allCategories.hasChildren(ofCategory: category) ? .expandable : .selectable
        
        super.init()
    }
    
    init(category: Category?,
         title: String,
         number: Int,
         level: Int,
         iconURL: String?,
         icon: UIImage?,
         type: CategoryMenuType) {
        self.category = category
        self.title = title
        self.number = number
        self.level = level
        self.iconURL = iconURL
        self.icon = icon
        self.type = type
        
        super.init()
    }
}

extension CategoryMenu {
    static func createMenus(_ categories: [Category], needMenuAll: Bool) -> [CategoryMenu] {
        var menus = categories.flatMap { (category) -> CategoryMenu? in
            if category.parentURL == nil {
                return CategoryMenu.init(category: category, level: 0)
            }
            return nil
        }
        menus = menus.sorted(by: { $0.0.category?.sort < $0.1.category?.sort })
        if needMenuAll {
            let total = categories.reduce(0, { $0.0 + $0.1.localizeNumberOfPosts })
            let menuAll = CategoryMenu.init(category: nil,
                                            title: "All".localized(),
                                            number: total,
                                            level: 0,
                                            iconURL: nil,
                                            icon: AppIcons.bigStarIcon,
                                            type: .selectable)
            menus.insert(menuAll, at: 0)
        }
        return menus
    }
}
