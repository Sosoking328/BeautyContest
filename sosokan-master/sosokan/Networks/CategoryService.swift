//
//  CategoryService.swift
//  sosokan
//
//  Created by An Phan on 5/17/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

class CategoryService: BaseService {
    static let shared = CategoryService()
    
    func fetchCategoriesWithCompletionHandler(completionHandler: @escaping CompletionHandler) {
        let request = "\(APIEndPoint.categories.path)?limit=10000&offset=0"
        
        GET(request, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                
                var categories = json.array?.flatMap({ Category(json: $0) })
                categories = categories?.filter({ (category) -> Bool in
                    return category.nameEnglish.lowercased().trim() != "all"
                })
                
                DataManager.shared.allCategories = categories!
                
                // For first load, featured categories will display all root categories.
                if DataManager.shared.featureCategories.count == 0 {
                    DataManager.shared.featureCategories = categories!.filter({ $0.parentURL == nil })
                    DataManager.shared.savedCategories = categories!.filter({ $0.parentURL == nil })
                }
                
                NotificationCenter.default.post(name: Notification.Name(Notifications.fetchedCategories), object: categories)
                
                completionHandler(json, nil)
            }
        }
    }
}
