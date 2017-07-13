import Foundation
import Alamofire
import SwiftyJSON

typealias JSONResponse = (NSURLRequest?, NSHTTPURLResponse?, Result<AnyObject>) -> Void

extension URLStringConvertible {
    func requestJSON(method method: Alamofire.Method = .GET,
                            parameters: [String: AnyObject]? = nil,
                            encoding: Alamofire.ParameterEncoding = .JSON,
                            headers: [String : String]? = nil,
                            onCompleted: JSONResponse) {
        Alamofire.request(method, self, parameters: parameters, encoding: encoding, headers: headers)
            .responseJSON(completionHandler: onCompleted)
    }
}