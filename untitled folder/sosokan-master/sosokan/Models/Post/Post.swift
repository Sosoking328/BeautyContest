//
//  Post.swift
//  sosokan
//

import UIKit
import SwiftyJSON
import CoreLocation

extension Keys {
    struct Post {
        static let id = "id"
        static let name = "name"
        static let price = "price"
        static let numberOfFavorites = "favoriteCount"
        static let numberOfShares = "shareCount"
        static let isFeature = "isFeatured"
        static let isStandout = "isStandout"
        static let createdAt = "created_on"
        static let ownerId = "user"
        static let headerImage = "imageHeader"
        static let location = "location"
        static let categoryId = "category"
        static let language = "language"
        static let images = "adimage"
        static let fullDescription = "description"
        static let comments = "comments"
        static let likes = "favorite"
        static let enablePhone = "enablePhone"
        static let enableEmail = "enableEmail"
    }
    
}

func ==(left: Post?, right: Post?) -> Bool {
    return left?.id == right?.id
}

struct Post {
    var id: Int
    var name: String
    var price: Double?
    var numberOfFavorites: Int
    var numberOfShares: Int
    var isFeature: Bool
    var isStandout: Bool
    var createdAt: Date
    var updatedAt: Date
    var ownerId: Int
    var headerImage: SimplifiedImage?
    var location: CLLocation?
    var categoryId: Int
    var language: SupportedLanguage
    var video: SimplifiedVideo?
    var images: [PImage]
    var fullDescription: String?
    var enablePhone: Bool
    var enableEmail: Bool
    var comments: [CommentObject]
    
    init(id: Int,
         name: String,
         price: Double?,
         numberOfFavorites: Int,
         numberOfShares: Int,
         isFeature: Bool,
         isStandout: Bool,
         createdAt: Date,
         updatedAt: Date,
         ownerId: Int,
         headerImage: SimplifiedImage?,
         location: CLLocation?,
         categoryId: Int,
         language: SupportedLanguage,
         video: SimplifiedVideo?,
         images: [PImage],
         fullDescription: String?,
         enablePhone: Bool,
         enableEmail: Bool,
         comments: [CommentObject]) {
        self.id = id
        self.name = name
        self.price = price
        self.numberOfFavorites = numberOfFavorites
        self.numberOfShares = numberOfShares
        self.isFeature = isFeature
        self.isStandout = isStandout
        self.createdAt = createdAt
        self.updatedAt = updatedAt
        self.ownerId = ownerId
        self.headerImage = headerImage
        self.location = location
        self.categoryId = categoryId
        self.language = language
        self.video = video
        self.images = images
        self.fullDescription = fullDescription
        self.enablePhone = enablePhone
        self.enableEmail = enableEmail
        self.comments = comments
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.Post.id].int,
            let name = json[Keys.Post.name].string,// where !name.isEmpty,
            let price = json[Keys.Post.price].double,
            let numberOfFavorites = json[Keys.Post.numberOfFavorites].int,
            let numberOfShares = json[Keys.Post.numberOfShares].int,
            let isFeature = json[Keys.Post.isFeature].bool,
            let isStandout = json[Keys.Post.isStandout].bool,
            let createdAtString = json[Keys.Post.createdAt].string, !createdAtString.isEmpty,
            let createdAt = createdAtString.toTime(withFormat: Keys.defaultDateFormat, timeZone: Keys.defaultTimeZone),
            let ownerId = json[Keys.Post.ownerId].int,
            let categoryId = json[Keys.Post.categoryId].int,
            let enablePhone = json[Keys.Post.enablePhone].bool,
            let enableEmail = json[Keys.Post.enableEmail].bool,
            let language = json[Keys.Post.language].string, !language.isEmpty
            else {
                return nil
        }
        self.id = id
        self.name = name
        self.price = price
        self.numberOfFavorites = numberOfFavorites
        self.numberOfShares = numberOfShares
        self.isFeature = isFeature
        self.isStandout = isStandout
        self.createdAt = createdAt
        // FIXME: missing updatedAt field
        self.updatedAt = createdAt
        self.ownerId = ownerId
        self.headerImage = SimplifiedImage.init(json: json[Keys.SimplifiedPost.headerImage])
        
        // TODO: Double check
        /*
        if let locationString = json[Keys.SimplifiedPost.location].string, !locationString.isEmpty,
            let startRange = locationString.rangeOfString("("),
            let endRange = locationString.rangeOfString(")") {
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
        
        self.categoryId = categoryId
        self.language = SupportedLanguage.init(code: language)
        // FIXME: missing video field
        self.video = nil
        self.images = json[Keys.Post.images].array?.flatMap({ PImage.init(json: $0) }) ?? []
        self.fullDescription = json[Keys.Post.fullDescription].string
        self.enablePhone = enablePhone
        self.enableEmail = enableEmail
        self.comments = json[Keys.Post.comments].array?.flatMap({ CommentObject.init(json: $0) }) ?? []
    }
}
