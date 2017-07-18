//
//  SimplifiedPost.swift
//  sosokan
//

import Foundation
import SwiftyJSON
import Alamofire
import CoreLocation

extension Keys {
    struct SimplifiedPost {
        static let id = "id"
        static let URL = "url"
        static let name = "name"
        static let price = "price"
        static let numberOfFavorites = "favoriteCount"
        static let numberOfShares = "shareCount"
        static let numberOfViews = "views"
        static let isFeature = "isFeatured"
        static let isStandout = "isStandout"
        static let createdAt = "created_on"
        static let ownerURL = "user"
        static let headerImage = "imageHeader"
        static let shortDescription = "short_description"
        static let location = "location"
        static let categoryURL = "category"
        static let language = "language"
    }
}

func ==(left: SimplifiedPost?, right: SimplifiedPost?) -> Bool {
    return left?.id == right?.id
}

struct SimplifiedPost {
    var id: Int
    var URL: String
    var name: String
    var price: Double?
    var numberOfFavorites: Int
    var numberOfShares: Int
    var numberOfViews: Int
    var isFeature: Bool
    var isStandout: Bool
    var createdAt: Date
    var ownerURL: String
    var headerImage: SimplifiedImage?
    var shortDescription: String?
    var location: CLLocation?
    var categoryURL: String
    var language: SupportedLanguage
    var video: SimplifiedVideo?
    
    init(id: Int,
         URL: String,
         name: String,
         price: Double?,
         numberOfFavorites: Int,
         numberOfShares: Int,
         numberOfViews: Int,
         isFeature: Bool,
         isStandout: Bool,
         createdAt: Date,
         ownerURL: String,
         headerImage: SimplifiedImage?,
         shortDescription: String?,
         location: CLLocation?,
         categoryURL: String,
         language: SupportedLanguage,
         video: SimplifiedVideo?) {
        self.id = id
        self.URL = URL
        self.name = name
        self.price = price
        self.numberOfFavorites = numberOfFavorites
        self.numberOfShares = numberOfShares
        self.numberOfViews = numberOfViews
        self.isFeature = isFeature
        self.isStandout = isStandout
        self.createdAt = createdAt
        self.ownerURL = ownerURL
        self.headerImage = headerImage
        self.shortDescription = shortDescription
        self.location = location
        self.categoryURL = categoryURL
        self.language = language
        self.video = video
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.SimplifiedPost.id].int,
            let URL = json[Keys.SimplifiedPost.URL].string, !URL.isEmpty,
            let name = json[Keys.SimplifiedPost.name].string,// where !name.isEmpty,
            let numberOfFavorites = json[Keys.SimplifiedPost.numberOfFavorites].int,
            let numberOfShares = json[Keys.SimplifiedPost.numberOfShares].int,
            let isFeature = json[Keys.SimplifiedPost.isFeature].bool,
            let isStandout = json[Keys.SimplifiedPost.isStandout].bool,
            let createdAtString = json[Keys.SimplifiedPost.createdAt].string, !createdAtString.isEmpty,
            let createdAt = createdAtString.toTime(withFormat: Keys.defaultDateFormat, timeZone: Keys.defaultTimeZone),
            let ownerURL = json[Keys.SimplifiedPost.ownerURL].string, !ownerURL.isEmpty,
            let categoryURL = json[Keys.SimplifiedPost.categoryURL].string, !categoryURL.isEmpty,
            let language = json[Keys.SimplifiedPost.language].string, !language.isEmpty,
            let numberOfViews = json[Keys.SimplifiedPost.numberOfViews].int
            else {
                return nil
        }
        self.id = id
        self.URL = URL
        self.name = name
        self.price = json[Keys.SimplifiedPost.price].double
        self.numberOfFavorites = numberOfFavorites
        self.numberOfShares = numberOfShares
        self.numberOfViews = numberOfViews
        self.isFeature = isFeature
        self.isStandout = isStandout
        self.createdAt = createdAt
        self.ownerURL = ownerURL
        self.headerImage = SimplifiedImage.init(json: json[Keys.SimplifiedPost.headerImage])
        self.shortDescription = json[Keys.SimplifiedPost.shortDescription].string
        
        // TODO: Double check
        /*
        if let locationString = json[Keys.SimplifiedPost.location].string, !locationString.isEmpty,
            (locationString as NSString).rangeOfString("(") != NSNotFound,
            (locationString as NSString).rangeOfString(")") != NSNotFound {
            let range = startRange.endIndex ..< endRange.startIndex
            let array = locationString[range].componentsSeparatedByString(" ")
            if array.count == 2,
                let longitude = CLLocationDegrees.init(array[0]),
                let latitude = CLLocationDegrees.init(array[1]) {
                let location = CLLocation.init(latitude: latitude, longitude: longitude)
                self.location = location
            }
        }
         */
        self.categoryURL = categoryURL
        self.language = SupportedLanguage(code: language)
    }
}

extension SimplifiedPost {
    var categoryId: Int? {
        let array = self.categoryURL.components(separatedBy: "/").filter({ !$0.trim().isEmpty })
        if let idString = array.last, let id = Int.init(idString) {
            return id
        }
        return nil
    }
    
    var ownerId: Int? {
        let array = self.ownerURL.components(separatedBy: "/").filter({ !$0.trim().isEmpty })
        if let idString = array.last, let id = Int.init(idString) {
            return id
        }
        return nil
    }
    
    var isEnglish: Bool {
        if language == .english {
            return true
        }
        return false
    }
    
    var isLiked: Bool {
        guard let _ = DataManager.shared.userProfile
            else { return false }
        return false
    }
}

typealias PostsRequestResults = (Int, String?, [SimplifiedPost])
typealias FetchPostsResults = (totalPosts: Int, nextRequest: String?, posts: [SimplifiedPost])
typealias FetchPostsHandler = (_ results: FetchPostsResults?, _ error: Error?) -> Void
