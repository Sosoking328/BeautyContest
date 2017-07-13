//
//  Property.swift
//  sosokan
//

import Foundation

func ==(left: Property?, right: Property?) -> Bool {
    return left?.id == right?.id
        && left?.name == right?.name
        && left?.nameInEnglish == right?.nameInEnglish
        && left?.nameInChinese == right?.nameInChinese
}

struct Property {
    var id: Int
    var name: String
    var nameInEnglish: String?
    var nameInChinese: String?
    
    init(id: Int,
         name: String,
         nameInEnglish: String?,
         nameInChinese: String?) {
        self.id = id
        self.name = name
        self.nameInEnglish = nameInEnglish
        self.nameInChinese = nameInChinese
    }
    
    init(id: Int,
         name: String) {
        self.id = id
        self.name = name
        self.nameInEnglish = nil
        self.nameInChinese = nil
    }
}

extension Property {
    static func dummyProperties() -> [Property] {
        return (0 ..< 10).map { (index) -> Property in
            return Property.init(id: index, name: "Property \(index)")
        }
    }
}