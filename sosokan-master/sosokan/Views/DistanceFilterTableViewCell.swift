//
//  DistanceFilterTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/25/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class DistanceFilterTableViewCell: TableViewCell {
    
    // Use as global variable
    static let cellRuseIdentifier = "DistanceFilterTableViewCell"
    
    // Define for control this view
    
    // MARK: - IBOutlets
    @IBOutlet weak var leftLabel: UILabel!
    @IBOutlet weak var rightLabel: UILabel!

    var accessoryImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Setup label on left
        leftLabel.font = UIFont.latoBold(size: 14)
        leftLabel.textColor = AppColor.textPrimary
        
        // Setup label on right
        rightLabel.font = UIFont.latoBold(size: 14)
        rightLabel.textColor = AppColor.orange
        
        accessoryImageView = UIImageView(image: UIImage(named: "down-arrow"))
        accessoryImageView.tintColor = AppColor.orange
        accessoryView = accessoryImageView
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}
