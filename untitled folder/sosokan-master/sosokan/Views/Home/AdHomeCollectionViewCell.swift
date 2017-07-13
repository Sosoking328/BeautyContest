//
//  AdHomeCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 5/31/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class AdHomeCollectionViewCell: UICollectionViewCell {

    static let identifier = String(describing: AdHomeCollectionViewCell.self)
    
    @IBOutlet weak var adImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    func renderAd(post: SimplifiedPost) {
        adImageView.kf.indicatorType = .activity
        if let headerImageURL = post.headerImage?.absoluteURL {
            adImageView.kf.setImage(with: headerImageURL, placeholder: AppIcons.adDefault)
        }
        else if let categoryId = post.categoryId,
            let category = DataManager.shared.allCategories.getCategory(withCategoryId: categoryId),
            let categoryIconURL = category.absoluteLocalizeIconURL {
            adImageView.kf.setImage(with: categoryIconURL, placeholder: AppIcons.adDefault)
        }
    }
}
