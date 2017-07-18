//
//  CommentContentTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 2/4/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class CommentContentTableViewCell: UITableViewCell {
    
    @IBOutlet weak var userAvatarImageView: UIImageView!
    @IBOutlet weak var userAvatarImageViewLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var userDisplayNameLabel: UILabel!
    @IBOutlet weak var commentCreatedAtLabel: UILabel!
    @IBOutlet weak var commentContentLabel: UILabel!
    @IBOutlet weak var favoriteButton: Button!
    @IBOutlet weak var numberOfFavoritesLabel: UILabel!
    @IBOutlet weak var actionsButton: Button!
    @IBOutlet weak var actionsLabel: UILabel!
    @IBOutlet weak var borderBottomView: UIView!
    @IBOutlet weak var borderBottomViewLeadingConstraint: NSLayoutConstraint!
    
    static let identifier = String(describing: CommentContentTableViewCell.self)
    
    fileprivate struct Constants {
        static let commentIndentationLevel = 0
        static let subCommentIndentationLevel = 5
        static let baseImageIndentation: CGFloat = 8
        static let baseBorderIndentation: CGFloat = 0
        static let indentationPerLevel: CGFloat = 50
        static let iconSize: CGFloat = 22
        static let deleteButtonHeight: CGFloat = 32
        static let editButtonHeight: CGFloat = 32
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    fileprivate func initialize() {
        self.userAvatarImageView.setNeedsLayout()
        self.userAvatarImageView.layoutIfNeeded()
        self.userAvatarImageView.roundify()
        self.userAvatarImageView.bordered(withColor: AppColor.border, width: 1)
        self.userAvatarImageView.image = AppIcons.userIcon
        self.userAvatarImageView.contentMode = .scaleAspectFit
        
        self.userDisplayNameLabel.font = UIFont.latoRegular(size: AppFontsizes.big)
        self.userDisplayNameLabel.textColor = AppColor.textPrimary
        
        self.commentCreatedAtLabel.font = UIFont.latoRegular(size: AppFontsizes.small)
        self.commentCreatedAtLabel.textColor = AppColor.textSecondary
        
        self.commentContentLabel.font = UIFont.latoRegular(size: AppFontsizes.regular)
        self.commentContentLabel.textColor = AppColor.textPrimary
        self.commentContentLabel.numberOfLines = 0
        self.commentContentLabel.lineBreakMode = .byWordWrapping
        
        self.favoriteButton.contentEdgeInsets = UIEdgeInsets.init(top: 5, left: 5, bottom: 5, right: 5)
        self.favoriteButton.setImage(AppIcons.heartIcon, for: .normal)
        self.favoriteButton.setImage(AppIcons.heartSelectedIcon, for: .selected)
        
        self.numberOfFavoritesLabel.font = UIFont.latoRegular(size: AppFontsizes.verySmall)
        self.numberOfFavoritesLabel.textColor = AppColor.textSecondary
        self.numberOfFavoritesLabel.textAlignment = .center
        
        self.actionsButton.contentEdgeInsets = UIEdgeInsets.init(top: 5, left: 5, bottom: 5, right: 5)
        self.actionsButton.setImage(AppIcons.angleDownIcon.resize(toWidth: Constants.iconSize)?.withRenderingMode(.alwaysTemplate), for: .normal)
        self.actionsButton.imageView?.tintColor = AppColor.grey
        self.actionsButton.setTitle(nil, for: .normal)
        
        self.actionsLabel.font = UIFont.latoRegular(size: AppFontsizes.verySmall)
        self.actionsLabel.textColor = AppColor.textSecondary
        self.actionsLabel.textAlignment = .center

        self.borderBottomView.backgroundColor = AppColor.border
    }
    
    func render(userAvatarURL avatarURL: URL?,
                              userDisplayName: String,
                              createdAt: Date,
                              commentContent: String,
                              numberOfFavorites: Int,
                              actionsTitle: String,
                              indentationLevel: Int,
                              isLiked: Bool) {
        self.userAvatarImageView.sd_setImage(with: avatarURL, placeholderImage: AppIcons.userIcon)
        self.userDisplayNameLabel.text = userDisplayName
        self.commentCreatedAtLabel.text = createdAt.timeAgoSinceNow
        self.commentContentLabel.text = commentContent
        self.numberOfFavoritesLabel.text = String.init(numberOfFavorites)
        self.actionsLabel.text = actionsTitle
        self.favoriteButton.isSelected = isLiked
        self.setIndentation(indentationLevel)
    }
    
    func setSelectedLikeButton(_ selected: Bool) {
        self.favoriteButton.isSelected = selected
    }
    
    func setHiddenBorderBottomView(_ hidden: Bool) {
        self.borderBottomView.backgroundColor = hidden ? AppColor.clear : AppColor.border
    }
    
    // MARK: Private method
    
    fileprivate func setIndentation(_ level: Int) {
        let level = CGFloat.init(level)
        self.userAvatarImageViewLeadingConstraint.constant = level * Constants.indentationPerLevel + Constants.baseImageIndentation
        self.borderBottomViewLeadingConstraint.constant = level * Constants.indentationPerLevel + Constants.baseBorderIndentation
    }
}
