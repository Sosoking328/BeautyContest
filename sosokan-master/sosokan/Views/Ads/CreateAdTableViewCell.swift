//
//  CreateAdTableViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/15/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

protocol CreateAdTableViewCellDelegate {
    func descTextViewDidChangedValue(_ textView: UITextView)
    func createAdCell(_ cell: CreateAdTableViewCell, changedValueSwitchAtIndex index: Int)
}

class CreateAdTableViewCell: UITableViewCell {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var placeholderLabel: UILabel!
    @IBOutlet weak var locationButton: UIButton!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var switcher: UISwitch!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var featureAdButton: UIButton!
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var advanceButton: UIButton!
    
    var delegate: CreateAdTableViewCellDelegate?
    var indexPath: IndexPath?
    var rightButtonAction: ((UIButton) -> Void)?
    var advanceDescButtonDidTouch: ((_ sender: AnyObject) -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        advanceButton?.setTitle("Advance".localized(), for: .normal)
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func switcherChangedValue(_ sender: UISwitch) {
        delegate?.createAdCell(self, changedValueSwitchAtIndex: indexPath!.row)
    }
    
    @IBAction func rightButtonTapped(_ sender: UIButton) {
        rightButtonAction?(sender)
    }
    
    @IBAction func advanceDescButtonDidTouch(_ sender: AnyObject) {
//        advanceDescButtonDidTouch(sender)
    }
}

// MARK: - UITextViewDelegate
extension CreateAdTableViewCell: UITextViewDelegate {
    func textViewDidChange(_ textView: UITextView) {
        if let text = textView.text, !text.isEmpty {
            placeholderLabel.isHidden = true
        }
        else {
            placeholderLabel.isHidden = false
        }
        delegate?.descTextViewDidChangedValue(textView)
    }
}
