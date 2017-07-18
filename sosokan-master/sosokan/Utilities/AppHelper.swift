//
//  AppHelper.swift
//  sosokan

import UIKit
import FBSDKLoginKit

class AppHelper: NSObject {
    /*
    static let gifURL = NSBundle.mainBundle().URLForResource("animation", withExtension: "gif")
    static var gifData: NSData? {
        guard let gifURL = gifURL else { return nil }
        
        return NSData(contentsOfURL: gifURL)
    }
     */
    
    class func showLoginAlert() {
        let message = "You need to login to your Sosokan account".localized()
        let signUpAction = UIAlertAction(title: "Sign Up".localized(), style: .default) { (alertAction) in
            AppState.setSignIn()
        }
        let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .cancel) { (alertAction) in
            // Do nothing
        }
        
        UIApplication.topViewController()?.showAlertWithActions("We're sorry".localized(),
                                                                message: message.localized(),
                                                                actions: [cancelAction, signUpAction])
    }
    
    class func showSignOutAlert() {
        let actionSheet = UIAlertController(title: "Are you sure you want to sign out?".localized(), message: nil, preferredStyle: .actionSheet)
        
        let logOutAction = UIAlertAction(title: "Sign out".localized(), style: UIAlertActionStyle.destructive) { (alertAction) -> Void in
            
            FBSDKLoginManager().logOut()
            FBSDKAccessToken.setCurrent(nil)
            User.logOut()
            AppState.setSignIn()
            
            NotificationCenter.default.post(name: NSNotification.Name(Notifications.didSignedOut), object: nil)
        }
        
        let cancelAction = UIAlertAction(title: "Cancel".localized(), style: UIAlertActionStyle.cancel) { (alertAction) -> Void in
            // Do nothing
        }
        
        actionSheet.addAction(logOutAction)
        actionSheet.addAction(cancelAction)
        
        UIApplication.topViewController()?.present(actionSheet, animated: true, completion: nil)
    }
    
    /*
    class func showLoadingGIF(data: NSData? = nil) {
        guard let view = AppDelegate.shared().window else { return }
        
        dispatch_async(dispatch_get_main_queue()) {
            let hud = MBProgressHUD.showHUDAddedTo(view, animated: true)
            
            if let data = (data ?? gifData) {
                let gifView = FLAnimatedImageView(frame: CGRectMake(0, 0, 75, 75))
                gifView.animatedImage = FLAnimatedImage(animatedGIFData: data)
                
                hud.mode = .CustomView
                hud.customView = gifView
                hud.color = AppColor.clear
                hud.backgroundColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.15)
            }
        }
    }
    
    class func hideLoadingGIF(animated: Bool = true) {
        guard let view = AppDelegate.shared().window else { return }
        
        dispatch_async(dispatch_get_main_queue()) {
            MBProgressHUD.hideAllHUDsForView(view, animated: true)
        }
    }
     */
    
    /*
    class func sortPosts(posts: [PFObject]) -> [PFObject] {
        var premiumPosts: [PFObject] = []
        var normalPosts: [PFObject] = []
        
        for post in posts {
            if let _ = post[AdvertisementKey.isFeature] as? Bool {
                premiumPosts.append(post)
            } else {
                normalPosts.append(post)
            }
        }
        
        premiumPosts = premiumPosts.sort({ (lhs, rhs) -> Bool in
            return (lhs[KEY_CREATED_AT] as? NSDate) > (rhs[KEY_CREATED_AT] as? NSDate)
        })
        
        normalPosts = normalPosts.sort({ (lhs, rhs) -> Bool in
            return (lhs[KEY_CREATED_AT] as? NSDate) > (rhs[KEY_CREATED_AT] as? NSDate)
        })
        
        return premiumPosts + normalPosts
    }
    */
}
