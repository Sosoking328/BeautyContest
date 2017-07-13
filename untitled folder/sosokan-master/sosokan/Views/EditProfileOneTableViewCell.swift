//
//  EditProfileOneTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/29/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class EditProfileOneTableViewCell: UITableViewCell {
    
    // MARK: Variables
    static let cellReuseIdentifier = "EditProfileOneTableViewCell"
    var didEndEditingAction: ((UITextField) -> Void)?
    
    // MARK: IBOutlets
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var entryTextField: UITextField!
    
    
    // MARK: View life circle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        titleLabel.font = UIFont.latoBold(size: 14)
        titleLabel.textColor = AppColor.brown
        
        entryTextField.borderStyle = .none
        entryTextField.textColor = AppColor.brown
        entryTextField.delegate = self
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    
    // Mark: Methods
    func focusOnTextField(_ isFocus: Bool) {
        if isFocus {
            entryTextField.becomeFirstResponder()
        } else {
            entryTextField.resignFirstResponder()
        }
    }
}

// MARK: UITextFieldDelegate
extension EditProfileOneTableViewCell: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        didEndEditingAction?(textField)
    }
}
