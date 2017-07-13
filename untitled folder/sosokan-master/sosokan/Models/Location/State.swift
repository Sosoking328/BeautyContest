//
//  State.swift
//  sosokan
//

import Foundation
import SwiftyJSON

func ==(left: State?, right: State?) -> Bool {
    return left?.code == right?.code
        && left?.name == right?.name
}

struct State {
    var code: String?
    var name: String?
    
    init?(json: JSON) {
        if let name = json["name"].string, !name.isEmpty,
            let code = json["abbreviation"].string, !code.isEmpty {
            self.name = name
            self.code = code
        }
        else {
            return nil
        }
    }
}

extension State {
    static func availableStates(_ language: SupportedLanguage) -> [State] {
        if language == .english {
            if let path = Bundle.main.path(forResource: "us_states", ofType: "json"),
                let data = try? Data.init(contentsOf: URL(fileURLWithPath: path)) {
                let json = JSON.init(data: data)
                let states = json.array?.flatMap({ State.init(json: $0) }) ?? []
                return states
            }
            return []
        }
        return []
    }
}
