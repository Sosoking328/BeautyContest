//
//  PostDetailViewController.swift
//  sosokan
//

import UIKit
import Material
import Cosmos
import MessageUI
import Social
import MobilePlayer
import FBSDKShareKit
import SwiftyJSON
import CHTCollectionViewWaterfallLayout
import MapKit

class PostDetailViewController: UIViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var commentContainerView: UIView!
    @IBOutlet weak var commentContainerViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var commentTextField: UITextField!
    @IBOutlet weak var sendCommentButton: Button!
    
    var simplifiedPost: SimplifiedPost!
    var postDetailHeaderView: PostDetailHeaderView!
    var postDetailFooterView: PostDetailFooterView!
    var currentPost: Post?
    var totalComments: Int = 0
    var comments: [CommentObject] = []
    var transitionDelegate: ZoomAnimatedTransitioningDelegate?
    var headerSlideImage: InputSource!
    var ownerProfile: Profile?
    var relatedPosts: [SimplifiedPost] = []
    var likedUserImages: [String] = Array(repeating: "", count: 20)
    
    fileprivate struct Constants {
        static let animationDuration = TimeInterval(0.5)
        static let mapViewHeight: CGFloat = 200.0
        static let videoViewHeight: CGFloat = 155.0
        static let loadingString = "Loading...".localized()
        static let gapBetweenCallButtonAndMapView: CGFloat = 8.0
        static let userLikedImageViewWidth: CGFloat = 35.0
        static let maximumNumberOfRelatedPosts = 6
        static let numberOfCommentsWillBeFetched = 5
    }
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.preparePostDetailHeaderView()
        self.preparePostDetailFooterView()
        
        self.prepareRelatedPostsCollectionView()
        self.prepareCommentTableView()
        self.prepareLikedCollectionView()
        
        self.prepareRelatedPostsLabel()
        self.prepareLikeLabel()
        self.prepareViewsNumberLabel()
        self.prepareDescriptionWebView()
        self.prepareMapView()
        self.prepareVideoViews()
        self.prepareDateLabel()
        self.prepareCarouselView()
        
        self.prepareOwnerAvatarImageView()
        self.render(ownerName: Constants.loadingString)
        self.render(ownerTotalPosts: Constants.loadingString)
        
        self.prepareSendMessageButton()
        self.prepareNameLabel()
        self.preparePriceLabel()
        self.prepareExpiredImageView()
        self.prepareCategoryButton()
        self.prepareFollowingButton()
        self.prepareAllCommentsLabel()
        self.prepareCommentContainerView()
        self.prepareSendCommentButton()
        
        self.compressPostDetailHeaderViewFrame()
        self.compressPostDetailFooterViewFrame()
        
        self.fetchPostDetail()
        self.fetchRelatedPosts()
        self.fetchOwnerProfile()
        self.fetchComments()
        self.fetchRelatedPosts()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.observerNotitications()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    // MARK: IBAction
    
    @IBAction func shareButtonTapped(_ sender: Any) {
        let deepLinkURL = URL(string: "http://sosokan.herokuapp.com/link/\(self.simplifiedPost.id)")
        let name = self.simplifiedPost.name.trimmingCharacters(in: .whitespacesAndNewlines)
        let imageURL = self.simplifiedPost.headerImage?.absoluteURL
        
        var sharingObjects: [AnyObject] = []
        sharingObjects.append(name as AnyObject)
        sharingObjects.append("\n" as AnyObject)
        sharingObjects.append(description as AnyObject)
        sharingObjects.append("\n" as AnyObject)
        if let imageURL = imageURL, let image = DataManager.shared.getCacheImage(imageURL) {
            sharingObjects.append(image)
        }
        sharingObjects.append("\n" as AnyObject)
        if let deepLinkURL = deepLinkURL {
            sharingObjects.append(deepLinkURL as AnyObject)
        }
        
        let shareWithFacebook = ShareWithFacebook()
        shareWithFacebook.customAction = { [weak self] in
            let link = FBSDKShareLinkContent()
            link.contentURL = deepLinkURL
            link.contentTitle = name
            link.imageURL = imageURL as URL!
            
            let dialog = FBSDKShareDialog()
            dialog.shareContent = link
            dialog.fromViewController = self
            dialog.mode = .native
            if !dialog.canShow() {
                dialog.mode = .feedBrowser
            }
            dialog.show()
        }
        
        let activity = UIActivityViewController(activityItems: sharingObjects, applicationActivities: [shareWithFacebook])
        activity.excludedActivityTypes = [UIActivityType.postToFacebook]
        self.navigationController?.present(activity, animated: true, completion: nil)
    }
    
    @IBAction func favoriteButtonTapped(_ sender: Any) {
    }
    
    @IBAction func reportButtonTapped(_ sender: Any) {
        guard let _ = DataManager.shared.currentUser
            else { return self.presentLoginAlert() }
        let reportViewController = ReportViewController(postId: simplifiedPost.id)
        self.navigationController?.show(reportViewController, sender: nil)
    }
    
    @IBAction func commentButtonTapped(_ sender: Any) {
    }
    
    @IBAction func commenTextFieldValueChanged(_ sender: AnyObject) {
        let text = (sender as? UITextField)?.text ?? ""
        self.sendCommentButton.isEnabled = !text.isEmpty
    }
    
    // MARK: Methods
    
    func dateLabelTapped(_ sender: AnyObject) {
        if self.postDetailHeaderView.dateLabel.tag == 0 {
            self.postDetailHeaderView.dateLabel.tag = 1
            self.postDetailHeaderView.dateLabel.text = self.simplifiedPost.createdAt.hourDay()
        }
        else {
            self.postDetailHeaderView.dateLabel.tag = 0
            self.postDetailHeaderView.dateLabel.text = self.simplifiedPost.createdAt.timeAgoSinceNow
        }
    }
    
    func userAvatarImageViewTapped(_ sender: AnyObject) {
        guard let ownerProfileId = simplifiedPost.ownerId
            else { return showErrorMessage(Messages.occurredError) }
        let viewController = UserProfileViewController(profileId: ownerProfileId)
        self.navigationController?.show(viewController, sender: nil)
    }
    
    func carouselViewTapped(_ sender: AnyObject) {
        let viewController = FullScreenSlideshowViewController()
        viewController.pageSelected = { [weak self] (page: Int) in
            guard let `self` = self else { return }
            self.postDetailHeaderView.carouselView.setScrollViewPage(page, animated: false)
        }
        viewController.initialImageIndex = self.postDetailHeaderView.carouselView.scrollViewPage
        viewController.inputs = self.postDetailHeaderView.carouselView.images
        self.transitionDelegate = ZoomAnimatedTransitioningDelegate(slideshowView: self.postDetailHeaderView.carouselView, slideshowController: viewController)
        viewController.transitioningDelegate = self.transitionDelegate
        self.present(viewController, animated: true, completion: nil)
    }
    
    func keyboardWillShowHandler(sender: Any) {
        if self.commentTextField.isFirstResponder {
            guard let notification = sender as? Notification,
                let userInfo = notification.userInfo
                else { return }
            guard let keyboardFrame = userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue else { return }
            let keyboardRectangle = keyboardFrame.cgRectValue
            let duration = userInfo[UIKeyboardAnimationDurationUserInfoKey] as? TimeInterval ?? Constants.animationDuration
            UIView.animate(withDuration: duration, animations: {
                let constant = keyboardRectangle.height
                self.commentContainerViewBottomConstraint.constant = constant
                self.view.setNeedsLayout()
                self.view.layoutIfNeeded()
            })
        }
    }
    
    func keyboardWillHideHandler(sender: Any) {
        if self.commentTextField.isFirstResponder {
            guard let notification = sender as? Notification,
                let userInfo = notification.userInfo
                else { return }
            let duration = userInfo[UIKeyboardAnimationDurationUserInfoKey] as? TimeInterval ?? Constants.animationDuration
            UIView.animate(withDuration: duration, animations: {
                self.commentContainerViewBottomConstraint.constant = 0
                self.view.layoutIfNeeded()
            })
        }
    }
    
    // MARK: Private method
    
    fileprivate func observerNotitications() {
        let center = NotificationCenter.default
        center.addObserver(self, selector: #selector(self.keyboardWillShowHandler(sender:)),
                           name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        center.addObserver(self, selector: #selector(self.keyboardWillHideHandler(sender:)),
                           name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    fileprivate func prepareCommentContainerView() {
        self.view.bringSubview(toFront: self.commentContainerView)
        self.commentContainerView.addTopBorderWithColor(AppColor.border, width: 1)
    }
    
    fileprivate func prepareSendCommentButton() {
        self.sendCommentButton.setImage(AppIcons.sendCommentIcon.tint(with: AppColor.grey), for: .disabled)
        self.sendCommentButton.setImage(AppIcons.sendCommentIcon.tint(with: AppColor.orange), for: .normal)
    }
    
    fileprivate func prepareAllCommentsLabel() {
        renderCommentsLabel(numberOfComments: totalComments)
    }
    
    fileprivate func renderCommentsLabel(numberOfComments number: Int) {
        postDetailHeaderView.allCommentsLabel.text = "Total: " + number.string()
    }
    
    fileprivate func fetchComments() {
        self.showNetworkIndicator()
        CommentService.shared.fetchComments(ofPost: simplifiedPost.id, limit: Constants.numberOfCommentsWillBeFetched, offset: 0, onSuccess: { (comments, _, totalComments) in
            self.hideNetworkIndicator()
            
            self.comments = comments ?? []
            self.tableView.reloadData()
            
            self.totalComments = totalComments ?? 0
            self.renderCommentsLabel(numberOfComments: self.totalComments)
            
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    fileprivate func prepareLikeLabel() {
        self.postDetailHeaderView.likeNumberLabel.text = self.simplifiedPost.numberOfFavorites.string()
    }
    
    fileprivate func prepareViewsNumberLabel() {
        self.postDetailHeaderView.viewsNumberLabel.text = self.simplifiedPost.numberOfViews.string()
    }
    
    fileprivate func prepareLikedCollectionView() {
        let nib = UINib(nibName: UserImageCollectionViewCell.identifier, bundle: nil)
        self.postDetailHeaderView.likedCollectionView.register(nib, forCellWithReuseIdentifier: UserImageCollectionViewCell.identifier)
        self.postDetailHeaderView.likedCollectionView.backgroundColor = AppColor.clear
        self.postDetailHeaderView.likedCollectionView.dataSource = self
        self.postDetailHeaderView.likedCollectionView.delegate = self
        
        let layout = UICollectionViewFlowLayout()
        layout.minimumInteritemSpacing = 0
        layout.scrollDirection = .horizontal
        self.postDetailHeaderView.likedCollectionView.collectionViewLayout = layout
    }
    
    fileprivate func prepareRelatedPostsLabel() {
        let style = NSMutableParagraphStyle()
        style.firstLineHeadIndent = 8
        self.postDetailFooterView.relatedPostsLabel.attributedText = NSAttributedString(string: "Related", attributes: [NSParagraphStyleAttributeName: style])
    }
    
    fileprivate func fetchRelatedPosts() {
        if let categoryId = simplifiedPost.categoryId {
            self.showNetworkIndicator()
            PostService.shared.fetchSimplifiedPostsByCategoryId(categoryId, limit: 50, offset: 0, onSuccess: { (posts, next, total) in
                self.relatedPosts = posts?.filter({$0.id != self.simplifiedPost.id}) ?? []
                self.postDetailFooterView.relatedPostsLoadingIndicatorView.stopAnimating()
                self.postDetailFooterView.collectionView.reloadData()
                self.compressPostDetailFooterViewFrame()
            }, onError: { (error) in
                debugPrint(error)
                self.postDetailFooterView.relatedPostsLoadingIndicatorView.stopAnimating()
            })
        }
    }
    
    fileprivate func preparePostDetailFooterView() {
        self.postDetailFooterView = PostDetailFooterView.fromNib()
        self.tableView.tableFooterView = self.postDetailFooterView
        compressPostDetailFooterViewFrame()
    }
    
    fileprivate func prepareRelatedPostsCollectionView() {
        let nib = UINib(nibName: AdCollectionViewCell.identifier, bundle: nil)
        self.postDetailFooterView.collectionView.register(nib, forCellWithReuseIdentifier: AdCollectionViewCell.identifier)

        let layout = CHTCollectionViewWaterfallLayout()
        layout.itemRenderDirection = .leftToRight
        layout.minimumColumnSpacing = 0
        layout.minimumInteritemSpacing = 0
        layout.columnCount = DataManager.shared.viewType.numberOfColumns
//        self.postDetailFooterView.collectionView.collectionViewLayout = layout
        
        self.postDetailFooterView.collectionView.backgroundColor = AppColor.white
        
        self.postDetailFooterView.collectionView.dataSource = self
        self.postDetailFooterView.collectionView.delegate = self
        
        self.postDetailFooterView.collectionView.isScrollEnabled = false
    }
    
    fileprivate func renderPostDetail() {
        guard let post = self.currentPost else { return }
        self.renderCarouselView(withImages: post.images)
        if let location = post.location {
            self.renderMapView(withLocation: location)
        }
        if let description = post.fullDescription, !description.isEmpty {
            self.renderDescription(withContent: description)
        }
        else {
            let currentHeight = self.postDetailHeaderView.descWebViewHeightConstraint.constant
            var newFrame = self.postDetailHeaderView.frame
            newFrame.size.height = newFrame.height - currentHeight
            self.postDetailHeaderView.frame = newFrame
            self.postDetailHeaderView.descWebViewHeightConstraint.constant = 0
            self.tableView.tableHeaderView = self.postDetailHeaderView
            UIView.animate(withDuration: Constants.animationDuration, animations: {
                self.postDetailHeaderView.webViewLoadingIndicator.stopAnimating()
                self.postDetailHeaderView.webViewLoadingIndicatorHeightConstraint.constant = 0
                self.postDetailHeaderView.webViewLoadingIndicatorBottomConstraint.constant = 0
                self.postDetailHeaderView.descWebView.alpha = 1
            }) 
        }
    }
    
    fileprivate func fetchPostDetail() {
        self.showNetworkIndicator()
        PostService.shared.fetchPost(postId: self.simplifiedPost.id, onSuccess: { (post) in
            self.hideNetworkIndicator()
            self.currentPost = post
            self.renderPostDetail()
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
 
    fileprivate func renderCarouselView(withImages images: [PImage]) {
        if !images.isEmpty {
            let imageSources = images.sorted(by: { $0.0.id < $0.1.id }).enumerated().flatMap { (index, image) -> InputSource? in
                guard let imageURL = image.absoluteURL else { return nil }
                var placeHolder = AppIcons.bigStarIcon
                if let categoryId = self.simplifiedPost.categoryId,
                    let category = DataManager.shared.allCategories.getCategory(withCategoryId: categoryId),
                    let categoryImageURL = category.absoluteLocalizeIconURL,
                    let cacheImage = DataManager.shared.getCacheImage(categoryImageURL) {
                    placeHolder = cacheImage
                }
                if index == 0, let headerImage = self.headerSlideImage as? SDWebImageSource,
                    let cacheImage = DataManager.shared.getCacheImage(headerImage.url) {
                    placeHolder = cacheImage
                }
                let image = AlamofireSource(urlString: imageURL, placeHolderImage: placeHolder, onCompleted: { [weak self] (image) in
                    guard let `self` = self else { return }
                    self.postDetailHeaderView.carouselView.contentScaleMode = .scaleAspectFill
                    })
                return image
            }
            self.postDetailHeaderView.carouselView.setImageInputs(imageSources)
            self.postDetailHeaderView.carouselView.pageControl.alpha = imageSources.count > 1 ? 1 : 0
        }
    }
    
    fileprivate func handleCommentsChange(editedComment comment: CommentObject, atIndex: Int) {
        self.comments[atIndex] = comment
        let indexPath = IndexPath(row: atIndex, section: 0)
        self.tableView.reloadRows(at: [indexPath], with: .automatic)
    }
    
    fileprivate func handleCommentsChange(newComment comment: CommentObject) {
        self.comments.append(comment)
        
        totalComments += 1
        renderCommentsLabel(numberOfComments: totalComments)
        
        self.tableView.beginUpdates()
        let summaryIndexPath = IndexPath(row: 0, section: 0)
        self.tableView.reloadRows(at: [summaryIndexPath], with: .none)
        let index = self.comments.count - 1
        let addedCommentIndexPath = IndexPath(row: index, section: 0)
        self.tableView.insertRows(at: [addedCommentIndexPath], with: .automatic)
        self.tableView.endUpdates()
        self.tableView.scrollToRow(at: addedCommentIndexPath, at: .top, animated: true)
    }
    
    fileprivate func handleCommentsChange(deletedComment comment: CommentObject) {
        guard let index = self.comments.index(where: { $0 == comment }) else { return }
        self.comments.remove(at: index)
        
        totalComments -= 1
        renderCommentsLabel(numberOfComments: totalComments)
        
        self.tableView.beginUpdates()
        let summaryIndexPath = IndexPath(row: 0, section: 0)
        self.tableView.reloadRows(at: [summaryIndexPath], with: .none)
        let deletedCommentIndexPath = IndexPath(row: index, section: 0)
        self.tableView.deleteRows(at: [deletedCommentIndexPath], with: .automatic)
        self.tableView.endUpdates()
    }
    
    fileprivate func presentCommentOptionsModalView(comment: CommentObject,
                                                        sourceView: UIView,
                                                        sourceRect: CGRect) {
        let nibName = String(describing: CommentOptionsTableViewController.self)
        let viewController = CommentOptionsTableViewController(nibName: nibName, bundle: nil)
        let reportAction: () -> Void = { [weak self] (_) in
            guard let `self` = self else { return }
            
        }
        var options: [CommentOptionsTableCell] = [
            CommentOptionsTableCell(AppIcons.flagIcon, "Report", reportAction)
        ]
        let isMyComment = comment.id == DataManager.shared.currentUser?.id
        if isMyComment {
            let deleteAction: () -> Void = { [weak self] (_) in
                guard let `self` = self else { return }
                /*
                 guard let currentUser = DataManager.shared.currentUser else { return }
                 self.showNetworkIndicator()
                 self.fetchUserId(withFirebaseId: currentUser.uid)
                 .debug()
                 .errorOnNil()
                 .flatMap({ self.fetchUserToken(withUserId: $0) })
                 .errorOnNil()
                 .flatMap({ self.deleteComment(comment: comment, userToken: $0) })
                 .errorOnNil()
                 .subscribe(onNext: { [weak self] (removedComment) in
                 guard let `self` = self else { return }
                 self.handleCommentsChange(deletedComment: removedComment)
                 self.hideNetworkIndicator()
                 }, onError: { [weak self] (error) in
                 guard let `self` = self else { return }
                 self.showErrorMessage(Messages.occurredError)
                 self.hideNetworkIndicator()
                 })
                 .addDisposableTo(self.rx_disposeBag)
                 self.selectedComment = nil
                 */
            }
            options.append(CommentOptionsTableCell(AppIcons.trashIcon, "Delete", deleteAction))
            
            let editAction: () -> Void = { [weak self] (_) in
                guard let `self` = self else { return }
                /*
                 self.selectedComment = comment
                 self.commentTextField.becomeFirstResponder()
                 */
            }
            options.append(CommentOptionsTableCell(AppIcons.writeCommentIcon, "Edit", editAction))
        }
        viewController.options = options
        viewController.modalPresentationStyle = .popover
        if let popover = viewController.popoverPresentationController {
            popover.sourceView = sourceView
            popover.sourceRect = sourceRect
            popover.permittedArrowDirections = .any
            viewController.preferredContentSize = CGSize(width: 200, height: 132)
            popover.delegate = self
        }
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    fileprivate func prepareCommentTableView() {
        let nib = UINib(nibName: CommentContentTableViewCell.identifier, bundle: nil)
        self.tableView.register(nib, forCellReuseIdentifier: CommentContentTableViewCell.identifier)
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.separatorStyle = .none
        self.tableView.keyboardDismissMode = .onDrag
        self.tableView.estimatedRowHeight = 200
        self.tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    fileprivate func prepareFollowingButton() {
//        self.postDetailHeaderView.followingButton.contentEdgeInsets = UIEdgeInsets(top: 5, left: 20, bottom: 5, right: 20)
//        self.postDetailHeaderView.followingButton.layer.cornerRadius = 5
//        self.postDetailHeaderView.followingButton.backgroundColor = AppColor.orange
//        self.postDetailHeaderView.followingButton.setTitleColor(AppColor.white, for: UIControlState())
//        self.postDetailHeaderView.followingButton.isHidden = false
    }
    
    fileprivate func fetchOwnerProfile() {
        guard let postOwnerId = simplifiedPost.ownerId else { return }
        self.showNetworkIndicator()
        ProfileService.shared.fetch(profile: postOwnerId, onSuccess: { (profile) in
            self.hideNetworkIndicator()
            self.ownerProfile = profile
            if let profile = profile {
                if let ownerImageURL = profile.absoluteImageURL {
                    let defaultUserImage = #imageLiteral(resourceName: "no-avatar")
                    self.render(ownerImageWithUrl: ownerImageURL, placeholderImage: defaultUserImage)
                    self.render(ownerName: profile.displayName)
                    self.render(ownerTotalPosts: profile.numberOfPosts.string())
                }
            }
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    private func renderOwnerAvatarImageView(image: UIImage, url: URL?) {
        postDetailHeaderView.avtImageView.kf.setImage(with: url, placeholder: image)
    }
    
    private func render(ownerImage image: UIImage?) {
        postDetailHeaderView.avtImageView.image = image
    }
    
    private func render(ownerImageWithUrl url: URL, placeholderImage: UIImage?) {
        postDetailHeaderView.avtImageView.kf.setImage(with: url, placeholder: placeholderImage)
    }
    
    private func render(ownerName name: String?) {
        postDetailHeaderView.userNameLabel.text = name
    }
    
    private func render(ownerTotalPosts string: String?) {
        postDetailHeaderView.totalAdLabel.text = string
    }
    
    fileprivate func prepareCategoryButton() {
        var title: String?
        if let categoryId = self.simplifiedPost.categoryId,
            let category = DataManager.shared.allCategories.getCategory(withCategoryId: categoryId) {
            if let parentCategoryId = category.parentCategoryId,
                let parentCategory = DataManager.shared.allCategories.getCategory(withCategoryId: parentCategoryId) {
                title = "\(parentCategory.localizeName) > \(category.localizeName)"
            }
            else {
                title = category.localizeName
            }
        }
        self.postDetailHeaderView.categoryButton.setTitle(title ?? "N/A", for: UIControlState())
    }
    
    fileprivate func prepareExpiredImageView() {
        self.postDetailHeaderView.expiredImageView.isHidden = true
    }
    
    fileprivate func preparePriceLabel() {
        self.postDetailHeaderView.priceLabel.isHidden = true
    }
    
    fileprivate func prepareNameLabel() {
        self.postDetailHeaderView.nameLabel.text = self.simplifiedPost.name
    }
    
    fileprivate func prepareSendMessageButton() {
        self.postDetailHeaderView.textMeButton.isEnabled = true
    }
    
    fileprivate func prepareOwnerAvatarImageView() {
        postDetailHeaderView.avtImageView.contentMode = .scaleAspectFill
        postDetailHeaderView.avtImageView.isUserInteractionEnabled = true
        let defaultUserImage = #imageLiteral(resourceName: "no-avatar")
        renderOwnerAvatarImageView(image: defaultUserImage, url: nil)
        
        let ownerAvatarImageViewTapGesture = UITapGestureRecognizer(target: self, action: #selector(userAvatarImageViewTapped(_:)))
        postDetailHeaderView.avtImageView.addGestureRecognizer(ownerAvatarImageViewTapGesture)
    }
    
    fileprivate func prepareCarouselView() {
        let carouselHeight = self.postDetailHeaderView.carouselViewHeightConstraint.constant
        self.view.layoutSubviews()
        let carouselWidth = UIScreen.main.bounds.width
        var newFrame = self.postDetailHeaderView.frame
        newFrame.size.height = newFrame.size.height - carouselHeight + carouselWidth
        self.postDetailHeaderView.frame = newFrame
        self.tableView.tableHeaderView = self.postDetailHeaderView
        self.postDetailHeaderView.carouselViewHeightConstraint.constant = carouselWidth
        if let headerImageURL = self.simplifiedPost.headerImage?.absoluteURL {
            let headerImage = SDWebImageSource(url: headerImageURL)
            self.postDetailHeaderView.carouselView.setImageInputs([headerImage])
            self.postDetailHeaderView.carouselView.contentScaleMode = .scaleAspectFill
            self.headerSlideImage = headerImage
        }
        else if let categoryId = self.simplifiedPost.categoryId,
            let category = DataManager.shared.allCategories.getCategory(withCategoryId: categoryId),
            let categoryImageURL = category.absoluteLocalizeIconURL {
            let headerImage = SDWebImageSource(url: categoryImageURL)
            self.postDetailHeaderView.carouselView.setImageInputs([headerImage])
            self.postDetailHeaderView.carouselView.contentScaleMode = .scaleAspectFill
            self.headerSlideImage = headerImage
        }
        else {
            let image = ImageSource(image: AppIcons.bigStarIcon)
            self.postDetailHeaderView.carouselView.setImageInputs([image])
            self.postDetailHeaderView.carouselView.contentScaleMode = .center
            self.headerSlideImage = image
        }
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.carouselViewTapped(_:)))
        self.postDetailHeaderView.carouselView.addGestureRecognizer(tap)
    }
    
    fileprivate func prepareDateLabel() {
        self.postDetailHeaderView.dateLabel.isUserInteractionEnabled = true
        self.postDetailHeaderView.dateLabel.text = self.simplifiedPost.createdAt.timeAgoSinceNow
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.dateLabelTapped(_:)))
        self.postDetailHeaderView.dateLabel.addGestureRecognizer(tap)
    }
    
    fileprivate func prepareVideoViews() {
        let videoHeight = self.postDetailHeaderView.videoContainerViewHeightConstraint.constant
        if let video = self.simplifiedPost.video {
            var newFrame = self.postDetailHeaderView.frame
            newFrame.size.height = newFrame.size.height - videoHeight + Constants.videoViewHeight
            self.postDetailHeaderView.frame = newFrame
            self.tableView.tableHeaderView = self.postDetailHeaderView
            self.postDetailHeaderView.videoContainerViewHeightConstraint.constant = Constants.videoViewHeight
            self.postDetailHeaderView.videoContainerView.backgroundColor = AppColor.orange
            if let videoImageURL = video.imageURL,
                let URL = URL(string: videoImageURL) {
                DispatchQueue.global().async {
                    UIImage.contentsOfURL(url: URL, completion: { (image, error) in
                        DispatchQueue.main.async(execute: {
                            self.postDetailHeaderView.videoImageView.image = image
                        })
                    })
                }
            }
        }
        else {
            var newFrame = self.postDetailHeaderView.frame
            newFrame.size.height = newFrame.size.height - videoHeight
            self.postDetailHeaderView.frame = newFrame
            self.tableView.tableHeaderView = self.postDetailHeaderView
            self.postDetailHeaderView.videoContainerViewHeightConstraint.constant = 0
        }
    }
    
    fileprivate func renderMapView(withLocation location: CLLocation) {
        let span = MKCoordinateSpanMake(0.05, 0.05)
        let region = MKCoordinateRegion(center: location.coordinate, span: span)
        self.postDetailHeaderView.mapView.setRegion(region, animated: true)
        
        let annotation = MKPointAnnotation()
        annotation.coordinate = location.coordinate
        
        let geoCoder = CLGeocoder()
        geoCoder.reverseGeocodeLocation(location, completionHandler: { (placemarks, error) in
            if let placemark = placemarks?.first {
                if let locationName = placemark.addressDictionary!["Name"] as? NSString {
                    annotation.title = locationName as String
                }
                if let street = placemark.addressDictionary!["Country"] as? NSString{
                    if let _ = placemark.addressDictionary!["ZIP"] as? NSString {
                        annotation.subtitle = street as String
                    }
                }
                self.postDetailHeaderView.mapView.addAnnotation(annotation)
            }
        })
    }
    
    fileprivate func prepareMapView() {
        let mapHeight = self.postDetailHeaderView.mapViewHeightConstraint.constant
        if let _ = self.simplifiedPost.location {
            var newFrame = self.postDetailHeaderView.frame
            newFrame.size.height = newFrame.size.height - mapHeight + Constants.mapViewHeight
            self.postDetailHeaderView.frame = newFrame
            self.postDetailHeaderView.mapViewHeightConstraint.constant = Constants.mapViewHeight
            self.tableView.tableHeaderView = self.postDetailHeaderView
            self.postDetailHeaderView.mapView.delegate = self
        }
        else {
            let callButtonMapViewGap = self.postDetailHeaderView.callButtonBottomConstraint.constant
            var newFrame = self.postDetailHeaderView.frame
            newFrame.size.height = newFrame.size.height - mapHeight + fabs(callButtonMapViewGap) + Constants.gapBetweenCallButtonAndMapView
            self.postDetailHeaderView.frame = newFrame
            self.postDetailHeaderView.callButtonBottomConstraint.constant = Constants.gapBetweenCallButtonAndMapView
            self.postDetailHeaderView.mapViewHeightConstraint.constant = 0
            self.tableView.tableHeaderView = self.postDetailHeaderView
            self.postDetailHeaderView.mapView.delegate = nil
        }
    }
    
    fileprivate func renderDescription(withContent content: String) {
        var html = "<html><head><title></title></head><body style=\"background:transparent;\">"
        html += "<style>"
        html += "* { max-width: 100% !important; };"
        html += "body { margin: 0; padding: 0; };"
        html += "</style>"
        html += "<div style=\"font-family: Lato; font-size: 15px; color: #373230;\">"
        html += content.replace("\n", withString: "<br/>")
        html += "</div>"
        html += "</body></html>"
        self.postDetailHeaderView.descWebView.loadHTMLString(html, baseURL: nil)
    }
    
    fileprivate func prepareDescriptionWebView() {
        self.postDetailHeaderView.descWebView.scrollView.isScrollEnabled = false
        self.postDetailHeaderView.descWebView.scrollView.contentInset = UIEdgeInsets.zero
        self.postDetailHeaderView.descWebView.delegate = self
        self.postDetailHeaderView.descWebView.alpha = 0
        self.postDetailHeaderView.descWebView.backgroundColor = AppColor.white
    }
    
    fileprivate func preparePostDetailHeaderView() {
        self.postDetailHeaderView = PostDetailHeaderView.fromNib()
        self.postDetailHeaderView.delegate = self
        self.tableView.tableHeaderView = self.postDetailHeaderView
        
        // TODO: Refactoring later.
        self.postDetailHeaderView.boostView.isHidden = simplifiedPost.ownerId != DataManager.shared.currentUser?.id
    }
    
    fileprivate func compressPostDetailHeaderViewFrame() {
        self.postDetailHeaderView.setNeedsLayout()
        self.postDetailHeaderView.layoutIfNeeded()
        
        var frame = self.postDetailHeaderView.frame
        let height = self.postDetailHeaderView.systemLayoutSizeFitting(UILayoutFittingCompressedSize).height
        frame.size.height = height
        self.postDetailHeaderView.frame = frame
        self.tableView.tableHeaderView = self.postDetailHeaderView
    }
    
    fileprivate func compressPostDetailFooterViewFrame() {
        self.postDetailFooterView.setNeedsLayout()
        self.postDetailFooterView.layoutIfNeeded()
        
        var frame = self.postDetailFooterView.frame
        let collectionViewSize = self.postDetailFooterView.collectionView.collectionViewLayout.collectionViewContentSize
        let height = self.postDetailFooterView.systemLayoutSizeFitting(UILayoutFittingCompressedSize).height + collectionViewSize.height
        frame.size.height = height
        self.postDetailFooterView.frame = frame
        self.tableView.tableFooterView = self.postDetailFooterView
    }
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        let viewController = RootViewController()
        viewController.simplifiedPosts = relatedPosts
        viewController.selectedPost = post
        navigationController?.show(viewController, sender: nil)
    }
}

// MARK: - UIWebViewDelegate
extension PostDetailViewController: UIWebViewDelegate {
    func webViewDidFinishLoad(_ webView: UIWebView) {
        UIView.animate(withDuration: Constants.animationDuration, animations: {
            self.postDetailHeaderView.webViewLoadingIndicator.stopAnimating()
            self.postDetailHeaderView.webViewLoadingIndicatorHeightConstraint.constant = 0
            self.postDetailHeaderView.webViewLoadingIndicatorBottomConstraint.constant = 0
            self.postDetailHeaderView.descWebView.alpha = 1
        })
        
        compressPostDetailHeaderViewFrame()
    }
    
    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        if navigationType == .linkClicked {
            guard let URL = request.url else { return true }
            let webViewViewController = WebViewViewController(nibName: String(describing: WebViewViewController.self), bundle: nil)
            webViewViewController.url = URL
            self.navigationController?.show(webViewViewController, sender: nil)
            return false
        }
        else {
            return true
        }
    }
}

// MARK: - MKMapViewDelegate

extension PostDetailViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        // Don't want to show a custom image if the annotation is the user's location.
        guard !annotation.isKind(of: MKUserLocation.self) else {
            return nil
        }
        
        // Better to make this class property
        let annotationIdentifier = "AnnotationIdentifier"
        
        var annotationView: MKAnnotationView?
        if let dequeuedAnnotationView = mapView.dequeueReusableAnnotationView(withIdentifier: annotationIdentifier) {
            annotationView = dequeuedAnnotationView
            annotationView?.annotation = annotation
        }
        else {
            let av = MKAnnotationView(annotation: annotation, reuseIdentifier: annotationIdentifier)
            av.rightCalloutAccessoryView = UIButton(type: .detailDisclosure)
            annotationView = av
        }
        
        if let annotationView = annotationView {
            // Configure your annotation view here
            annotationView.canShowCallout = true
            annotationView.image = UIImage(named: "pin-map")
        }
        
        return annotationView
    }
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        guard let annotation = view.annotation else { return }
        
        let regionDistance: CLLocationDistance = 10000
        let regionSpan = MKCoordinateRegionMakeWithDistance(annotation.coordinate, regionDistance, regionDistance)
        let options = [
            MKLaunchOptionsMapCenterKey: NSValue(mkCoordinate: regionSpan.center),
            MKLaunchOptionsMapSpanKey: NSValue(mkCoordinateSpan: regionSpan.span)
        ]
        
        let placemark = MKPlacemark(coordinate: annotation.coordinate, addressDictionary: nil)
        let mapItem = MKMapItem(placemark: placemark)
        mapItem.name = self.simplifiedPost.name
        mapItem.openInMaps(launchOptions: options)
    }
}

// MARK: - convenience init
extension PostDetailViewController {
    convenience init() {
        self.init(nibName: "PostDetailViewController", bundle: nil)
    }
}

// MARK: - MFMailComposeViewControllerDelegate
extension PostDetailViewController: MFMailComposeViewControllerDelegate {
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        var message: String?
        switch result {
        case MFMailComposeResult.cancelled:
            message = "Mail have been cancelled"
        case MFMailComposeResult.saved:
            message = "Mail have been saved"
        case MFMailComposeResult.sent:
            message = "Mail have been sent"
        case MFMailComposeResult.failed:
            message = "Mail have been sent fail"
        }
        
        if let message = message {
            self.showOkeyMessage("Alert!".localized(), message: message)
        }
        self.dismiss(animated: true, completion: nil)
    }
}

// MARK: - UIPopoverPresentationControllerDelegate
extension PostDetailViewController: UIPopoverPresentationControllerDelegate {
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle {
        return .none
    }
}


// MARK: - UITableViewDataSource
extension PostDetailViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.comments.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CommentContentTableViewCell.identifier,
                                                 for: indexPath) as! CommentContentTableViewCell
        let comment = self.comments[indexPath.row]
        cell.selectionStyle = .none
        cell.render(userAvatarURL: comment.userAvatarURL,
                    userDisplayName: comment.userDisplayName,
                    createdAt: comment.createdAt,
                    commentContent: comment.content,
                    numberOfFavorites: comment.numberOfFavorites,
                    actionsTitle: "Actions",
                    indentationLevel: 0,
                    isLiked: false)
        cell.setHiddenBorderBottomView(comment == self.comments.last)
        
        return cell
    }
}

// MARK: - UITableViewDelegate
extension PostDetailViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
    }
}


// MARK: - UICollectionViewDataSource
extension PostDetailViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == self.postDetailFooterView.collectionView {
            let numberOfRelatedPosts = self.relatedPosts.count
            return numberOfRelatedPosts > Constants.maximumNumberOfRelatedPosts ? Constants.maximumNumberOfRelatedPosts : numberOfRelatedPosts
        }
        else if collectionView == self.postDetailHeaderView.likedCollectionView {
            return self.likedUserImages.count
        }
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == self.postDetailFooterView.collectionView {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AdCollectionViewCell.identifier,
                                                          for: indexPath) as! AdCollectionViewCell
            let post = self.relatedPosts[indexPath.row]
            cell.render(withPost: post)
            
            return cell
        }
        else if collectionView == self.postDetailHeaderView.likedCollectionView {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UserImageCollectionViewCell.identifier,
                                                          for: indexPath) as! UserImageCollectionViewCell
            cell.imageView.image = AppIcons.userIcon
            cell.moreLabel.isHidden = indexPath.item < likedUserImages.count - 1
            
            return cell
        }
        return UICollectionViewCell()
    }
}

// MARK: PostDetailHeaderViewDelegate
extension PostDetailViewController: PostDetailHeaderViewDelegate {
   
    func boostButtonTapped(sender: Any) {

    }
    
    func likeButtonTapped(sender: Any) {
        
    }
    
    func viewsButtonTapped(sender: Any) {
        
    }
    
    func followButtonTapped(sender: Any) {
        
    }
    
    func phoneButtonTapped(sender: Any) {
        guard let phone = self.postDetailHeaderView.callButton.currentTitle,
            let url = URL(string:"tel://" + phone)
            else { return }
        UIApplication.shared.openURL(url)
    }
    
    func messageButtonTapped(sender: Any) {
        
    }
    
    func categoryButtonTapped(sender: Any) {
        
    }
    
    func playVideoButtonTapped(sender: Any) {
        guard let videoURL = self.simplifiedPost.video?.videoURL,
            let URL = URL(string: videoURL)
            else { return }
        let playerVC = MobilePlayerViewController(contentURL: URL)
        playerVC.title = self.simplifiedPost.name
        playerVC.activityItems = [videoURL]
        present(playerVC, animated: true, completion: nil)
    }
    
    func viewCommentsButtonTapped(sender: Any) {
        let viewController = CommentsCollectionViewController(commentsRequest: nil, nextCommentsRequest: nil, totalComments: nil)
        viewController.comments = self.comments
        self.show(viewController, sender: nil)
    }
    
    func commentButtonTapped(sender: Any) {
        self.commentTextField.becomeFirstResponder()
    }
    
    func favoriteButtonTapped(sender: Any) {
        
    }
}

// MARK: - CHTCollectionViewDelegateWaterfallLayout, UICollectionViewDelegate
extension PostDetailViewController: CHTCollectionViewDelegateWaterfallLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: NSInteger) -> CGFloat {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: NSInteger) -> UIEdgeInsets {
        return UIEdgeInsets.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == self.postDetailFooterView.collectionView {
            let post = self.relatedPosts[indexPath.item]
            let size = AdCollectionViewCell.dymamicSizeForCell(forPost: post,
                                                               inCollectionView: collectionView,
                                                               withColumns: DataManager.shared.viewType.numberOfColumns)
            return size
        }
        else if collectionView == self.postDetailHeaderView.likedCollectionView {
            return CGSize(width: Constants.userLikedImageViewWidth, height: Constants.userLikedImageViewWidth)
        }
        
        return CGSize.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == self.postDetailFooterView.collectionView {
            let post = self.relatedPosts[indexPath.item]
            self.presentPostDetailViewControllers(selectedPost: post)
        }
    }
}
