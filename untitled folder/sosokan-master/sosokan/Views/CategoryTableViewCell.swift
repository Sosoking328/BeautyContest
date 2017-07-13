//
//  CategoryTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/9/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class CategoryTableViewCell: UITableViewCell {

    // MARK: - IBOutlets
    @IBOutlet weak var subCatImageView: UIImageView!
    @IBOutlet weak var subCategoryName: UILabel!
    @IBOutlet weak var classifiedsAmountLabel: UILabel!
    @IBOutlet weak var checkButton: UIButton!
    @IBOutlet weak var borderView: UIView!
    @IBOutlet weak var imageViewLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var borderViewLeadingConstraint: NSLayoutConstraint!
    
    fileprivate struct Constants {
        static let baseImageViewLeadingSpace: CGFloat = 8
        static let baseBorderViewLeadingSpace: CGFloat = 16
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

    func setAdCount(_ count: Int) {
        if count == 1 {
            classifiedsAmountLabel.text = "\(count) " + "classified".localized()
        }
        else {
            classifiedsAmountLabel.text = "\(count) " + "classifieds".localized()
        }
    }
    
    func indented(_ level: Int) {
        self.imageViewLeadingConstraint.constant = Constants.baseImageViewLeadingSpace + Constants.baseBorderViewLeadingSpace * CGFloat.init(level)
        self.borderViewLeadingConstraint.constant = Constants.baseBorderViewLeadingSpace * CGFloat(level)
        
    }
    
    // MARK: Private method
    fileprivate func initialize() {
        self.subCatImageView.contentMode = .scaleAspectFit
        
        self.subCategoryName.font = UIFont.latoBold(size: AppFontsizes.big)
        
        self.borderView.backgroundColor = AppColor.border
    }
}
