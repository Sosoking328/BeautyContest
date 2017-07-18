//
//  FConversation.swift
//  sosokan
//
//  Created by An Phan on 10/6/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

struct FConversationKey {
    static let id = "id"
    static let postId = "postId"
    static let postOwnerId = "postOwnerId"
    static let postCategoryId = "postCategoryId"
    static let postName = "postName"
    static let postCreatedAt = "postCreatedAt"
    static let postHeaderImageURL = "postHeaderImageURL"
    static let createdAt = "createdAt"
    static let updatedAt = "updatedAt"
    static let lastMessageId = "lastMessageId"
    static let lastMessageContent = "lastMessageContent"
    static let lastMessageSentBy = "lastMessageSentBy"
    static let lastMessageTimestamp = "lastMessageTimestamp"
    static let unreadLastMessageUsers = "unreadLastMessageUsers"
    static let tempDeleteUsers = "tempDeleteUsers"
    static let users = "users"
    static let waitingUsers = "waitingUsers"
}

class FConversation: NSObject {
    var id: String
    var postId: String
    var postOwnerId: String
    var postCategoryId: String
    var postName: String
    var postCreatedAt: TimeInterval
    var postHeaderImageURL: String
    var createdAt: TimeInterval
    var updatedAt: TimeInterval
    var lastMessageId: String
    var lastMessageContent: String
    var lastMessageSentBy: String
    var lastMessageTimestamp: TimeInterval
    var unreadLastMessageUsers: JSONType
    var tempDeleteUsers: JSONType
    var users: JSONType
    var waitingUsers: JSONType = [:]
    
    init(id: String, postId: String, postOwnerId: String, postCategoryId: String, postName: String, postCreatedAt: TimeInterval, postHeaderImageURL: String, createdAt: TimeInterval, updatedAt: TimeInterval, lastMessageId: String, lastMessageContent: String, lastMessageSentBy: String, lastMessageTimestamp: TimeInterval, unreadLastMessageUsers: JSONType, tempDeleteUsers: JSONType, users: JSONType) {
        self.id = id
        self.postId = postId
        self.postOwnerId = postOwnerId
        self.postCategoryId = postCategoryId
        self.postName = postName
        self.postCreatedAt = postCreatedAt
        self.postHeaderImageURL = postHeaderImageURL
        self.createdAt = createdAt
        self.updatedAt = updatedAt
        self.lastMessageId = lastMessageId
        self.lastMessageContent = lastMessageContent
        self.lastMessageSentBy = lastMessageSentBy
        self.lastMessageTimestamp = lastMessageTimestamp
        self.unreadLastMessageUsers = unreadLastMessageUsers
        self.tempDeleteUsers = tempDeleteUsers
        self.users = users
        
        super.init()
    }
    
    init(json: JSONType) {
        self.id = json[FConversationKey.id] as? String ?? ""
        self.postId = json[FConversationKey.postId] as? String ?? ""
        self.postOwnerId = json[FConversationKey.postOwnerId] as? String ?? ""
        self.postCategoryId = json[FConversationKey.postCategoryId] as? String ?? ""
        self.postName = json[FConversationKey.postName] as? String ?? ""
        self.postCreatedAt = json[FConversationKey.postCreatedAt] as? TimeInterval ?? 0
        self.postHeaderImageURL = json[FConversationKey.postHeaderImageURL] as? String ?? ""
        self.createdAt = json[FConversationKey.createdAt] as? TimeInterval ?? 0
        self.updatedAt = json[FConversationKey.updatedAt] as? TimeInterval ?? 0
        self.lastMessageId = json[FConversationKey.lastMessageId] as? String ?? ""
        self.lastMessageContent = json[FConversationKey.lastMessageContent] as? String ?? ""
        self.lastMessageSentBy = json[FConversationKey.lastMessageSentBy] as? String ?? ""
        self.lastMessageTimestamp = json[FConversationKey.lastMessageTimestamp] as? TimeInterval ?? 0
        self.unreadLastMessageUsers = json[FConversationKey.unreadLastMessageUsers] as? JSONType ?? [:]
        self.tempDeleteUsers = json[FConversationKey.tempDeleteUsers] as? JSONType ?? [:]
        self.users = json[FConversationKey.users] as? JSONType ?? [:]
        self.waitingUsers = json[FConversationKey.waitingUsers] as? JSONType ?? [:]
        
        super.init()
    }
}

//extension FConversation: Convertible {
    /*
    func toJSON() -> JSONType {
        let json = [FConversationKey.id: self.id as AnyObject,
            FConversationKey.postId: self.postId as AnyObject,
            FConversationKey.postOwnerId: postOwnerId as AnyObject,
            FConversationKey.postCategoryId: self.postCategoryId as AnyObject,
            FConversationKey.postName: self.postName as AnyObject,
            FConversationKey.postCreatedAt: self.postCreatedAt as AnyObject,
            FConversationKey.postHeaderImageURL: self.postHeaderImageURL as AnyObject,
            FConversationKey.createdAt: self.createdAt,
            FConversationKey.updatedAt: Date.getTimestamp(),
            FConversationKey.lastMessageId: self.lastMessageId,
            FConversationKey.lastMessageContent: self.lastMessageContent,
            FConversationKey.lastMessageSentBy: self.lastMessageSentBy,
            FConversationKey.lastMessageTimestamp: self.lastMessageTimestamp,
            FConversationKey.unreadLastMessageUsers: self.unreadLastMessageUsers as JSONType,
            FConversationKey.tempDeleteUsers: self.tempDeleteUsers as JSONType,
            FConversationKey.users: self.users as JSONType,
            FConversationKey.waitingUsers: self.waitingUsers as JSONType]
        
        return json as JSONType
    }
     */
//}
