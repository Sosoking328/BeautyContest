//
//  FeatureCategoryCollectionViewCell.swift
//  sosokan
//

import UIKit

class FeatureCategoryCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var backgroundImageView: UIImageView!
    @IBOutlet weak var selectedImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var topBorderView: UIView!
    @IBOutlet weak var bottomBorderView: UIView!
    @IBOutlet weak var leftBorderView: UIView!
    @IBOutlet weak var rightBorderView: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    fileprivate func initialize() {
        self.backgroundImageView.contentMode = .scaleAspectFill
        
        self.titleLabel.textColor = AppColor.white
        self.titleLabel.numberOfLines = 3
        self.titleLabel.lineBreakMode = .byWordWrapping
        self.titleLabel.font = UIFont.latoRegular(size: AppFontsizes.veryBig)
        self.titleLabel.shadowColor = AppColor.textSecondary
        self.titleLabel.backgroundColor = UIColor.init(white: 0, alpha: 0.5)
        
        [self.topBorderView, self.bottomBorderView, self.leftBorderView, self.rightBorderView].forEach({
            self.contentView.bringSubview(toFront: $0!)
            $0?.backgroundColor = AppColor.white
        })
        
        self.bringSubview(toFront: self.selectedImageView)
    }

}
