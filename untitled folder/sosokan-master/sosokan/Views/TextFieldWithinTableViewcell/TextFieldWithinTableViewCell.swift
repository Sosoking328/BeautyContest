//
//  TextFieldWithinTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 12/5/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class TextFieldWithinTableViewCell: UITableViewCell {
    
    @IBOutlet weak var textField: UITextField!

    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Add search icon in left
        let imageView = UIImageView(image: AppIcons.searchIcon)
        imageView.frame = CGRect(origin: imageView.bounds.origin, size: CGSize(width: imageView.bounds.width + 20, height: imageView.bounds.height))
        imageView.contentMode = .center
        self.textField.backgroundColor = UIColor.white
        self.textField.leftViewMode = .always
        self.textField.leftView = imageView
        self.textField.clearButtonMode = .always
        self.textField.placeholder = "Search here".localized()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
}
