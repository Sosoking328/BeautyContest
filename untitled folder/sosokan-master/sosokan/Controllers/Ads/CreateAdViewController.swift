//
//  CreateAdViewController.swift
//  sosokan
//
//  Created by An Phan on 6/14/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Photos
import Localize_Swift
import LocationPicker
import MapKit
import TSMessages
import DKImagePickerController
import CLImageEditor
import MobileCoreServices
import MobilePlayer
import Then

enum CreateAdItem: Int {
    case TITLE, desc, category, price, location, address, fax, website, enable_EMAIL, email_ADDRESS, enable_CALL, phone_NUMBER
    
    static func count() -> Int {
        return 12
    }
    
    var title: String {
        switch self {
        case .TITLE:
            return "TITLE"
        case .desc:
            return "DESCRIPTION"
        case .category:
            return "CATEGORY"
        case .price:
            return "PRICE"
        case .location:
            return "LOCATION"
        case .address:
            return "ADDRESS"
        case .fax:
            return "FAX"
        case .website:
            return "WEBSITE"
        case .enable_EMAIL:
            return "EMAIL ME"
        case .email_ADDRESS:
            return "EMAIL"
        case .enable_CALL:
            return "CALL ME"
        case .phone_NUMBER:
            return "PHONE"
        }
    }
    
    var placeHolder: String {
        switch self {
        case .TITLE:
            return "Type your title here..."
        case .desc:
            return "Describe your classifieds here"
        case .category:
            return ""
        case .price:
            return "Enter a price for your classifieds"
        case .location:
            return "No location selected"
        case .address:
            return "Address"
        case .fax:
            return "Fax"
        case .website:
            return "Website"
        case .enable_EMAIL:
            return ""
        case .email_ADDRESS:
            return "Email address"
        case .enable_CALL:
            return ""
        case .phone_NUMBER:
            return "Phone number"
        }
    }
}

// MARK: - CreateAdViewController

class CreateAdViewController: UIViewController {}
/*
    // MARK: IBOutlet
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var editPhotoButton: UIButton!
    @IBOutlet weak var postButton: UIButton!
    @IBOutlet weak var carouselView: ImageSlideshow!
    @IBOutlet weak var camcorderButton: UIButton!
    @IBOutlet weak var playVideoButton: UIButton!
    
    // MARK: Variable
    var locationManager: CLLocationManager?
    var inputAddress: String?
    var inputFax: String?
    var inputEmail: String?
    var inputPhone: String?
    var inputWebsite: String?
    var imagePickerController = UIImagePickerController()
    var post: Post?
    var ref: FIRDatabaseReference!
    var storageRef: FIRStorageReference!
    private var isCoupon = false
    private var userProfile: Profile!
    private var videoDurationLimited = 9.0
    private var selectedCategory: Category?
    private var adTitle = ""
    private var adDesc = ""
    private var adPrice = 0.0
    private var coupon = ""
    private var adDiscount = ""
    private var isFeatured = false
    var selectedImages = [UIImage]() {
        didSet {
            if selectedImages.count > 1 {
                carouselView?.pageControl.alpha = 1
            }
            else {
                carouselView?.pageControl.alpha = 0
            }
        }
    }
    var isSelectedNewImages: Bool = false
    private var enableEmail: Variable<Bool> = Variable.init(false)
    private var enablePhone: Variable<Bool> = Variable.init(false)
    private var premiumFeatures: [PremiumFeature] = []
    private var remainingCredit: Double {
        return self.userProfile.credit
    }
    var totalCreditWillPay: Double {
        return premiumFeatures.reduce(0.0, combine: { $0.0 + $0.1.creditCost })
    }
    private var videoURLReadOnly: NSURL? {
        didSet {
            if let videoURLReadOnly = videoURLReadOnly {
                dispatch_async(dispatch_get_main_queue(), {
                    self.playVideoButton.hidden = false
                })
                
                dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
                    let asset = AVAsset.init(URL: videoURLReadOnly)
                    
                    let imageGenerator = AVAssetImageGenerator.init(asset: asset)
                    imageGenerator.appliesPreferredTrackTransform = true
                    
                    let image = try? UIImage.init(CGImage: imageGenerator.copyCGImageAtTime(kCMTimeZero, actualTime: nil))
                    
                    dispatch_async(dispatch_get_main_queue(), {
                        self.playVideoButton.setBackgroundImage(image, forState: .Normal)
                    })
                })
            } else {
                playVideoButton.hidden = true
            }
            
            if videoURLReadOnly == nil {
                if let index = premiumFeatures.indexOf(.Video) {
                    premiumFeatures.removeAtIndex(index)
                }
            }
            else if !premiumFeatures.contains(.Video) {
                premiumFeatures.append(.Video)
            }
        }
    }
    private var descIsHTML = false {
        didSet {
            if !descIsHTML {
                if let index = premiumFeatures.indexOf(.RichText) {
                    premiumFeatures.removeAtIndex(index)
                }
            }
            else if !premiumFeatures.contains(.RichText) {
                premiumFeatures.append(.RichText)
            }
        }
    }
    
    private var selectedLocation: Location? {
        didSet {
            if let locCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: CreateAdItem.LOCATION.rawValue, inSection: 0)) as? CreateAdTableViewCell {
                locCell.textField.text = self.selectedLocation?.address
            }
        }
    }
    
    private let defaultTopImage = UIImage(named: "messageTopImage")!
    
    // MARK: - View controller lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.ref = FIRDatabase.database().reference()
        self.storageRef = FIRStorage.storage().reference()
        self.userProfile = DataManager.sharedInstance.userProfile.value
        
        // Edit photo button
        editPhotoButton.addBorderWithColor(UIColor.whiteColor(), width: 1)
        editPhotoButton.layer.cornerRadius = 15.0
        
        prepareButton()
        prepareCarouselView()
        
        // Default value
        self.inputAddress = self.userProfile.address
        self.inputFax = self.userProfile.fax
        self.inputPhone = self.userProfile.phone
        self.inputEmail = self.userProfile.email
        self.inputWebsite = self.userProfile.website
        if let post = self.post {
            title = "EDIT POST".localized()
            postButton.setTitle("UPDATE".localized(), forState: .Normal)
            render(post)
        }
        self.prepareTableView()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        prepareNavigationBar()
        
        setText()
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        
        view.endEditing(true)
        NSNotificationCenter.defaultCenter().removeObserver(self, name: LCLLanguageChangeNotification, object: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "pushAdDescVCSegue" {
            
            let isPurchasedPremiumDesc = self.post?.isPremiumDesc ?? false
            
            let premiumAction = UIAlertAction(title: "Continue".localized(), style: .Default, handler: { (_) in
                let descVC = segue.destinationViewController as! AdDescriptionViewController
                descVC.desc = self.adDesc
                descVC.finishedComposedDescription = { htmlString in
                    debugPrint(htmlString)
                    self.descIsHTML = true
                    self.adDesc = htmlString
                    self.tableView.reloadData()
                }
                self.navigationController?.showViewController(descVC, sender: nil)
                /*
                // Already purchased PremiumFeature.RichText
                // or It isn't purchased but have enough credit
                if isPurchasedPremiumDesc || self.remainingCredit >= self.totalCreditWillPay + PremiumFeature.RichText.creditCost {
                    let descVC = segue.destinationViewController as! AdDescriptionViewController
                    descVC.desc = self.adDesc
                    descVC.finishedComposedDescription = { htmlString in
                        debugPrint(htmlString)
                        self.descIsHTML = true
                        self.adDesc = htmlString
                        self.tableView.reloadData()
                    }
                    
                    self.navigationController?.showViewController(descVC, sender: nil)
                }
                else {
                    self.presentBuyCreditAlert()
                }
                */
            })
            
            let basicAction = UIAlertAction(title: "Use basic description".localized(), style: .Default, handler: { (_) in
                let indexPath = NSIndexPath(forRow: CreateAdItem.DESC.rawValue, inSection: 0)
                let cell = self.tableView.cellForRowAtIndexPath(indexPath) as? CreateAdTableViewCell
                
                if self.descIsHTML == true {
                    // Make sure reload cell to reveal normal text
                    self.descIsHTML = false
                    
                    // Not all content of HTML can convert to normal text, so clear them at all.
                    self.adDesc = ""
                }
                
                self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Middle, animated: true)
                cell?.textView.hidden = false
                cell?.webView.hidden = true
                cell?.textView.text = self.adDesc
                cell?.textView?.becomeFirstResponder()
            })
            
            let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .Cancel, handler: nil)
            
            let message: String = "FREE\nYou can edit more complex content".localized()
            
            /*
            if isPurchasedPremiumDesc {
                message = "Editing is free because you already purchased."
            }
            else {
                message = "Advance description will cost $0.99 in credits\nYou can edit more complex content.\nSpecial Promotion!!! All premium features are free until 12/31/2016."
            }
            */
            
            showAlert(title: "Advance description".localized(),
                      message: message.localized(),
                      style: .ActionSheet,
                      actions: [premiumAction, basicAction, cancelAction])
        }
        else if segue.identifier == "pushLocationPickerVCSegue" {
            let locationPicker = segue.destinationViewController as! LocationPickerViewController
            locationPicker.showCurrentLocationButton = true
            locationPicker.useCurrentLocationAsHint = true
            locationPicker.showCurrentLocationInitially = true
            locationPicker.mapType = .Standard
            locationPicker.completion = { self.selectedLocation = $0 }
            
            let button = UIButton.init()
            button.setTitle("Use my location".localized(), forState: .Normal)
            button.contentEdgeInsets = UIEdgeInsets.init(top: 8, left: 8, bottom: 8, right: 8)
            button.titleLabel?.font = UIFont.latoBold(size: 14)
            button.backgroundColor = AppColor.orange
            button.layer.cornerRadius = 5
            button.sizeToFit()
            locationPicker.view.layoutSubviews()
            let viewFrame = UIScreen.mainScreen().bounds
            var buttonFrame = button.frame
            buttonFrame.origin.x = (viewFrame.width - button.frame.size.width) / 2
            buttonFrame.origin.y = viewFrame.height - button.frame.size.height - 16
            button.frame = buttonFrame
            locationPicker.view.addSubview(button)
            locationPicker.view.bringSubviewToFront(button)
            button.rx_action = Action.init(workFactory: { [weak self] _ in
                guard let `self` = self else { return .empty() }
                self.locationManager = CLLocationManager.init()
                self.locationManager?.requestWhenInUseAuthorization()
                self.locationManager?.startUpdatingLocation()
                self.locationManager?.rx_didFailWithError
                    .bindNext({ [weak self] (error) in
                        guard let `self` = self else { return }
                        self.showErrorMessage("Failed to detect location".localized())
                        })
                    .addDisposableTo(self.rx_disposeBag)
                
                self.locationManager?.rx_didUpdateLocations
                    .debug()
                    .map({ $0.first })
                    .bindNext({ [weak self] currentLocation in
                        guard let `self` = self else { return }
                        debugPrint(currentLocation)
                        if let currentLocation = currentLocation {
                            self.locationManager?.stopUpdatingLocation()
                            self.locationManager = nil
                            let geoCoder = CLGeocoder()
                            geoCoder.reverseGeocodeLocation(currentLocation, completionHandler: { (placemarks, error) -> Void in
                                let placeArray = placemarks
                                var placeMark: CLPlacemark!
                                placeMark = placeArray?[0]
                                var name: String = ""
                                if let locationName = placeMark.addressDictionary?["Name"] as? NSString {
                                    name = locationName as String
                                }
                                self.selectedLocation = Location(name: name, placemark: placeMark)
                                self.navigationController?.popViewControllerAnimated(true)
                            })
                        }
                        else {
                            self.showErrorMessage("Failed to detect location".localized())
                        }
                        })
                    .addDisposableTo(self.rx_disposeBag)
                return .empty()
                })
        } else if segue.identifier == "newPostOptionsSegue" {
            let optionsController = segue.destinationViewController as! CreateAdOptionsViewController
            
            // Already purchased premium features
            if let isFeature = self.post?.isFeature where isFeature {
                optionsController.purchasedPremiumFeatures.append(.FeaturedPost)
            }
            if let _ = self.post?.video {
                optionsController.purchasedPremiumFeatures.append(.Video)
            }
            if let isStandout = self.post?.isStandout where isStandout {
                optionsController.purchasedPremiumFeatures.append(.StandOut)
            }
            if let isPremiumDesc = self.post?.isPremiumDesc where isPremiumDesc {
                optionsController.purchasedPremiumFeatures.append(.RichText)
            }
            
            // Total premium features
            optionsController.premiumFeatures = self.premiumFeatures
            
            optionsController.didSelectDoneButton = { (totalFee: Double, premiumFeatures: [PremiumFeature]) in
                let postTuple = self.getPostTupleFromInfo()
                let post = postTuple.post
                let isNewPost = postTuple.isNewPost
                
                // This post is not featured post if doesn't have "FEATURE MY AD"
                post.isFeature = premiumFeatures.contains(.FeaturedPost)
                post.isStandout = premiumFeatures.contains(.StandOut)
                
                // Remove video if doesn't have "VIDEO AD"
                if !premiumFeatures.contains(.Video) {
                    self.videoURLReadOnly = nil
                }
                
                // Remove video if doesn't have "Rich Text AD"
                if !premiumFeatures.contains(.RichText) && self.descIsHTML {
                    self.descIsHTML = false
                    self.adDesc = ""
                    self.tableView.reloadData()
                    self.showOkeyMessage("Error!".localized(), message: "You removed rich text, please re-enter a plain text description for the ad.".localized())
                    return
                }
                
                let onError: NSError? -> Void = { error in
                    self.dismissHUD()
                    self.showAlert(title: "Error!".localized(), message: error?.localizedDescription ?? "Try again!".localized())
                }
                
                let saveLocationIfNeeded: (onCompleted: () -> Void) -> Void = { onCompleted in
                    if let location = post.location {
                        let geofireRef = self.ref.child(References.geoFire)
                        let geoFire = GeoFire(firebaseRef: geofireRef)
                        geoFire.setLocation(location, forKey: post.id, withCompletionBlock: { (error) in
                            if let error = error {
                                onError(error)
                                onCompleted()
                            }
                            else {
                                onCompleted()
                            }
                        })
                    }
                    onCompleted()
                }
                
                let finish: (images: [Image]) -> Void = { images in
                    // FIXME: missing update credit
                    guard let selectedCategory = self.selectedCategory else { return }
                    
                    if self.isSelectedNewImages {
                        post.headerImage = images.first
                        post.imageIds.removeAll()
                        for image in images {
                            post.imageIds[image.id] = image.descendingTime
                        }
                    }
                    
                    let metaId         = "\(post.id)"
                    let metaPostId     = post.id
                    let metaStringData = "\(self.userProfile.phone) \(self.userProfile.email) \(post.name) \(post.postPlainDescription)"
                    let ascendingTime  = post.createdAt
                    let descendingTime = (0 - post.createdAt)
                    let postMeta       = FPostMeta.init(id: metaId, postId: metaPostId, stringData: metaStringData, ascendingTime: ascendingTime, descendingTime: descendingTime)
                    
                    let languageRefNode = IS_ENGLISH ? FCategoryKey.postsEnglish : FCategoryKey.postsChinese
                    let anotherlanguageRefNode = IS_ENGLISH ? FCategoryKey.postsChinese : FCategoryKey.postsEnglish
                    var childUpdates: JSONType = [
                        "/\(References.categories)/\(CATEGROGY_ALL_ID)/\(anotherlanguageRefNode)/\(post.id)": [:],
                        "/\(References.categories)/\(CATEGROGY_ALL_ID)/\(languageRefNode)/\(post.id)": descendingTime,
                        "/\(References.categories)/\(selectedCategory.id)/\(anotherlanguageRefNode)/\(post.id)": [:],
                        "/\(References.categories)/\(selectedCategory.id)/\(languageRefNode)/\(post.id)": descendingTime,
                        "/\(References.users)/\(self.userProfile.id)/\(UserKey.posts)/\(post.id)": descendingTime,
                        "/\(References.posts)/\(post.id)": post.toJSON(),
                        "/\(References.postMetas)/\(post.id)": postMeta.toJSON(),
                        ]
                    
                    let parentCategories = DataManager.sharedInstance.getParentCategories(selectedCategory, onMultipleDeepLevel: true)
                    for parent in parentCategories {
                        childUpdates["/\(References.categories)/\(parent.id)/\(anotherlanguageRefNode)/\(post.id)"] = [:]
                        childUpdates["/\(References.categories)/\(parent.id)/\(languageRefNode)/\(post.id)"] = descendingTime
                    }
                    
                    if let postVideo = post.video {
                        childUpdates["/\(References.videos)/\(postVideo.id)"] = postVideo.toJSON()
                    }
                    
                    if self.isSelectedNewImages {
                        for image in images {
                            childUpdates["/\(References.images)/\(image.id)"] = image.toJSON()
                        }
                    }
                    
                    self.ref.updateChildValues(childUpdates, withCompletionBlock: { (error, _) in
                        self.dismissHUD()
                        if let error = error {
                            debugPrint(error)
                        }
                        else {
                            
                            //FIRMessaging.messaging().subscribeToTopic(Topics.topics + post.id)
                            
                            if isNewPost {
                                let subscribingUserIds = self.userProfile.usersSubscribingMe.map({ $0.0 })
                                for userId in subscribingUserIds {
                                    NotificationHelper.getToken(userId: userId, onCompleted: { (token) in
                                        guard let token = token else { return }
                                        var maxBadge = 1
                                        NotificationHelper.setBadgeNumber(userId: userId, type: .Following, onCompleted: { (error, badge) in
                                            debugPrint(badge)
                                            if let error = error {
                                                debugPrint(error)
                                            }
                                            else {
                                                maxBadge = max(maxBadge, badge)
                                                let title = "New post by".localized() + " \(self.userProfile.displayName)"
                                                let message = "\(self.adTitle)"
                                                //let topic = Topics.topics + self.userProfile.id
                                                let subscribeNotification: JSONType = [
                                                    //"to": topic,
                                                    "registration_ids": [token],
                                                    "notification": [
                                                        "title": title,
                                                        "body": message,
                                                        "icon": "ICON",
                                                        "sound" : "default",
                                                        "badge": maxBadge,
                                                        "data": [
                                                            "userId": self.userProfile.id,
                                                            "notificationType": NotificationType.Subscription.rawValue,
                                                            "content-available": 1
                                                        ]
                                                    ]
                                                ]
                                                
                                                NotificationHelper.pushMessage(subscribeNotification, onCompleted: nil)
                                            }
                                        })
                                    })
                                }
                            }
                            
                            self.showAlertWithActions("Alert!".localized(),
                                message: (isNewPost ? "Post Successfully!".localized() : "Update Successfully!").localized(),
                                actions: [
                                    UIAlertAction(title: "Ok".localized(),
                                        style: .Default,
                                        handler: { _ in
                                            self.dismissViewControllerAnimated(true, completion: nil)
                                    })
                                ])
                        }
                    })
                    
                    if isNewPost {
                        NSNotificationCenter.defaultCenter().postNotificationName(Notifications.createdNewAd, object: nil)
                        NSNotificationCenter.defaultCenter().postNotificationName(Notifications.reloadPosts, object: nil)
                    }
                }
                
                self.showHUD()
                
                if let videoURLReadOnly = self.videoURLReadOnly?.absoluteString where videoURLReadOnly != post.video?.absoluteVideoURL?.absoluteString {
                    self.saveVideo("\(post.id)\(NSDate.getTimestamp())", onError: onError, onCompleted: { video in
                        post.video = video
                        if self.isSelectedNewImages {
                            post.imageIds = [:]
                            post.headerImage = nil
                            
                            if let currentImage = self.selectedImages.first {
                                self.saveImages(self.userProfile.id, allImages: self.selectedImages, currentImage: currentImage, onError: onError, onCompleted: { [weak self] (images) in
                                    guard let _ = self else { return }
                                    post.headerImage = images.first
                                    for image in images.map({ $0 }) {
                                        post.imageIds[image.id] = image.descendingTime
                                    }
                                    saveLocationIfNeeded(onCompleted: {
                                        finish(images: images)
                                    })
                                    })
                            }
                            else {
                                saveLocationIfNeeded(onCompleted: {
                                    finish(images: [])
                                })
                            }
                        } else {
                            saveLocationIfNeeded(onCompleted: {
                                finish(images: [])
                            })
                        }
                    })
                }
                else {
                    if self.isSelectedNewImages {
                        post.imageIds = [:]
                        post.headerImage = nil
                        
                        if let currentImage = self.selectedImages.first {
                            self.saveImages(self.userProfile.id, allImages: self.selectedImages, currentImage: currentImage, onError: onError, onCompleted: { [weak self] (images) in
                                guard let _ = self else { return }
                                post.headerImage = images.first
                                for image in images.map({ $0 }) {
                                    post.imageIds[image.id] = image.descendingTime
                                }
                                saveLocationIfNeeded(onCompleted: {
                                    finish(images: images)
                                })
                                })
                        }
                        else {
                            saveLocationIfNeeded(onCompleted: {
                                finish(images: [])
                            })
                        }
                    } else {
                        saveLocationIfNeeded(onCompleted: {
                            finish(images: [])
                        })
                    }
                }
            }
        }
    }
    
    // MARK: - IBActions
    @IBAction func unwindToCreateAdVC(segue: UIStoryboardSegue) {
    }
    
    @IBAction func playVideoButtonDidTouch(sender: AnyObject) {
        guard let videoURLReadOnly = videoURLReadOnly else { return }
        
        let playerVC = MobilePlayerViewController(contentURL: videoURLReadOnly)
        playerVC.title = self.post?.name
        playerVC.activityItems = [videoURLReadOnly] // Check the documentation for more information.
        presentMoviePlayerViewControllerAnimated(playerVC)
    }
    
    @IBAction func postAdButtonDidTouched(sender: UIButton?) {
        guard !adTitle.isEmpty
            else { return showErrorMessage("Please enter your post title".localized()) }
        
        guard !adDesc.isEmpty
            else { return showErrorMessage("Please enter description".localized()) }
        
        guard let _ = selectedCategory
            else { return showErrorMessage("Please select category".localized()) }
        
        self.performSegueWithIdentifier("newPostOptionsSegue", sender: nil)
    }
    
    @IBAction func backButtonDidTouched(button: UIButton) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    
    @IBAction func editPhotoButtonDidTouched(sender: UIButton) {
        if selectedImages.count > 0 {
            let currentImage = selectedImages[carouselView.currentItemIndex]
            let imageEditorVC = CLImageEditor(image: currentImage, delegate: self)
            presentViewController(imageEditorVC, animated: true, completion: nil)
        }
        else {
            cameraButtonDidTouched(nil)
        }
    }
    
    @IBAction func camcorderButtonDidTouched(sender: UIButton) {
        
        //let isPurchasedVideo = self.post?.video != nil
        
        let cameraAction = UIAlertAction(title: "Camera".localized(), style: .Default, handler: { (_) in
            
            if UIImagePickerController.isSourceTypeAvailable(.Camera) {
                self.imagePickerController = UIImagePickerController()
                self.imagePickerController.delegate = self
                self.imagePickerController.allowsEditing = true
                self.imagePickerController.sourceType = .Camera
                self.imagePickerController.videoMaximumDuration = self.videoDurationLimited - 1
                self.imagePickerController.mediaTypes = [kUTTypeMovie as String]
                self.imagePickerController.videoQuality = .TypeIFrame1280x720
                
                self.presentViewController(self.imagePickerController, animated: true, completion: nil)
            }
            else {
                self.showAlert(title: "Error!".localized(), message: "Error while trying access your camera!".localized())
            }
            
            /*
            // Already purchased PremiumFeature.Video
            // or It isn't purchased but have enough credit
            if isPurchasedVideo || self.remainingCredit >= self.totalCreditWillPay + PremiumFeature.Video.creditCost {
                if UIImagePickerController.isSourceTypeAvailable(.Camera) {
                    self.imagePickerController = UIImagePickerController()
                    self.imagePickerController.delegate = self
                    self.imagePickerController.allowsEditing = true
                    self.imagePickerController.sourceType = .Camera
                    self.imagePickerController.videoMaximumDuration = self.videoDurationLimited - 1
                    self.imagePickerController.mediaTypes = [kUTTypeMovie as String]
                    self.imagePickerController.videoQuality = .TypeIFrame1280x720
                    
                    self.presentViewController(self.imagePickerController, animated: true, completion: nil)
                }
                else {
                    self.showAlert(title: "Error!".localized(), message: "Error while trying access your camera!".localized())
                }
            }
            else {
                self.presentBuyCreditAlert()
            }
            */
        })
        
        let libraryAction = UIAlertAction(title: "Choose from library".localized(), style: .Default, handler: { (_) in
            
            let pickerController = DKImagePickerController()
            pickerController.singleSelect = true
            pickerController.sourceType = .Photo
            pickerController.assetType = .AllVideos
            pickerController.videoFetchPredicate = NSPredicate(format: "duration <= %f", self.videoDurationLimited)
            pickerController.didSelectAssets = { (assets: [DKAsset]) in
                guard let video = assets.first else { return }
                
                video.fetchAVAssetWithCompleteBlock({ (AVAsset, info) in
                    self.videoURLReadOnly = AVAsset?.valueForKey("URL") as? NSURL
                })
            }
            
            self.presentViewController(pickerController, animated: true) {}
            
            /*
            // Already purchased PremiumFeature.Video
            // or It isn't purchased but have enough credit
            if isPurchasedVideo || self.remainingCredit >= self.totalCreditWillPay + PremiumFeature.Video.creditCost {
                let pickerController = DKImagePickerController()
                pickerController.singleSelect = true
                pickerController.sourceType = .Photo
                pickerController.assetType = .AllVideos
                pickerController.videoFetchPredicate = NSPredicate(format: "duration <= %f", self.videoDurationLimited)
                pickerController.didSelectAssets = { (assets: [DKAsset]) in
                    guard let video = assets.first else { return }
                    
                    video.fetchAVAssetWithCompleteBlock({ (AVAsset, info) in
                        self.videoURLReadOnly = AVAsset?.valueForKey("URL") as? NSURL
                    })
                }
                
                self.presentViewController(pickerController, animated: true) {}
            }
            else {
                self.presentBuyCreditAlert()
            }
            */
        })
        
        let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .Cancel, handler: nil)
        
        let message: String = "FREE\nAdd a 9 seconds video".localized()
        
        /*
        if isPurchasedVideo {
            message = "Editing is free because you already purchased."
        } else {
            message = "Adding video is our premium feature will cost 0.99 credits to add a 9 seconds video. Do you want to continue?\nSpecial Promotion!!! All premium features are free until 12/31/2016."
        }
        */
        
        showAlert(title: "Adding video".localized(),//"Premium feature".localized(),
                  message: message.localized(),
                  style: .ActionSheet,
                  actions: [cameraAction, libraryAction, cancelAction])
    }
    
    @IBAction func cameraButtonDidTouched(sender: UIButton?) {
        
        let pickerController = DKImagePickerController()
        pickerController.showsCancelButton = true
        pickerController.maxSelectableCount = 5
        pickerController.assetType = .AllPhotos
        pickerController.didSelectAssets = { [weak self] (assets: [DKAsset]) in
            guard let `self` = self else { return }
            
            self.isSelectedNewImages = true
            
            if assets.isEmpty {
                self.selectedImages.removeAll()
                self.carouselView.setImageInputs([ImageSource(image: self.defaultTopImage)])
            }
            else {
                self.selectedImages.removeAll()
                var inputImages = [InputSource].init([])
                for asset in assets {
                    asset.fetchFullScreenImageWithCompleteBlock({ (image, info) in
                        inputImages.append(ImageSource(image: image!))
                        self.selectedImages.append(image!)
                        self.carouselView.setImageInputs(inputImages)
                    })
                }
            }
        }
        
        presentViewController(pickerController, animated: true) {}
    }
    
    // MARK: - Methods
    
    func textFieldDidChangedValue(textField: UITextField) {
        if textField.tag == CreateAdItem.TITLE.rawValue {
            adTitle = textField.text ?? ""
        }
        else if textField.tag == CreateAdItem.PRICE.rawValue {
            if isCoupon {
                coupon = textField.text ?? ""
            }
            else {
                adPrice = Double(textField.text ?? "0") ?? 0
            }
        }
    }
    
    func setText() {
        
        editPhotoButton.setTitle("TAP TO EDIT PHOTO".localized(), forState: .Normal)
        if let _ = post {
            title = "EDIT POST".localized()
            postButton.setTitle("UPDATE".localized(), forState: .Normal)
        }
        else {
            title = "NEW POST".localized()
            postButton.setTitle("POST".localized(), forState: .Normal)
        }
    }
    
    func presentCreditsVC() {
        let creditsNC = StoryBoardManager.viewController(storyBoardName: "Credits",
                                                         viewControllerName: "CreditsNCID") as! UINavigationController
        presentViewController(creditsNC, animated: true, completion: nil)
    }
    
    // MARK: - Private methods
    private func prepareTableView() {
        self.enableEmail.asObservable()
            .subscribeNext({ [weak self] enable in
                guard let `self` = self else { return }
                self.tableView.reloadData()
                if enable {
                    let indexPath = NSIndexPath.init(forRow: CreateAdItem.EMAIL_ADDRESS.rawValue, inSection: 0)
                    self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Middle, animated: true)
                }
                })
            .addDisposableTo(self.rx_disposeBag)
        
        self.enablePhone.asObservable()
            .subscribeNext({ [weak self] enable in
                guard let `self` = self else { return }
                self.tableView.reloadData()
                if enable {
                    let indexPath = NSIndexPath.init(forRow: CreateAdItem.PHONE_NUMBER.rawValue, inSection: 0)
                    self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Middle, animated: true)
                }
                })
            .addDisposableTo(self.rx_disposeBag)
    }
    
    private func saveImages(userId: String, allImages: [UIImage], currentImage: UIImage, savedImages: [Image] = [], onError: ((NSError)->Void), onCompleted: (([Image])->Void)) {
        let now = NSDate.getTimestamp()
        let imageId = self.ref.child(References.images).childByAutoId().key
        let imageRef = self.storageRef.child(References.images).child("\(userId)_\(now)_\(imageId).jpg")
        
        imageRef.putData(currentImage.lowestQualityJPEGNSData, metadata: nil) { [weak self] (metadata, error) in
            guard let `self` = self else {
                return
            }
            
            if let error = error {
                onError(error)
            }
            else {
                let metadata = metadata!
                let width = Double(currentImage.size.width)
                let height = Double(currentImage.size.height)
                let now = NSDate.getTimestamp()
                let image = Image.init(id: imageId, createdAt: now, descendingTime: -now, updatedAt: now, imageURL: metadata.downloadURL()!.absoluteString, isVideoThumb: false, isStoredInStorage: true, status: true, userId: userId, width: width, height: height)
                let newSavedImages = savedImages + [image]
                
                if currentImage == allImages.last {
                    onCompleted(newSavedImages)
                }
                else {
                    let currentImageIndex = allImages.indexOf(currentImage)!
                    let nextImage = allImages[currentImageIndex + 1]
                    self.saveImages(userId, allImages: allImages, currentImage: nextImage, savedImages: newSavedImages, onError: onError, onCompleted: onCompleted)
                }
            }
        }
    }
    
    private func presentBuyCreditAlert() {
        let cancelAction = UIAlertAction.init(title: "No".localized(), style: .Default, handler: nil)
        let buyCreditAction = UIAlertAction.init(title: "Buy Credits".localized(), style: .Default, handler: { (_) in
            self.presentCreditsVC()
        })
        
        showAlert(title: "Sosokan".localized(), message: "There is not enough available credits to enable this function. Would you like to buy more credits now?".localized(), actions: [cancelAction, buyCreditAction])
    }
    
    private func getPostTupleFromInfo() -> (post: FPost, isNewPost: Bool) {
        let now = NSDate.getTimestamp()
        let postId = self.ref.child(References.posts).childByAutoId().key
        
        if let post = self.post {
            let postEdited = FPost.init(categoryId: self.selectedCategory!.id,
                                  isChinese: !IS_ENGLISH,
                                  coupon: self.coupon,
                                  createdAt: post.createdAt,
                                  postDescription: self.adDesc,
                                  mailable: self.enableEmail.value,
                                  callable: self.enablePhone.value,
                                  isFeature: self.isFeatured,
                                  numberOfFlags: post.numberOfFlags,
                                  isHidden: post.isHidden,
                                  id: post.id,
                                  isStandout: post.isStandout,
                                  name: self.adTitle,
                                  prices: self.adPrice,
                                  saleOff: post.saleOff,
                                  updatedAt: now,
                                  ownerId: self.userProfile.id,
                                  numberOfLikes: post.numberOfLikes,
                                  expiredAt: post.expiredAt,
                                  video: post.video,
                                  isPremiumDesc: self.descIsHTML,
                                  headerImage: post.headerImage,
                                  imageIds: post.imageIds,
                                  favoritedUsers: post.favoritedUsers,
                                  descendingTime: post.descendingTime,
                                  postPlainDescription: self.descIsHTML ? self.adDesc.html2String : self.adDesc,
                                  location: self.selectedLocation?.location,
                                  address: self.inputAddress,
                                  fax: self.inputFax,
                                  email: self.inputEmail,
                                  phone: self.inputPhone,
                                  website: self.inputWebsite)
            
            return (post: postEdited, isNewPost: false)
        }
        else {
            let descendingTime = 0 - now
            let postPlainDescription: String = self.descIsHTML ? self.adDesc.html2String : self.adDesc
            let post = FPost.init(categoryId: self.selectedCategory!.id, isChinese: !IS_ENGLISH, coupon: self.coupon, createdAt: now, postDescription: self.adDesc, mailable: self.enableEmail.value, callable: self.enablePhone.value, isFeature: false, numberOfFlags: 0, isHidden: false, id: postId, isStandout: false, name: self.adTitle, prices: self.adPrice, saleOff: 0, updatedAt: now, ownerId: self.userProfile.id, numberOfLikes: 0, expiredAt: nil, video: nil, isPremiumDesc: false, headerImage: nil, imageIds: [:], favoritedUsers: [:], descendingTime: descendingTime, postPlainDescription: postPlainDescription, location: self.selectedLocation?.location, address: self.inputAddress, fax: self.inputFax, email: self.inputEmail, phone: self.inputPhone, website: self.inputWebsite)
            
            return (post: post, isNewPost: true)
        }
    }
    
    private func saveVideo(name: String, onError: (NSError? -> Void), onCompleted: ((FVideo) -> Void)) {
        guard let videoURLReadOnly = self.videoURLReadOnly else {
            return onError(nil)
        }
        
        let storageRef = FIRStorage.storage().reference()
        let _ = NSDate.getTimestamp()
        let videoRef = storageRef.child("videos").child("\(name)")
        let imageRef = storageRef.child("images").child("\(name)")
        
        let fileManager = NSFileManager.defaultManager()
        
        let asset = AVAsset.init(URL: videoURLReadOnly)
        
        let videoExporter = AVAssetExportSession.init(asset: asset, presetName: AVAssetExportPreset1280x720)
        var videOuputURL = NSURL.init(fileURLWithPath: NSTemporaryDirectory().stringByAppendingString("output.mov"))
        
        let imageGenerator = AVAssetImageGenerator.init(asset: asset)
        imageGenerator.appliesPreferredTrackTransform = true
        var imageOutputURL = NSURL.init(fileURLWithPath: NSTemporaryDirectory().stringByAppendingString("output.png"))
        
        // Remove file if existed
        // Create new image out put URL if remove failed
        if fileManager.fileExistsAtPath(imageOutputURL.path!) {
            do {
                try fileManager.removeItemAtURL(imageOutputURL)
            } catch {
                debugPrint(error)
                
                let name = "output_\(NSDate().timeIntervalSince1970).png"
                imageOutputURL = NSURL.init(fileURLWithPath: NSTemporaryDirectory().stringByAppendingString(name))
            }
        }
        
        guard let image = try? UIImage.init(CGImage: imageGenerator.copyCGImageAtTime(kCMTimeZero, actualTime: nil))
            where image.mediumQualityJPEGNSData.writeToFile(imageOutputURL.path!, atomically: true)
            else {
                return onError(nil)
        }
        
        imageRef.putFile(imageOutputURL, metadata: nil, completion: { (metadata, error) in
            if let error = error {
                return onError(error)
            }
            guard let metadata = metadata else {
                return onError(nil)
            }
            
            let videosRef = self.ref.child(References.videos)
            let videoId = videosRef.childByAutoId().key
            let imageId = videosRef.child(videoId).childByAutoId().key
            let width = Double(image.size.width)
            let height = Double(image.size.height)
            let now = NSDate.getTimestamp()
            let videoImage = Image.init(id: imageId, createdAt: now, descendingTime: -now, updatedAt: now, imageURL: metadata.downloadURL()!.absoluteString, isVideoThumb: true, isStoredInStorage: true, status: true, userId: self.userProfile.id, width: width, height: height)
            
            // Remove file if existed
            // Create new video out put URL if remove failed
            if fileManager.fileExistsAtPath(videOuputURL.path!) {
                do {
                    try fileManager.removeItemAtURL(videOuputURL)
                } catch {
                    debugPrint(error)
                    
                    let name = "output_\(now).mov"
                    videOuputURL = NSURL.init(fileURLWithPath: NSTemporaryDirectory().stringByAppendingString(name))
                }
            }
            
            guard let videoExporter = videoExporter
                else {
                    return onError(nil)
            }
            
            videoExporter.outputFileType = AVFileTypeQuickTimeMovie
            videoExporter.outputURL = videOuputURL
            videoExporter.exportAsynchronouslyWithCompletionHandler({
                
                // Put video
                videoRef.putFile(videOuputURL, metadata: nil) { (metadata, error) in
                    if let error = error {
                        return onError(error)
                    } else if let metadata = metadata {
                        let videoURL = metadata.downloadURL()!.absoluteString
                        let createdAt = NSDate.getTimestamp()
                        let video = FVideo.init(id: videoId, createdAt: createdAt, updatedAt: createdAt, ownerId: self.userProfile.id, videoURL: videoURL, videoImage: videoImage)
                        return onCompleted(video)
                    }
                }
            })
        })
    }
    
    private func prepareButton() {
        playVideoButton.hidden = true
    }
    
    private func prepareCarouselView() {
        carouselView.contentScaleMode = .ScaleAspectFill
        carouselView.slideshowInterval = 0
        carouselView.pageControlPosition = .InsideScrollView
        carouselView.pageControl.currentPageIndicatorTintColor = UIColor(red: 15/255.0, green: 172/255.0, blue: 205/255.0, alpha: 1)
        carouselView.pageControl.pageIndicatorTintColor = UIColor.lightGrayColor()
        carouselView.pageControl.hidesForSinglePage = false
        carouselView.pageControl.alpha = 0
        if self.selectedImages.isEmpty {
            carouselView.pageControl.alpha = 0
            carouselView.setImageInputs([ImageSource(image: self.defaultTopImage)])
        }
        else {
            carouselView.pageControl.alpha = 1
        }
    }
    
    private func prepareNavigationBar() {
        // Navigation
        navigationController?.navigationBar.translucent = true
        navigationController?.navigationBar.barStyle = .Black   // Change status bar to light mode.
        navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: .Default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.tintColor = AppColor.white
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.white]
        setNeedsStatusBarAppearanceUpdate()
        
        //        let title = String(format: "Balance".localized() + ": $%.2f", self.userProfile.credit)
        //        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: title, style: .Plain, target: self, action: #selector(self.presentCreditsVC))
    }
    
    private func render(post: FPost) {
        adTitle         = post.name
        adDesc          = post.postDescription
        adPrice         = post.prices
        coupon          = post.coupon
        enableEmail.value    = post.mailable
        enablePhone.value    = post.callable
        descIsHTML      = post.isPremiumDesc
        self.inputAddress = post.address
        self.inputFax = post.fax
        self.inputEmail = post.email
        self.inputPhone = post.phone
        self.inputWebsite = post.website
        
        self.selectedCategory = DataManager.sharedInstance.getCategory(post.categoryId)
        
        if self.selectedImages.isEmpty {
            self.carouselView.setImageInputs([ImageSource.init(image: self.defaultTopImage)])
        }
        else {
            self.carouselView.setImageInputs(self.selectedImages.map({ ImageSource.init(image: $0) }))
        }
        
        if self.selectedImages.count > 1 {
            self.carouselView.pageControl.alpha = 1
        }
        else {
            self.carouselView.pageControl.alpha = 0
        }
        
        loadAdLocation()
    }
    
    private func loadAdLocation() -> String {
        if let location = self.post?.location {
            let geoCoder = CLGeocoder()
            var name = ""
            geoCoder.reverseGeocodeLocation(location, completionHandler: { (placemarks, error) -> Void in
                let placeArray = placemarks
                
                // Place details
                var placeMark: CLPlacemark!
                placeMark = placeArray?[0]
                
                if let locationName = placeMark.addressDictionary?["Name"] as? NSString {
                    name = locationName as String
                }
                
                self.selectedLocation = Location(name: name, placemark: placeMark)
            })
            return name
        }
        else if let location = self.selectedLocation {
            return location.address
        }
        else {
            return ""
        }
    }
    
    private func presentNoMediaPermissionPage() {
        let noMediaPermissionVC = NoMediaPermissionViewController(nibName: "NoMediaPermissionViewController", bundle: nil)
        let noMediaPermissionNC = UINavigationController(rootViewController: noMediaPermissionVC)
        noMediaPermissionVC.navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        noMediaPermissionVC.navigationController?.navigationBar.barTintColor = UIColor.blackColor()
        
        presentViewController(noMediaPermissionNC, animated: true, completion: nil)
    }
}

// MARK: - UITableViewDataSource

extension CreateAdViewController: UITableViewDataSource {
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return CreateAdItem.count()
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cellID: String
        switch indexPath.row {
        case CreateAdItem.DESC.rawValue:
            cellID = "descCellID"
        case CreateAdItem.CATEGORY.rawValue:
            cellID = "categoryCellID"
        case CreateAdItem.ENABLE_CALL.rawValue, CreateAdItem.ENABLE_EMAIL.rawValue:
            cellID = "switchCellID"
        default:
            cellID = "createAdCellID"
        }
        
        let cell = tableView.dequeueReusableCellWithIdentifier(cellID, forIndexPath: indexPath) as! CreateAdTableViewCell
        cell.delegate = self
        cell.indexPath = indexPath
        
        if let itemIndex = CreateAdItem(rawValue: indexPath.row) {
            cell.titleLabel.text = itemIndex.title.localized()
            cell.textField?.placeholder = itemIndex.placeHolder.localized()
            cell.descLabel?.text = itemIndex.placeHolder.localized()  // For Feature field
            cell.textField?.tag = indexPath.row
            cell.textField?.delegate = self
            cell.textField?.addTarget(self,
                                      action: #selector(self.textFieldDidChangedValue(_:)),
                                      forControlEvents: .EditingChanged)
        }
        
        switch indexPath.row {
        case CreateAdItem.ADDRESS.rawValue:
            cell.textField.text = self.inputAddress
            cell.textField.rx_text
                .subscribeNext({ [weak self] text in
                    guard let `self` = self else { return }
                    self.inputAddress = text
                    })
                .addDisposableTo(cell.rx_reusableDisposeBag)
        case CreateAdItem.FAX.rawValue:
            cell.textField.text = self.inputFax
            cell.textField.rx_text
                .subscribeNext({ [weak self] text in
                    guard let `self` = self else { return }
                    self.inputFax = text
                    })
                .addDisposableTo(cell.rx_reusableDisposeBag)
        case CreateAdItem.EMAIL_ADDRESS.rawValue:
            cell.textField.text = self.inputEmail
            cell.textField.rx_text
                .subscribeNext({ [weak self] text in
                    guard let `self` = self else { return }
                    self.inputEmail = text
                    })
                .addDisposableTo(cell.rx_reusableDisposeBag)
        case CreateAdItem.PHONE_NUMBER.rawValue:
            cell.textField.text = self.inputPhone
            cell.textField.rx_text
                .subscribeNext({ [weak self] text in
                    guard let `self` = self else { return }
                    self.inputPhone = text
                    })
                .addDisposableTo(cell.rx_reusableDisposeBag)
        case CreateAdItem.WEBSITE.rawValue:
            cell.textField.text = self.inputWebsite
            cell.textField.rx_text
                .subscribeNext({ [weak self] text in
                    guard let `self` = self else { return }
                    self.inputWebsite = text
                    })
                .addDisposableTo(cell.rx_reusableDisposeBag)
        case CreateAdItem.TITLE.rawValue:
            cell.textField.text = adTitle
        case CreateAdItem.LOCATION.rawValue:
            cell.titleLabel.text = "LOCATION".localized()
            cell.locationButton?.hidden = false
            cell.textField?.enabled = false
            cell.selectionStyle = .Default
            cell.textField.text = loadAdLocation()
        case CreateAdItem.CATEGORY.rawValue:
            cell.textField.text = IS_ENGLISH ? selectedCategory?.nameInEnglish : selectedCategory?.nameInChinese
        case CreateAdItem.PRICE.rawValue:
            if isCoupon {
                cell.titleLabel.text = "DISCOUNT".localized()
                cell.textField.placeholder = "Enter the percentage of your discount".localized()
                cell.textField.keyboardType = .Default
                cell.textField.text = coupon
            }
            else {
                cell.titleLabel.text = CreateAdItem.PRICE.title.localized()
                cell.textField.placeholder = CreateAdItem.PRICE.placeHolder.localized()
                cell.textField.keyboardType = .DecimalPad
                cell.textField.text = adPrice > 0.0 ? String(adPrice) : ""
            }
        case CreateAdItem.ENABLE_EMAIL.rawValue:
            cell.switcher.on = enableEmail.value
        case CreateAdItem.ENABLE_CALL.rawValue:
            cell.switcher.on = enablePhone.value
        case CreateAdItem.DESC.rawValue:
            cell.delegate = self
            cell.placeholderLabel.text = CreateAdItem.DESC.placeHolder.localized()
            cell.placeholderLabel.hidden = !adDesc.isEmpty
            if descIsHTML {
                cell.textView.hidden = true
                cell.webView.hidden = false
                let style = "<style>  * { max-width: 100% !important; } ; body { margin: 0; padding: 0; }</style>"
                let finalHTML = "<div style=\"font-family: Lato; font-size: 15\">\(self.adDesc)</div>" + style
                cell.webView.loadHTMLString(finalHTML, baseURL: nil)
            }
            else {
                cell.textView.hidden = false
                cell.webView.hidden = true
                cell.textView.text = adDesc
            }
        default:
            cell.locationButton?.hidden = true
            cell.textField?.enabled = true
            cell.selectionStyle = .None
        }
        
        return cell
    }
}

// MARK: - UITableViewDelegate

extension CreateAdViewController: UITableViewDelegate {
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        
        if indexPath.row == CreateAdItem.LOCATION.rawValue {
            performSegueWithIdentifier("pushLocationPickerVCSegue", sender: nil)
        }
        else if indexPath.row == CreateAdItem.CATEGORY.rawValue {
            /*
            let listCategoriesViewController = ListCategoriesViewController(nibName: String(ListCategoriesViewController), bundle: nil)
            let _ = listCategoriesViewController.view
            
            listCategoriesViewController.didSelectedCategory = { [weak self] (category: FCategory) -> Void in
                guard let `self` = self else { return }
                
                self.selectedCategory = category
                
                if let couponDiscountRoot = DataManager.sharedInstance.getCategory(CATEGORY_COUPON_DISCOUNT_ID) {
                    let couponDiscountCategories = [couponDiscountRoot] + DataManager.sharedInstance.getChildCategories(couponDiscountRoot, onMultipleDeepLevel: true)
                    if couponDiscountCategories.contains({ $0.id == category.id }) {
                        self.isCoupon = true
                    }
                    else {
                        self.isCoupon = false
                    }
                }
                else {
                    self.isCoupon = false
                }
                self.tableView.reloadData()
                debugPrint(category)
            }
             */
            navigationController?.pushViewController(listCategoriesViewController, animated: true)
        }
        else if indexPath.row == CreateAdItem.DESC.rawValue {
            
            let cell = tableView.cellForRowAtIndexPath(indexPath) as! CreateAdTableViewCell
            
            cell.textView.becomeFirstResponder()
        }
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        switch indexPath.row {
        case CreateAdItem.DESC.rawValue:
            return 141
        case CreateAdItem.EMAIL_ADDRESS.rawValue:
            if self.enableEmail.value {
                return 64
            }
            else {
                return 0
            }
        case CreateAdItem.PHONE_NUMBER.rawValue:
            if self.enablePhone.value {
                return 64
            }
            else {
                return 0
            }
        default:
            return 64
        }
    }
}

// MARK: - UIImagePickerControllerDelegate
extension CreateAdViewController: UIImagePickerControllerDelegate {
    
    func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        let mediaType = info[UIImagePickerControllerMediaType] as! String
        
        if mediaType == kUTTypeMovie as String {
            videoURLReadOnly = info[UIImagePickerControllerMediaURL] as? NSURL
        }
        
        // Dismiss photo or camera view
        dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController) {
        imagePickerController.dismissViewControllerAnimated(true, completion: nil)
    }
}

// MARK: - UINavigationControllerDelegate

extension CreateAdViewController: UINavigationControllerDelegate {
    func navigationController(navigationController: UINavigationController,
                              willShowViewController viAewController: UIViewController, animated: Bool) {
        // Navigation bar style
        navigationController.navigationBar.barStyle = .Black
        navigationController.navigationBar.tintColor = UIColor.whiteColor()
        navigationController.navigationBar.barTintColor = AppColor.orange
        navigationController.navigationBar.translucent = false
        navigationController.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
    }
}

// MARK: - UITextFieldDelegate

extension CreateAdViewController: UITextFieldDelegate {
    // TODO: Removed this code after removed IQKeyboardManager
    func textFieldDidBeginEditing(textField: UITextField) {
        //IQKeyboardManager.sharedManager().enableAutoToolbar = false
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        //IQKeyboardManager.sharedManager().enableAutoToolbar = true
    }
}

// MARK: - CreateAdTableViewCellDelegate
extension CreateAdViewController: CreateAdTableViewCellDelegate {
    func descTextViewDidChangedValue(textView: UITextView) {
        adDesc = textView.text
    }
    
    func createAdCell(cell: CreateAdTableViewCell, changedValueSwitchAtIndex index: Int) {
        switch index {
        case CreateAdItem.ENABLE_EMAIL.rawValue:
            enableEmail.value = cell.switcher.on
        default:
            enablePhone.value = cell.switcher.on
        }
    }
}

// MARK: - UIScrollViewDelegate

extension CreateAdViewController: UIScrollViewDelegate {
    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        dismissKeyboad()
    }
}

// MARK: - CLImageEditorDelegate

extension CreateAdViewController: CLImageEditorDelegate {
    func imageEditor(editor: CLImageEditor!, didFinishEdittingWithImage image: UIImage!) {
        editor.dismissViewControllerAnimated(true, completion: nil)
        self.isSelectedNewImages = true
        selectedImages[carouselView.currentItemIndex] = image
        var inputImages = [InputSource]()
        for (_, image) in selectedImages.enumerate() {
            inputImages.append(ImageSource(image: image))
        }
        carouselView.setImageInputs(inputImages)
    }
}
 */
