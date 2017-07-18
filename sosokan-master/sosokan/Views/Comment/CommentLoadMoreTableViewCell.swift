//
//  CommentLoadMoreTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 2/4/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class CommentLoadMoreTableViewCell: UITableViewCell {

    @IBOutlet weak var loadMoreButton: Button!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    fileprivate func initialize() {
        self.loadMoreButton.setTitleColor(AppColor.orange, for: .normal)
        self.loadMoreButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.big)
    }
    
    func render(withButtonTitle title: String) {
        self.loadMoreButton.setTitle(title, for: .normal)
    }
}
