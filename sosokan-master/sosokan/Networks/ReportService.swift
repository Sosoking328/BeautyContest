//
//  ReportService.swift
//  sosokan
//
//  Created by An Phan on 5/17/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

typealias FetchReportReasonsSuccessful = ([ReportReason]?) -> Void
typealias SendPostReportSuccessful = () -> Void

class ReportService: BaseService {
    static let shared = ReportService()
    
    func fetchReasons(onSuccess: @escaping FetchReportReasonsSuccessful, onError: @escaping ErrorHandler) {
        
        var params: [String: Any] = [:]
        
        params[Keys.kLanguage] = SupportedLanguage.current().code
        
        GET(APIEndPoint.reportOptions.path, params: params).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                let reasons = json[Keys.kResults].array?.flatMap({ ReportReason(json: $0) })
                onSuccess(reasons)
                debugPrint(json)
            }
        }
    }
    
    func sendReport(post postId: Int,
                    user userId: Int,
                    reason reasonId: Int,
                    content: String?,
                    onSuccess: @escaping SendPostReportSuccessful,
                    onError: @escaping ErrorHandler) {
        var params: [String: Any] = [:]
        let host = AppConfig.Environment.hostPath
        params["ad"] = "\(host)\(APIEndPoint.posts.path)\(postId)/"
        params["user"] = "\(host)\(APIEndPoint.users.path)\(userId)/"
        params["reason"] = "\(host)\(APIEndPoint.reportOptions.path)\(reasonId)/"
        params["content"] = content ?? ""
        POST(APIEndPoint.report.path, params: params).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                onError(error)
                debugPrint(error)
            }
            else {
                onSuccess()
                debugPrint(json)
            }
        }
    }
    
    func getReportReasonsWithCompletionHandler(_ completionHandler: @escaping CompletionHandler) {
        GET(APIEndPoint.reportOptions.path, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                completionHandler(json, nil)
            }
        }
    }
    
    func sendReportForAd(_ postId: Int, userId: Int, reasonId: Int, content: String, completionHandler: @escaping CompletionHandler) {
        let params: JSONType = ["ad": "\(APIEndPoint.posts.path)\(postId)/" as AnyObject,
                                "user": "\(APIEndPoint.users.path)\(userId)/" as AnyObject,
                                "reason": "\(APIEndPoint.reportOptions.path)\(reasonId)/" as AnyObject,
                                "content": content as AnyObject]
        POST(APIEndPoint.reportOptions.path, params: params).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                completionHandler(json, nil)
            }
        }
    }
}
