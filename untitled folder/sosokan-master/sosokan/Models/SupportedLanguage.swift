//
//  SupportedLanguage.swift
//  sosokan
//

import UIKit

extension Keys {
    struct SupportedLanguage {
        static let english = "en"
        static let chinese = "zh-hans"
    }
}

enum SupportedLanguage {
    case english
    case chinese
    
    init(code: String) {
        switch code {
        case Keys.SupportedLanguage.chinese:
            self = .chinese
        default:
            self = .english
        }
    }

    var code: String {
        switch self {
        case .english:
            return Keys.SupportedLanguage.english
        case .chinese:
            return Keys.SupportedLanguage.chinese
        }
    }
}

extension SupportedLanguage {
    static func current() -> SupportedLanguage {
        return IS_ENGLISH ? SupportedLanguage.english : SupportedLanguage.chinese
    }
}
