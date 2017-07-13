//
//  UserImageCollectionViewCell.swift
//  sosokan
//

import UIKit

class UserImageCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var moreLabel: UILabel!

    static let identifier = String(describing: UserImageCollectionViewCell.self)
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.moreLabel.roundify()
        self.imageView.roundify()
        self.imageView.bordered(withColor: AppColor.border, width: 1)
    }
}
