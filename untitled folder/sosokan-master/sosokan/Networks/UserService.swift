//
//  UserService.swift
//  sosokan
//
//  Created by An Phan on 5/17/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation
import Alamofire

class UserService: BaseService {
    static let shared = UserService()
    
    // Login user with phone number and verify code from Ringcaptcha
    func logIn(_ phone: String, countryCode: String, verifyCode: String, completionHandler: @escaping CompletionHandler) {
        let params: [String: AnyObject] = ["phone": "\(countryCode)\(phone)" as AnyObject,
                                           "code": verifyCode as AnyObject]
        POST(APIEndPoint.loginViaPhoneNumber.path, params: params).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else if json["status"].stringValue == "ERROR" {
                let error = NSError(domain:SosokanError.network, code:100,
                                    userInfo:[NSLocalizedDescriptionKey: "Incorrect code"])
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                if let id = json[Keys.User.id].int,
                    let csrfToken = json[Keys.User.csrfToken].string, !csrfToken.isEmpty,
                    let accessToken = json[Keys.User.accessToken].string, !accessToken.isEmpty,
                    let username = json[Keys.User.username].string {
                    let _ = User.saveUser(id,
                                          csrfToken: csrfToken,
                                          accessToken: accessToken,
                                          username: username)
                    DataManager.shared.currentUser = User(id: id, csrfToken: csrfToken, accessToken: accessToken, username: username)
                    
                }
                
                // Fetching user profile.
                ProfileService.shared.fetchUserProfileWithCompletionHandler({ (json, error) in
                    // Do nothing
                })
                
                completionHandler(json, nil)
            }
        }
    }
}
