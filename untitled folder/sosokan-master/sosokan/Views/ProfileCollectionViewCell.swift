//
//  ProfileCollectionViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/15/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class ProfileCollectionViewCell: UICollectionViewCell {
    
    // MARK: Variables
    static let cellReuseIdentifier = "ProfileCollectionViewCell"
    
    // MARK: IBOutlets
    @IBOutlet weak var backgroundPulseView: PulseView!
    @IBOutlet weak var centerImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    
    // MARK: View circle life
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // setup views
        titleLabel.font = UIFont.latoBold(size: 17)
        centerImageView.contentMode = .center
    }
}
