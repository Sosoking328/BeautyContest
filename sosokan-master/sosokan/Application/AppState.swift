//
//  AppState.swift
//  Sosokan

import Foundation

struct AppState {
    
    // Configs to showing the home page
    static func setHome(_ onCompleted: (() -> Void)? = nil) {
        DispatchQueue.main.async(execute: {
            
            // Take a screenshot of the login page
            let snapshot = AppDelegate.shared().window?.snapshotView(afterScreenUpdates: true)
            
            AppDelegate.shared().window?.rootViewController = nil
            AppDelegate.shared().window = UIWindow(frame: UIScreen.main.bounds)
            AppDelegate.shared().window?.rootViewController = AppDelegate.shared().drawerViewController
            AppDelegate.shared().drawerViewController.closeDrawer(.left, animated: false) { (finished) in
                onCompleted?()
            }
//            let viewController = PaginationViewController(filterOptions: DataManager.sharedInstance.filterOptions, viewType: DataManager.sharedInstance.viewType)
//            let navigation = UINavigationController(rootViewController: viewController)
            let homeNC = StoryBoardManager.viewController("Home", viewControllerName: "homeNCID")
            AppDelegate.shared().centerViewController = homeNC
            AppDelegate.shared().window?.makeKeyAndVisible()
            
            // Add the login screenshot to the home view.
            if let snapshot = snapshot {
                homeNC.view.addSubview(snapshot)
                
                // Animate to remove the login screenshot from the home view.
                UIView.animate(withDuration: 0.3, animations: {
                    snapshot.layer.opacity = 0
                }, completion: { (finish) in
                    snapshot.removeFromSuperview()
                })
            }
        })
    }
    
    
    // Configs to showing the sign-in page
    static func setSignIn() {
        // Take a screenshot of the current page
        let snapshot = AppDelegate.shared().window?.snapshotView(afterScreenUpdates: true)
        
        AppDelegate.shared().window?.rootViewController = nil
        AppDelegate.shared().window = UIWindow(frame: UIScreen.main.bounds)

        let signUpVC = SignUpViewController()
        let navigation = UINavigationController(rootViewController: signUpVC)
        AppDelegate.shared().window?.rootViewController = navigation
        AppDelegate.shared().window?.makeKeyAndVisible()
        
        // Add the login screenshot to the home view.
        if let snapshot = snapshot {
            signUpVC.view.addSubview(snapshot)
            
            // Animate to remove the login screenshot from the home view.
            UIView.animate(withDuration: 0.3, animations: {
                snapshot.layer.opacity = 0
            }, completion: { (finish) in
                snapshot.removeFromSuperview()
            })
        }
    }
    
    static func presentCreateAdVCWithAd(_ post: Post?, image: [UIImage] = []) {
        /*
        let createAdNC = StoryBoardManager.viewController(storyBoardName: "Post",
                                                          viewControllerName: "createAdNC") as! UINavigationController
        let createAdVC = createAdNC.topViewController as! CreateAdViewController
        createAdVC.post = post
        
        if !image.isEmpty {
            createAdVC.selectedImages = image
        }
        if let rootVC = AppDelegate.shared().window?.rootViewController {
            rootVC.presentViewController(createAdNC, animated: true, completion: nil)
        }
         */
    }
    
    static func clearSessionUser() {
        UserAuth.lastEmail = nil
        UserAuth.authToken = nil
        UserAuth.lastUserId = nil
        UserAuth.lastUserPhone = nil
        UserAuth.lastUserName = nil
    }
    
    // Authentication
    struct UserAuth {
        static var defaults: UserDefaults! {
            return UserDefaults.standard
        }
        
        static let firstLaunchKey: String   = "AppState.UserAuth.firstLaunch"
        static let authTokenKey: String     = "AppState.UserAuth.authToken"
        static let lastEmailKey: String     = "AppState.UserAuth.lastEmail"
        static let lastUserIdKey: String    = "AppState.UserAuth.lastUserId"
        static let lastUserNameKey: String  = "AppState.UserAuth.lastUserName"
        static let userPhoneKey: String     = "AppState.UserAuth.phone"
        
        static var isAuthenticated: Bool {
            return authToken?.isEmpty == false
        }
        
        // Current session user token
        static var authToken: String? {
            get {
                return defaults.object(forKey: authTokenKey) as! String?
            }
            set {
                if (newValue == nil) {
                    defaults.removeObject(forKey: authTokenKey)
                } else {
                    defaults.set(newValue, forKey: authTokenKey)
                }
                defaults.synchronize()
            }
        }
        
        // Current session user email
        static var lastEmail: String? {
            get {
            return defaults.object(forKey: lastEmailKey) as! String?
            }
            set {
                if (newValue == nil) {
                    defaults.removeObject(forKey: lastEmailKey)
                } else {
                    defaults.set(newValue, forKey: lastEmailKey)
                }
                defaults.synchronize()
            }
        }
        
        // Current session user id
        static var lastUserId: String? {
            get {
            return defaults.object(forKey: lastUserIdKey) as! String?
            }
            set {
                if (newValue == nil) {
                    defaults.removeObject(forKey: lastUserIdKey)
                } else {
                    defaults.set(newValue!, forKey: lastUserIdKey)
                }
                
                defaults.synchronize()
            }
        }
        
        // Current session user name
        static var lastUserName: String? {
            get {
                return defaults.object(forKey: lastUserNameKey) as! String?
            }
            set {
                if (newValue == nil) {
                    defaults.removeObject(forKey: lastUserNameKey)
                } else {
                    defaults.set(newValue, forKey: lastUserNameKey)
                }
                defaults.synchronize()
            }
        }
        
        // Current session user phone
        static var lastUserPhone: String? {
            get {
                return defaults.object(forKey: userPhoneKey) as? String
            }
            set {
                if (newValue == nil) {
                    defaults.removeObject(forKey: userPhoneKey)
                } else {
                    defaults.set(newValue, forKey: userPhoneKey)
                }
                defaults.synchronize()
            }
        }
    }
}

