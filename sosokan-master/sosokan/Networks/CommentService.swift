//
//  CommentService.swift
//  sosokan
//
//  Created by An Phan on 5/17/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

typealias FetchCommentsSuccessful = (_ comments: [CommentObject]?, _ nextRequest: String?, _ totalComments: Int?) -> Void

class CommentService: BaseService {
    static let shared = CommentService()
    
    func fetchComments(ofPost id: Int,
                       limit: Int? = nil,
                       offset: Int? = nil,
                       onSuccess: @escaping FetchCommentsSuccessful,
                       onError: @escaping ErrorHandler) {
        
        var params: [String : Any] = [:]
        
//        params[Keys.kLanguage] = SupportedLanguage.current().code
        
        if let limit = limit,
            let offset = offset {
            params[Keys.kLimit] = limit
            params[Keys.kOffset] = offset
        }
        
        let path = APIEndPoint.comments.path + "?object_pk=\(id)"
        GET(path, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let comments = json[Keys.kResults].array?.flatMap({ CommentObject(json: $0) })
                let nextRequest = json[Keys.kCount].string
                let totalComments = json[Keys.kCount].int
                onSuccess(comments, nextRequest, totalComments)
                debugPrint(json)
            }
        }
    }
    
    func likeUnlikeComment(_ commentId: Int, isLiked: Bool, completionHandler: @escaping CompletionHandler) {
        let path = isLiked ? "\(commentId)/rate/1" : "\(commentId)/rate/-1"
        let commentPath = APIEndPoint.comments.path + path
        
        GET(commentPath, params: nil).responseSwiftyJSON { (_, _, json, error) in
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
    
    func deleteComment(_ commentId: Int, completionHandler: @escaping CompletionHandler) {
        
        DELETE("\(APIEndPoint.comments.path)\(commentId)/", params: nil).responseSwiftyJSON { (_, _, json, error) in
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
    
    fileprivate func addComment(_ postId: Int, content: String, completionHandler: @escaping CompletionHandler) {
        let params: JSONType = ["object_pk": postId as AnyObject,
                                "comment": content as AnyObject]
        
        POST(APIEndPoint.comments.path, params: params).responseSwiftyJSON { (_, _, json, error) in
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
    
    fileprivate func updateComment(_ postId: Int, comment: CommentObject, userId: String, completionHandler: @escaping CompletionHandler) {
        let params: JSONType = ["object_pk": postId as AnyObject,
                                "comment": comment.content as AnyObject,
                                "user_id": userId as AnyObject]
        PUT("\(APIEndPoint.comments.path)\(comment.id)/", params: params).responseSwiftyJSON { (_, _, json, error) in
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
}
