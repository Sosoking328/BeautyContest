//
//  FMessage.swift
//  sosokan
//
//  Created by An Phan on 10/6/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

struct FMessageKey {
    static let id = "id"
    static let conversationId = "conversationId"
    static let userId = "userId"
    static let userDisplayName = "userDisplayName"
    static let imageURL = "imageURL"
    static let timestamp = "timestamp"
    static let content = "content"
}

class FMessage: NSObject {
    var id: String
    var conversationId: String
    var userId: String
    var userDisplayName: String
    var imageURL: String
    var timestamp: TimeInterval
    var content: String
    
    init(id: String, conversationId: String, userId: String, userDisplayName: String, imageURL: String, timestamp: TimeInterval, content: String) {
        self.id = id
        self.conversationId = conversationId
        self.userId = userId
        self.userDisplayName = userDisplayName
        self.imageURL = imageURL
        self.timestamp = timestamp
        self.content = content
        
        super.init()
    }
    
    init(json: JSONType) {
        self.id = json[FMessageKey.id] as? String ?? ""
        self.conversationId = json[FMessageKey.conversationId] as? String ?? ""
        self.userId = json[FMessageKey.userId] as? String ?? ""
        self.userDisplayName = json[FMessageKey.userDisplayName] as? String ?? ""
        self.imageURL = json[FMessageKey.imageURL] as? String ?? ""
        self.timestamp = json[FMessageKey.timestamp] as? TimeInterval ?? 0
        self.content = json[FMessageKey.content] as? String ?? ""
        
        super.init()
    }
}

extension FMessage: Convertible {
    func toJSON() -> JSONType {
        let json: JSONType = [
            FMessageKey.id: self.id as AnyObject,
            FMessageKey.conversationId: self.conversationId as AnyObject,
            FMessageKey.userId: self.userId as AnyObject,
            FMessageKey.userDisplayName: self.userDisplayName as AnyObject,
            FMessageKey.imageURL: self.imageURL as AnyObject,
            FMessageKey.timestamp: self.timestamp as AnyObject,
            FMessageKey.content: self.content as AnyObject
        ]
        return json
    }
}
