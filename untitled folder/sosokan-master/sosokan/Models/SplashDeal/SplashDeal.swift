//
//  SplashDeal.swift
//  sosokan
//

import Foundation
import SwiftyJSON
import Alamofire

extension Keys {
    struct SplashDeal {
        static let id = "id"
        static let URL = "video"
        static let language = "language"
        static let createdAt = "created"
        static let updatedAt = "modified"
    }
}

struct SplashDeal {
    var id: Int
    var URL: String
    var language: SupportedLanguage
    var createdAt: Date
    var updatedAt: Date
    
    init(id: Int,
         URL: String,
         language: SupportedLanguage,
         createdAt: Date,
         updatedAt: Date) {
        self.id = id
        self.URL = URL
        self.language = language
        self.createdAt = createdAt
        self.updatedAt = updatedAt
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.SplashDeal.id].int,
            let URL = json[Keys.SplashDeal.URL].string, !URL.isEmpty,
            let language = json[Keys.SplashDeal.language].string, !language.isEmpty,
            let createdAtString = json[Keys.SplashDeal.createdAt].string, !createdAtString.isEmpty,
            let createdAt = createdAtString.toTime(withFormat: Keys.nanoSecondDateFormat, timeZone: Keys.defaultTimeZone),
            let updatedAtString = json[Keys.SplashDeal.updatedAt].string, !updatedAtString.isEmpty,
            let updatedAt = updatedAtString.toTime(withFormat: Keys.nanoSecondDateFormat, timeZone: Keys.defaultTimeZone)
            else { return nil }
        self.init(id: id,
                  URL: URL,
                  language: SupportedLanguage.init(code: language),
                  createdAt: createdAt,
                  updatedAt: updatedAt)
    }
}

extension SplashDeal {
    var absoluteURL: Foundation.URL! {
        return Foundation.URL.init(string: self.URL)
    }
}

extension SplashDeal {
    static func getDeals(_ language: SupportedLanguage, completionHandler: @escaping CompletionHandler) -> [SplashDeal] {
        let request = AppConfig.Environment.hostPath + APIEndPoint.splashDeal.path + "?language=\(language.code)"
        
        var deals = [SplashDeal]()
        Alamofire.request(request, method: .get).responseSwiftyJSON { (_, _, json, error) in
            if let error = error {
                debugPrint(error)
                completionHandler(nil, error)
            }
            else {
                debugPrint(json)
                deals = json.array!.flatMap({ SplashDeal(json: $0) })
                completionHandler(json, nil)
            }
        }
        
        return deals
    }
}
