//
//  CreditsTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 8/27/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class CreditsTableViewCell: UITableViewCell {
    
    static let cellID = "CreditsCellID"

    @IBOutlet weak var getCreditButton: UIButton!
    @IBOutlet weak var creditTitleLabel: UILabel!
    
    var getCreditAction: ((UIButton) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        getCreditButton.layer.cornerRadius = 3.0
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func getCreditButtonTapped(_ sender: UIButton) {
        getCreditAction?(sender)
    }
}
