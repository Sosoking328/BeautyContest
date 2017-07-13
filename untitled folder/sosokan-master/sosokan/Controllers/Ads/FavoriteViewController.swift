//
//  FavoriteViewController.swift
//  sosokan
//
//  Created by An Phan on 6/25/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Localize_Swift
import Then
import UIScrollView_InfiniteScroll
import CHTCollectionViewWaterfallLayout
import FBSDKShareKit
import SwiftyJSON
import DZNEmptyDataSet

class FavoriteViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    var isFavorites = false
    
    fileprivate struct Constants {
        fileprivate struct ReuseIdentifiers {
            static let followUserCollectionViewCell = String(describing: FollowUserCollectionViewCell.self)
            static let postCollectionViewCell = String(describing: AdCollectionViewCell.self)
        }
    }
    
    var userId: Int!
    fileprivate var posts = DataManager.shared.favoritedPosts
    fileprivate var isFetchingPosts: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let leftBarButton = UIBarButtonItem(image: AppIcons.leftBarMenuIcon,
                                            style: .plain,
                                            target: self,
                                            action: #selector(handleLeftButton))
        navigationItem.leftBarButtonItem = leftBarButton
        
        prepareCollectionView()
        
        showNetworkIndicator()
        isFetchingPosts = false
        if let userId = User.currentUser()?.id {
            if isFavorites {
                PostService.shared.fetchFavoritedPostsForUser(userId: userId, completionHandler: { (json, error) in
                    self.hideNetworkIndicator()
                    self.isFetchingPosts = true
                    if let error = error {
                        debugPrint(error)
                    }
                    else {
                        self.posts = DataManager.shared.favoritedPosts
                        self.collectionView.reloadData()
                    }
                })
            }
            else {
                PostService.shared.fetchSimplifiedPostsByUser(userId: userId, onSuccess: { (posts, next, total) in
                    self.hideNetworkIndicator()
                    self.isFetchingPosts = true
                    self.posts = posts ?? []
                    self.collectionView.reloadData()
                }, onError: { (error) in
                    self.hideNetworkIndicator()
                    self.isFetchingPosts = true
                    debugPrint(error)
                })
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = isFavorites ? "FAVORITES".localized() : "MY POSTS".localized()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Methods
    
    func handleLeftButton() {
        AppDelegate.shared().toggleLeftDrawer(self, animated: true)
    }
    
    // MARK: Private method
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        let viewController = RootViewController()
        viewController.simplifiedPosts = posts
        viewController.selectedPost = post
        navigationController?.show(viewController, sender: nil)
    }
    
    fileprivate func prepareCollectionView() {
        let nib = UINib(nibName: Constants.ReuseIdentifiers.followUserCollectionViewCell, bundle: nil)
        self.collectionView.register(nib, forCellWithReuseIdentifier: Constants.ReuseIdentifiers.followUserCollectionViewCell)
        let adNib = UINib(nibName: AdCollectionViewCell.identifier, bundle: nil)
        self.collectionView.register(adNib, forCellWithReuseIdentifier: AdCollectionViewCell.identifier)
        
        self.collectionView.backgroundColor = AppColor.white
        self.collectionView.dataSource = self
        self.collectionView.delegate = self
        self.collectionView.keyboardDismissMode = .onDrag
        
        let layout = CHTCollectionViewWaterfallLayout()
        layout.itemRenderDirection = .leftToRight
        layout.minimumColumnSpacing = 0
        layout.minimumInteritemSpacing = 0
        layout.columnCount = 2
        self.collectionView.collectionViewLayout = layout
        
        self.collectionView.emptyDataSetSource = self
        self.collectionView.emptyDataSetDelegate = self
    }
}

extension FavoriteViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return posts.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.ReuseIdentifiers.postCollectionViewCell,
                                                      for: indexPath) as! AdCollectionViewCell
        let post = self.posts[indexPath.row]
        cell.render(withPost: post)
        
        return cell
    }
}

// MARK: CHTCollectionViewDelegateWaterfallLayout
extension FavoriteViewController: UICollectionViewDelegate {
    /*
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        let width = collectionView.frame.width / 3
        let height = width * 3.8 / 3
        return CGSize(width: width, height: height)
    }
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        let profile = self.userProfiles[indexPath.item]
        let viewController = UserProfileViewController.init(profileId: profile.id)
        self.navigationController?.showViewController(viewController, sender: nil)
    }
     */
}

// MARK: Convenience init
extension FavoriteViewController {
    convenience init(userId: Int) {
        let nibName = String(describing: FavoriteViewController.self)
        self.init(nibName: nibName, bundle: nil)
        self.userId = userId
    }
}

// MARK: - DZNEmptyDataSetSource

extension FavoriteViewController: DZNEmptyDataSetSource {
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

extension FavoriteViewController: DZNEmptyDataSetDelegate {
    func emptyDataSetShouldAllowTouch(_ scrollView: UIScrollView!) -> Bool {
        return true
    }
    
    func emptyDataSetShouldAllowScroll(_ scrollView: UIScrollView!) -> Bool {
        return true
    }
    
    func emptyDataSet(_ scrollView: UIScrollView!, didTap button: UIButton!) {
        
    }
}

// MARK: - CHTCollectionViewDelegateWaterfallLayout, UICollectionViewDelegate

extension FavoriteViewController: CHTCollectionViewDelegateWaterfallLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: NSInteger) -> UIEdgeInsets {
        return UIEdgeInsets.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == self.collectionView {
            let post = self.posts[indexPath.item]
            let size = AdCollectionViewCell.dymamicSizeForCell(forPost: post,
                                                               inCollectionView: collectionView,
                                                               withColumns: 2)
            return size
        }
        
        return CGSize.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let post = self.posts[indexPath.item]
        self.presentPostDetailViewControllers(selectedPost: post)
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint,
                                   targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        if scrollView.contentOffset.y < 0 { return }
        if targetContentOffset.pointee.y < scrollView.contentOffset.y {
            print("Going up!")
            NotificationCenter.default.post(Notification(name: Notification.Name(rawValue: Notifications.scrollUp), object: nil))
        } else {
            print("Going down!")
            NotificationCenter.default.post(Notification(name: Notification.Name(rawValue: Notifications.scrollDown), object: nil))
        }
    }
}
