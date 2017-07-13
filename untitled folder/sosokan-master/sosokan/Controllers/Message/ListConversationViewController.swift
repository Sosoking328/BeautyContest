//
//  ListConversationViewController.swift
//  sosokan

import UIKit
import SDWebImage
import JSQMessagesViewController

class ListConversationViewController: UIViewController {}
/*
    
    // MARK: Variable
    var conversations = [FConversation].init([])
    var ref: FIRDatabaseReference!
    var conversationRefs: Variable<[FIRDatabaseReference]> = Variable.init([])
    var userProfile: Observable<UserProfile?> = Observable.just(nil)
    var userId: String!
    var messageTableViewContentOffset = CGPointZero
    let bottomBarHeight: CGFloat = 50
    
    // MARK: IBOutlet
    @IBOutlet weak var messageTableView: UITableView!
    @IBOutlet weak var bottomBarBottomConstaint: NSLayoutConstraint!
    @IBOutlet weak var bottomBarSegmentControl: UISegmentedControl!
    
    // MARK: IBAction
    @IBAction func leftBarButtonDidTouched(sender: AnyObject) {
        AppDelegate.shared().toggleLeftDrawer(sender, animated: true)
    }
    
    @IBAction func bottomSegmentedControlDidChanged(sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex {
        case 2:
            AppDelegate.shared().createAdButtonDidTouched()
        default:
            break
        }
        
        sender.selectedSegmentIndex = -1
    }
    
    // MARK: View controller life circle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.ref = FIRDatabase.database().reference()
        
        self.prepareTableView()
        self.prepareBottomBar()
        self.addLeftToRightSwipeGesture()
        self.load()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        messageTableView.addObserver(self, forKeyPath: "contentOffset", options: .New, context: nil)
        
        // Local view
        setText()
        prepareNavigationbar()
        
        if let userId = FIRAuth.auth()?.currentUser?.uid {
            NotificationHelper.resetBadgeNumber(userId: userId, type: .Message, onCompleted: nil)
        }
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        
        messageTableView.removeObserver(self, forKeyPath: "contentOffset")
    }
    
    override func observeValueForKeyPath(keyPath: String?, ofObject object: AnyObject?, change: [String : AnyObject]?, context: UnsafeMutablePointer<Void>) {
        if keyPath == "contentOffset" {
            messageTableViewContentOffset = messageTableView.contentOffset
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Method
    func setText() {
        // Title
        title = "MESSAGES".localized()
    }
    
    // MARK: Private method
    private func addLeftToRightSwipeGesture() {
        let gesture = UISwipeGestureRecognizer.init(target: self, action: Selector.init())
        gesture.direction = .Right
        gesture.rx_event
            .subscribeNext({ [weak self] _ in
                guard let `self` = self else { return }
                AppDelegate.shared().toggleLeftDrawer(self, animated: true)
                })
            .addDisposableTo(self.rx_disposeBag)
        self.view.addGestureRecognizer(gesture)
    }
    
    private func prepareBottomBar() {
        let images = [UIImage(named: "homeTabBarIcon"),
                      UIImage(named: "searchTabBarIcon"),
                      UIImage(named: "addTabBarIcon"),
                      UIImage(named: "heartTabBarIcon"),
                      UIImage(named: "gearTabBarIcon")]
        
        let topBorder = CALayer()
        topBorder.frame = CGRectMake(0, 0, UIScreen.mainScreen().bounds.width, 1)
        topBorder.borderWidth = 1
        topBorder.borderColor = AppColor.border.CGColor
        bottomBarSegmentControl.layer.addSublayer(topBorder)
        
        bottomBarSegmentControl.backgroundColor = AppColor.white
        bottomBarSegmentControl.tintColor = AppColor.white
        bottomBarSegmentControl.setDividerImage(UIImage(), forLeftSegmentState: .Normal, rightSegmentState: .Normal, barMetrics: .Default)
        bottomBarSegmentControl.removeAllSegments()
        images.enumerate().forEach { (index, image) in
            let image = image?.imageWithRenderingMode(.AlwaysOriginal)
            bottomBarSegmentControl.insertSegmentWithImage(image, atIndex: index, animated: false)
        }
    }
    
    private func load() {
        guard let userId = DataManager.sharedInstance.currentUser?.uid else {
            return
        }
        
        self.observerNewConversation(ofUserId: userId)
            .flatMap({
                self.observerConversation(withRef: $0)
            })
            .filterNil()
            .distinctUntilChanged()
            .subscribeNext({ [weak self] conversation in
                guard let `self` = self else { return }
                
                let waitingUserIds = conversation.waitingUsers.map({ $0.0 })
                
                if let index = self.conversations.indexOf({ $0.id == conversation.id }) {
                    
                    if waitingUserIds.contains(userId) {
                        self.conversations.removeAtIndex(index)
                        
                        let indexPath = NSIndexPath.init(forRow: index, inSection: 0)
                        self.messageTableView.deleteItemsAtIndexPaths([indexPath], animationStyle: .Automatic)
                    }
                    else {
                        self.conversations[index] = conversation
                        self.conversations = self.conversations.sort({ $0.0.updatedAt > $0.1.updatedAt })
                        self.messageTableView.reloadData()
                    }
                }
                else {
                    if !waitingUserIds.contains(userId) {
                        self.conversations.append(conversation)
                    }
                    
                    self.conversations = self.conversations.sort({ $0.0.updatedAt > $0.1.updatedAt })
                    self.messageTableView.reloadData()
                }
            })
            .addDisposableTo(self.rx_disposeBag)
        
        self.observerRemovedConversation(ofUserId: userId)
            .map({ conversationId in
                (conversationId, self.conversations.indexOf({ $0.id == conversationId }))
            })
            .subscribeNext({ [weak self] (conversationId, index) in
                guard let `self` = self else { return }
                
                let conversationRef = self.ref.child(References.conversations).child(conversationId)
                conversationRef.removeAllObservers()
                
                if let index = index {
                    self.conversations.removeAtIndex(index)
                    
                    let indexPath = NSIndexPath.init(forRow: index, inSection: 0)
                    self.messageTableView.deleteItemsAtIndexPaths([indexPath], animationStyle: .Automatic)
                }
            })
            .addDisposableTo(self.rx_disposeBag)
    }
    
    private func observerNewConversation(ofUserId userId: String) -> Observable<FIRDatabaseReference> {
        let userConversationsRef = self.ref.child(References.users).child(userId).child(UserKey.conversations)
        return userConversationsRef.rx_observe(.ChildAdded)
            .map({ self.ref.child(References.conversations).child($0.key) })
    }
    
    private func observerRemovedConversation(ofUserId userId: String) -> Observable<String> {
        let userConversationsRef = self.ref.child(References.users).child(userId).child(UserKey.conversations)
        userConversationsRef.queryOrderedByValue()
        return userConversationsRef.rx_observe(.ChildRemoved)
            .map({ $0.key })
    }
    
    private func observerConversation(withRef ref: FIRDatabaseReference) -> Observable<FConversation?> {
        let observer = Observable.create({ (observer: AnyObserver<FIRDataSnapshot>) -> Disposable in
            ref.rx_observe(.Value)
                .subscribeNext({ snapshot in
                    observer.on(.Next(snapshot))
                })
                .addDisposableTo(self.rx_disposeBag)
            return NopDisposable.instance
        })
        return observer.map({ snapshot in
            if let json = snapshot.value as? JSONType where !json.isEmpty {
                let conversation = FConversation.init(json: json)
                return conversation
            }
            else {
                return nil
            }
        })
    }
    
    private func prepareNavigationbar() {
        // Appearance
        navigationController?.navigationBar.barStyle = .Default   // Change status bar to light mode.
        navigationController?.navigationBar.setBackgroundImage(nil, forBarMetrics: .Default)
        navigationController?.navigationBar.shadowImage = nil
        navigationController?.navigationBar.barTintColor = AppColor.white
        navigationController?.navigationBar.tintColor = AppColor.textPrimary
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.orange]
        navigationController?.navigationBar.translucent = false
        setNeedsStatusBarAppearanceUpdate()
    }
    
    private func prepareTableView() {
        let cellIdentifier = ConversationTableViewCell.cellReuseIdentifier
        let cellRowHeight = ConversationTableViewCell.cellDefaultRowHeight
        let tableFooterView = UIView(frame: CGRectZero)
        
        messageTableView.registerNib(UINib(nibName: cellIdentifier, bundle: nil), forCellReuseIdentifier: cellIdentifier)
        messageTableView.rowHeight = cellRowHeight
        messageTableView.tableFooterView = tableFooterView
        messageTableView.dataSource = self
        messageTableView.delegate = self
    }
    
    private func startingConversation(conversation: FConversation) {
        let vc = StoryBoardManager.viewController(storyBoardName: "Message", viewControllerName: String(ConversationViewController)) as! ConversationViewController
        vc.conversation = conversation
        navigationController?.showViewController(vc, sender: nil)
    }
}

// MARK: UITableViewDataSource
extension ListConversationViewController: UITableViewDataSource {
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return conversations.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = ConversationTableViewCell.cellReuseIdentifier
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! ConversationTableViewCell
        let conversation = conversations[indexPath.row]
        
        cell.render(conversation)
        
        return cell
    }
}

// MARK: UITableViewDelegate
extension ListConversationViewController: UITableViewDelegate {
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let conversation = conversations[indexPath.row]
        self.startingConversation(conversation)
    }
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            guard let userId = FIRAuth.auth()?.currentUser?.uid else { return }
            let conversation = self.conversations[indexPath.row]
            let conversationRef = self.ref.child(References.conversations).child(conversation.id)
            let waitingUserRef = conversationRef.child(FConversationKey.waitingUsers).child(userId)
            waitingUserRef.setValue(-NSDate.getTimestamp())
        }
    }
}
*/
