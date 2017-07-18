//
//  ProfileService.swift
//  sosokan
//
//  Created by An Phan on 5/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

typealias FetchProfileSuccessful = (Profile?) -> Void

class ProfileService: BaseService {
    
    static let shared = ProfileService()
    
    func fetch(profile id: Int,
               onSuccess: @escaping FetchProfileSuccessful,
               onError: @escaping ErrorHandler) {
        let path = APIEndPoint.profile.path + id.string() + "/"
        GET(path, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let profile = Profile(json: json)
                onSuccess(profile)
                debugPrint(json)
            }
        }
    }
    
    // Fetch user profile info and saving it to DataManager
    func fetchUserProfileWithCompletionHandler(_ completionHandler: @escaping CompletionHandler) {
        GET(APIEndPoint.profile.path, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                
                let currentUserProfile = Profile(json: json)
                DataManager.shared.userProfile = currentUserProfile
                NotificationCenter.default.post(name: Notification.Name(Notifications.fetchedCurrentUserProfile), object: currentUserProfile)
                completionHandler(json, nil)
            }
        }
    }
    
    // Update user profile info.
    // TODO: Doing later
}
