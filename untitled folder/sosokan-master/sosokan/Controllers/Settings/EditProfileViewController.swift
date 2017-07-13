//
//  EditProfileViewController.swift
//  sosokan
//

import UIKit
import Photos

class EditProfileViewController: UIViewController {
    
    // MARK: IBOulet
    
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var separatorAvatarTableView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var saveButton: UIButton!
    
    // MARK: Variables
    
    fileprivate var imagePickerController = UIImagePickerController()
    fileprivate var tempUserProfile = DataManager.shared.userProfile!
    
    fileprivate struct Constants {
        static let defaultCellRowHeight: CGFloat = 64
        static let defaultHeaderHeight: CGFloat = 45
    }
    
    fileprivate enum ProfileSetting: Int {
        case company = 0, name, note, address, city, zip, state, phone, email, web, isEnableEmail, isEnablePhoneCall
        
        static var numberOfStringSettings: Int {
            return 10
        }
        
        static var numberOfBooleanSettings: Int {
            return 2
        }
        
        var title: String {
            switch self {
            case .company:
                return "COMPANY NAME".localized()
            case .name:
                return "NAME".localized()
            case .note:
                return "NOTE".localized()
            case .address:
                return "ADDRESS".localized()
            case .city:
                return "CITY".localized()
            case .zip:
                return "ZIP".localized()
            case .state:
                return "STATE".localized()
            case .phone:
                return "PHONE".localized()
            case .email:
                return "EMAIL".localized()
            case .web:
                return "WEB".localized()
            case .isEnableEmail:
                return "ALLOW PEOPLE TO EMAIL ME".localized()
            case .isEnablePhoneCall:
                return "ALLOW PEOPLE TO CALL ME".localized()
            }
        }
        
        var placeHolder: String? {
            switch self {
            case .company:
                return "Name of your company".localized()
            case .name:
                return "Name will be displayed".localized()
            case .note:
                return "Note".localized()
            case .address:
                return "Address".localized()
            case .city:
                return "City".localized()
            case .zip:
                return "Zip".localized()
            case .state:
                return "State".localized()
            case .phone:
                return "Phone number".localized()
            case .email:
                return "Email address".localized()
            case .web:
                return "Website".localized()
            default:
                return nil
            }
        }
    }
    
    // MARK: IBActions
    
    @IBAction func saveButtonTapped(sender: Any) {
        
    }
    
    // MARK: view controller life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()

        prepareBackground()
        prepareAvatarImageView()
        prepareTableView()
        prepareSaveButton()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        setText()
        
        prepareNavigationBar()
        prepareLeftNavigationBarButton()
        prepareNavigationTitle()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Methods
    
    func setText() {
        saveButton.setTitle("SAVE".localized(), for: .normal)
    }
    
    func avatarImageViewTapped(sender: Any) {
        let cancelAction = UIAlertAction(title: Messages.alertCancel.localized(), style: .cancel,handler: { action -> Void in
            //
        })
        let photoAction = UIAlertAction(title: Messages.cameraTakePhoto.localized(), style: .default, handler: { action -> Void in
            if UIImagePickerController.isSourceTypeAvailable(.camera) {
                self.checkForCameraAccess()
            }
        })
        let cameraRollAction = UIAlertAction(title: Messages.cameraRoll.localized(), style: .default, handler: { action -> Void in
            self.checkForPhotosAccess()
        })
        self.showAlert(Messages.cameraTitle.localized(),
                       message: Messages.cameraDescription.localized(),
                       style: UIAlertControllerStyle.actionSheet,
                       actions: [cancelAction, photoAction, cameraRollAction])
    }
    
    // MARK: fileprivate methods
    
    fileprivate func getProfileSettingValue(forSetting setting: ProfileSetting) -> Any? {
        switch setting {
        case .company:
            return tempUserProfile.company
        case .name:
            return tempUserProfile.displayName
        case .note:
            return tempUserProfile.note
        case .address:
            return tempUserProfile.address
        case .city:
            return tempUserProfile.city
        case .zip:
            return tempUserProfile.zip
        case .state:
            return tempUserProfile.state
        case .phone:
            return tempUserProfile.phone
        case .email:
            return tempUserProfile.email
        case .web:
            return tempUserProfile.website
        case .isEnableEmail:
            return tempUserProfile.enableEmail
        case .isEnablePhoneCall:
            return tempUserProfile.enableCall
        }
    }
    
    fileprivate func setProfileSetting(value: Any, forSetting setting: ProfileSetting) {
        switch setting {
        case .company:
            guard let company = value as? String else { return }
            tempUserProfile.company = company
        case .name:
            guard let displayName = value as? String else { return }
            tempUserProfile.displayName = displayName
        case .note:
            guard let note = value as? String else { return }
            tempUserProfile.note = note
        case .address:
            guard let address = value as? String else { return }
            tempUserProfile.address = address
        case .city:
            guard let city = value as? String else { return }
            tempUserProfile.city = city
        case .zip:
            guard let zip = value as? String else { return }
            tempUserProfile.zip = zip
        case .state:
            guard let state = value as? String else { return }
            tempUserProfile.state = state
        case .phone:
            guard let phone = value as? String else { return }
            tempUserProfile.phone = phone
        case .email:
            guard let email = value as? String else { return }
            tempUserProfile.email = email
        case .web:
            guard let website = value as? String else { return }
            tempUserProfile.website = website
        case .isEnableEmail:
            guard let enableEmail = value as? Bool else { return }
            tempUserProfile.enableEmail = enableEmail
        case .isEnablePhoneCall:
            guard let enableCall = value as? Bool else { return }
            tempUserProfile.enableCall = enableCall
        }
    }
    
    fileprivate func popViewController() {
        navigationController?.resignFirstResponder()
        _ = navigationController?.popViewController(animated: true)
    }
    
    fileprivate func checkForCameraAccess() {
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
                                                            self.imagePickerController.sourceType    = UIImagePickerControllerSourceType.camera
                                                            self.imagePickerController.allowsEditing = true
                                                            self.present(self.imagePickerController, animated: true, completion: { () in
                                                                UIApplication.shared.setStatusBarStyle(UIStatusBarStyle.lightContent, animated: true)
                                                            })
                                                        }
                                                        else {
                                                            print("access denied")
                                                            //self.presentNoMediaPermissionPage()
                                                        }
            })
        case .authorized:
            print("Access authorized")
            self.imagePickerController.sourceType    = UIImagePickerControllerSourceType.camera
            self.imagePickerController.allowsEditing = true
            self.present(self.imagePickerController, animated: true, completion: { () in
                UIApplication.shared.setStatusBarStyle(UIStatusBarStyle.lightContent, animated: true)
            })
        case .denied, .restricted:
            print("Denied Restricted")
            self.presentNoMediaPermissionPage()
        }
    }
    
    fileprivate func checkForPhotosAccess() {
        let status = PHPhotoLibrary.authorizationStatus()
        switch status {
        case .authorized:
            // handle authorized status
            print("Authorized")
            self.imagePickerController.sourceType    = UIImagePickerControllerSourceType.photoLibrary
            self.imagePickerController.allowsEditing = true
            self.present(self.imagePickerController, animated: true, completion: { () in
                UIApplication.shared.setStatusBarStyle(UIStatusBarStyle.lightContent, animated: true)
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
                    self.imagePickerController.sourceType    = UIImagePickerControllerSourceType.photoLibrary
                    self.imagePickerController.allowsEditing = true
                    self.present(self.imagePickerController, animated: true, completion: { () in
                        UIApplication.shared.setStatusBarStyle(UIStatusBarStyle.lightContent, animated: true)
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
    
    fileprivate func presentNoMediaPermissionPage() {
        let noMediaPermissionVC = NoMediaPermissionViewController(nibName: "NoMediaPermissionViewController", bundle: nil)
        let noMediaPermissionNC = UINavigationController(rootViewController: noMediaPermissionVC)
        noMediaPermissionVC.navigationController?.navigationBar.tintColor = AppColor.white
        noMediaPermissionVC.navigationController?.navigationBar.barTintColor = AppColor.white
        
        present(noMediaPermissionNC, animated: true, completion: nil)
    }
    
    fileprivate func prepareNavigationBar() {
        navigationController?.navigationBar.barStyle = .black
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.tintColor = AppColor.white
        navigationController?.navigationBar.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.0)
        navigationController?.navigationBar.isTranslucent = true
    }
    
    fileprivate func prepareNavigationTitle() {
        let boldAattrs = [
            NSFontAttributeName: UIFont.latoBold(size: 18),
            NSForegroundColorAttributeName: AppColor.white
        ]
        // FIXME: navigation title is user's display name
        let boldString = NSMutableAttributedString(string: "AN TEST", attributes: boldAattrs)
        
        let normalAattrs = [
            NSFontAttributeName: UIFont.latoRegular(size: 14),
            NSForegroundColorAttributeName: UIColor(red: 1, green: 1, blue: 1, alpha: 0.5)
        ]
        // FIXME: navigation title is user's note
        let normalString = NSMutableAttributedString(string: "AN TEST", attributes: normalAattrs)
        
        boldString.append(NSMutableAttributedString(string: "\n"))
        boldString.append(normalString)
        
        let title = UILabel()
        title.backgroundColor = AppColor.clear
        title.attributedText = boldString
        title.textAlignment = .center
        title.numberOfLines = 2
        
        navigationItem.titleView = title
        title.sizeToFit()
    }
    
    fileprivate func prepareLeftNavigationBarButton() {
        let leftBarButton = UIBarButtonItem(image: AppIcons.backIcon, style: .plain, target: self, action: #selector(popViewController(sender:)))
        navigationItem.leftBarButtonItem = leftBarButton
    }

    fileprivate func prepareBackground() {
        view.backgroundColor = AppColor.brown
    }

    fileprivate func prepareAvatarImageView() {
        avatarImageView.setNeedsLayout()
        avatarImageView.layoutIfNeeded()
        avatarImageView.roundify()
        avatarImageView.addBorderWithColor(AppColor.border, width: 1)
        avatarImageView.isUserInteractionEnabled = true
        avatarImageView.contentMode = .scaleAspectFill
        let avatarImageViewTapGesture = UITapGestureRecognizer(target: self, action: #selector(avatarImageViewTapped(sender:)))
        avatarImageView.addGestureRecognizer(avatarImageViewTapGesture)
        let defaultUserImage = #imageLiteral(resourceName: "no-avatar")
        renderUserAvatarImageView(image: defaultUserImage, url: tempUserProfile.absoluteImageURL)
    }
    
    fileprivate func renderUserAvatarImageView(image: UIImage, url: URL? = nil) {
        avatarImageView.image = image
        if let url = url {
            avatarImageView.kf.setImage(with: url, placeholder: image)
        }
    }
    
    fileprivate func prepareTableView() {
        tableView.register(UINib(nibName: EditProfileOneTableViewCell.cellReuseIdentifier, bundle: nil), forCellReuseIdentifier: EditProfileOneTableViewCell.cellReuseIdentifier)
        tableView.register(UINib(nibName: EditProfileTwoTableViewCell.cellReuseIdentifier, bundle: nil), forCellReuseIdentifier: EditProfileTwoTableViewCell.cellReuseIdentifier)
        tableView.register(UINib(nibName: EditProfileThreeTableViewCell.cellReuseIdentifier, bundle: nil), forCellReuseIdentifier: EditProfileThreeTableViewCell.cellReuseIdentifier)
        tableView.register(UINib(nibName: EditProfilePrivateSettingTableViewCell.cellReuseIdentifier, bundle: nil), forCellReuseIdentifier: EditProfilePrivateSettingTableViewCell.cellReuseIdentifier)
        tableView.separatorInset = UIEdgeInsets.zero
        tableView.backgroundColor = AppColor.clear
        tableView.separatorColor = AppColor.border
        tableView.tableHeaderView?.backgroundColor = AppColor.clear
        tableView.rowHeight = Constants.defaultCellRowHeight
        tableView.dataSource = self
        tableView.delegate = self
        
        separatorAvatarTableView.backgroundColor = AppColor.white
    }
    
    fileprivate func prepareSaveButton() {
        saveButton.setTitleColor(AppColor.white, for: .normal)
        saveButton.backgroundColor = AppColor.orange
        saveButton.titleLabel?.font = UIFont.latoBold(size: 14)
    }
    
    fileprivate func uploadUserAvatar() {
        avatarImageView.alpha = 0.7
//        avatarImageView.rn_activityView.color = AppColor.clear
//        avatarImageView.showActivityView()
        
        // FIXME: upload user avatar
    }
}

// MARK: UITableViewDataSource
extension EditProfileViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return ProfileSetting.numberOfStringSettings
        case 1:
            return ProfileSetting.numberOfBooleanSettings
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var resultCell: UITableViewCell
        switch indexPath.section {
        case 0:
            guard let profileSetting = ProfileSetting(rawValue: indexPath.row) else { return UITableViewCell() }
            let cell = tableView.dequeueReusableCell(withIdentifier: EditProfileOneTableViewCell.cellReuseIdentifier, for: indexPath) as! EditProfileOneTableViewCell
            cell.titleLabel.text = profileSetting.title
            cell.entryTextField.placeholder = profileSetting.placeHolder
            cell.entryTextField.text = getProfileSettingValue(forSetting: profileSetting) as? String
            cell.entryTextField.addTarget(self, action: #selector(dismissKeyboad), for: .editingDidEndOnExit)
            resultCell = cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: EditProfilePrivateSettingTableViewCell.cellReuseIdentifier, for: indexPath) as! EditProfilePrivateSettingTableViewCell
            let profileSetting = indexPath.row == 0 ? ProfileSetting.isEnableEmail : ProfileSetting.isEnablePhoneCall
            cell.titleLabel.text = profileSetting.title
            cell.switcher.isOn = getProfileSettingValue(forSetting: profileSetting) as? Bool ?? false
            resultCell = cell
        default:
            return UITableViewCell()
        }
        resultCell.selectionStyle = .none
        resultCell.layoutMargins = UIEdgeInsets.zero
        resultCell.backgroundColor = AppColor.white
        return resultCell

        /*
        let cell = tableView.dequeueReusableCellWithIdentifier(EditProfileThreeTableViewCell.cellReuseIdentifier, forIndexPath: indexPath) as! EditProfileThreeTableViewCell
        let category = self.categories[indexPath.row]
        
        cell.layoutMargins = UIEdgeInsetsZero
        cell.backgroundColor = AppColor.white
        cell.titleLabel.text = (IS_ENGLISH ? category.nameInEnglish : category.nameInChinese).uppercaseString
        cell.checked(self.currentSubscriptions.contains(category.id))
        cell.indented(category.deepLevel + 1)
        
        return cell
         */
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 1:
            return "    " + "PRIVATE SETTINGS".localized()//"SUBSCRIPTIONS".localized()
        case 2:
            return "    " + "PRIVATE SETTINGS".localized()
        default:
            return nil
        }
    }
}

// MARK: UITableViewDelegate

extension EditProfileViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch section {
        case 1:
            return Constants.defaultHeaderHeight
        default:
            return 0.1
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
}

// MARK: - UIImagePickerControllerDelegate

extension EditProfileViewController: UIImagePickerControllerDelegate {
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        avatarImageView.image = info[UIImagePickerControllerEditedImage] as? UIImage
        uploadUserAvatar()
        dismiss(animated: true, completion: nil)
    }
}

// MARK: - UINavigationControllerDelegate

//extension EditProfileViewController: UINavigationControllerDelegate {
//    func navigationController(_ navigationController: UINavigationController, willShow viewController: UIViewController, animated: Bool) {
//        navigationController.navigationBar.barStyle = .black
//        navigationController.navigationBar.tintColor = AppColor.white
//        navigationController.navigationBar.barTintColor = AppColor.orange
//        navigationController.navigationBar.isTranslucent = false
//        navigationController.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.white]
//    }
//}

// MARK: - UIScrollViewDelegate

extension EditProfileViewController: UIScrollViewDelegate {
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        view.endEditing(true)
    }
}
