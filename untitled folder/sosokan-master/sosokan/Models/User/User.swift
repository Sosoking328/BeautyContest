import Foundation
import SwiftyJSON

extension Keys {
    struct User {
        static let id: String = "user_id"
        static let csrfToken: String = "csrftoken"
        static let accessToken: String = "key"
        static let username: String = "username"
    }
}

struct User {
    var id: Int
    var csrfToken: String
    var accessToken: String
    var username: String
    
    var email: String?
    var phone: String?
    var country: String?
    
    init(id: Int,
                 csrfToken: String,
                 accessToken: String,
                 username: String) {
        self.id = id
        self.csrfToken = csrfToken
        self.accessToken = accessToken
        self.username = username
    }
    
    static func saveUser(_ id: Int?,
                        csrfToken: String?,
                        accessToken: String?,
                        username: String?) -> Bool {
        let userDefaults = UserDefaults.standard
        userDefaults.setValue(id, forKey: Keys.id)
        userDefaults.setValue(csrfToken, forKey: Keys.csrfToken)
        userDefaults.setValue(accessToken, forKey: Keys.accessToken)
        userDefaults.setValue(username, forKey: Keys.username)
        
        return userDefaults.synchronize()
    }
}

extension User {
    static func currentUser() -> User? {
        let userDefaults = UserDefaults.standard
        if let id = userDefaults.value(forKey: Keys.id) as? Int,
            let csrfToken = userDefaults.value(forKey: Keys.csrfToken) as? String, !csrfToken.isEmpty,
            let accessToken = userDefaults.value(forKey: Keys.accessToken) as? String, !accessToken.isEmpty,
            let username = userDefaults.value(forKey: Keys.username) as? String {
            return User(id: id, csrfToken: csrfToken, accessToken: accessToken, username: username)
        }
        
        return nil
    }
    
    
    static func isLoggedIn() -> Bool {
        let userDefaults = UserDefaults.standard
        if let _ = userDefaults.value(forKey: Keys.id) as? Int,
            let csrfToken = userDefaults.value(forKey: Keys.csrfToken) as? String, !csrfToken.isEmpty,
            let accessToken = userDefaults.value(forKey: Keys.accessToken) as? String, !accessToken.isEmpty,
            let _ = userDefaults.value(forKey: Keys.username) as? String {
            return true
        }
        return false
    }
    
    static func logOut() {
        _ = User.saveUser(nil, csrfToken: nil, accessToken: nil, username: nil)
    }
}
