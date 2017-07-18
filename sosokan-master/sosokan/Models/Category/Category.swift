//
//  Category.swift
//  sosokan
//

import Foundation
import SwiftyJSON

func == (lCat: Category, rCat: Category) -> Bool {
    return lCat.id == rCat.id
}

extension Keys {
    struct Category {
        static let id = "id"
        static let URL = "url"
        static let nameEnglish = "name"
        static let nameChinese = "nameChinese"
        static let numberOfPostsEnglish = "count_en"
        static let numberOfPostsChinese = "count_zh_hans"
        static let level = "deepLevel"
        static let parentURL = "parent"
        static let sort = "sort"
        static let popular = "popular"
        static let iconEnglishURL = "iconEnglish"
        static let iconChineseURL = "iconChinese"
        static let backgroundImageURL = "background_image_url"
    }
}

class Category: NSObject, NSCoding {
    var id: Int
    var URL: String
    var nameEnglish: String
    var nameChinese: String
    var numberOfPostsEnglish: Int
    var numberOfPostsChinese: Int
    var sort: Int
    var popular: Int
    var iconEnglishURL: String?
    var iconChineseURL: String?
    var parentURL: String?
    var backgroundImageURL: String?
    
    init(id: Int,
         URL: String,
         nameEnglish: String,
         nameChinese: String,
         numberOfPostsEnglish: Int,
         numberOfPostsChinese: Int,
         sort: Int,
         popular: Int,
         iconEnglishURL: String?,
         iconChineseURL: String?,
         parentURL: String?,
         backgroundImageURL: String?) {
        self.id = id
        self.URL = URL
        self.nameEnglish = nameEnglish
        self.nameChinese = nameChinese
        self.numberOfPostsEnglish = numberOfPostsEnglish
        self.numberOfPostsChinese = numberOfPostsChinese
        self.sort = sort
        self.popular = popular
        self.iconEnglishURL = iconEnglishURL
        self.iconChineseURL = iconChineseURL
        self.parentURL = parentURL
        self.backgroundImageURL = backgroundImageURL
        
        super.init()
    }
    
    init?(json: JSON) {
        guard let id = json[Keys.Category.id].int,
            let URL = json[Keys.Category.URL].string, !URL.isEmpty,
            let nameEnglish = json[Keys.Category.nameEnglish].string,
            let nameChinese = json[Keys.Category.nameChinese].string,
            let numberOfPostsEnglish = json[Keys.Category.numberOfPostsEnglish].int,
            let numberOfPostsChinese = json[Keys.Category.numberOfPostsChinese].int,
            let sort = json[Keys.Category.sort].int,
            let popular = json[Keys.Category.popular].int
            else {
                return nil
        }
        self.id = id
        self.URL = URL
        self.nameEnglish = nameEnglish
        self.nameChinese = nameChinese
        self.numberOfPostsEnglish = numberOfPostsEnglish
        self.numberOfPostsChinese = numberOfPostsChinese
        self.sort = sort
        self.popular = popular
        if let iconEnglishURL = json[Keys.Category.iconEnglishURL].string, !iconEnglishURL.isEmpty {
            self.iconEnglishURL = iconEnglishURL
        }
        if let iconChineseURL = json[Keys.Category.iconChineseURL].string, !iconChineseURL.isEmpty {
            self.iconChineseURL = iconChineseURL
        }
        if let parentURL = json[Keys.Category.parentURL].string, !parentURL.isEmpty {
            self.parentURL = parentURL
        }
        if let backgroundImageURL = json[Keys.Category.backgroundImageURL].string, !backgroundImageURL.isEmpty {
            self.backgroundImageURL = backgroundImageURL
        }
        
        super.init()
    }
    
    required init(coder decoder: NSCoder) {
        self.id = decoder.decodeInteger(forKey: Keys.Category.id)
        self.URL = decoder.decodeObject(forKey: Keys.Category.URL) as? String ?? ""
        self.nameEnglish = decoder.decodeObject(forKey: Keys.Category.nameEnglish) as? String ?? ""
        self.nameChinese = decoder.decodeObject(forKey: Keys.Category.nameChinese) as? String ?? ""
        self.numberOfPostsEnglish = decoder.decodeInteger(forKey: Keys.Category.numberOfPostsEnglish)// as? Int ?? 0
        self.numberOfPostsChinese = decoder.decodeInteger(forKey: Keys.Category.numberOfPostsChinese)// as? Int ?? 0
        self.sort = decoder.decodeInteger(forKey: Keys.Category.sort)// as? Int ?? 0
        self.popular = decoder.decodeInteger(forKey: Keys.Category.popular)// as? Int ?? 0
        self.iconEnglishURL = decoder.decodeObject(forKey: Keys.Category.iconEnglishURL) as? String ?? ""
        self.iconChineseURL = decoder.decodeObject(forKey: Keys.Category.iconChineseURL) as? String ?? ""
        self.parentURL = decoder.decodeObject(forKey: Keys.Category.parentURL) as? String ?? ""
        self.backgroundImageURL = decoder.decodeObject(forKey: Keys.Category.backgroundImageURL) as? String ?? ""
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: Keys.Category.id)
        coder.encode(URL, forKey: Keys.Category.URL)
        coder.encode(nameEnglish, forKey: Keys.Category.nameEnglish)
        coder.encode(nameChinese, forKey: Keys.Category.nameChinese)
        coder.encode(numberOfPostsEnglish, forKey: Keys.Category.numberOfPostsEnglish)
        coder.encode(numberOfPostsChinese, forKey: Keys.Category.numberOfPostsChinese)
        coder.encode(sort, forKey: Keys.Category.sort)
        coder.encode(popular, forKey: Keys.Category.popular)
        coder.encode(iconEnglishURL, forKey: Keys.Category.iconEnglishURL)
        coder.encode(iconChineseURL, forKey: Keys.Category.iconChineseURL)
        coder.encode(parentURL, forKey: Keys.Category.parentURL)
        coder.encode(backgroundImageURL, forKey: Keys.Category.backgroundImageURL)
    }
}

extension Category {
    var localizeIconURL: String? {
        return SupportedLanguage.current() == .english ? self.iconEnglishURL : self.iconChineseURL
    }
    
    var absoluteLocalizeIconURL: Foundation.URL? {
        if let url = self.localizeIconURL {
            return Foundation.URL(string: url)
        }
        return nil
    }
    
    var absoluteBackgroundImageURL: Foundation.URL? {
        if let url = self.backgroundImageURL {
            return Foundation.URL(string: url)
        }
        return nil
    }
    
    var localizeName: String {
        return SupportedLanguage.current() == .english ? self.nameEnglish : self.nameChinese
    }
    
    var localizeNumberOfPosts: Int {
        return SupportedLanguage.current() == .english ? self.numberOfPostsEnglish : self.numberOfPostsChinese
    }
    
    var parentCategoryId: Int? {
        let array = self.parentURL?.components(separatedBy: "/").filter({ !$0.trim().isEmpty })
        if let idString = array?.last, let id = Int(idString) {
            return id
        }
        return nil
    }
}
