//
//  AdCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/12/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import DateToolsSwift
import Localize_Swift

let adCellBottomMargin = 12

class AdCollectionViewCell: UICollectionViewCell {
    
    static let identifier = String(describing: AdCollectionViewCell.self)
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var imageContainerView: UIView!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var createdTimeLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var likeButton: UIButton!
    @IBOutlet weak var likeNumberLabel: UILabel!
    @IBOutlet weak var viewButton: UIButton!
    @IBOutlet weak var viewNumberLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var expiredImageView: UIImageView!
    @IBOutlet weak var userImageView: UIImageView!
    
    fileprivate struct Constants {
        static let imageViewHeightInList: CGFloat = 156.0
        static let imageViewHeightInGrid: CGFloat = 100.0
        static let defaultIconWidth: CGFloat = 55.0
        static let postNameFont = UIFont.latoBold(size: AppFontsizes.big)
        static let postDescriptionFont = UIFont.latoRegular(size: AppFontsizes.small)
        static let gapImageAndButton: CGFloat = 0.0
        static let buttonHeight: CGFloat = 20.0
        static let gapButtonAndName: CGFloat = 5.0
        static let gapNameAndDescription: CGFloat = 5.0
        static let gapDescriptionAndBottom: CGFloat = 8.0
        static let gapBetweenColumns: CGFloat = 6.0
        static let defaultBorderViewWidth: CGFloat = 1.0
        static let boldBorderViewWidth: CGFloat = 2.0
        static let nameLabelTextStyle = NSMutableParagraphStyle().then({
            $0.lineHeightMultiple = 1.1
            $0.firstLineHeadIndent = 28
        })
    }
    
    var likeButtonDidTouch: ((Any?) -> Void)?
    
    // MARK: - View life cycle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
    }
    
    // MARK: - IBActions
    
    @IBAction func likeButtonTapped(_ sender: Any) {
        likeButtonDidTouch?(sender)
    }
    
    // MARK: - Private methods
    
    fileprivate func initialize() {
        
        self.containerView.backgroundColor = UIColor.clear
        self.containerView.layer.cornerRadius = 10
        self.containerView.layer.masksToBounds = true
        
        self.imageContainerView.layer.cornerRadius = 10
        self.imageContainerView.layer.shadowColor = AppColor.grey.cgColor
        self.imageContainerView.layer.shadowOffset = CGSize(width: 0, height: 1.5)
        self.imageContainerView.layer.shadowOpacity = 1
        self.imageContainerView.layer.shadowRadius = 3.5
        
        self.imageView.layer.cornerRadius = 10
        self.imageView.clipsToBounds = true
        
        // Setup created time label
        self.createdTimeLabel.textColor = AppColor.textSecondary
        self.createdTimeLabel.font = UIFont.latoRegular(size: AppFontsizes.small)
        self.createdTimeLabel.tag = 0
        
        // Setup name label
        self.nameLabel.textColor = AppColor.textPrimary
        self.nameLabel.font = Constants.postNameFont
        self.nameLabel.numberOfLines = 2
        self.nameLabel.lineBreakMode = .byWordWrapping
        
        // Setup favorite button
        self.likeButton.tintColor = UIColor.white
        self.likeButton.setImage(AppIcons.heartIcon, for: .normal)
        self.likeButton.imageView?.contentMode = .center
        self.likeButton.setTitle(nil, for: .normal)
        self.likeButton.cornerRadius = 5
        
        // Setup share button
        self.viewButton.tintColor = UIColor.white
        self.viewButton.setImage(AppIcons.eyeIcon, for: .normal)
        self.viewButton.imageView?.contentMode = .center
        self.viewButton.setTitle(nil, for: .normal)
        self.viewButton.cornerRadius = 5
        self.viewButton.isUserInteractionEnabled = false
        
        self.expiredImageView.image = nil
        
        self.userImageView.image = AppIcons.userIcon
        self.userImageView.roundify()
        self.userImageView.contentMode = .scaleAspectFill
        self.userImageView.bordered(withColor: AppColor.border, width: 1)
        
        self.likeNumberLabel.textColor = AppColor.textSecondary
        self.likeNumberLabel.font = UIFont.latoRegular(size: AppFontsizes.extraSmall)
        
        self.viewNumberLabel.textColor = AppColor.textSecondary
        self.viewNumberLabel.font = UIFont.latoRegular(size: AppFontsizes.extraSmall)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(timestampLabelTapped))
        createdTimeLabel.addGestureRecognizer(tap)
    }
    
    // MARK: - Methods
    
    func render(withPost post: SimplifiedPost) {

        self.createdTimeLabel.text = post.createdAt.timeAgoSinceNow
        
        imageView.kf.indicatorType = .activity
        if let headerImageURL = post.headerImage?.absoluteURL {
            imageView.kf.setImage(with: headerImageURL, placeholder: nil)
        }
        else if let categoryId = post.categoryId,
            let category = DataManager.shared.allCategories.getCategory(withCategoryId: categoryId),
            let categoryIconURL = category.absoluteLocalizeIconURL {
            imageView.kf.setImage(with: categoryIconURL, placeholder: nil)
        }
        /*
        DispatchQueue.global().async {
            var image: UIImage?
            if let categoryId = post.categoryId,
                let category = DataManager.sharedInstance.allCategories.getCategory(withCategoryId: categoryId),
                let categoryIconURL = category.absoluteLocalizeIconURL,
                let cacheImage = DataManager.sharedInstance.getCacheImage(categoryIconURL) {
                image = cacheImage.size.width > cacheImage.size.height ? cacheImage.resize(toWidth: Constants.defaultIconWidth) : cacheImage.resize(toHeight: Constants.defaultIconWidth)
            }
            image = image ?? AppIcons.bigStarIcon.resize(toWidth: Constants.defaultIconWidth)
            DispatchQueue.main.async(execute: { 
                self.imageView.image = image
                self.imageView.contentMode = .center
            })
            if let headerImageURL = post.headerImage?.absoluteURL {
                self.imageView.sd_setImage(with: headerImageURL, placeholderImage: image, options: [], completed: { (image, _, _, _) in
                    self.imageView.contentMode = .scaleAspectFill
                })
            }
            else if let categoryId = post.categoryId,
                let category = DataManager.sharedInstance.allCategories.getCategory(withCategoryId: categoryId),
                let categoryIconURL = category.absoluteLocalizeIconURL {
                self.imageView.sd_setImage(with: categoryIconURL, placeholderImage: image, options: [], completed: { (image, _, _, _) in
                    let image = image!.size.width > image!.size.height ? image!.resize(toWidth: Constants.defaultIconWidth) : image!.resize(toHeight: Constants.defaultIconWidth)
                    DispatchQueue.main.async(execute: {
                        self.imageView.image = image
                        self.imageView.contentMode = .scaleAspectFill
                    })
                })
            }
        }
        */
        
        self.nameLabel.textColor = post.isStandout ? AppColor.orange : AppColor.textPrimary
        self.nameLabel.attributedText = NSAttributedString.init(string: post.name,
                                                                attributes: [NSParagraphStyleAttributeName: Constants.nameLabelTextStyle])
        self.likeNumberLabel.text = post.numberOfFavorites.string()
        self.viewNumberLabel.text = post.numberOfViews.string()
        let price = post.price ?? 0
        if price > 0 {
            self.priceLabel.isHidden = false
            self.priceLabel.text = "   $" + (price.truncatingRemainder(dividingBy: 1) == 0 ? Int(price).string() : "\(price.roundTo(places: 2))") + "   "
        }
        else {
            self.priceLabel.isHidden = true
            self.priceLabel.text = nil
        }
    }
    
    func timestampLabelTapped() {
        
    }
}

extension AdCollectionViewCell {
    class func dymamicSizeForCell(forPost post: SimplifiedPost, inCollectionView collectionView: UICollectionView, withColumns columns: Int) -> CGSize {
        let width: CGFloat = collectionView.frame.size.width / CGFloat.init(columns)
        
        /*
        // Gap columns to 2 left and right
        width -= Constants.gapBetweenColumns * CGFloat.init(columns)
         */
        
        var height: CGFloat = 0
        
        // Image height
        height += width
        
        // Gap image and button
        height += Constants.gapImageAndButton
        
        // Button height
        height += Constants.buttonHeight
        
        // Gap button and name label
        height += Constants.gapButtonAndName
        
        // Name label height
//        height += UILabel.dynamicLabelHeight(maxWidth: width, font: Constants.postNameFont, content: post.name)
        height += UILabel.dynamicLabelHeight(maxWidth: width,
                                             font: Constants.postNameFont,
                                             content: post.name,
                                             attributes: [NSParagraphStyleAttributeName: Constants.nameLabelTextStyle])
        
        // Gap description label and bottom
        height += Constants.gapDescriptionAndBottom
        
        return CGSize(width: width, height: height)
    }
}
