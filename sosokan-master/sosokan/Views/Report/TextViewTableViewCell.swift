//
//  TextViewTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 1/11/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit

class TextViewTableViewCell: UITableViewCell {
    
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var placeHolderLabel: UILabel!
    
    var textViewValueChanged: ((String) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    fileprivate func initialize() {
        self.placeHolderLabel.numberOfLines = 0
        self.placeHolderLabel.lineBreakMode = .byWordWrapping
        self.placeHolderLabel.textColor = AppColor.textSecondary
        
        textView.delegate = self
    }
}

// MARK: - UITextViewDelegate

extension TextViewTableViewCell: UITextViewDelegate {
    func textViewDidChange(_ textView: UITextView) {
        placeHolderLabel.alpha = textView.text.isEmpty ? 1 : 0
        textViewValueChanged?(textView.text)
    }
}
