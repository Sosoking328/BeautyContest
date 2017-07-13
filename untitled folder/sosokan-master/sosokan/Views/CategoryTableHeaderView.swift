//
//  CategoryTableHeaderView.swift
//  sosokan
//
//  Created by An Phan on 6/9/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import FZAccordionTableView

class CategoryTableHeaderView: FZAccordionTableViewHeaderView {
    static let kDefaultAccordionHeaderViewHeight: CGFloat = 64
    static let kAccordionHeaderViewReuseIdentifier = "AccordionHeaderViewReuseIdentifier"
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var catNameLabel: UILabel!
    @IBOutlet weak var classifiedsAmountLabel: UILabel!
    @IBOutlet weak var checkButton: UIButton!

    override func prepareForReuse() {
        checkButton.isSelected = false
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        backgroundColor = UIColor.red
    }
    
    func setAdCount(_ count: Int) {
        if count == 1 {
            classifiedsAmountLabel.text = "\(count) " + "classified".localized()
        }
        else {
            classifiedsAmountLabel.text = "\(count) " + "classifieds".localized()
        }
    }
}
