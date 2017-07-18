//
//  Authenticator.swift
//  sosokan
//
//  Created by An Phan on 1/12/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import Foundation

extension Keys {
    struct Authenticator {
        static let accessToken: String = "Authorization"
    }
}

struct Authenticator {
    var accessToken: String
    
    fileprivate init(accessToken: String) {
        self.accessToken = accessToken
    }
}

extension Authenticator {
    static func getAuthenticator() -> Authenticator {
        var token: String
        #if DEBUG
            token = "c5d89107573c141fb135663645018a9898744b57" //"ba6d3e5320314a1cea211f431cb89828d61ff3e4"
        #else
            token = "ba6d3e5320314a1cea211f431cb89828d61ff3e4"
        #endif
        return Authenticator.init(accessToken: "Token \(token)")
    }
}
