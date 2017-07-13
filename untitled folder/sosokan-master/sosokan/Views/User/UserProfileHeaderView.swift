//
//  UserProfileHeaderView.swift
//  sosokan
//

import UIKit
import Material
import Cosmos

class UserProfileHeaderView: UIView {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var backgroundImageView: UIImageView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var ratingView: CosmosView!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var followingLabel: UILabel!
    @IBOutlet weak var followingNumberLabel: UILabel!
    @IBOutlet weak var followerLabel: UILabel!
    @IBOutlet weak var followerNumberLabel: UILabel!
    @IBOutlet weak var likeLabel: UILabel!
    @IBOutlet weak var likeNumberLabel: UILabel!
    @IBOutlet weak var collectionLabel: UILabel!
    @IBOutlet weak var myPostingLabel: UILabel!
    @IBOutlet weak var followButton: Button!
    @IBOutlet weak var messageButton: Button!
    @IBOutlet weak var verticalSlash1: UIView!
    @IBOutlet weak var verticalSlash2: UIView!
    @IBOutlet weak var verticalSlash3: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    fileprivate func initialize() {
        self.containerView.sendSubview(toBack: self.backgroundImageView)
        
        self.backgroundImageView.image = UIImage.init(named: "userProfileHeaderImage")
        self.backgroundImageView.contentMode = .scaleAspectFill
        self.backgroundImageView.clipsToBounds = true
        
        self.avatarImageView.roundify()
        self.avatarImageView.bordered(withColor: AppColor.white, width: 2)
        
        self.nameLabel.font = UIFont.latoRegular(size: AppFontsizes.veryBig)
        
        [self.followingLabel,
            self.followingNumberLabel,
            self.followerLabel,
            self.followerNumberLabel,
            self.likeLabel,
            self.likeNumberLabel]
            .forEach({
                $0!.textColor = AppColor.white
            })
        
        [self.followingLabel,
            self.followerLabel,
            self.likeLabel,
            self.collectionLabel]
            .forEach({
                $0.font = UIFont.latoRegular(size: AppFontsizes.small)
            })
        
        let plusIcon = AppIcons.plusIcon.resize(toWidth: 8)?.tint(with: AppColor.white)
        self.followButton.setTitle("Follow".localized(), for: .normal)
        self.followButton.setTitleColor(AppColor.white, for: .normal)
        self.followButton.setImage(plusIcon, for: .normal)
        self.followButton.titleLabel?.font = UIFont.latoRegular(size: AppFontsizes.small)
        self.followButton.backgroundColor = AppColor.orange
        self.followButton.cornerRadius = 10
        self.followButton.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 10)
        
        [self.verticalSlash1,
         self.verticalSlash2,
         self.verticalSlash3]
            .forEach({
                $0!.backgroundColor = AppColor.border
            })
        
    }
}
