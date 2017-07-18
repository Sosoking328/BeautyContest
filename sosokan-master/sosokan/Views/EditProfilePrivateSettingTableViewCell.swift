//
//  EditProfilePrivateSettingTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 7/16/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class EditProfilePrivateSettingTableViewCell: UITableViewCell {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var switcher: UISwitch!
    
    // MARK: - Variable
    static let cellReuseIdentifier = "EditProfilePrivateSettingTableViewCell"
    var didChangedValue: ((UISwitch) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    @IBAction func switcherChangedValue(_ switcher: UISwitch) {
        didChangedValue?(switcher)
    }
}
