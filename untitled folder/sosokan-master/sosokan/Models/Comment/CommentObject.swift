//
//  CommentObject.swift
//  sosokan
//
//  Created by An Phan on 2/4/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import Foundation
import SwiftyJSON

extension Keys {
    struct Comment {
        static let id: String = "id"
        static let postId: String = "object_pk"
        static let userDisplayName: String = "user_name"
        static let content: String = "comment"
        static let createdAt: String = "submit_date"
        static let firebaseUserId: String = "firebase_user_id"
        static let numberOfFavorites: String = "rating_likes"
        static let userAvatarURL: String = "avatar"
        static let isFavorited: String = "user_voted"
    }
}

func ==(left: CommentObject?, right: CommentObject?) -> Bool {
    return left?.id == right?.id
}

struct CommentObject: IntIdentifierObjectModel {
    var id: Int!
    var postId: Int
    var userDisplayName: String
    var userAvatarURL: URL?
    var content: String
    var createdAt: Date
    var numberOfFavorites: Int
    var firebaseUserId: String?
    var parentId: Int?
    var taggedUserIds: [Int]
    var isFavorited: Bool
    
    init() {
        self.id = 0
        self.postId = 0
        self.userDisplayName = ""
        self.userAvatarURL = nil
        self.content = ""
        self.createdAt = Date.init()
        self.numberOfFavorites = 0
        self.firebaseUserId = nil
        self.parentId = nil
        self.taggedUserIds = []
        self.isFavorited = false
    }
    
    init(id: Int,
         postId: Int,
         userDisplayName: String,
         userAvatarURL: URL?,
         content: String,
         createdAt: Date,
         numberOfFavorites: Int,
         firebaseUserId: String?,
         parentId: Int?,
         taggedUserIds: [Int],
         isFavorited: Bool) {
        self.id = id
        self.postId = postId
        self.userDisplayName = userDisplayName
        self.userAvatarURL = userAvatarURL
        self.content = content
        self.createdAt = createdAt
        self.numberOfFavorites = numberOfFavorites
        self.firebaseUserId = firebaseUserId
        self.parentId = parentId
        self.taggedUserIds = taggedUserIds
        self.isFavorited = isFavorited
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.Comment.id].int,
        let postId = json[Keys.Comment.postId].string,
        let userDisplayName = json[Keys.Comment.userDisplayName].string,
        let content = json[Keys.Comment.content].string,
        let createdAt = json[Keys.Comment.createdAt].string,
        let numberOfFavorites = json[Keys.Comment.numberOfFavorites].int,
        let isFavorited = json[Keys.Comment.isFavorited].bool
            else {
                return nil
        }
        self.id = id
        self.postId = Int.init(postId)!
        self.userDisplayName = userDisplayName
        self.userAvatarURL = json[Keys.Comment.userAvatarURL].string == nil ? nil : URL(string: json[Keys.Comment.userAvatarURL].string!)
        self.content = content
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        self.createdAt = dateFormatter.date(from: createdAt)!
        self.numberOfFavorites = numberOfFavorites
        self.firebaseUserId = json[Keys.Comment.firebaseUserId].string
        self.parentId = nil
        self.taggedUserIds = []
        self.isFavorited = isFavorited
    }
}

typealias CommentsRequestResults = (Int, String?, [CommentObject])

extension CommentObject {
    static func dummyComments(numberOfComments number: Int,
                                               postId: Int,
                                               userDisplayName: String,
                                               firebaseUserId: String?,
                                               parentId: Int?,
                                               taggedUserIds: [Int]) -> [CommentObject] {
        let comments: [CommentObject] = (0 ..< number).map { (index) -> CommentObject in
            let longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum, justo ac interdum semper, nisi arcu pulvinar tortor, eget venenatis ex arcu vel enim."
            let shortText = "Lorem ipsum dolor"
            let avatarURL = URL.init(string: "https://graph.facebook.com/2041794186046461/picture?type=large")
            let commentObject = CommentObject(id: index,
                postId: postId,
                userDisplayName: userDisplayName,
                userAvatarURL: index % 3 == 0 ? avatarURL : nil,
                content: index % 2 == 0 ? longText : shortText,
                createdAt: Date(),  // Date().addTimeInterval(-index*3600)
                numberOfFavorites: index * 123,
                firebaseUserId: firebaseUserId,
                parentId: parentId,
                taggedUserIds: taggedUserIds,
                isFavorited: false)
            
            return commentObject
        }
        
        return comments
    }
}
