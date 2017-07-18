//
//  FollowUserCollectionViewCell.swift
//  sosokan
//

import UIKit
import Material

class FollowUserCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var unfollowButton: Button!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    fileprivate func initialize() {
        
        self.containerView.layer.cornerRadius = 10
        self.containerView.layer.shadowColor = AppColor.grey.cgColor
        self.containerView.layer.shadowOffset = CGSize(width: 0, height: 1.5)
        self.containerView.layer.shadowOpacity = 1
        self.containerView.layer.shadowRadius = 3.5
        
        self.userImageView.roundify()
        self.userImageView.bordered(withColor: AppColor.border, width: 1)
        
        self.unfollowButton.layer.cornerRadius = 8
        self.unfollowButton.backgroundColor = AppColor.orange
        self.unfollowButton.setTitleColor(AppColor.white, for: .normal)
        self.unfollowButton.titleLabel?.font = UIFont.latoRegular(size: AppFontsizes.small)
        
        self.userNameLabel.textColor = AppColor.textSecondary
    }
    
    func render(_ profile: Profile) {
        if let url = profile.absoluteImageURL {
            self.userImageView.kf.setImage(with: url, placeholder: AppIcons.userIcon)
        }
        else {
            self.userImageView.image = AppIcons.userIcon
        }
        self.userNameLabel.text = profile.displayName
    }
}
