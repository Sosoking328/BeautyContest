//
//  ProfileViewController.swift
//  sosokan
//
//  Created by David Nguyentan on 6/15/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material
import Photos
import Localize_Swift
import Doorbell
import Then

class SettingCellConfiguration {
    var image: UIImage?
    var title: String
    var action: () -> Void
    
    init(image: UIImage?, title: String, action: @escaping () -> Void) {
        self.image = image
        self.title = title
        self.action = action
    }
}

class ProfileViewController: UIViewController {

    // MARK: IBOutlets
    @IBOutlet weak var topImageView: UIImageView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var fakeNavigationBarView: UIView!
    @IBOutlet weak var leftMenuButton: Button!
    @IBOutlet weak var languageButton: UIButton!
    @IBOutlet weak var versionLabel: UILabel!
//    @IBOutlet weak var creditButton: UIBarButtonItem!
    
    // MARK: Variables
    var settingCellConfigs = [SettingCellConfiguration]()
    var topImageViewBlurView: UIView?
    var imagePickerController = UIImagePickerController()
    
    // MARK: IBActions
    
    @IBAction func unwindToProfileVC(segue: UIStoryboardSegue) {
    }
    
    @IBAction func leftMenuButtonTapped(sender: Button) {
        print("leftMenuButtonTapped")
    }
    
    @IBAction func avatarImageViewTapped(_ sender: Any) {
        updateUserAvatar()
    }

    @IBAction func getCreditsButtonTapped(sender: UIBarButtonItem) {
    }
    
    // MARK: View controller life circle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Setup subviews
        prepareVariable()
        prepareAvatarImage()
        setupCollectionView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.setText),
                                               name: NSNotification.Name(rawValue: LCLLanguageChangeNotification),
                                               object: nil)
        setText()
        
        // Setup navigation bar
        setupNavigationBar()
        
        // Navigation bar title
        prepareNavigationTitle()
        
        // Hidden the post button
        AppDelegate.shared().postAdButton.isHidden = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        dismissKeyboad()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        
        // Add a blur view to top background if needed
        addTopImageViewBlurViewIfNeeded()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "popoverLanguageVCSegue" {
            if let controller = segue.destination as? LanguageTableViewController {
                controller.delegate = self
                controller.popoverPresentationController?.delegate = self
                controller.preferredContentSize = CGSize(width: 220, height: 128)
            }
        }
    }
    
    // MARK: Funtions
    
    func setText() {
        prepareNavigationTitle()
        versionLabel.text = UIApplication.versionBuild()
        if IS_ENGLISH {
            languageButton.setTitle("🇺🇸   " + "English", for: .normal)
        } else {
            languageButton.setTitle("🇨🇳   " + "Chinese (Simplified)".localized(), for: .normal)
        }
        collectionView.reloadData()
    }
    
    func setupNavigationBar() {
        // Setup navigation bar
        navigationController?.navigationBar.barStyle = .black
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.tintColor = Color.white
        navigationController?.navigationBar.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.0)
        navigationController?.navigationBar.isTranslucent = true
    }
    
    func prepareNavigationTitle() {
        
        // Left menu button
        let leftButton = UIBarButtonItem(image: UIImage(named: "left-menu"), style: .plain, target: self, action: #selector(handleLeftNavButtonTapped))
        navigationItem.leftBarButtonItem = leftButton
        
        
        // Right menu button
        guard let _ = DataManager.shared.userProfile else { return }
        /*
        if let role = user["role"] as? String, r = UserRole(rawValue: role) where r.compareWith(UserRole.Admin) {
            let rightButton = UIBarButtonItem(image: UIImage(named: "users"), style: .Plain, target: self, action: #selector(handleRightNavButtonTapped))
            navigationItem.rightBarButtonItem = rightButton
        }
        */
        // Navigation title
        
        let boldText = DataManager.shared.userProfile?.displayName ?? ""
        let boldAttrs = [
            NSFontAttributeName: UIFont.latoBold(size: 18),
            NSForegroundColorAttributeName: AppColor.white
        ]
        let boldString = NSMutableAttributedString(string: boldText, attributes: boldAttrs)
        
        // Display username as navigation title
        navigationItem.titleView = UILabel()
            .then({
                $0.backgroundColor = AppColor.clear
                $0.attributedText = boldString
                $0.textAlignment = .center
                $0.sizeToFit()
            })
        
        // Display username & posts count as nagitaion title
//        let numberOfPosts = userProfile.posts.count
        let subfix = /*numberOfPosts*/5 > 1 ? "classifieds" : "classified"
        let normalAattrs = [
            NSFontAttributeName: UIFont.latoRegular(size: 14),
            NSForegroundColorAttributeName: UIColor(red: 1, green: 1, blue: 1, alpha: 0.8)
        ]
        let normalString = NSMutableAttributedString(string: "\(/*numberOfPosts*/5)" + " " + subfix.localized(), attributes: normalAattrs)
        
        boldString.append(NSMutableAttributedString(string: "\n"))
        boldString.append(normalString)
        
        self.navigationItem.titleView = UILabel()
            .then({
                $0.backgroundColor = AppColor.clear
                $0.attributedText = boldString
                $0.textAlignment = .center
                $0.numberOfLines = 2
                $0.sizeToFit()
            })
    }

    func setupCollectionView() {
        collectionView.dataSource = self
        collectionView.delegate = self
    }
    
    func handleLeftNavButtonTapped(sender: AnyObject) {
        AppDelegate.shared().toggleLeftDrawer(sender, animated: true)
    }
    
    func addTopImageViewBlurViewIfNeeded() {
        if topImageViewBlurView == nil {
            topImageViewBlurView = UIView(frame: topImageView.frame)
            topImageViewBlurView!.backgroundColor = UIColor(white: 0, alpha: 0.25)
            topImageView.addSubview(topImageViewBlurView!)
        }
    }
    
    // MARK: - Private methods
    private func prepareVariable() {
        
        settingCellConfigs.append(SettingCellConfiguration(image: UIImage(named: "human"),
            title: "Edit Profile",
            action: { [weak self] in
                guard let `self` = self else {
                    return
                }
                
                if let _ = DataManager.shared.userProfile {
                    let editProfileViewController = StoryBoardManager.viewController("EditProfile", viewControllerName: String(describing: EditProfileViewController.self)) as! EditProfileViewController
//                    editProfileViewController.userProfile = userProfile
                    self.navigationController?.show(editProfileViewController, sender: nil)
                }
                else {
                    AppHelper.showLoginAlert()
                }
            }))
        
        settingCellConfigs.append(SettingCellConfiguration(image: UIImage(named: "photo"),
            title: "Feedback",
            action: { [weak self] in
                guard let this = self else { return }
                
                let appId = "4288"
                let appKey = "4UbgZYwt2nWKCY7KjNyTjDA4ezTMm03e2wK6nAWcXvdtC6ACbghlkcf7KG5XRF0a"
                
                let feedback = Doorbell.init(apiKey: appKey, appId: appId)
                feedback?.showFeedbackDialog(in: self, completion: { (error, isCancelled) in
                    if let error = error {
                        debugPrint(error)
                        this.showErrorMessage(error.localizedDescription)
                    }
                })
            }))
        
        settingCellConfigs.append(SettingCellConfiguration(image: UIImage(named: "subscription-large"),
            title: "Following",
            action: { [weak self] in
                guard let this = self else { return }
                
                this.showSosokanMessage("Feature is not available".localized())
                
//                if let userProfile = DataManager.shared.userProfile.value {
//                    let vc = StoryBoardManager.viewController(storyBoardName: "EditProfile", viewControllerName: "EditProfileViewController") as! EditProfileViewController
//                    vc.scrollToSubscription = true
//                    vc.userProfile = userProfile
//                    this.navigationController?.showViewController(vc, sender: nil)
//                } else {
//                    AppHelper.showLoginAlert()
//                }
            }))
        
        settingCellConfigs.append(SettingCellConfiguration(image: UIImage(named: "faq"),
            title: "FAQ",
            action: { [weak self] in
                guard let this = self else { return }
                
                let vc = StoryBoardManager.viewController("FAQ", viewControllerName: "FAQViewController")
                this.navigationController?.show(vc, sender: nil)
            }))
        
        settingCellConfigs.append(SettingCellConfiguration(image: UIImage(named: "friends"),
            title: "Invite Friends",
            action: { [weak self] in
                guard let this = self else { return }
                
                let vc = StoryBoardManager.viewController("Profile", viewControllerName: "InvitationTableViewController")
                this.navigationController?.show(vc, sender: nil)
            }))
        
        var image: UIImage?
        var title: String
        
        if let _ = DataManager.shared.currentUser {
            image = UIImage(named: "logOut")
            title = "Logout".localized()
        } else {
            image = UIImage(named: "login-large")
            title = "Login".localized()
        }
        
        settingCellConfigs.append(SettingCellConfiguration(image: image,
            title: title,
            action: { [weak self] in
                guard let _ = self else { return }
                
                if let _ = DataManager.shared.currentUser {
                    AppHelper.showSignOutAlert()
                } else {
                    AppState.setSignIn()
                }
            }))
    }
    
    private func checkForCameraAccess() {
        let authorizationStatus = AVCaptureDevice.authorizationStatus(forMediaType: AVMediaTypeVideo)
        switch authorizationStatus {
        case .notDetermined:
            // permission dialog not yet presented, request authorization
            self.showHUD()
            
            AVCaptureDevice.requestAccess(forMediaType: AVMediaTypeVideo,
                                                      completionHandler: { (granted:Bool) -> Void in
                                                        self.dismissHUD()
                                                        
                                                        if granted {
                                                            print("access granted")
                                                            self.imagePickerController.sourceType    = .camera
                                                            self.imagePickerController.allowsEditing = true
                                                            self.present(self.imagePickerController, animated: true, completion: { () in
                                                                UIApplication.shared.setStatusBarStyle(.lightContent, animated: true)
                                                            })
                                                        }
                                                        else {
                                                            print("access denied")
                                                            //                                                            self.presentNoMediaPermissionPage()
                                                        }
            })
        case .authorized:
            print("Access authorized")
            self.imagePickerController.sourceType    = .camera
            self.imagePickerController.allowsEditing = true
            self.present(self.imagePickerController, animated: true, completion: { () in
                UIApplication.shared.setStatusBarStyle(.lightContent, animated: true)
            })
        case .denied, .restricted:
            print("Denied Restricted")
            self.presentNoMediaPermissionPage()
        }
    }
    
    private func checkForPhotosAccess() {
        let status = PHPhotoLibrary.authorizationStatus()
        switch status {
        case .authorized:
            // handle authorized status
            print("Authorized")
            self.imagePickerController.sourceType    = .photoLibrary
            self.imagePickerController.allowsEditing = true
            self.present(self.imagePickerController, animated: true, completion: { () in
                UIApplication.shared.setStatusBarStyle(.lightContent, animated: true)
            })
        case .denied, .restricted :
            // handle denied status
            print("Denied, Restricted")
            self.presentNoMediaPermissionPage()
            break
        case .notDetermined:
            // ask for permissions
            print("NotDetermined")
            self.showHUD()
            
            PHPhotoLibrary.requestAuthorization() { (status) -> Void in
                self.dismissHUD()
                
                switch status {
                case .authorized:
                    // as above
                    self.imagePickerController.sourceType    = .photoLibrary
                    self.imagePickerController.allowsEditing = true
                    self.present(self.imagePickerController, animated: true, completion: { () in
                        UIApplication.shared.setStatusBarStyle(.lightContent, animated: true)
                    })
                case .denied, .restricted:
                    // as above
                    self.presentNoMediaPermissionPage()
                    break
                case .notDetermined:
                    // won't happen but still
                    break
                }
            }
        }
    }
    
    private func presentNoMediaPermissionPage() {
        let noMediaPermissionVC = NoMediaPermissionViewController(nibName: "NoMediaPermissionViewController", bundle: nil)
        let noMediaPermissionNC = UINavigationController(rootViewController: noMediaPermissionVC)
        noMediaPermissionVC.navigationController?.navigationBar.tintColor = UIColor.white
        noMediaPermissionVC.navigationController?.navigationBar.barTintColor = UIColor.black
        
        present(noMediaPermissionNC, animated: true, completion: nil)
    }
    
    private func updateUserAvatar() {
        if User.currentUser() == nil {
            AppHelper.showLoginAlert()
        }
        else {
            let actionSheet:UIAlertController = UIAlertController(title:Messages.cameraTitle.localized(),
                                                                  message:Messages.cameraDescription.localized(), preferredStyle:.actionSheet)
            
            let cancelAction = UIAlertAction(title:Messages.alertCancel.localized(), style:.cancel,handler: {
                action -> Void in
            })
            actionSheet.addAction(cancelAction)
            
            let photoAction = UIAlertAction(title:Messages.cameraTakePhoto.localized().localized(), style: .default, handler: {
                action -> Void in
                if UIImagePickerController.isSourceTypeAvailable(.camera) {
                    self.checkForCameraAccess()
                }
            })
            actionSheet.addAction(photoAction)
            
            let cameraRollAction = UIAlertAction(title:Messages.cameraRoll.localized(), style:.default, handler:{
                action -> Void in
                
                self.checkForPhotosAccess()
            })
            actionSheet.addAction(cameraRollAction)
            imagePickerController.delegate = self
            present(actionSheet, animated: true, completion:nil)
        }
    }
    
    private func prepareAvatarImage() {
        let noAvatarImage = UIImage(named: "camera_large")
        topImageView.then({
            $0.image = noAvatarImage
            $0.contentMode = .center
            $0.setNeedsLayout()
            $0.layoutIfNeeded()
        })
        /*
        DataManager.shared.userProfile
            .asObservable()
            .filterNil()
            .subscribeNext({ [weak self] userProfile in
                guard let `self` = self else { return }
                if let URL = userProfile.avatar?.absoluteImageURL {
                    self.topImageView.contentMode = .ScaleAspectFill
                    DataManager.shared.getCacheImage(URL, onCompleted: { [weak self] image in
                        guard let `self` = self else { return }
                        if let image = image {
                            self.topImageView.image = image
                        }
                        else {
                            self.topImageView.sd_setImageWithURL(URL, placeholderImage: noAvatarImage)
                        }
                        })
                }
                else {
                    self.topImageView.image = noAvatarImage
                }
                })
            .addDisposableTo(self.rx_disposeBag)
         */
    }
}

// MARK: - For UICollectionViewDataSource
extension ProfileViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return settingCellConfigs.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileCollectionViewCell.cellReuseIdentifier, for: indexPath as IndexPath) as! ProfileCollectionViewCell
        let cellConfig = settingCellConfigs[indexPath.row]
        
        cell.backgroundPulseView.borderWidth = 0.5
        cell.backgroundPulseView.borderColor = Color.grey.lighten3
        cell.centerImageView.image = cellConfig.image
        cell.titleLabel.text = cellConfig.title.uppercased().localized()
        
        return cell
    }
}

// MARK: - For UICollectionViewDelegate

extension ProfileViewController: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        settingCellConfigs[indexPath.row].action()
    }
}

// MARK - For UICollectionViewDelegateFlowLayout

extension ProfileViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let collectionViewWidth = collectionView.frame.width
        let collectionViewHeight = collectionView.frame.height
        
        return CGSize(width: collectionViewWidth / 2, height: collectionViewHeight / 3)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
}

// MARK: - UIImagePickerControllerDelegate

extension ProfileViewController: UIImagePickerControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        guard let _ = DataManager.shared.userProfile else { return }
        let selectedImage = info[UIImagePickerControllerEditedImage] as? UIImage
        topImageView.contentMode = .scaleAspectFill
        topImageView.image = selectedImage
        topImageView.alpha = 0.7
        
        //TODO: Perform update user avatar
        
        // Dismiss photo or camera view
        dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        imagePickerController.dismiss(animated: true, completion: nil)
    }
}

// MARK: - UINavigationControllerDelegate
//extension ProfileViewController: UINavigationControllerDelegate {
//    func navigationController(_ navigationController: UINavigationController, willShow viewController: UIViewController, animated: Bool) {
//        // Navigation bar style
//        navigationController.navigationBar.barStyle = .black
//        navigationController.navigationBar.tintColor = UIColor.white
//        navigationController.navigationBar.barTintColor = AppColor.orange
//        navigationController.navigationBar.isTranslucent = false
//        navigationController.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.white]
//    }
//}

// MARK: - UIPopoverPresentationControllerDelegate

extension ProfileViewController: UIPopoverPresentationControllerDelegate {
    func adaptivePresentationStyle(for controller: UIPresentationController,
                                   traitCollection: UITraitCollection) -> UIModalPresentationStyle {
        return .none
    }
}

// MARK: - LanguageTableViewControllerDelegate

extension ProfileViewController: LanguageTableViewControllerDelegate {
    func tableViewSelectedIndex(_ index: Int) {
        if index == 0 {
            languageButton.setTitle("🇺🇸   " + "English", for: .normal)
        }
        else {
            languageButton.setTitle("🇨🇳   " + "Chinese (Simplified)".localized(), for: .normal)
        }
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: Notifications.languageChanged), object: nil)
    }
}
