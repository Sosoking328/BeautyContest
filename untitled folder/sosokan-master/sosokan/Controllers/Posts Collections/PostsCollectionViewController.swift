//
//  PostsCollectionViewController.swift
//  sosokan
//

import UIKit
import SwiftyJSON
import DZNEmptyDataSet
import CHTCollectionViewWaterfallLayout

class PostsCollectionViewController: UIViewController {
    
    @IBOutlet weak var postsCollectionView: UICollectionView!
    
    var parentNavigationController: UINavigationController?
    
    var category: Category?
    var viewType: ViewType!
    var simplifiedPosts: [SimplifiedPost] = []
    
    fileprivate var isFetchingPosts: Bool = false
    fileprivate var postsPerPage: Int = 50
    fileprivate var currentPostsOffset: Int = 0
    
    fileprivate struct Constants {
        struct ReuseIdentifier {
            static let postCollectionViewCell = "AdCollectionViewCell"
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        preparePostsCollectionView()
        
        fetchPosts()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Method
    
    func changePostsCollectionViewLayout(_ viewType: ViewType) {
        self.viewType = viewType
        if let layout = self.postsCollectionView?.collectionViewLayout as? CHTCollectionViewWaterfallLayout {
            layout.columnCount = viewType.numberOfColumns
            self.postsCollectionView.reloadData()
        }
    }
    
    // MARK: Private method
    
    fileprivate func fetchPosts() {
        guard let category = category else { return }
        self.isFetchingPosts = true
        self.showNetworkIndicator()
        PostService.shared.fetchSimplifiedPostsByCategoryId(category.id, limit: postsPerPage, offset: currentPostsOffset, onSuccess: { (simplifiedPosts, _, _) in
            self.isFetchingPosts = false
            self.hideNetworkIndicator()
            self.simplifiedPosts = simplifiedPosts ?? []
            self.postsCollectionView.reloadData()
        }, onError: { (error) in
            self.isFetchingPosts = false
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    fileprivate func removePosts(_ animated: Bool) {
        if self.simplifiedPosts.isEmpty {
            return self.postsCollectionView.reloadData()
        }
        let numberOfPosts = self.simplifiedPosts.count
        self.simplifiedPosts.removeAll()
        if animated {
            let indexPaths = (0 ..< numberOfPosts).map({ IndexPath.init(item: $0, section: 0) })
            self.postsCollectionView.deleteItems(at: indexPaths)
        }
        else {
            self.postsCollectionView.reloadData()
        }
    }
    
    fileprivate func preparePostsCollectionView() {
        let nib = UINib(nibName: Constants.ReuseIdentifier.postCollectionViewCell, bundle: nil)
        self.postsCollectionView.register(nib, forCellWithReuseIdentifier: Constants.ReuseIdentifier.postCollectionViewCell)
        
        let layout = CHTCollectionViewWaterfallLayout()
        layout.itemRenderDirection = .leftToRight
        layout.minimumColumnSpacing = 0
        layout.minimumInteritemSpacing = 0
        layout.columnCount = self.viewType.numberOfColumns
        self.postsCollectionView.collectionViewLayout = layout
 
        self.postsCollectionView.backgroundColor = AppColor.white
        
        self.postsCollectionView.dataSource = self
        self.postsCollectionView.delegate = self
        
        self.postsCollectionView.emptyDataSetSource = self
        self.postsCollectionView.emptyDataSetDelegate = self
        
        // MARK: Load more posts
        /*
        self.postsCollectionView.infiniteScrollIndicatorStyle = .gray
        self.postsCollectionView.addInfiniteScroll(handler: { [weak self] collectionView in
            guard let `self` = self,
                let nextRequest = self.nextRequest,
                let nextRequestURL = URL(string: nextRequest)
                else { return collectionView.finishInfiniteScroll() }
            
            self.isFetchingPosts = true
            self.showNetworkIndicator()
            PostService.shared.fetchSimplifiedPosts(endPoint: nextRequest, onSuccess: { (simplifedPosts, nextRequest, totalPosts) in
                self.isFetchingPosts = false
                self.hideNetworkIndicator()
                self.nextRequest = nextRequest
                if let simplifedPosts = simplifedPosts, !simplifedPosts.isEmpty {
                    let max = self.simplifiedPosts.count
                    self.simplifiedPosts.append(contentsOf: simplifedPosts)
                    let indexPaths = (max ..< max + simplifedPosts.count).map({ IndexPath(row: $0, section: 0) })
                    self.postsCollectionView.insertItems(at: indexPaths)
                }
                collectionView.finishInfiniteScroll()
            }, onError: { (error) in
                self.isFetchingPosts = false
                self.hideNetworkIndicator()
                self.showErrorMessage(Messages.occurredError)
                collectionView.finishInfiniteScroll()
            })
            /*
            SimplifiedPost.fetchPosts(request: nextRequest, onCompleted: { [weak self] (results, error) in
                guard let `self` = self else { return }
                self.isFetchingPosts = false
                self.hideNetworkIndicator()
                if let error = error {
                    debugPrint(error)
                    self.showErrorMessage(Messages.occurredError)
                }
                else if let results = results {
                    self.nextRequest = results.nextRequest
                    if !results.posts.isEmpty {
                        let max = self.simplifiedPosts.count
                        self.simplifiedPosts.appendContentsOf(results.posts)
                        let indexPaths = (max ..< max + results.posts.count).map({ NSIndexPath(forRow: $0, inSection: 0) })
                        self.postsCollectionView.insertItemsAtIndexPaths(indexPaths)
                    }
                }
                collectionView.finishInfiniteScroll()
            })
             */
        })
         */
    }
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        let viewController = RootViewController()
        viewController.simplifiedPosts = self.simplifiedPosts
        viewController.selectedPost = post
        let navigation = self.parentNavigationController ?? self.navigationController
        navigation?.show(viewController, sender: nil)
    }
}

// MARK: - UICollectionViewDataSource
extension PostsCollectionViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == self.postsCollectionView {
            return self.simplifiedPosts.count
        }
        
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.ReuseIdentifier.postCollectionViewCell,
                                                      for: indexPath) as! AdCollectionViewCell
        let post = self.simplifiedPosts[indexPath.row]
        cell.render(withPost: post)
        
        // Handle favorite/unfavorite
        cell.likeButtonDidTouch = { sender in
            PostService.shared.likeUnlikePost(withPostId: post.id, isLiked: !post.isLiked, completionHandler: { (json, error) in
                if let error = error {
                    debugPrint(error)
                }
                else {
                    debugPrint(json!)
                }
            })
        }
        
        return cell
    }
}

// MARK: - CHTCollectionViewDelegateWaterfallLayout, UICollectionViewDelegate

extension PostsCollectionViewController: CHTCollectionViewDelegateWaterfallLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: NSInteger) -> UIEdgeInsets {
        return UIEdgeInsets.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == self.postsCollectionView {
            let post = self.simplifiedPosts[indexPath.item]
            let size = AdCollectionViewCell.dymamicSizeForCell(forPost: post,
                                                               inCollectionView: collectionView,
                                                               withColumns: self.viewType.numberOfColumns)
            return size
        }
        
        return CGSize.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let post = self.simplifiedPosts[indexPath.item]
        self.presentPostDetailViewControllers(selectedPost: post)
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint,
                                   targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        if scrollView.contentOffset.y < 0 { return }
        if targetContentOffset.pointee.y < scrollView.contentOffset.y {
            print("Going up!")
            NotificationCenter.default.post(name: NSNotification.Name(Notifications.scrollUp), object: nil)
        } else {
            print("Going down!")
            NotificationCenter.default.post(name: Notification.Name(Notifications.scrollDown), object: nil)
        }
    }
}

// MARK: - DZNEmptyDataSetSource
extension PostsCollectionViewController: DZNEmptyDataSetSource {
    func image(forEmptyDataSet scrollView: UIScrollView!) -> UIImage! {
        return AppIcons.locationBarIcon
    }
    
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString! {
        let attributes = [
            NSFontAttributeName: UIFont.latoBold(size: AppFontsizes.veryBig),
            NSForegroundColorAttributeName: AppColor.textSecondary
        ]
        if self.isFetchingPosts {
            return NSAttributedString(string: "Finding ads", attributes: attributes)
        }
        return NSAttributedString(string: "Nothing here", attributes: attributes)
    }
    
    func backgroundColor(forEmptyDataSet scrollView: UIScrollView!) -> UIColor! {
        return AppColor.white
    }
}

// MARK: - DZNEmptyDataSetDelegate
extension PostsCollectionViewController: DZNEmptyDataSetDelegate {
    func emptyDataSetShouldAllowTouch(_ scrollView: UIScrollView!) -> Bool {
        return true
    }
    
    func emptyDataSetShouldAllowScroll(_ scrollView: UIScrollView!) -> Bool {
        return true
    }
    
    func emptyDataSet(_ scrollView: UIScrollView!, didTap button: UIButton!) {
        
    }
}

// MARK: Convenience init
extension PostsCollectionViewController {
    convenience init(category: Category,
                     viewType: ViewType,
                     parentNavigationController: UINavigationController?) {
        self.init(nibName: "PostsCollectionViewController", bundle: nil)
        self.category = category
        self.viewType = viewType
        self.parentNavigationController = parentNavigationController
    }
    
    convenience init(posts: [SimplifiedPost],
                     viewType: ViewType,
                     parentNavigationController: UINavigationController?) {
        self.init(nibName: "PostsCollectionViewController", bundle: nil)
        self.simplifiedPosts = posts
        self.viewType = viewType
        self.parentNavigationController = parentNavigationController
    }
}
