//
//  HomeHeaderView.swift
//  sosokan
//
//  Created by An Phan on 5/25/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class HomeHeaderView: UICollectionReusableView {

    @IBOutlet weak var bannerView: ImageSlideshow!
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    @IBOutlet weak var featuredItemCollectionView: UICollectionView!
    @IBOutlet weak var recentSearchCollectionView: UICollectionView!
    @IBOutlet weak var bannerHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var recentSearchButton: UIButton!
    @IBOutlet weak var featuredButton: UIButton!
    @IBOutlet weak var recentSearchViewHeightContraint: NSLayoutConstraint!
    @IBOutlet weak var featuredViewHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var categoryTitleLabel: UILabel!
    @IBOutlet weak var featuredItemTitleLabel: UILabel!
    @IBOutlet weak var recentSearchTitleLabel: UILabel!
    @IBOutlet weak var nearbyTitleLabel: UILabel!
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    let categoryCellID = String(describing: CategoryCollectionViewCell.self)
    var posts = DataManager.shared.posts
    var featuredCategories = DataManager.shared.featureCategories.sorted(by: {$0.sort < $1.sort})
    var banners = [BannerObject]()
    var recentlySearchedPosts = [SimplifiedPost]()
    
    var didSelectedCategory:((Category) -> Void)?
    var didSelectedPost:((SimplifiedPost) -> Void)?
    var didSelectedBanner:((URL) -> Void)?
    var didSelectRecentlySearches:(([SimplifiedPost]) -> Void)?
    var didSelectFeaturedItems:(([SimplifiedPost]) -> Void)?
    var didSelectAddCategoryItem: (() -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        setText()
        
        categoryCollectionView.dataSource = self
        categoryCollectionView.delegate = self
        categoryCollectionView.register(UINib(nibName: categoryCellID, bundle: nil),
                                        forCellWithReuseIdentifier: categoryCellID)
        
        featuredItemCollectionView.dataSource = self
        featuredItemCollectionView.delegate = self
        featuredItemCollectionView.register(UINib(nibName: AdHomeCollectionViewCell.identifier, bundle: nil),
                                            forCellWithReuseIdentifier: AdHomeCollectionViewCell.identifier)
        
        recentSearchCollectionView.dataSource = self
        recentSearchCollectionView.delegate = self
        recentSearchCollectionView.register(UINib(nibName: AdHomeCollectionViewCell.identifier, bundle: nil),
                                            forCellWithReuseIdentifier: AdHomeCollectionViewCell.identifier)
        
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(reload),
                                               name: NSNotification.Name(rawValue: Notifications.fetchedCategories),
                                               object: nil)
        
        // Banners list
        bannerView.contentScaleMode = .scaleAspectFill
        bannerView.slideshowInterval = 3
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(bannerDidTapped))
        bannerView.addGestureRecognizer(tapGesture)
        
        // Fetching list of banners
        BannerService.shared.fetchBannerWithCompletionHandler { (banners, error) in
            if let banners = banners {
                debugPrint(banners)
                self.banners = banners
                let bannerImageSources = banners.map({ banner -> SDWebImageSource? in
                    if let bannerImageURL = banner.imageURL {
                        let imageSource = SDWebImageSource(url: bannerImageURL,
                                                           placeholder: AppIcons.bannerPlaceHolder!)
                        return imageSource
                    }
                    else {
                        return nil
                    }
                }).flatMap({ $0 })
                if bannerImageSources.isEmpty {
                    self.bannerHeightConstraint.constant = 0.0
                }
                else {
                    self.bannerHeightConstraint.constant = 450.0
                    self.bannerView.setImageInputs(bannerImageSources)
                    self.bannerView.reloadInputViews()
                }
            }
        }
        
        // Fetch categories list.
        CategoryService.shared.fetchCategoriesWithCompletionHandler { (json, error) in
            if error == nil {
                self.featuredCategories = DataManager.shared.featureCategories.sorted(by: {$0.sort < $1.sort})
                self.categoryCollectionView.reloadData()
            }
        }
        
        if let keywords = UserDefaults.standard.string(forKey: Keys.recentlyKeyword), !keywords.isEmpty {
            // Fetching posts list by recently searched.
            PostService.shared.fetchSimplifiedPostsByCategoryId(keyword: keywords, limit: 30, offset: 0, onSuccess: { (posts, next, total) in
                self.recentlySearchedPosts = posts ?? []
                
                // Saving recently searches to DataManager
                DataManager.shared.recentSearches = self.recentlySearchedPosts
                
                if self.recentlySearchedPosts.count == 0 {
                    self.setRecentSearchViewHidden(isHidden: true)
                }
                
                self.recentSearchCollectionView.reloadData()
            }) { (error) in
                debugPrint(error)
                self.setRecentSearchViewHidden(isHidden: true)
            }
        }
        else {
            setRecentSearchViewHidden(isHidden: true)
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - IBActions
    @IBAction func featuredButtonTapped(_ sender: Any) {
        
        // TODO: Replace by featured items
        didSelectFeaturedItems?(DataManager.shared.recentSearches)
    }
    
    @IBAction func recentSearchButtonTapped(_ sender: Any) {
        didSelectRecentlySearches?(DataManager.shared.recentSearches)
    }
    
    func setText() {
        categoryTitleLabel.text = "Categories".localized()
        featuredItemTitleLabel.text = "Featured items".localized()
        recentSearchTitleLabel.text = "Recently searched".localized()
        nearbyTitleLabel.text = "Nearby items".localized()
    }
    
    func bannerDidTapped() {
        guard let inputSource = self.bannerView?.currentSlideshowItem?.image as? SDWebImageSource else { return }
        if let index = self.banners.index(where: { $0.imageURL == inputSource.url }) {
            let bannerObject = self.banners[index]
            guard let url = bannerObject.URL else { return }
            didSelectedBanner?(url)
        }
    }
    
    func reload() {
        posts = DataManager.shared.posts
        featuredCategories = DataManager.shared.featureCategories.sorted(by: {$0.sort < $1.sort})
        
        categoryCollectionView.reloadData()
        featuredItemCollectionView.reloadData()
        recentSearchCollectionView.reloadData()
    }
    
    // MARK: - Private methods
    
    fileprivate func setRecentSearchViewHidden(isHidden: Bool) {
        if isHidden {
            recentSearchViewHeightContraint.constant = 0.0
            var tmpFrame = frame
            tmpFrame.size.height -= 125.0
            frame = tmpFrame
        }
        else {
            recentSearchViewHeightContraint.constant = 125.0
        }
    }
}

// MARK: - UICollectionViewDataSource

extension HomeHeaderView: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == categoryCollectionView {
            return featuredCategories.count > 0 ? featuredCategories.count + 1 : 0
        }
        else if collectionView == recentSearchCollectionView {
            return recentlySearchedPosts.count
        }
        
        // TODO: Update later
        return posts.count > 10 ? 10 : 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == categoryCollectionView {
            let cellID = indexPath.item == featuredCategories.count ? "AddCategoryCellID" : categoryCellID
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellID, for: indexPath) as! CategoryCollectionViewCell
            
            if indexPath.item < featuredCategories.count {
                cell.renderData(category: featuredCategories[indexPath.row])
            }
            
            return cell
        }
        else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AdHomeCollectionViewCell.identifier, for: indexPath) as! AdHomeCollectionViewCell
            
            var post: SimplifiedPost?
            if collectionView == recentSearchCollectionView {
                post = recentlySearchedPosts[indexPath.row]
            }
            else {
                post = posts[indexPath.row]
            }
            
            if let post = post {
                cell.renderAd(post: post)
            }
            
            return cell
        }
    }
}

// MARK: - UICollectionViewDelegate

extension HomeHeaderView: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == categoryCollectionView {
            if indexPath.item == featuredCategories.count {
                didSelectAddCategoryItem?()
            }
            else {
                let category = featuredCategories[indexPath.row]
                didSelectedCategory?(category)
            }
        }
        else {
            let post = posts[indexPath.row]
            didSelectedPost?(post)
        }
    }
}
