//
//  RateObject.swift
//  sosokan
//
//  Created by An Phan on 12/5/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation

func ==(first: RateObject, second: RateObject) -> Bool {
    return first.postId == second.postId && first.userId == second.userId
}

struct RateObjectKey {
    static let postId = "postId"
    static let userId = "userId"
    static let createdAtTimestamp = "createdAt"
    static let updatedAtTimestamp = "updatedAt"
    static let value = "value"
}

struct RateObject: Convertible, Equatable {
    let postId: String
    let userId: String
    let createdAtTimestamp: TimeInterval
    var createdAt: Date {
        get {
            return Date.init(timeIntervalSince1970: self.createdAtTimestamp)
        }
    }
    var updatedAtTimestamp: TimeInterval
    var updatedAt: Date {
        get {
            return Date.init(timeIntervalSince1970: self.updatedAtTimestamp)
        }
        set {
            self.updatedAtTimestamp = newValue.timeIntervalSince1970
        }
    }
    var value: Double
    
    init(postId: String, userId: String, createdAtTimestamp: TimeInterval, updatedAtTimestamp: TimeInterval, value: Double) {
        self.postId = postId
        self.userId = userId
        self.createdAtTimestamp = createdAtTimestamp
        self.updatedAtTimestamp = updatedAtTimestamp
        self.value = value
    }
    
    init?(json: JSONType) {
        guard let postId = json[RateObjectKey.postId] as? String else { return nil }
        guard let userId = json[RateObjectKey.userId] as? String else { return nil }
        guard let value = json[RateObjectKey.value] as? Double else { return nil }
        let createdAtTimestamp =  json[RateObjectKey.createdAtTimestamp] as? TimeInterval ?? Date.init().timeIntervalSince1970
        let updatedAtTimestamp =  json[RateObjectKey.updatedAtTimestamp] as? TimeInterval ?? Date.init().timeIntervalSince1970
        self.init(postId: postId, userId: userId, createdAtTimestamp: createdAtTimestamp, updatedAtTimestamp: updatedAtTimestamp, value: value)
    }
    
    func toJSON() -> JSONType {
        var json: JSONType = [:]
        json[RateObjectKey.postId] = self.postId as AnyObject?
        json[RateObjectKey.userId] = self.userId as AnyObject?
        json[RateObjectKey.value] = self.value as AnyObject?
        json[RateObjectKey.createdAtTimestamp] = self.createdAtTimestamp as AnyObject?
        json[RateObjectKey.updatedAtTimestamp] = self.updatedAtTimestamp as AnyObject?
        return json
        
    }
}
