//
//  DataManager.swift
//  sosokan
//
//  Created by An Phan on 6/11/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation
import Then
import SDWebImage
import CoreLocation

typealias JSONType = [String: AnyObject]

class DataManager {
    
    init() {
    }
    
    static let shared = DataManager()
    
    // Posts list.
    var posts = [SimplifiedPost]()
    var myPosts = [SimplifiedPost]()
    var favoritedPosts = [SimplifiedPost]()
    
    // Data list for showing at the home page.
    var categories = [Category]()
    var recentSearches = [SimplifiedPost]()
    var featuredPosts = [SimplifiedPost]()
    var nearbyPosts = [SimplifiedPost]()
    
    var notificationNumbers: (message: Int, following: Int, subscription: Int) = (message: 0, following: 0, subscription: 0)
    var viewType: ViewType = ViewType.defaultViewType()
    
    var allCategories = [Category]()
    var featureCategories = [Category]()
    var filterOptions: FilterOptions = FilterOptions.defaultOptions()
    var myCity: City?
    var splashVideo: SplashVideo?
    var userProfile: Profile?
    var currentUser: User?
    
    var currentLocation: CLLocation?
    
    let featuredCategoriesKey = "Sosokan.FeaturedCategories"
    var savedCategories: [Category]? {
        get {
            if let data: Data = UserDefaults.standard.object(forKey: featuredCategoriesKey) as? Data {
                return NSKeyedUnarchiver.unarchiveObject(with: data) as! [Category]?
            }
            
            return nil
        }
        set {
            if (newValue == nil) {
                UserDefaults.standard.removeObject(forKey: featuredCategoriesKey)
            } else {
                let data = NSKeyedArchiver.archivedData(withRootObject: newValue!)
                UserDefaults.standard.set(data, forKey: featuredCategoriesKey)
            }
            UserDefaults.standard.synchronize()
        }
    }
    /*
    func getTheNextPost(post: FPost) -> FPost? {
        if let index = self.posts.indexOf(post) {
            if index == self.posts.endIndex {
                return nil
            }
            else if (index + 1) == self.posts.endIndex {
                return self.posts.last
            }
            else {
                return self.posts[index + 1]
            }
        }
        else {
            return nil
        }
    }
    
    func getThePreviousPost(post: FPost) -> FPost? {
        if let index = self.posts.indexOf(post) {
            if index == self.posts.startIndex {
                return nil
            }
            else if (index - 1) == self.posts.startIndex {
                return self.posts.first
            }
            else {
                return self.posts[index - 1]
            }
        }
        else {
            return nil
        }
    }
    
    
    func getCategory(categoryId: String) -> FCategory? {
        if let index = self.categories.indexOf({ $0.id == categoryId }) {
            return self.categories[index]
        }
        else {
            return nil
        }
    }
    
    func getChildCategories(category: FCategory, onMultipleDeepLevel: Bool) -> [FCategory] {
        var children = categories.filter({ $0.parentId == category.id })
        if !onMultipleDeepLevel {
            return children
        }
        else {
            for child in children {
                children.appendContentsOf(self.getChildCategories(child, onMultipleDeepLevel: onMultipleDeepLevel))
            }
            return children
        }
    }
    
    func getParentCategories(category: FCategory, onMultipleDeepLevel: Bool) -> [FCategory] {
        var parents = categories.filter({ $0.id == category.parentId })
        
        if !onMultipleDeepLevel {
            return parents
        }
        else {
            for parent in parents {
                parents.appendContentsOf(self.getParentCategories(parent, onMultipleDeepLevel: onMultipleDeepLevel))
            }
            return parents
        }
    }
    */
    
    func getCacheImage(_ URL: Foundation.URL, onCompleted: @escaping ((UIImage?) -> Void)) {
        SDWebImageManager.shared().cachedImageExists(for: URL) { (finish) in
            if finish {
                let key = SDWebImageManager.shared().cacheKey(for: URL)
                onCompleted(SDImageCache.shared().imageFromDiskCache(forKey: key))
            }
            else {
                onCompleted(nil)
            }
        }
    }
    
    func getCacheImage(_ url: URL) -> UIImage? {
        guard let cachedImageKey = SDWebImageManager.shared().cacheKey(for: url) else { return nil }
        return SDImageCache.shared().imageFromCache(forKey: cachedImageKey)
    }
    
    func getCacheImage(_ URLString: String) -> UIImage? {
        guard let URL = URL.init(string: URLString) else { return nil }
        return self.getCacheImage(URL)
    }
    
    func setCacheImage(_ URL: URL, image: UIImage) {
        let cachedImageKey = SDWebImageManager.shared().cacheKey(for: URL)
        SDImageCache.shared().store(image, forKey: cachedImageKey)
    }
}
