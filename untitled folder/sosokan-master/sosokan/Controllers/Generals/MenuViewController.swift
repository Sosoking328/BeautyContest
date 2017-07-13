//
//  MenuViewController.swift
//  sosokan
//
//  Created by An Phan on 6/7/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material
import Localize_Swift
import Then
import SDWebImage
import FBSDKLoginKit
import SVProgressHUD
import Kingfisher

class MenuViewController: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var avtImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var totalAdLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableHeaderView: UIView!
    
    // MARK: - Properties
    let menuTitles = ["HOME", "MESSAGES", "MY POSTS", "FAVORITES", "FOLLOWING", "SETTINGS", "LOG OUT"];
    let menuImages = ["home", "message", "post", "favorite", "subscription", "setting", "signout"]
    
    private struct Constants {
        static let tableHeaderViewDefaultHeight: CGFloat = 80
        static let tableHeaderViewUserProfileHeight: CGFloat = 150
        static let defaultUserImage = #imageLiteral(resourceName: "no-avatar")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.observerNotifications()
        
        self.prepareTableView()
        self.prepareTableHeaderView()
        self.prepareUserProfileImageView()
        self.prepareUserProfileNameLabel()
        self.prepareUserProfileTotalPostsLabel()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.reloadMenu),
                                               name: NSNotification.Name(rawValue: LCLLanguageChangeNotification),
                                               object: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: Functions
    
    func pushToViewController(_ viewController: UIViewController) {
        AppDelegate.shared().centerViewController = viewController
    }
    
    func presentSettingsPage() {
         let profileViewController = StoryBoardManager.viewController("Profile",
                                                                      viewControllerName: "ProfileViewControllerNav")
         self.pushToViewController(profileViewController)
    }
    
    // MARK: - Methods
    
    func reloadMenu() {
        self.tableView.reloadData()
        
        if !User.isLoggedIn() {
            var frame = tableHeaderView.frame
            frame.size.height = Constants.tableHeaderViewDefaultHeight
            tableHeaderView.frame = frame
            tableView.tableHeaderView = tableHeaderView
            
            avtImageView.isHidden = true
            
            totalAdLabel.text = nil
            
            nameLabel.text = nil
        }
    }
    
    func handleNotification(currentUserProfileFetched sender: Notification?) {
        guard let currentUserProfile = DataManager.shared.userProfile else { return }
        self.renderUser(profile: currentUserProfile)
    }
    
    // MARK: - Private method
    
    fileprivate func prepareTableView() {
        self.tableView.dataSource = self
        self.tableView.delegate = self
    }
    
    fileprivate func observerNotifications() {
        
        let notificationCenter = NotificationCenter.default
        
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.didLoggedIn),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.createdNewAd),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.updatedUserAvatar),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(Notifications.didSignedOut),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.updatedUserInfo),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.toggleLeftMenu),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.updatedUserInfo),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.reloadMenu),
                                       name: NSNotification.Name(rawValue: Notifications.languageChanged),
                                       object: nil)
        notificationCenter.addObserver(self,
                                       selector: #selector(self.handleNotification(currentUserProfileFetched:)),
                                       name: Notification.Name(Notifications.fetchedCurrentUserProfile),
                                       object: nil)
    }
    
    fileprivate func showSignOutAlert() {
        AppDelegate.shared().postAdButton.isHidden = true
        
        let actionSheet = UIAlertController(title: "Are you sure you want to sign out?".localized(),
                                            message: nil,
                                            preferredStyle: UIAlertControllerStyle.actionSheet)
        
        let logOutAction = UIAlertAction(title: "Sign out".localized(), style: UIAlertActionStyle.destructive) { (alertAction) -> Void in
            SVProgressHUD.show()
            
            /*
             if let currentUser = FIRAuth.auth()?.currentUser {
             NotificationHelper.setToken(userId: currentUser.uid, token: nil, onCompleted: { (error) in
             
             SVProgressHUD.dismiss()
             
             if let error = error {
             debugPrint(error)
             self.showErrorMessage(Messages.occurredError)
             }
             else {
             try! FIRAuth.auth()?.signOut()
             FBSDKLoginManager.init().logOut()
             FBSDKAccessToken.setCurrentAccessToken(nil)
             AppState.setSignIn()
             }
             })
             }
             else {
             
             SVProgressHUD.dismiss()
             
             try! FIRAuth.auth()?.signOut()
             FBSDKLoginManager.init().logOut()
             FBSDKAccessToken.setCurrentAccessToken(nil)
             AppState.setSignIn()
             }
             */
        }
        
        let cancelAction = UIAlertAction(title: "Cancel".localized(), style: UIAlertActionStyle.cancel) { (alertAction) -> Void in
            // Do nothing
            AppDelegate.shared().postAdButton.isHidden = false
        }
        
        actionSheet.addAction(logOutAction)
        actionSheet.addAction(cancelAction)
        
        let topVC = UIApplication.topViewController()
        topVC?.present(actionSheet, animated: true, completion: nil)
    }
    
    fileprivate func loginValidation(_ message: String, validationSuccessful: () -> Void) {
        guard let _ = DataManager.shared.currentUser else {
            let signUpAction = UIAlertAction(title: "Sign Up".localized(), style: .default) { (alertAction) in
                AppState.setSignIn()
            }
            let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .cancel) { (alertAction) in
                // Do nothing
            }
            
            return self.showAlertWithActions("We're sorry".localized(), message: message, actions: [cancelAction, signUpAction])
        }
        
        validationSuccessful()
    }
    
    fileprivate func prepareTableHeaderView() {
        let tapGestuse = UITapGestureRecognizer(target: self,
                                                action: #selector(self.presentSettingsPage))
        self.tableHeaderView.addGestureRecognizer(tapGestuse)
        
        self.tableHeaderView.backgroundColor = Color.clear
        
        var frame = self.tableHeaderView.frame
        frame.size.height = User.isLoggedIn() ? Constants.tableHeaderViewUserProfileHeight : Constants.tableHeaderViewDefaultHeight
        self.tableHeaderView.frame = frame
        self.tableView.tableHeaderView = self.tableHeaderView
    }
    
    fileprivate func prepareUserProfileImageView() {
        self.avtImageView.roundify()
        self.avtImageView.contentMode = .scaleAspectFill
        self.avtImageView.image = Constants.defaultUserImage
        self.avtImageView.isHidden = !User.isLoggedIn()
    }
    
    fileprivate func prepareUserProfileNameLabel() {
        self.nameLabel.text = nil
    }
    
    fileprivate func prepareUserProfileTotalPostsLabel() {
        self.totalAdLabel.text = nil
    }
    
    fileprivate func renderUser(profile: Profile) {
        
        avtImageView.isHidden = !User.isLoggedIn()
        if let userImageURL = profile.absoluteImageURL {
            self.avtImageView.kf.setImage(with: userImageURL, placeholder: Constants.defaultUserImage)
        }
        
        self.nameLabel.text = profile.displayName
        
        let numberOfPostsSufix = profile.numberOfPosts > 1 ? "classifieds".localized() : "classified".localized()
        self.totalAdLabel.text = profile.numberOfPosts.string() + " " + numberOfPostsSufix
        
        self.tableView.reloadData()
    }
    
}

// MARK: - UITableViewDataSource

extension MenuViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return menuTitles.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "menuCellID", for: indexPath) as! MenuTableViewCell
        
        let isLoggedIn = User.isLoggedIn()
        let menuTitle = menuTitles[indexPath.row].localized()
        let menuImage = UIImage(named: menuImages[indexPath.row])
        
        switch indexPath.row {
        case menuTitles.count - 1: // Login
            cell.render(image: isLoggedIn ? menuImage : UIImage(named: "login"),
                        title: isLoggedIn ? menuTitle : "LOGIN".localized(),
                        number: nil)
        case 1: // Messages
            cell.render(image: menuImage,
                        title: menuTitle,
                        number: nil)
            /*
            if numberOfMessages > 0 {
                cell.menuNumberLabel.text = "\(numberOfMessages)"
                cell.menuNumberLabel.backgroundColor = AppColor.orange
            }
            else {
                cell.menuNumberLabel.text = ""
                cell.menuNumberLabel.backgroundColor = nil
            }
            */
        case 4: // Followings
            cell.render(image: menuImage,
                        title: menuTitle,
                        number: DataManager.shared.userProfile?.numberOfFollowings)
        default:
            cell.render(image: menuImage,
                        title: menuTitle,
                        number: nil)
        }
        
        return cell
    }
}

// MARK: - UITableViewDelegate

extension MenuViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
//            let viewController = PaginationViewController(filterOptions: DataManager.shared.filterOptions,
//                                                          viewType: DataManager.shared.viewType)
//            let navigation = UINavigationController.init(rootViewController: viewController)
            let homeNC = StoryBoardManager.viewController("Home", viewControllerName: "homeNCID")
            pushToViewController(homeNC)
        case 1:
            
            // TODO: Debug code. Remove later.
            break
            /*
            let message = "You need to login to your Sosokan account first to see your messages!"
            
            loginValidation(message.localized(), validationSuccessful: {
                let vc = StoryBoardManager.viewController("Message",
                                                          viewControllerName: "ConversationNavigationController")
                self.pushToViewController(vc)
            })
             */
        case 2, 3:
            var message = "You need to login to your Sosokan account first to see your favorites!"
            if indexPath.row == 2 {
                message = message.replacingOccurrences(of: "favorites", with: "posts")
            }
            
            loginValidation(message.localized(), validationSuccessful: {
                let nc = StoryBoardManager.viewController("Favorite",
                                                          viewControllerName: "favoriteNCID") as! UINavigationController
                let favVC = nc.topViewController as! FavoriteViewController
                favVC.isFavorites = indexPath.row == 3
                
                self.pushToViewController(nc)
            })
        case 4:
            // FIXME: MISSING USER AUTH
            let viewController = FollowingViewController(userId: 16)
            let navigation = UINavigationController(rootViewController: viewController)
            self.pushToViewController(navigation)
            /*
             if let currentUser = FIRAuth.auth()?.currentUser {
             let followingViewController = FollowingViewController.init(nibName: String.init(FollowingViewController), bundle: nil)
             followingViewController.userId = currentUser.uid
             let navigationController = UINavigationController.init(rootViewController: followingViewController)
             self.pushToViewController(navigationController)
             }
             else {
             AppHelper.showLoginAlert()
             }
             */
        case 5:
            let vc = StoryBoardManager.viewController("Profile", viewControllerName: "ProfileViewControllerNav")
            pushToViewController(vc)
        case 6:
            if User.isLoggedIn() {
                AppHelper.showSignOutAlert()
            }
            else {
                AppState.setSignIn()
            }
        default:
            break
        }
    }
}
