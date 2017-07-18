//
//  APIEndPoint.swift
//  sosokan
//
//  Created by An Phan on 1/5/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import Foundation

import SwiftyJSON

enum APIEndPoint {
    
    case categories
    case posts
    case banners
    case reportOptions
    case loginViaFacebook
    case report
    case postId
    case profile
    case tokens
    case comments
    case users
    case loginViaPhoneNumber
    case getWechatCustomToken
    case splashDeal
    case splashVideo
    case favoritedPosts
    
    var path: String {
        switch self {
        case .categories:
            return "api/categories/"
        case .posts:
            return "api/ads/"
        case .banners:
            return "api/banners/"
        case .reportOptions:
            return "api/flagchoices/"
        case .loginViaFacebook:
            return "rest-auth/facebook/"
        case .report:
            return "api/flags/"
        case .postId:
            return "api/ads/"
        case .profile:
            return "api/user/"
        case .tokens:
            return "api/tokens/"
        case .comments:
            return "api/comments/"
        case .users:
            return "api/users/"
        case .loginViaPhoneNumber:
            return "api/phone_login/"
        case .getWechatCustomToken:
            return "api/get_firebase_key/"
        case .splashDeal:
            return "api/splashes/"
        case .splashVideo:
            return "api/splashes/"
        case .favoritedPosts:
            return "api/ads/?favorites="
        }
    }
}
