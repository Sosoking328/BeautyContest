//
//  BaseService.swift

import Foundation
import Alamofire
import SwiftyJSON

typealias CompletionHandler = (_ response: JSON?, _ error: Error?) -> Void
typealias ErrorHandler = (_ error: Error) -> Void

class BaseService {
 
    // MARK: - Request methods
    
    func GET(_ endpoint:String, params: [String : Any]?) -> Alamofire.DataRequest {
        return sendRequest(.get, endpoint:endpoint, params: params)
    }
    
    func POST(_ endpoint:String, params: [String : Any]?) -> Alamofire.DataRequest {
        return sendRequest(.post, endpoint:endpoint, params: params)
    }
    
    func PUT(_ endpoint:String, params: [String : Any]?) -> Alamofire.DataRequest {
        return sendRequest(.put, endpoint:endpoint, params: params)
    }
    
    func DELETE(_ endpoint:String, params: [String : Any]?) -> Alamofire.DataRequest {
        return sendRequest(.delete, endpoint:endpoint, params: params)
    }
    
    func PATCH(_ endpoint:String, params: [String : Any]?) -> Alamofire.DataRequest {
        return sendRequest(.patch, endpoint:endpoint, params: params)
    }
    
    // MARK: - Private
    
    fileprivate func sendRequest(_ method: Alamofire.HTTPMethod, endpoint: String!, params:[String : Any]?) -> Alamofire.DataRequest {
        
        // Configure Alamofire shared manager header
        var header = ["Content-Type": "application/json", "Vary": "Accept"]
        if let user = User.currentUser() {
            header = ["Content-Type": "application/json", Keys.Authenticator.accessToken: "Token \(user.accessToken)"]
        }
        
        if method == .get {
            return Alamofire.request(path(endpoint), method: method, parameters: params, encoding: URLEncoding.default, headers: header).validate().responseJSON(completionHandler: { (response :DataResponse<Any>) in
                // Do nothing
            })
        }
        else {
            return Alamofire.request(path(endpoint), method: method, parameters: params, encoding: JSONEncoding.default, headers: header).validate().responseJSON(completionHandler: { (response :DataResponse<Any>) in
                // Do nothing
            })
        }
    }
    
    fileprivate func path(_ endpoint: String) -> String {
        return  "\(AppConfig.Environment.hostPath)\(endpoint)"
    }
}
