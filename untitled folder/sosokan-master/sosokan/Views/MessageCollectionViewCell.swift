//
//  MessageCollectionViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 7/11/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import JSQMessagesViewController

class MessageCollectionViewCell: JSQMessagesCollectionViewCell {
    
    // MARK: Variable
    
    // MARK: IBOutlet
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var contentLabel: UILabel!
    
    // MARK: View life circle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        
    }
}
