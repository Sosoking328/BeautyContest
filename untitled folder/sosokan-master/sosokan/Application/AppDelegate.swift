//
//  AppDelegate.swift
//  sosokan

import UIKit
import Material
import TSMessages
import FBSDKLoginKit
import STPopup
import SVProgressHUD

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    // MARK: Properties
    let defaultTransitionTime = TimeInterval(0.35)
    var window: UIWindow?
    var isOpen: Bool?
    var postAdButton: UIButton!
    
    // MARK: Private properties
    fileprivate let screenWidth     = UIScreen.main.bounds.width
    fileprivate let screenHeight    = UIScreen.main.bounds.height
    fileprivate let bottomBarHeight = CGFloat(50)
    
    class func shared() -> AppDelegate! {
        return UIApplication.shared.delegate as? AppDelegate
    }
    
    // MARK: Delegate life cycle
    func application(_ application: UIApplication, didFinishLaunchingWithOptions
        launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        // Use Firebase library to configure APIs
        BuddyBuildSDK.setup()
        WXApi.registerApp("wx79982ec7495cf6b0")
        
        configAppearance()
        
        //Prepare all the global views before set home view controller
        preparePostNewAdButton()
        
        let userDefaults = UserDefaults.standard
        if userDefaults.value(forKey: Keys.AppStateFlag.appLaunchFirstTime) == nil {
            // Remove authorization in first launch after re-install app
            userDefaults.setValue(nil, forKey: Keys.AppStateFlag.noLaunchReady)
            userDefaults.setValue(false, forKey: Keys.AppStateFlag.appLaunchFirstTime)

            FBSDKLoginManager.init().logOut()
        }
        else {
            // The flag check hot deals already popup once
            userDefaults.setValue(true, forKey: Keys.AppStateFlag.noLaunchReady)
        }
        
        // Get saved featured categories. 
        if let saveCats = DataManager.shared.savedCategories, saveCats.count > 0 {
            DataManager.shared.featureCategories = saveCats
        }
        
        if let _ = User.currentUser() {
            AppState.setHome()
            DataManager.shared.currentUser = User.currentUser()
            ProfileService.shared.fetchUserProfileWithCompletionHandler({ (json, error) in
                // Do nothing
            })
            CategoryService.shared.fetchCategoriesWithCompletionHandler(completionHandler: { (json, error) in
                // Do nothing
            })
        }
        else {
            AppState.setSignIn()
        }
        
        return true
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }
    
    
    func applicationDidEnterBackground(_ application: UIApplication) {
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        application.applicationIconBadgeNumber = 0
        FBSDKAppEvents.activateApp()
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        // Do nothing
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        debugPrint(userInfo)
        
        guard let aps = userInfo["aps"] as? JSONType else { return }
        guard let alert = aps["alert"] as? JSONType else { return }
        guard let title = alert["title"] as? String else { return }
        guard let message = alert["body"] as? String else { return }
        
        TSMessage.showNotification(withTitle: title, subtitle: message, type: .message)
    }
    
    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
        
        WXApi.handleOpen(url, delegate: self)
        
        FBSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: sourceApplication, annotation: annotation)
        
        return true
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        guard let touch = event?.allTouches?.first else  { return }
        let location = touch.location(in: self.window)
        let statusBarFrame = UIApplication.shared.statusBarFrame
        if statusBarFrame.contains(location) {
            NotificationCenter.default.post(name: Notification.Name(rawValue: Notifications.statusBarTouched), object: nil)
        }
    }
    
    func application(_ application: UIApplication, handleOpen url: URL) -> Bool {
        return WXApi.handleOpen(url, delegate: self)
    }
    
    // MARK: - Side menu
    fileprivate var _drawerViewController: KGDrawerViewController?
    var drawerViewController: KGDrawerViewController {
        get {
            if let viewController = _drawerViewController {
                return viewController
            }
            return prepareDrawerViewController()
        }
    }
    
    func prepareDrawerViewController() -> KGDrawerViewController {
        let drawerViewController = KGDrawerViewController()
        let leftVC = StoryBoardManager.viewController("Drawers", viewControllerName: "menuVCID")
        let homeVC = StoryBoardManager.viewController("Home", viewControllerName: "homeNCID")
        drawerViewController.centerViewController = homeVC
        drawerViewController.leftViewController = leftVC
        drawerViewController.backgroundImage = UIImage(named: "side-menu-bg")
        
        
        _drawerViewController = drawerViewController
        
        return drawerViewController
    }
    
    func toggleLeftDrawer(_ sender:AnyObject, animated:Bool) {
        _drawerViewController?.toggleDrawer(.left, animated: true, complete: { (finished) -> Void in
            // do nothing
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: Notifications.toggleLeftMenu), object: self, userInfo: nil)
        })
    }
    
    func toggleRightDrawer(_ sender:AnyObject, animated:Bool) {
        _drawerViewController?.toggleDrawer(.right, animated: true, complete: { (finished) -> Void in
            // do nothing
        })
    }
    
    fileprivate var _centerViewController: UIViewController?
    var centerViewController: UIViewController {
        get {
            if let viewController = _centerViewController {
                return viewController
            }
            return StoryBoardManager.viewController("Home", viewControllerName: "homeNCID")
        }
        set {
            if let drawerViewController = _drawerViewController {
                drawerViewController.closeDrawer(drawerViewController.currentlyOpenedSide, animated: true) { finished in }
                if drawerViewController.centerViewController != newValue {
                    drawerViewController.centerViewController = newValue
                }
            }
            _centerViewController = newValue
        }
    }
    
    // MARK: Method
    func createAdButtonDidTouched() {
        let message = "You need to login to your Sosokan account first to post your classifieds!".localized()
        guard let _ = DataManager.shared.currentUser else {
            let signUpAction = UIAlertAction(title: "Sign Up".localized(), style: .default) { (alertAction) in
                AppState.setSignIn()
            }
            let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .cancel) { (alertAction) in
                // Do nothing
            }
            UIApplication.topViewController()?.showAlertWithActions("We're sorry".localized(),
                                                                    message: message,
                                                                    actions: [cancelAction, signUpAction])
            
            return
        }
        
        AppState.presentCreateAdVCWithAd(nil)
    }
    
    // MARK: Private Method
    fileprivate func configAppearance() {
        UIApplication.shared.statusBarStyle = .lightContent
        
        UINavigationBar.appearance().titleTextAttributes = [NSFontAttributeName: UIFont.avenirLTStdRoman(size: 18),
                                                            NSForegroundColorAttributeName: AppColor.white]
        UINavigationBar.appearance().setBackgroundImage(UIImage(named: "navigationbar")!.resizableImage(withCapInsets: UIEdgeInsets.zero, resizingMode: .stretch), for: .default)
        UINavigationBar.appearance().tintColor = UIColor.white
        
        SVProgressHUD.setDefaultMaskType(.clear)
        SVProgressHUD.setBackgroundColor(UIColor.clear)
        SVProgressHUD.setRingRadius(15)
        SVProgressHUD.setRingNoTextRadius(15)
        SVProgressHUD.setForegroundColor(AppColor.orange)
        SVProgressHUD.setOffsetFromCenter(UIOffset.init(horizontal: 0, vertical: 35))
        
        let popup = STPopupNavigationBar.appearance()
        popup.barTintColor = AppColor.orange
        popup.tintColor = AppColor.white
        popup.barStyle = .default
        popup.titleTextAttributes = [NSFontAttributeName: UIFont.latoBold(size: AppFontsizes.big),
                                     NSForegroundColorAttributeName: AppColor.white]
    }
    
    fileprivate func preparePostNewAdButton() {
        postAdButton = UIButton(frame: CGRect(x: screenWidth - 100, y: screenHeight - 100, width: 100, height: 100))
        postAdButton.setImage(UIImage(named: "post-ad"), for: UIControlState())
        postAdButton.addTarget(self, action: #selector(AppDelegate.createAdButtonDidTouched), for: .touchUpInside)
        postAdButton.isHidden = true
    }
}

// MARK: - WXApiDelegate
extension AppDelegate: WXApiDelegate {
    func onResp(_ resp: BaseResp!) {
        if let authResponse = resp as? SendAuthResp {
            if authResponse.errCode == WXSuccess.rawValue {
                NotificationCenter.default.post(name: Notification.Name(rawValue: Notifications.loggedInWithWechat), object: authResponse.code)
            }
            else {
                NotificationCenter.default.post(name: Notification.Name(rawValue: Notifications.loggedInWithWechat), object: nil)
            }
        }
    }
    
    func onReq(_ req: BaseReq!) {
        
    }
}
