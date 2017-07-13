//
//  PopularCategoryCollectionViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 10/15/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class PopularCategoryCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var categoryNameLabel: UILabel!
    @IBOutlet weak var selectedUnderLineView: UIView!

    class func fromNib() -> PopularCategoryCollectionViewCell! {
        let nib = Bundle.main.loadNibNamed(String(describing: PopularCategoryCollectionViewCell.self), owner: self, options: nil)
        return nib!.first as! PopularCategoryCollectionViewCell
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.categoryNameLabel.numberOfLines = 1
        self.categoryNameLabel.textColor = AppColor.orange
        self.categoryNameLabel.textAlignment = .center
        self.categoryNameLabel.font = UIFont.latoRegular(size: 14)
        
        self.selectedUnderLineView.backgroundColor = AppColor.clear
    }
    
    func setSelectedCategory(_ selected: Bool) {
        if selected {
            self.backgroundColor = AppColor.orange
            self.categoryNameLabel.textColor = AppColor.white
            self.categoryNameLabel.font = UIFont.latoBold(size: 14)
//            self.selectedUnderLineView.backgroundColor = AppColor.orange
        }
        else {
            self.backgroundColor = AppColor.white
            self.categoryNameLabel.textColor = AppColor.orange
            self.categoryNameLabel.font = UIFont.latoRegular(size: 14)
//            self.selectedUnderLineView.backgroundColor = AppColor.clear
        }
    }
}
