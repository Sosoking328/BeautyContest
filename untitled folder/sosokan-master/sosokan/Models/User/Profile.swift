//
//  Profile.swift
//  sosokan
//

import Foundation
import SwiftyJSON

extension Keys {
    struct Profile {
        static let id = "id"
        static let imageURL = "image_url"
        static let address = "address"
        static let enableCall = "callAble"
        static let city = "city"
        static let company = "companyName"
        static let credit = "credit"
        static let enableEmail = "emailAble"
        static let numberOfPosts = "myAdvertiseCount"
        static let note = "note"
        static let displayName = "display_name"
        static let website = "website"
        static let phone = "phoneNumber"
        static let state = "state"
        static let zip = "zip"
        static let email = "email"
        static let userId = "user"
    }
}

func ==(left: Profile?, right: Profile?) -> Bool {
    return left?.id == right?.id
        && left?.imageURL == right?.imageURL
        && left?.address == right?.address
        && left?.enableCall == right?.enableCall
        && left?.city == right?.city
        && left?.company == right?.company
        && left?.credit == right?.credit
        && left?.enableEmail == right?.enableEmail
        && left?.numberOfPosts == right?.numberOfPosts
        && left?.note == right?.note
        && left?.displayName == right?.displayName
        && left?.website == right?.website
        && left?.phone == right?.phone
        && left?.state == right?.state
        && left?.zip == right?.zip
        && left?.email == right?.email
        && left?.userId == right?.userId
}

struct Profile {
    var id: Int
    var imageURL: String?
    var address: String?
    var enableCall: Bool
    var city: String?
    var company: String?
    var credit: Double
    var enableEmail: Bool
    var numberOfPosts: Int
    var note: String?
    var displayName: String?
    var website: String?
    var phone: String?
    var state: String?
    var zip: String?
    var email: String?
    var userId: Int
    var numberOfLikes: Int = 0
    var numberOfFollowings: Int = 0
    var numberOfFollowers: Int = 0
    var numberOfShares: Int = 0
    
    init(id: Int,
         imageURL: String?,
         address: String?,
         enableCall: Bool,
         city: String?,
         company: String?,
         credit: Double,
         enableEmail: Bool,
         numberOfPosts: Int,
         note: String?,
         displayName: String?,
         website: String?,
         phone: String?,
         state: String?,
         zip: String?,
         email: String?,
         userId: Int) {
        self.id = id
        self.imageURL = imageURL
        self.address = address
        self.enableCall = enableCall
        self.city = city
        self.company = company
        self.credit = credit
        self.enableEmail = enableEmail
        self.numberOfPosts = numberOfPosts
        self.note = note
        self.displayName = displayName
        self.website = website
        self.phone = phone
        self.state = state
        self.zip = zip
        self.email = email
        self.userId = userId
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.Profile.id].int,
            let enableCall = json[Keys.Profile.enableCall].bool,
            let enableEmail = json[Keys.Profile.enableEmail].bool,
            let credit = json[Keys.Profile.credit].double,
            let numberOfPosts = json[Keys.Profile.numberOfPosts].int,
            let userId = json[Keys.Profile.userId].int
            else { return nil }
        self.id = id
        if let imageURL = json[Keys.Profile.imageURL].string, !imageURL.isEmpty {
            self.imageURL = imageURL
        }
        if let address = json[Keys.Profile.address].string, !address.isEmpty {
            self.address = address
        }
        self.enableCall = enableCall
        if let city = json[Keys.Profile.city].string, !city.isEmpty {
            self.city = city
        }
        if let company = json[Keys.Profile.company].string, !company.isEmpty {
            self.company = company
        }
        self.credit = credit
        self.enableEmail = enableEmail
        self.numberOfPosts = numberOfPosts
        if let note = json[Keys.Profile.note].string, !note.isEmpty {
            self.note = note
        }
        if let displayName = json[Keys.Profile.displayName].string, !displayName.isEmpty {
            self.displayName = displayName
        }
        if let website = json[Keys.Profile.website].string, !website.isEmpty {
            self.website = website
        }
        if let phone = json[Keys.Profile.phone].string, !phone.isEmpty {
            self.phone = phone
        }
        if let state = json[Keys.Profile.state].string, !state.isEmpty {
            self.state = state
        }
        if let zip = json[Keys.Profile.zip].string, !zip.isEmpty {
            self.zip = zip
        }
        if let email = json[Keys.Profile.email].string, !email.isEmpty {
            self.email = email
        }
        self.userId = userId
    }
}

extension Profile {
    var absoluteImageURL: URL? {
        guard let imageURL = self.imageURL else { return nil }
        return URL.init(string: imageURL)
    }
}
