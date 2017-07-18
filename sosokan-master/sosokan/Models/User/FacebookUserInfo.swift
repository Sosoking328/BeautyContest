//
//  FacebookUserInfo.swift
//  sosokan
//

import Foundation

struct FacebookUserInfo {
    var id: String
    var email: String?
    var firstName: String?
    var lastName: String?
    var imageURL: String?
    var token: String
    
    init(id: String, email: String?, firstName: String?, lastName: String?, imageURL: String?, token: String) {
        self.id = id
        self.email = email
        self.firstName = firstName
        self.lastName = lastName
        self.imageURL = imageURL
        self.token = token
    }
}