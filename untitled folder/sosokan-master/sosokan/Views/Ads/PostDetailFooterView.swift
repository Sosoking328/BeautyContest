//
//  PostDetailFooterView.swift
//  sosokan
//

import UIKit

class PostDetailFooterView: UIView {
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var relatedPostsLabel: UILabel!
    @IBOutlet weak var relatedPostsLoadingIndicatorView: UIActivityIndicatorView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.relatedPostsLabel.textColor = AppColor.orange
        self.relatedPostsLabel.font = UIFont.latoRegular(size: AppFontsizes.big)
        self.relatedPostsLabel.addBottomBorderWithColor(AppColor.orange, width: 2)
        
        self.relatedPostsLoadingIndicatorView.hidesWhenStopped = true
        self.relatedPostsLoadingIndicatorView.startAnimating()
    }
    
    
}