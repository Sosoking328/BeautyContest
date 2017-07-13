//
//  PostDetailHeaderView.swift
//  sosokan
//
//  Created by A on 1/14/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit
import Cosmos
import Material
import MapKit

protocol PostDetailHeaderViewDelegate {
    func boostButtonTapped(sender: Any)
    func likeButtonTapped(sender: Any)
    func viewsButtonTapped(sender: Any)
    func followButtonTapped(sender: Any)
    func phoneButtonTapped(sender: Any)
    func messageButtonTapped(sender: Any)
    func categoryButtonTapped(sender: Any)
    func playVideoButtonTapped(sender: Any)
    func viewCommentsButtonTapped(sender: Any)
}

class PostDetailHeaderView: UIView {
    
    // MARK: - IBOutlets
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var likeButton: UIButton!
    @IBOutlet weak var likeNumberLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var viewsButton: UIButton!
    @IBOutlet weak var viewsNumberLabel: UILabel!
    @IBOutlet weak var followButton: UIButton!
    @IBOutlet weak var avtImageView: UIImageView!
    @IBOutlet weak var ratingView: CosmosView!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var totalAdLabel: UILabel!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var mapViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var callButton: UIButton!
    @IBOutlet weak var textMeButton: UIButton!
    @IBOutlet weak var featuredStampImageView: UIImageView!
    @IBOutlet weak var descWebView: UIWebView!
    @IBOutlet weak var descWebViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var categoryButton: UIButton!
    @IBOutlet weak var expiredImageView: UIImageView!
    @IBOutlet weak var categoryTitleLabel: UILabel!
    @IBOutlet weak var carouselView: ImageSlideshow!
    @IBOutlet weak var carouselViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var contactInfoView: UIView!
    @IBOutlet weak var videoContainerViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var videoContainerView: UIView!
    @IBOutlet weak var videoImageView: UIImageView!
    @IBOutlet weak var playVideoButton: UIButton!
    @IBOutlet weak var followingButton: UIButton!
    @IBOutlet weak var webViewLoadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var webViewLoadingIndicatorHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var webViewLoadingIndicatorBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var callButtonBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var likedImageView: UIImageView!
    @IBOutlet weak var likedCollectionView: UICollectionView!
    @IBOutlet weak var viewAllCommentsButton: UIButton!
    @IBOutlet weak var allCommentsLabel: UILabel!
    @IBOutlet weak var boostView: UIView!
    
    // MARK: - Delegate
    var delegate: PostDetailHeaderViewDelegate?
    
    // MARK: - View life cycle
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    // MARK: - Fileprivate methods
    
    fileprivate func initialize() {
        self.nameLabel.font = UIFont.latoBold(size: 18)
        self.nameLabel.numberOfLines = 0
        self.nameLabel.lineBreakMode = .byWordWrapping
        self.nameLabel.textColor = AppColor.textPrimary
        
        self.likeButton.setImage(AppIcons.heartIcon, for: .normal)
        self.likeButton.setImage(AppIcons.heartSelectedIcon, for: .selected)
        self.likeButton.setTitleColor(AppColor.orange, for: .normal)
        self.likeButton.setTitleShadowColor(AppColor.white, for: .normal)
        
        self.carouselView.backgroundColor = UIColor.white
        self.carouselView.contentScaleMode = .center
        self.carouselView.slideshowInterval = 5
        self.carouselView.pageControlPosition = .insideScrollView
        self.carouselView.pageControl.currentPageIndicatorTintColor = AppColor.sliderIndicator
        self.carouselView.pageControl.pageIndicatorTintColor = AppColor.textSecondary
        self.carouselView.pageControl.hidesForSinglePage = true
        self.carouselView.pageControl.alpha = 0
        
        self.mapView.autoresizingMask = [.flexibleHeight, .flexibleTopMargin, .flexibleBottomMargin]
        self.mapViewHeightConstraint.constant = 0
        
        self.webViewLoadingIndicator.activityIndicatorViewStyle = .gray
        self.webViewLoadingIndicator.hidesWhenStopped = true
        self.webViewLoadingIndicator.startAnimating()
        
        // Setup favorite button
        self.likeButton.tintColor = Color.white
        self.likeButton.setImage(AppIcons.heartIcon, for: .normal)
        self.likeButton.imageView?.contentMode = .center
        self.likeButton.setTitle(nil, for: .normal)
        self.likeButton.cornerRadius = 5
        
        // Setup share button
        self.viewsButton.tintColor = Color.white
        self.viewsButton.setImage(AppIcons.eyeIcon, for: .normal)
        self.viewsButton.imageView?.contentMode = .center
        self.viewsButton.setTitle(nil, for: .normal)
        self.viewsButton.cornerRadius = 5
        self.viewsButton.isUserInteractionEnabled = false
        
        self.followButton.setTitle("FOLLOW".localized(), for: .normal)
        self.followButton.setTitleColor(AppColor.white, for: .normal)
        self.followButton.titleLabel?.font = UIFont.latoRegular(size: AppFontsizes.verySmall)
        self.followButton.backgroundColor = AppColor.orange
        self.followButton.setImage(AppIcons.plusIcon.resize(toWidth: 5)?.tint(with: AppColor.white), for: .normal)
        self.followButton.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 10)
        self.followButton.cornerRadius = 10
        
        self.callButton.cornerRadius = 20
        self.textMeButton.cornerRadius = 20
        self.textMeButton.layer.borderColor = UIColor.navTitleColor().cgColor
        self.textMeButton.layer.borderWidth = 1.0
        self.boostView.cornerRadius = 10.0
        
        [self.likeNumberLabel, self.viewsNumberLabel].forEach({
            $0?.font = UIFont.latoRegular(size: AppFontsizes.extraSmall)
            $0?.textColor = AppColor.textSecondary
            $0?.textAlignment = .center
        })
        
        self.avtImageView.roundify()
        
        self.viewAllCommentsButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.big)
        self.allCommentsLabel.font = UIFont.latoRegular(size: AppFontsizes.small)
    }
    
    // MARK: IBAction
    
    @IBAction func boostButtonTapped(_ sender: Any) {
        self.delegate?.boostButtonTapped(sender: sender)
    }
    
    @IBAction func likeButtonTapped(_ sender: Any) {
        self.delegate?.likeButtonTapped(sender: sender)
    }
    
    @IBAction func viewsButtonTapped(_ sender: Any) {
        self.delegate?.viewsButtonTapped(sender: sender)
    }
    
    @IBAction func followButtonTapped(_ sender: Any) {
        self.delegate?.followButtonTapped(sender: sender)
    }
    
    @IBAction func callMeButtonTapped(_ sender: Any) {
        self.delegate?.phoneButtonTapped(sender: sender)
    }
    
    @IBAction func textMeButtonTapped(_ sender: Any) {
        self.delegate?.messageButtonTapped(sender: sender)
    }
    
    @IBAction func categoryButtonTapped(_ sender: Any) {
        self.delegate?.categoryButtonTapped(sender: sender)
    }
}
