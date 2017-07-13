//
//  HomeViewController.swift
//  sosokan
//
//  Created by An Phan on 5/25/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import STPopup

class HomeViewController: UIViewController {

    @IBOutlet weak var postCollectionView: UICollectionView!
    @IBOutlet weak var cameraButton: UIButton!
    @IBOutlet weak var bottomBarContainerView: UIView!
    @IBOutlet weak var bottomBarContainerViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var searchButton: UIButton!
    
    fileprivate let adHomeCellID = String(describing: AdHomeCollectionViewCell.self)
    fileprivate var nearbyPosts = DataManager.shared.posts
    fileprivate var bottomBarView: BottomBarView!
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        prepareSearchButton()
        prepareLocationBarButton()
        prepareBottomBarView()
        postCollectionView.register(UINib(nibName: adHomeCellID, bundle: nil),
                                    forCellWithReuseIdentifier: adHomeCellID)
        
        if DataManager.shared.posts.count == 0 {
            showHUD()
        }
        
        // Fetching list of nearby posts
        PostService.shared.fetchSimplifiedPostsByCategoryId(nil, location: DataManager.shared.currentLocation,
                                                          limit: 50, offset: 0, onSuccess: { (posts, next, total) in
            self.dismissHUD()
            self.nearbyPosts = posts ?? []
            self.postCollectionView.reloadData()
            
        }) { (error) in
            self.dismissHUD()
            debugPrint(error)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
//        prepareWhiteNavigationBar()
        searchButton.setTitle("Search on Sosokan".localized(), for: .normal)
        
        postCollectionView.reloadData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }    

    // MARK: - IBActions
    
    @IBAction func leftMenuButtonTapped(_ sender: Any) {
        AppDelegate.shared().toggleLeftDrawer(self, animated: true)
    }
    
    @IBAction func camButtonTapped(_ sender: Any) {
        let mediaOptionNC = StoryBoardManager.viewController("Post", viewControllerName: "MediaOptionNC")
        present(mediaOptionNC, animated: true, completion: nil)
    }
    
    // MARK: - Private methods
    
    fileprivate func prepareSearchButton() {
        searchButton.layer.cornerRadius = 3.0
        searchButton.layer.borderColor = AppColor.white.cgColor
        searchButton.layer.borderWidth = 1.0
    }
    
    fileprivate func prepareLocationBarButton() {
        let locationButton = UIBarButtonItem(image: AppIcons.locationBarIcon,
                                             style: .plain,
                                             target: self,
                                             action: #selector(locationButtonTapped))
        locationButton.imageInsets = UIEdgeInsetsMake(0, -35, 0, 0)
        var barButtons = self.navigationItem.leftBarButtonItems ?? []
        barButtons.append(locationButton)
        
        self.navigationItem.leftBarButtonItems = barButtons
    }
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        let viewController = RootViewController()
        viewController.simplifiedPosts = self.nearbyPosts
        viewController.selectedPost = post
        navigationController?.show(viewController, sender: nil)
    }
    
    fileprivate func prepareBottomBarView() {
        bottomBarView = BottomBarView.fromNib()
        bottomBarView.frame = CGRect(x: 0, y: 0,
                                     width: self.bottomBarContainerView.frame.width,
                                     height: self.bottomBarContainerView.frame.height)
        bottomBarView.addTopBorderWithColor(AppColor.border, width: 1)
        bottomBarContainerView.addSubview(self.bottomBarView)
        
        bottomBarView.browseButtonAction = {    // Open categories gird page
            let categoryVC = SelectCategoriesViewController(selectedCategories: [])
            let categoryNC = UINavigationController(rootViewController: categoryVC)
            self.present(categoryNC, animated: true, completion: nil)
        }
        bottomBarView.profileButtonAction = {
            let profileViewController = StoryBoardManager.viewController("Profile",
                                                                         viewControllerName: "ProfileViewControllerNav")
            AppDelegate.shared().centerViewController = profileViewController
        }
    }
    
    // MARK: Methods
    
    func locationButtonTapped() {
        let locationPopupViewController = LocationViewController(didChangeFilterOptions: didChangeFilterOptions)
        
        let popupController = STPopupController(rootViewController: locationPopupViewController)
        popupController.containerView.layer.cornerRadius = 5
        popupController.backgroundView?.backgroundColor = AppColor.popupBackgroundColor
        
        let popupBackgroundTapGesture = UITapGestureRecognizer(target: self, action: #selector(popupBackgroundDidTouch(sender:)))
        popupController.backgroundView?.addGestureRecognizer(popupBackgroundTapGesture)
        
        popupController.present(in: self)
    }
    
    func popupBackgroundDidTouch(sender: Any?) {
        self.popupController?.dismiss()
    }
    
    func didChangeFilterOptions(_ filterOptions: FilterOptions) {
        DataManager.shared.filterOptions = filterOptions
        
        //TODO: handle filter options change - make requests
    }
}

extension HomeViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return nearbyPosts.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AdHomeCollectionViewCell.identifier,
                                                      for: indexPath) as! AdHomeCollectionViewCell
        let post = nearbyPosts[indexPath.row]
        cell.renderAd(post: post)
        
        return cell
    }
}

// MARK: - CHTCollectionViewDelegateWaterfallLayout, UICollectionViewDelegate

extension HomeViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let size = (collectionView.frame.size.width - 10) / 3
        
        return CGSize(width: size, height: size)
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        referenceSizeForHeaderInSection section: Int) -> CGSize {
        var size = (collectionView.collectionViewLayout as! UICollectionViewFlowLayout).headerReferenceSize
        
        if DataManager.shared.recentSearches.count == 0 {
            size.height -= 125.0
        }
        else {
            size.height = 850.0
        }
        
        return size
    }
}

extension HomeViewController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let post = self.nearbyPosts[indexPath.item]
        presentPostDetailViewControllers(selectedPost: post)
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint,
                                   targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        if scrollView.contentOffset.y < 0 { return }
        if targetContentOffset.pointee.y < scrollView.contentOffset.y {
            UIView.animate(withDuration: 0.5, animations: {
                self.bottomBarContainerViewBottomConstraint.constant = 0
                self.view.setNeedsLayout()
                self.view.layoutIfNeeded()
            })
        }
        else {
            UIView.animate(withDuration: 0.5, animations: {
                self.bottomBarContainerViewBottomConstraint.constant = -50.0
                self.view.setNeedsLayout()
                self.view.layoutIfNeeded()
            })
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionElementKindSectionHeader {
            let homeHeaderView = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionElementKindSectionHeader,
                                                                                 withReuseIdentifier: "homeHeaderView",
                                                                                 for: indexPath) as! HomeHeaderView
            homeHeaderView.reload()
            
            homeHeaderView.didSelectedCategory = {category in
                let pageVC = PaginationViewController(filterOptions: DataManager.shared.filterOptions,
                                                      viewType: DataManager.shared.viewType,
                                                      mainCategory: category)
                self.navigationController?.pushViewController(pageVC, animated: true)
            }
            homeHeaderView.didSelectedPost = { post in
                self.presentPostDetailViewControllers(selectedPost: post)
            }
            homeHeaderView.didSelectedBanner = { url in
                let webViewVC = WebViewViewController(nibName: String(describing: WebViewViewController.self),
                                                      bundle: nil)
                webViewVC.url = url
                self.navigationController?.show(webViewVC, sender: nil)
            }
            homeHeaderView.didSelectRecentlySearches = { recentlySearches in
                debugPrint(recentlySearches)
                let postsVC = PostsCollectionViewController(posts: recentlySearches,
                                                            viewType: DataManager.shared.viewType,
                                                            parentNavigationController: self.navigationController)
                postsVC.title = "Recently Searches"
                self.navigationController?.pushViewController(postsVC, animated: true)
            }
            homeHeaderView.didSelectFeaturedItems = { featuredItems in
                debugPrint(featuredItems)
                let postsVC = PostsCollectionViewController(posts: featuredItems,
                                                            viewType: DataManager.shared.viewType,
                                                            parentNavigationController: self.navigationController)
                postsVC.title = "Featured Items"
                self.navigationController?.pushViewController(postsVC, animated: true)
            }
            homeHeaderView.didSelectAddCategoryItem = {
                let viewController = FeatureCategoriesController()
                self.navigationController?.show(viewController, sender: nil)
            }
            
            return homeHeaderView
        }
        
        return UICollectionReusableView()
    }
}
