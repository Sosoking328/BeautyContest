//
//  ConversationViewController.swift
//  sosokan

import UIKit
import SDWebImage
import JSQMessagesViewController
import Localize_Swift

class ConversationViewController: JSQMessagesViewController {}
/*
    
    // MARK: - IBOutlets
    
    // MARK: Variable
    var headerView: ConversationHeaderView = ConversationHeaderView.fromNib()
    var outgoingBubbleImageView: JSQMessagesBubbleImage!
    var incomingBubbleImageView: JSQMessagesBubbleImage!
    var messages: [JSQMessage] = []
    var firebaseMessages: Variable<[FMessage]> = Variable.init([])
    var conversation: FConversation!
    var ref: FIRDatabaseReference!
    var post: FPost?
    weak var userProfile: UserProfile? {
        return DataManager.sharedInstance.userProfile.value
    }
    var avatars = Dictionary<String, JSQMessagesAvatarImage>()
    let blankAvatar = JSQMessagesAvatarImageFactory.avatarImageWithImage(UIImage(named: "no-avatar"), diameter: 30)
    
    // MARK: IBAction
    @IBAction func leftBarButtonTouched(sender: AnyObject) {
        navigationController?.popViewControllerAnimated(true)
    }
    
    // MARK: View controller life circle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareHeaderView()
        
        self.automaticallyAdjustsScrollViewInsets = false
        
        navigationController?.navigationBar.translucent = false
        
        inputToolbar.contentView.leftBarButtonItem = nil
        inputToolbar.contentView.textView.placeHolder = "Type here..".localized()
        
        prepareVariable()
        prepareView()
        render()
        prepareRx()
        
        FIRMessaging.messaging().subscribeToTopic(Topics.topics + self.conversation.postId)
        
        if let userId = FIRAuth.auth()?.currentUser?.uid {
            let childUpdates: JSONType = [
                "/\(References.conversations)/\(self.conversation.id)/\(FConversationKey.unreadLastMessageUsers)/\(userId)": [:],
                ]
            self.ref.updateChildValues(childUpdates)
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        
        let height = self.headerView.systemLayoutSizeFittingSize(UILayoutFittingCompressedSize).height
        self.headerView.frame.origin.y = -height
        self.topContentAdditionalInset = height
        //        self.collectionView.contentInset = UIEdgeInsets.init(top: height, left: 0, bottom: 0, right: 0)
        //        self.collectionView.invalidateIntrinsicContentSize()
        //        self.collectionView.collectionViewLayout.invalidateLayout()
        
        debugPrint(self.collectionView.contentInset)
        debugPrint(self.headerView.frame)
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        setText()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Methods
    func prepareHeaderView() {
        self.headerView.translatesAutoresizingMaskIntoConstraints = false
        self.collectionView.addSubview(self.headerView)
        self.headerView.imageWidthConstraint.constant = UIScreen.mainScreen().bounds.width
        let centerX = NSLayoutConstraint(item: self.headerView, attribute: .CenterX, relatedBy: .Equal, toItem: self.collectionView, attribute: .CenterX, multiplier: 1, constant: 0)
        let topConstraint = NSLayoutConstraint(item: self.headerView, attribute: .Bottom, relatedBy: .Equal, toItem: self.collectionView, attribute: .Top, multiplier: 1, constant: 0)
        self.collectionView.addConstraints([centerX, topConstraint])
        
        let loading = "Loading...".localized()
        self.headerView.nameLabel.text = loading
        self.headerView.timeLabel.text = loading
        self.headerView.titleLabel.text = loading
        
        self.headerView.titleLabel.font = UIFont.latoBold(size: 16)
        
        let tap = UITapGestureRecognizer.init(target: self, action: Selector.init())
        tap.rx_event.subscribeNext ({ [weak self] (_) in
            guard let `self` = self else { return }
            
            if self.headerView.timeLabel.tag == 0 {
                self.headerView.timeLabel.tag = 1
                let day = self.conversation.postCreatedAt.toNSDate()
                self.headerView.timeLabel.text = day.hourDay()
            }
            else {
                self.headerView.timeLabel.tag = 0
                let day = self.conversation.postCreatedAt.toNSDate()
                self.headerView.timeLabel.text = day.timeAgoSinceNow()
            }
        })
            .addDisposableTo(self.rx_disposeBag)
        self.headerView.timeLabel.addGestureRecognizer(tap)
        
        let tapToHeaderView = UITapGestureRecognizer.init(target: self, action: Selector.init())
        tapToHeaderView.rx_event
            .doOnNext({ _ in
                UIApplication.sharedApplication().networkActivityIndicatorVisible = true
            })
            .flatMap({ _ -> Observable<FIRDataSnapshot> in
                let postId = self.conversation.postId
                let postRef = self.ref.child(References.posts).child(postId)
                return postRef.rx_observeOnce(.Value)
            })
            .doOnNext({ _ in
                UIApplication.sharedApplication().networkActivityIndicatorVisible = false
            })
            .subscribeNext({ [weak self] (snapshot) in
                guard let `self` = self else { return }
                
                if let json = snapshot.value as? JSONType where !json.isEmpty {
                    let post = FPost.init(json: json)
                    self.showPostDetailViewController(withPost: post)
                }
                else {
                    self.showErrorMessage(Messages.occurredError)
                }
                })
            .addDisposableTo(self.rx_disposeBag)
        self.headerView.addGestureRecognizer(tapToHeaderView)
    }
    
    func setText() {
        // Title
        title = "MESSAGE".localized()
    }
    
    // MARK: - UICollectionViewDataSource
    
    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }
    
    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = super.collectionView(collectionView, cellForItemAtIndexPath: indexPath) as! JSQMessagesCollectionViewCell
        cell.avatarImageView.sizeToFit()
        cell.avatarContainerView.sizeToFit()
        
        let message = messages[indexPath.item]
        
        if message.senderId == senderId {
            cell.textView!.textColor = UIColor.whiteColor()
        } else {
            cell.textView!.textColor = UIColor.blackColor()
        }
        
        let tapToImageView = UITapGestureRecognizer.init(target: self, action: Selector.init())
        tapToImageView.rx_event
            .flatMap({ _ in Observable.just(message.senderId) })
            .subscribeNext({ [weak self] (userId) in
                guard let `self` = self else { return }
                
                self.presentUserProfilePage(withUserId: 16)
                })
            .addDisposableTo(self.rx_disposeBag)
        cell.avatarContainerView.addGestureRecognizer(tapToImageView)
        
        return cell
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, messageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageData! {
        let data = messages[indexPath.row]
        
        return data
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, didDeleteMessageAtIndexPath indexPath: NSIndexPath!) {
        messages.removeAtIndex(indexPath.row)
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, messageBubbleImageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageBubbleImageDataSource! {
        let message = messages[indexPath.item]
        if message.senderId == senderId {
            return outgoingBubbleImageView
        } else {
            return incomingBubbleImageView
        }
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, avatarImageDataForItemAtIndexPath indexPath: NSIndexPath!) -> JSQMessageAvatarImageDataSource! {
        let index = indexPath.row
        let userId = self.firebaseMessages.value[index].userId
        if let image = self.avatars[userId] {
            return image
        }
        else {
            return self.blankAvatar
        }
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, attributedTextForMessageBubbleTopLabelAtIndexPath indexPath: NSIndexPath!) -> NSAttributedString! {
        let data = self.collectionView(self.collectionView, messageDataForItemAtIndexPath: indexPath)
        if (self.senderDisplayName == data.senderDisplayName()) {
            return NSAttributedString(string: "You".localized())
        }
        
        return NSAttributedString(string: data.senderDisplayName())
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, layout collectionViewLayout: JSQMessagesCollectionViewFlowLayout!, heightForMessageBubbleTopLabelAtIndexPath indexPath: NSIndexPath!) -> CGFloat {
        return kJSQMessagesCollectionViewCellLabelHeightDefault
    }
    
    override func didPressSendButton(button: UIButton!, withMessageText text: String!, senderId: String!,
                                     senderDisplayName: String!, date: NSDate!) {
        
        guard let userProfile = self.userProfile else { return }
        
        let conversationId = self.conversation.id
        let messageId = self.ref.child(References.messages).childByAutoId().key
        let imageURL = userProfile.avatar?.imageURL ?? ""
        let now = NSDate.getTimestamp()
        let unreadUserIds = Array.init(self.conversation.users.keys).filter({ $0 != userProfile.id })
        
        let message = FMessage.init(id: messageId, conversationId: conversationId, userId: userProfile.id, userDisplayName: userProfile.displayName, imageURL: imageURL, timestamp: now, content: text)
        self.conversation.`do`({
            $0.updatedAt = message.timestamp
            $0.lastMessageId = message.id
            $0.lastMessageContent = message.content
            $0.lastMessageSentBy = message.userDisplayName
            $0.lastMessageTimestamp = message.timestamp
            for id in unreadUserIds {
                $0.unreadLastMessageUsers[id] = true
            }
            $0.waitingUsers = [:]
        })
        var childUpdates: JSONType = [
            "/\(References.conversations)/\(conversationId)": self.conversation.toJSON(),
            "/\(References.messages)/\(conversationId)/\(messageId)": message.toJSON(),
            "/\(References.posts)/\(self.conversation.postId)/\(References.conversations)/\(conversationId)": true
        ]
        
        let timestamp = -NSDate.getTimestamp()
        for userId in Array.init(self.conversation.users.keys) {
            childUpdates["/\(References.users)/\(userId)/\(References.conversations)/\(conversationId)/\(self.conversation.postId)"] = -timestamp
        }
        
        self.ref.updateChildValues(childUpdates) { [weak self] (error, _) in
            guard let `self` = self else { return }
            if let _ = error {
                self.showErrorMessage(Messages.occurredError)
            }
            else {
                for userId in unreadUserIds {
                    NotificationHelper.getToken(userId: userId, onCompleted: { (token) in
                        guard let token = token else { return }
                        var maxBadge = 1
                        NotificationHelper.setBadgeNumber(userId: userId, type: .Message, onCompleted: { (error, badge) in
                            debugPrint(badge)
                            if let error = error {
                                debugPrint(error)
                            }
                            else {
                                maxBadge = max(maxBadge, badge)
                                let title = userProfile.displayName
                                let message = message.content
                                //let topic = Topics.topics + self.conversation.postId
                                let messageNotification: JSONType = [
                                    //"to": topic
                                    "registration_ids": [token],
                                    "notification": [
                                        "title": title,
                                        "body": message,
                                        "icon": "ICON",
                                        "sound" : "default",
                                        "badge": maxBadge,
                                        "data": [
                                            "userId": userProfile.id,
                                            "notificationType": NotificationType.Message.rawValue,
                                            "content-available": 1
                                        ]
                                    ]
                                ]
                                NotificationHelper.pushMessage(messageNotification, onCompleted: nil)
                            }
                        })
                    })
                }
            }
        }
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, attributedTextForCellBottomLabelAtIndexPath indexPath: NSIndexPath!) -> NSAttributedString! {
        let message: JSQMessage = self.messages[indexPath.item]
        
        return JSQMessagesTimestampFormatter.sharedFormatter().attributedTimestampForDate(message.date)
    }
    
    override func collectionView(collectionView: JSQMessagesCollectionView!, layout collectionViewLayout: JSQMessagesCollectionViewFlowLayout!, heightForCellBottomLabelAtIndexPath indexPath: NSIndexPath!) -> CGFloat {
        return kJSQMessagesCollectionViewCellLabelHeightDefault
    }
    
    // MARK: Private method
    
    private func presentUserProfilePage(withUserId id: Int) {
        let viewController = UserProfileViewController.init(profileId: id)
        self.navigationController?.showViewController(viewController, sender: nil)
    }
    
    private func showPostDetailViewController(withPost post: FPost!) {
        let identifier = "adDetailVCID"
        let viewController = StoryBoardManager.homeStoryBoard().instantiateViewControllerWithIdentifier(identifier) as! AdDetailViewController
        viewController.post = post
        if let navigationViewController = self.navigationController {
            navigationViewController.showViewController(viewController, sender: nil)
        }
        else {
            self.showViewController(viewController, sender: nil)
        }
    }
    
    private func prepareRx() {
        let postId = self.conversation.postId
        let conversationId = self.conversation.id
        
        let postRef = self.ref.child(References.posts).child(postId)
        postRef.observeSingleEventOfType(.Value, withBlock: { [weak self] snapshot in
            guard let `self` = self else { return }
            if let json = snapshot.value as? JSONType where !json.isEmpty {
                let post = FPost.init(json: json)
                self.post = post
                
                self.self.headerView.imageView?
                    .`do`({ imageView in
                        if let URL = post.headerImage?.absoluteImageURL {
                            // get image from cache
                            DataManager.sharedInstance.getCacheImage(URL, onCompleted: { (image) in
                                if let image = image {
                                    imageView.contentMode = .ScaleAspectFill
                                    imageView.image = image
                                }
                            })
                            // refresh cached image
                            UIImage.contentsOfURL(URL, completion: { (image, error) in
                                if let image = image {
                                    imageView.contentMode = .ScaleAspectFill
                                    imageView.image = image
                                    DataManager.sharedInstance.setCacheImage(URL, image: image)
                                }
                            })
                        }
                    })
            }
            else {
                
            }
            })
        
        
        let ownerRef = self.ref.child(References.users).child(self.conversation.postOwnerId)
        ownerRef.observeSingleEventOfType(.Value, withBlock: { [weak self] snapshot in
            guard let `self` = self else {
                return
            }
            guard let json = snapshot.value as? JSONType else {
                return
            }
            let ownerProfile = UserProfile.init(json: json)
            let ownerImageURL = ownerProfile.avatar?.imageURL ?? ""
            self.headerView.nameLabel?.text = ownerProfile.displayName
            self.headerView.avatarImageView?.sd_setImageWithURL(NSURL.init(string: ownerImageURL), placeholderImage: UIImage.init(named: "no-avatar"))
            })
        
        let messagesRef = self.ref.child(References.messages).child(conversationId)
        messagesRef.rx_observe(.Value)
            .subscribeNext({ [weak self] snapshot in
                guard let `self` = self else {
                    return
                }
                let json = snapshot.value as? JSONType ?? [:]
                let messageJSONs = json.map({ $0.1 as? JSONType }).flatMap({ $0 })
                self.firebaseMessages.value = messageJSONs.map({ FMessage.init(json: $0) }).sort({ $0.0.timestamp < $0.1.timestamp })
                })
            .addDisposableTo(self.rx_disposeBag)
        
        
        self.firebaseMessages.asObservable()
            .subscribeNext({ [weak self] firebaseMessages in
                guard let `self` = self else { return }
                if !firebaseMessages.isEmpty {
                    self.messages = firebaseMessages.map({ JSQMessage.init(senderId: $0.userId, senderDisplayName: $0.userDisplayName, date: $0.timestamp.toNSDate(), text: $0.content) })
                    self.finishSendingMessageAnimated(false)
                }
                })
            .addDisposableTo(self.rx_disposeBag)
    }
    
    private func prepareVariable() {
        self.senderId = self.userProfile?.id ?? ""
        self.senderDisplayName = self.userProfile?.displayName ?? ""
        self.headerView.avatarImageView.roundify()
        self.ref = FIRDatabase.database().reference()
        
        let factory = JSQMessagesBubbleImageFactory.init()
        self.outgoingBubbleImageView = factory.outgoingMessagesBubbleImageWithColor(UIColor.jsq_messageBubbleBlueColor())
        self.incomingBubbleImageView = factory.incomingMessagesBubbleImageWithColor(UIColor.jsq_messageBubbleLightGrayColor())
        
        let userIds = self.conversation.users.map({ $0.0 })
        for userId in userIds {
            let userAvatarRef = self.ref.child(References.users).child(userId).child(UserKey.avatar).child(ImageKey.imageUrl)
            guard let cacheKeyAvatarURL = NSURL.init(string: userAvatarRef.URL) else { return }
            // Get cache image
            DataManager.sharedInstance.getCacheImage(cacheKeyAvatarURL, onCompleted: { [weak self] (image) in
                guard let `self` = self else { return }
                if let image = image {
                    self.avatars[userId] = JSQMessagesAvatarImageFactory.avatarImageWithImage(image, diameter: 30)
                }
                })
            // Refresh cache imgage
            userAvatarRef.observeSingleEventOfType(.Value, withBlock: { [weak self] snapshot in
                guard let `self` = self else { return }
                guard let URLString = snapshot.value as? String else { return }
                guard let URL = NSURL.init(string: URLString) else { return }
                UIImage.contentsOfURL(URL, completion: { (image, error) in
                    if let image = image {
                        self.avatars[userId] = JSQMessagesAvatarImageFactory.avatarImageWithImage(image, diameter: 30)
                        DataManager.sharedInstance.setCacheImage(cacheKeyAvatarURL, image: image)
                        // Refresh current avatar
                        let displayedIndexPaths = self.collectionView.indexPathsForVisibleItems()
                        let displayedMessages = displayedIndexPaths.map({ self.firebaseMessages.value[$0.item] })
                        if let index = displayedMessages.map({ $0.userId }).indexOf(userId) {
                            let indexPath = NSIndexPath.init(forItem: index, inSection: 0)
                            UIView.performWithoutAnimation({
                                self.collectionView?.reloadItemsAtIndexPaths([indexPath])
                            })
                        }
                    }
                })
                })
            
        }
    }
    
    private func prepareView() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboad))
        collectionView.addGestureRecognizer(tap)
        collectionView.backgroundColor = UIColor(red: 248/255, green: 248/255, blue: 248/255, alpha: 1)
        
        self.headerView.backgroundView.alpha = 1.0
    }
    
    private func prepareNavigationbar() {
        // Appearance
        navigationController?.navigationBar.barStyle = .Black
        navigationController?.navigationBar.setBackgroundImage(UIImage(), forBarMetrics: .Default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.barTintColor = AppColor.white
        navigationController?.navigationBar.tintColor = AppColor.white
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.white]
        navigationController?.navigationBar.translucent = true
    }
    
    
    private func render() {
        self.headerView.titleLabel.text = self.conversation.postName
        self.headerView.timeLabel.text = self.conversation.postCreatedAt.toNSDate().timeAgoSinceNow()
        
        self.self.headerView.imageView.contentMode = .Center
        self.self.headerView.imageView.image = UIImage.init(named: "bigStar")
        if let category = DataManager.sharedInstance.getCategory(self.conversation.postCategoryId), URL = category.icon?.absoluteImageURL {
            DataManager.sharedInstance.getCacheImage(URL, onCompleted: { (image) in
                if let image = image {
                    let resizedImage = image.resize(toWidth: 128)
                    self.self.headerView.imageView.image = resizedImage
                }
            })
        }
    }
    
    /*
     private func renderAdInfo() {
     advertisement.fetchInBackgroundWithBlock { (object, error) in
     self.hudCondition.getAdvertisement = true
     self.hideHUDWhenFinish()
     
     guard let object = object else { return debugPrint(error) }
     
     self.advertisement = object
     self.adTitleLabel.text = (self.advertisement["Name"] as? String ?? "")
     if let time = self.advertisement.createdAt?.timeAgoSinceNow() {
     self.timestampLabel.text = "     " + time
     }
     
     // Animation to display overlay view
     UIView.animateWithDuration(0.5) {
     self.overlayView.alpha = 1.0
     }
     
     // Display ad image
     if let adImage = self.advertisement["header"] as? PFFile {
     self.topImageView.rn_activityView.color = UIColor.clearColor()
     self.topImageView.showActivityView()
     adImage.getDataInBackgroundWithBlock { (imageData: NSData?, error:NSError?) -> Void in
     if let error = error {
     debugPrint(error)
     self.forceHideHUDWhenError()
     } else {
     self.topImageView.hideActivityView()
     if let imageData = imageData {
     self.topImageView.image = UIImage(data: imageData)
     self.topImageViewWidthConstraint.constant = CGRectGetWidth(self.collectionView.frame)
     }
     }
     }
     }
     else if let category = self.advertisement[AdvertisementKey.category] as? Category {
     do {
     try category.fetchIfNeeded()
     }
     catch {
     print("There was an error")
     }
     if let iconFile = category["icon"] as? PFFile {
     iconFile.getDataInBackgroundWithBlock { (imageData: NSData?, error:NSError?) -> Void in
     if let error = error {
     debugPrint(error)
     self.forceHideHUDWhenError()
     } else {
     if let image = imageData {
     self.topImageView.image = UIImage(data: image)
     self.topImageViewWidthConstraint.constant = CGRectGetWidth(self.collectionView.frame)
     }
     }
     }
     }
     }
     
     if let user = self.advertisement["owner_user"] as? PFUser {
     self.ownerNameLabel.text = user.username
     if let avatar = user["profile_picture"] as? PFFile {
     self.ownerAvtImageView.rn_activityView.color = UIColor.clearColor()
     self.ownerAvtImageView.showActivityView()
     avatar.getDataInBackgroundWithBlock { (imageData: NSData?, error:NSError?) -> Void in
     if let error = error {
     debugPrint(error)
     self.forceHideHUDWhenError()
     } else {
     self.ownerAvtImageView.hideActivityView()
     if let imageData = imageData {
     self.ownerAvtImageView.image = UIImage(data: imageData)
     }
     }
     }
     }
     else {
     self.ownerAvtImageView.image = UIImage(named: "no-avatar")
     }
     }
     }
     }
     */
}
 */
