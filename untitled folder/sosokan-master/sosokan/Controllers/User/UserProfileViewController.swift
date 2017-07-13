//
//  UserProfileViewController.swift
//  sosokan
//

import UIKit
import CHTCollectionViewWaterfallLayout
import UIScrollView_InfiniteScroll
import FBSDKShareKit
import Cosmos
import PageMenu

class UserProfileViewController: UIViewController {
    
    @IBOutlet weak var headerContainerView: UIView!
    @IBOutlet weak var headerContainerViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var backgroundImageView: UIImageView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var followingLabel: UILabel!
    @IBOutlet weak var followingNumberLabel: UILabel!
    @IBOutlet weak var followerLabel: UILabel!
    @IBOutlet weak var followerNumberLabel: UILabel!
    @IBOutlet weak var likeLabel: UILabel!
    @IBOutlet weak var likeNumberLabel: UILabel!
    @IBOutlet weak var shareLabel: UILabel!
    @IBOutlet weak var shareNumberLabel: UILabel!
    @IBOutlet weak var myPostingLabel: UILabel!
    @IBOutlet weak var messageButton: UIButton!
    @IBOutlet weak var verticalSlash1: UIView!
    @IBOutlet weak var verticalSlash2: UIView!
    @IBOutlet weak var verticalSlash3: UIView!
    @IBOutlet weak var contentContainerView: UIView!
    
    var profileId: Int!
    var userProfile: Profile?
    fileprivate var simplifiedPosts: [SimplifiedPost] = []
    fileprivate var pageMenu: CAPSPageMenu?
    
    fileprivate struct Constants {
        static let loadingString = "Loading...".localized()
        static let animationDuration: TimeInterval = 0.35
        static let headerContainerViewHeight: CGFloat = 250
        static let otherString = "Other".localized()
        
        struct ReuseIdentifier {
            static let postCell = "AdCollectionViewCell"
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        prepareHeaderContainerView()
        prepareBackgroundImageView()
        prepareAvatarImageView()
        prepareNameLabel()
        prepareFollowingLabels()
        prepareFollowerLabels()
        prepareLikeLabels()
        prepareShareLabels()
        prepareFollowBarButton()
        prepareSlashViews()
        prepareMyPostingLabel()
        prepareMessageButton()
        
        render(locationLabel: Constants.loadingString)
        render(ratingLabel: Constants.loadingString)
        
        
        fetchUserProfile(profileId: profileId)
        fetchPosts(byUser: profileId)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        preapareNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Private method
    
    fileprivate func fetchPosts(byUser userId: Int) {
        showNetworkIndicator()
        PostService.shared.fetchSimplifiedPostsByUser(userId: userId, onSuccess: { (simplifiedPosts, _, _) in
            self.simplifiedPosts = simplifiedPosts ?? []
            let separatedPosts = self.separatePosts(simplifiedPosts ?? [])
            self.preparePagingMenuController(separatedPosts: separatedPosts)
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    fileprivate func render(locationLabel text: String?) {
        locationLabel.text = text
    }
    
    fileprivate func preparePagingMenuController(separatedPosts: [(String, [SimplifiedPost])]) {
        let viewControllers = separatedPosts.map { (categoryName, posts) -> UIViewController in
            let postsSortedByDate = posts.sorted(by: { $0.0.createdAt > $0.1.createdAt })
            let postsCollectionViewController = PostsCollectionViewController(posts: postsSortedByDate,
                                                                              viewType: DataManager.shared.viewType,
                                                                              parentNavigationController: navigationController)
            postsCollectionViewController.title = categoryName
            return postsCollectionViewController
        }
        renderPagingControllers(viewControllers)
        /*
        let request = APIEndPoint.Posts.path
        let viewController = PostsCollectionViewController(postsRequest: request, nextRequest: nil, parentNavigationController: navigationController)
        viewController.title = "News"
        
        let viewController2 = PostsCollectionViewController(postsRequest: request, nextRequest: nil, parentNavigationController: navigationController)
        viewController2.title = "Couponds"
        
        let viewController3 = PostsCollectionViewController(postsRequest: request, nextRequest: nil, parentNavigationController: navigationController)
        viewController3.title = "For rent"
        
        let viewController4 = PostsCollectionViewController(postsRequest: request, nextRequest: nil, parentNavigationController: navigationController)
        viewController4.title = "Jobs"
        
        let viewController5 = PostsCollectionViewController(postsRequest: request, nextRequest: nil, parentNavigationController: navigationController)
        viewController5.title = "Real Estate"
        renderPagingControllers(controllers: [viewController, viewController2, viewController3, viewController4, viewController5])
         */
    }
    
    fileprivate func separatePosts(_ posts: [SimplifiedPost]) -> [(String, [SimplifiedPost])] {
        var separatedPosts: [String: [SimplifiedPost]] = [:]
        var otherPosts: [SimplifiedPost] = []
        
        // separate posts by category name
        // otherwise append to `otherPosts`
        for post in posts {
            if let index = DataManager.shared.allCategories.index(where: { $0.URL == post.categoryURL }) {
                let categoryName = DataManager.shared.allCategories[index].localizeName
                var postsByCategory = separatedPosts[categoryName] ?? []
                postsByCategory.append(post)
                separatedPosts[categoryName] = postsByCategory
            }
            else {
                otherPosts.append(post)
            }
        }
        
        // sort by category name
        // append `otherPosts` to last
        var results: [(String, [SimplifiedPost])] = []
        results = separatedPosts.sorted(by: { $0.0.key < $0.1.key })
        if !otherPosts.isEmpty {
            results.append((Constants.otherString, otherPosts))
        }
        return results
    }
    
    fileprivate func renderPagingControllers(_ controllers: [UIViewController]) {
        pageMenu?.view.removeFromSuperview()
        
        let parameters: [CAPSPageMenuOption] = [
            .viewBackgroundColor(AppColor.white),
            .scrollMenuBackgroundColor(AppColor.white),
            .selectionIndicatorColor(AppColor.orange),
            .menuItemSeparatorColor(AppColor.clear),
            .menuMargin(15),
            .menuItemMargin(0),
            .menuHeight(33),
            .selectedMenuItemLabelColor(AppColor.orange),
            .unselectedMenuItemLabelColor(AppColor.textSecondary),
            .useMenuLikeSegmentedControl(false),
            .menuItemWidthBasedOnTitleTextWidth(true),
            .menuItemSeparatorWidth(80)
        ]
        pageMenu = CAPSPageMenu(viewControllers: controllers,
                                          frame: CGRect(x: 0.0, y: 0.0, width: contentContainerView.frame.width, height: contentContainerView.frame.height),
                                          pageMenuOptions: parameters)
        contentContainerView.addSubview(pageMenu!.view)
    }
    
    fileprivate func prepareHeaderContainerView() {
        headerContainerView.sendSubview(toBack: backgroundImageView)
    }
    
    fileprivate func prepareBackgroundImageView() {
        let headerImage = #imageLiteral(resourceName: "userProfileHeaderImage")
        backgroundImageView.image = headerImage
        backgroundImageView.contentMode = .scaleAspectFill
        backgroundImageView.clipsToBounds = true
    }
    
    fileprivate func prepareAvatarImageView() {
        avatarImageView.roundify()
        avatarImageView.bordered(withColor: AppColor.white, width: 2)
        avatarImageView.contentMode = .scaleAspectFill
        let defaultUserAvatar = #imageLiteral(resourceName: "no-avatar")
        renderUserAvatarImageView(image: defaultUserAvatar, url: userProfile?.absoluteImageURL)
    }
    
    fileprivate func renderUserAvatarImageView(image: UIImage, url: URL?) {
        avatarImageView.image = image
        if let url = url {
            avatarImageView.kf.setImage(with: url, placeholder: image)
        }
    }
    
    fileprivate func prepareNameLabel() {
        nameLabel.font = UIFont.latoRegular(size: AppFontsizes.veryBig)
        render(userName: Constants.loadingString)
    }
    
    fileprivate func render(userName: String?) {
        nameLabel.text = userName
    }
    
    fileprivate func prepareFollowingLabels() {
        [followingLabel, followingNumberLabel].forEach({ $0!.textColor = AppColor.white })
        followingLabel.font = UIFont.latoRegular(size: AppFontsizes.regular)
        render(numberOfFollowingsLabel: Constants.loadingString)
    }
    
    fileprivate func render(numberOfFollowingsLabel text: String?) {
        followingNumberLabel.text = text
    }
    
    fileprivate func prepareFollowerLabels() {
        [followerLabel, followerNumberLabel].forEach({ $0!.textColor = AppColor.white })
        followerLabel.font = UIFont.latoRegular(size: AppFontsizes.regular)
        render(numberOfFollowersLabel: Constants.loadingString)
    }
    
    fileprivate func render(numberOfFollowersLabel text: String?) {
        followerNumberLabel.text = text
    }
    
    fileprivate func prepareLikeLabels() {
        [likeLabel, likeNumberLabel].forEach({ $0!.textColor = AppColor.white })
        likeLabel.font = UIFont.latoRegular(size: AppFontsizes.regular)
        render(numberOfLikesLabel: Constants.loadingString)
    }
    
    fileprivate func render(numberOfLikesLabel text: String?) {
        likeNumberLabel.text = text
    }
    
    fileprivate func prepareShareLabels() {
        [shareLabel, shareNumberLabel].forEach({ $0!.textColor = AppColor.white })
        shareLabel.font = UIFont.latoRegular(size: AppFontsizes.regular)
        render(numberOfSharesLabel: Constants.loadingString)
    }
    
    fileprivate func render(numberOfSharesLabel text: String?) {
        shareNumberLabel.text = text
    }
    
    fileprivate func prepareSlashViews() {
        [verticalSlash1, verticalSlash2, verticalSlash3].forEach({
            $0!.backgroundColor = AppColor.border
        })
    }
    
    fileprivate func prepareFollowBarButton() {
        let followButton = UIButton(frame: CGRect(x: 0, y: 0, width: 90, height: 20))
        followButton.setTitle("FOLLOW".localized(), for: .normal)
        followButton.setTitleColor(AppColor.white, for: .normal)
        followButton.titleLabel?.font = UIFont.latoRegular(size: AppFontsizes.verySmall)
        followButton.backgroundColor = AppColor.orange
        followButton.setImage(AppIcons.plusIcon.resize(toWidth: 5)?.tint(with: AppColor.white), for: .normal)
        followButton.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 10)
        followButton.cornerRadius = 10
        
        let followBarButton = UIBarButtonItem(customView: followButton)
        
        let spaceBarButton = UIBarButtonItem(barButtonSystemItem: .fixedSpace, target: nil, action: nil)
        spaceBarButton.width = -7
        
        navigationItem.rightBarButtonItems = [spaceBarButton, followBarButton]
    }
    
    fileprivate func prepareRatingViews() {
        ratingLabel.font = UIFont.latoBold(size: AppFontsizes.verySmall)
    }
    
    func render(ratingLabel text: String?) {
        ratingLabel.text = text
    }
    
    fileprivate func prepareMyPostingLabel() {
        myPostingLabel.font = UIFont.latoRegular(size: AppFontsizes.small)
    }
    
    fileprivate func prepareMessageButton() {
        messageButton.backgroundColor = AppColor.orange
        messageButton.titleLabel?.font = UIFont.latoRegular(size: AppFontsizes.small)
        messageButton.setTitle("MESSAGE".localized(), for: .normal)
        messageButton.setTitleColor(AppColor.white, for: .normal)
        messageButton.contentEdgeInsets = UIEdgeInsetsMake(5, 30, 5, 30)
    }
    
    fileprivate func fetchUserProfile(profileId id: Int) {
        showNetworkIndicator()
        ProfileService.shared.fetch(profile: profileId, onSuccess: { (profile) in
            self.hideNetworkIndicator()
            self.userProfile = profile
            self.render(userName: profile?.displayName)
            self.render(numberOfSharesLabel: profile?.numberOfShares.string())
            self.render(numberOfLikesLabel: profile?.numberOfLikes.string())
            self.render(numberOfFollowersLabel: profile?.numberOfFollowers.string())
            self.render(numberOfFollowingsLabel: profile?.numberOfFollowings.string())
            self.render(locationLabel: profile?.address)
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    fileprivate func preapareNavigationBar() {
        automaticallyAdjustsScrollViewInsets = false
        
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.backgroundColor = AppColor.clear
        navigationController?.navigationBar.isTranslucent = true
        navigationController?.navigationBar.tintColor = AppColor.white
    }
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        
    }
}

// MARK: Convenience init
extension UserProfileViewController {
    convenience init(profileId: Int) {
        self.init(nibName: "UserProfileViewController", bundle: nil)
        self.profileId = profileId
    }
}
