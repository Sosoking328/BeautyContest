//
//  CategoryCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 5/30/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import Kingfisher

class CategoryCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var categoryImageView: UIImageView!
    @IBOutlet weak var categoryNameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    func renderData(category: Category) {
        categoryNameLabel.text = SupportedLanguage.current() == .english ? category.nameEnglish : category.nameChinese
        categoryImageView.kf.indicatorType = .activity
        if let background = category.backgroundImageURL, let backgroundUrl = URL(string: background) {
            categoryImageView.kf.setImage(with: backgroundUrl)
        }
        else {
            let iconString = SupportedLanguage.current() == .english ? category.iconEnglishURL : category.iconChineseURL
            categoryImageView.kf.setImage(with: URL(string: iconString!))
        }
    }
}
