//
//  MenuTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/7/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class MenuTableViewCell: UITableViewCell {

    // MARK: - IBOutlets
    @IBOutlet weak var menuImageView: UIImageView!
    @IBOutlet weak var menuTitleLabel: UILabel!
    @IBOutlet weak var menuNumberLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        menuNumberLabel.textAlignment = .center
        menuNumberLabel.layer.cornerRadius = 11
        menuNumberLabel.clipsToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state.
    }
    
    
    func render(image: UIImage?, title: String?, number: Int?) {
        self.menuImageView.image = image
        self.menuTitleLabel.text = title
        if let number = number, number > 1 {
            self.menuNumberLabel.text = number.string()
            self.menuNumberLabel.backgroundColor = AppColor.orange
        }
        else {
            self.menuNumberLabel.text = nil
            self.menuNumberLabel.backgroundColor = AppColor.clear
        }
    }
}
