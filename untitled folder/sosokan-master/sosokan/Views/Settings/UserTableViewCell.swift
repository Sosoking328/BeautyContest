//
//  UserTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 7/25/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class UserTableViewCell: UITableViewCell {
    
    // MARK: - IBOutlets
    @IBOutlet weak var avtImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var activeButton: UIButton!

    var activeButtonDidTapped: ((UIButton) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Initialization code
        avtImageView.roundify()
        activeButton.layer.cornerRadius = 5.0
        activeButton.isHidden = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func activeButtonTapped(_ button: UIButton) {
        activeButtonDidTapped?(button)
        button.isSelected = !button.isSelected
        button.backgroundColor = button.isSelected ? AppColor.orange : UIColor.rgbColor(red: 255, green: 90, blue: 97)
    }
}
