//
//  EditProfileTwoTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/29/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class EditProfileTwoTableViewCell: UITableViewCell {
    // MARK: Variables
    static let cellReuseIdentifier = "EditProfileTwoTableViewCell"
    var leftAction: (() -> Void)!
    var leftDidEndEditingAction: ((UITextField) -> Void)?
    var rightAction: (() -> Void)!
    var rightDidEndEditingAction: ((UITextField) -> Void)?
    
    // MARK: IBOulet
    @IBOutlet weak var separatorView: UIView!
    @IBOutlet weak var leftTitleLabel: UILabel!
    @IBOutlet weak var leftTextField: UITextField!
    @IBOutlet weak var rightTitleLabel: UILabel!
    @IBOutlet weak var rightTextField: UITextField!
    
    
    // MARK: View life circle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        separatorView.backgroundColor = AppColor.border
        
        [leftTitleLabel, rightTitleLabel].forEach { (l) in
            l?.font = UIFont.latoBold(size: 14)
            l?.textColor = AppColor.brown
        }
        
        [leftTextField, rightTextField].forEach { (tf) in
            tf?.borderStyle = .none
            tf?.textColor = AppColor.brown
            tf?.delegate = self
        }
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(handleTapOnCell(_:)))
        addGestureRecognizer(tap)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    // Mark: Methods
    func handleTapOnCell(_ sender: UITapGestureRecognizer) {
        let cellWidth = frame.size.width
        
        if sender.location(in: self).x > cellWidth / 2 {
            rightAction()
        } else {
            leftAction()
        }
    }
    
    func focusOnLeftTextField(_ isFocus: Bool) {
        rightTextField.resignFirstResponder()
        
        if isFocus {
            leftTextField.becomeFirstResponder()
        } else {
            leftTextField.resignFirstResponder()
        }
    }
    
    func focusOnRightTextField(_ isFocus: Bool) {
        leftTextField.resignFirstResponder()
        
        if isFocus {
            rightTextField.becomeFirstResponder()
        } else {
            rightTextField.resignFirstResponder()
        }
    }
}

// MARK: UITextFieldDelegate
extension EditProfileTwoTableViewCell: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == leftTextField {
            leftDidEndEditingAction?(textField)
        } else {
            rightDidEndEditingAction?(textField)
        }
    }
}
