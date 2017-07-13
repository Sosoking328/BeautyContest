//
//  CommentWriteNewTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 2/6/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class CommentWriteNewTableViewCell: UITableViewCell {

    @IBOutlet weak var commentContentTextField: UITextField!
    @IBOutlet weak var sendCommentButton: Button!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    fileprivate func initialize() {
        self.sendCommentButton.setImage(AppIcons.writeCommentIcon, for: .normal)
        self.sendCommentButton.imageView?.contentMode = .scaleAspectFit
    }
    
    func render(withPlaceHolder placeHolder: String?, commentContent: String?) {
        self.commentContentTextField.placeholder = placeHolder
        self.commentContentTextField.text = commentContent
    }
}
