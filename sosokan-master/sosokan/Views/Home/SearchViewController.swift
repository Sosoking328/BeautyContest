//
//  SearchViewController.swift
//  sosokan
//
//  Created by An Phan on 6/5/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import CHTCollectionViewWaterfallLayout

class SearchViewController: UIViewController {

    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var overlayView: UIView!
    
    var posts = [SimplifiedPost]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        searchBar.becomeFirstResponder()
        searchBar.placeholder = "Search on Sosokan".localized()
        
        prepareCollectionView()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        searchBar.resignFirstResponder()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    fileprivate func prepareCollectionView() {
        let nib = UINib(nibName: AdCollectionViewCell.identifier, bundle: nil)
        collectionView.register(nib, forCellWithReuseIdentifier: AdCollectionViewCell.identifier)
        
        let layout = CHTCollectionViewWaterfallLayout()
        layout.itemRenderDirection = .leftToRight
        layout.minimumColumnSpacing = 0
        layout.minimumInteritemSpacing = 0
        layout.columnCount = 3
        collectionView.collectionViewLayout = layout
        
        collectionView.backgroundColor = AppColor.white
    }

    // MARK: - IBActions
    
    @IBAction func overlayViewTapped(_ sender: Any) {
        searchBar.resignFirstResponder()
        setOverlayViewHidden(isHide: true)
    }

    @IBAction func backButtonTapped(_ sender: Any) {
        _ = navigationController?.popViewController(animated: true)
    }
    
    // MARK: - Private methods
    
    fileprivate func presentPostDetailViewControllers(selectedPost post: SimplifiedPost) {
        let viewController = RootViewController()
        viewController.simplifiedPosts = posts
        viewController.selectedPost = post
        navigationController?.show(viewController, sender: nil)
    }
    
    fileprivate func setOverlayViewHidden(isHide: Bool) {
        if isHide {
            self.overlayView.isHidden = false
            UIView.animate(withDuration: 0.3, animations: { 
                self.overlayView.alpha = 0.0
            }, completion: { (finish) in
                self.overlayView.isHidden = true
            })
        }
        else {
            UIView.animate(withDuration: 0.3, animations: {
                self.overlayView.alpha = 1.0
            }, completion: { (finish) in
                self.overlayView.isHidden = false
            })
        }
    }
}

// MARK: - UICollectionViewDataSource

extension SearchViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.posts.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AdCollectionViewCell.identifier,
                                                      for: indexPath) as! AdCollectionViewCell
        let post = self.posts[indexPath.row]
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

extension SearchViewController: CHTCollectionViewDelegateWaterfallLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: NSInteger) -> UIEdgeInsets {
        return UIEdgeInsets.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == collectionView {
            let post = self.posts[indexPath.item]
            let size = AdCollectionViewCell.dymamicSizeForCell(forPost: post,
                                                               inCollectionView: collectionView,
                                                               withColumns: 3)
            return size
        }
        
        return CGSize.zero
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let post = self.posts[indexPath.item]
        self.presentPostDetailViewControllers(selectedPost: post)
    }
}

extension SearchViewController: UISearchBarDelegate {
    func searchBarShouldBeginEditing(_ searchBar: UISearchBar) -> Bool {
        setOverlayViewHidden(isHide: false)
    
        return true
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if let keyword = searchBar.text, !keyword.isEmpty {
            
            // Save recently keywords
            UserDefaults.standard.set(keyword, forKey: Keys.recentlyKeyword)
            UserDefaults.standard.synchronize()
            
            showHUD()
            PostService.shared.fetchSimplifiedPostsByCategoryId(nil, location: nil, keyword: keyword, limit: 50, offset: 0, onSuccess: { (posts, next, total) in
                self.dismissHUD()
                self.setOverlayViewHidden(isHide: true)
                self.posts = posts ?? []
                self.collectionView.reloadData()
            }, onError: { (error) in
                debugPrint(error)
                self.dismissHUD()
                self.setOverlayViewHidden(isHide: true)
                // TODO: Display no results
            })
        }
    }
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        searchBar.resignFirstResponder()
    }
}
