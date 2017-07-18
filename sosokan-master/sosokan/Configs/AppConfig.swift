//
//  AppConfigs.swift

import Foundation

enum BuildTarget {
    case debug, adHoc, test, release
}

struct AppConfig {
    struct ServerHosts {
        static let Local = "http://sosokan-staging.herokuapp.com/"
        static let Staging = "http://sosokan-staging.herokuapp.com/"
        static let Production = "http://sosokan.herokuapp.com/"
    }
    
    struct ExternalURLs {
        static let forgotPassword = ""
        static let faq = ""
        static let support = ""
        static let terms = ""
    }
    
    #if DEBUG
    static let buildTarget: BuildTarget = .debug
    struct Environment {
    static let hostPath: String = ServerHosts.Production
    }
    #elseif ADHOC
    static let buildTarget: BuildTarget = .adHoc
    struct Environment {
    static let hostPath = ServerHosts.Staging
    }
    #elseif RELEASE
    static let buildTarget: BuildTarget = .release
    struct Environment {
    static let hostPath = ServerHosts.Production
    static let useSSL = false
    }
    #else // Compiler Flag not set!!!
    static let buildTarget: BuildTarget = .debug
    struct Environment {
        static let hostPath = ServerHosts.Production
        static let useSSL = false
    }
    #endif
}
