//
//  AdDetailBottomBarView.swift
//  sosokan
//
//  Created by An Phan on 6/20/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class AdDetailBottomBarView: UIView {

    @IBOutlet weak var shareButton: UIButton!
    @IBOutlet weak var favoriteButton: UIButton!
    @IBOutlet weak var reportButton: UIButton!
    @IBOutlet weak var commentButton: UIButton!
    @IBOutlet weak var shareLabel: UILabel!
    @IBOutlet weak var favoriteLabel: UILabel!
    @IBOutlet weak var reportLabel: UILabel!
    @IBOutlet weak var commentLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        
        initialize()
    }
    
    // MARK: Private method
    
    fileprivate func initialize() {
        self.clipsToBounds = true
        self.addTopBorderWithColor(AppColor.border, width: 1)
        
        [self.shareLabel, self.favoriteLabel, self.reportLabel, self.commentLabel].forEach({
            $0!.font = UIFont.avenirLTStdRoman(size: AppFontsizes.extraSmall)
            $0!.text = $0!.text!.localized()
        })
    }

}
