//
//  PostService.swift
//  sosokan
//
//  Created by An Phan on 5/17/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation
import CoreLocation

typealias FetchPostSuccessful = (_ post: Post?) -> Void
typealias FetchSimplifiedPostsSuccessful = (_ posts: [SimplifiedPost]?, _ nextRequest: String?, _ totalPosts: Int?) -> Void

class PostService: BaseService {
    static let shared = PostService()
    
    func fetchPost(postId id: Int, onSuccess: @escaping FetchPostSuccessful, onError: @escaping ErrorHandler) {
        let request = APIEndPoint.posts.path + id.string() + "/"
        GET(request, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let post = Post(json: json)
                onSuccess(post)
            }
        }
    }
    
    func fetchSimplifiedPostsByCategoryId(_ categoryId: Int? = nil, location: CLLocation? = nil,
                                          keyword: String? = nil, limit: Int, offset: Int,
                                       onSuccess: @escaping FetchSimplifiedPostsSuccessful,
                                       onError: @escaping ErrorHandler) {
        var params: [String : Any] = ["limit": limit,
                                      "offset": offset,
                                      "language": SupportedLanguage.current().code]
        if let categoryId = categoryId {
            params["category"] = categoryId
        }
        
        if let loc = location {
            params["dist"] = 10
            params["point"] = "\(loc.coordinate.longitude),\(loc.coordinate.latitude)"
        }
        
        if let keyword = keyword {
            params["search"] = keyword
        }
        
        GET(APIEndPoint.posts.path, params: params).responseSwiftyJSON { (req, resp, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let simplifiedPosts = json[Keys.kResults].array?.flatMap({ SimplifiedPost(json: $0) })
                let nextRequest = json[Keys.kNext].string
                let totalPosts = json[Keys.kCount].int
                
                DataManager.shared.posts = simplifiedPosts ?? []
                
                onSuccess(simplifiedPosts, nextRequest, totalPosts)
            }
        }
    }
    
    func fetchSimplifiedPostsByUser(userId: Int, onSuccess: @escaping FetchSimplifiedPostsSuccessful, onError: @escaping ErrorHandler) {
        let endPoint = APIEndPoint.posts.path + "?user=\(userId)"
        GET(endPoint, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let simplifiedPosts = json[Keys.kResults].array?.flatMap({ SimplifiedPost(json: $0) })
                let nextRequest = json[Keys.kNext].string
                let totalPosts = json[Keys.kCount].int
                
                DataManager.shared.myPosts = simplifiedPosts ?? []
                
                onSuccess(simplifiedPosts, nextRequest, totalPosts)
            }
        }
    }
    
    func fetchPost(withPostId postId: Int, completionHandler: @escaping CompletionHandler) {
        let request = APIEndPoint.posts.path + "\(postId)/"
        GET(request, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                completionHandler(json, error)
            }
        }
    }
    
    /*
    func fetchPosts(withEndPoint endPoint: String, completionHandler: @escaping CompletionHandler) {
        GET(endPoint, params: nil).responseSwiftyJSON { (req, resp, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                
                // TODO: Hanlde pagination.
                let posts = json[Keys.kResults].arrayValue.flatMap({ SimplifiedPost(json: $0) })
                DataManager.shared.posts = posts
                
                completionHandler(json, error)
            }
        }
    }
    */
    func likeUnlikePost(withPostId postId: Int, isLiked: Bool, completionHandler: @escaping CompletionHandler) {
        var endpoint = APIEndPoint.posts.path + "\(postId)"
        if isLiked {
            endpoint += "/like/"
        }
        else {
            endpoint += "/unlike/"
        }
        POST(endpoint, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                completionHandler(json, error)
            }
        }
    }
    
    func fetchFavoritedPostsForUser(userId: Int, completionHandler: @escaping CompletionHandler) {
        let endPoint = APIEndPoint.favoritedPosts.path + "\(userId)"
        GET(endPoint, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                let posts = json[Keys.kResults].arrayValue.flatMap({ SimplifiedPost(json: $0) })
                DataManager.shared.favoritedPosts = posts
                
                completionHandler(json, error)
            }
        }
    }
    
    func fetchPostsForUser(userId: Int, completionHandler: @escaping CompletionHandler) {
        let endPoint = APIEndPoint.favoritedPosts.path + "\(userId)"
        GET(endPoint, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                let posts = json[Keys.kResults].arrayValue.flatMap({ SimplifiedPost(json: $0) })
                DataManager.shared.favoritedPosts = posts
                
                completionHandler(json, error)
            }
        }
    }
}
