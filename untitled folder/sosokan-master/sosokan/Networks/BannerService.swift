//
//  BannerService.swift
//  sosokan
//
//  Created by An Phan on 5/30/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

typealias FetchBannerSuccessful = (_ post: [BannerObject]?, _ error: Error?) -> Void

class BannerService: BaseService {
    static let shared = BannerService()
    
    func fetchBannerWithCompletionHandler(completionHandler: @escaping FetchBannerSuccessful) {
        let endpoint = APIEndPoint.banners.path + "?language=" + SupportedLanguage.current().code
        GET(endpoint, params: nil).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                completionHandler(nil, error)
            }
            else {
                let banners = json.arrayValue.flatMap({ BannerObject(json: $0 )})
                completionHandler(banners, nil)
            }
        }
    }
}
