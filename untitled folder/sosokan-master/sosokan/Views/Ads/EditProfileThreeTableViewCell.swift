//
//  EditProfileThreeTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/29/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class EditProfileThreeTableViewCell: UITableViewCell {

    // MARK: Variables
    static let cellReuseIdentifier = "EditProfileThreeTableViewCell"
    var checkIV: UIImageView!
    var baseIndent: CGFloat = 18
    
    // MARK: IBOutlets
    @IBOutlet weak var indentConstraint: NSLayoutConstraint!
    @IBOutlet weak var checkView: UIView!
    @IBOutlet weak var titleLabel: UILabel!
    
    // MARK: View life circle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .none
        
        checkView.setNeedsLayout()
        checkView.layoutIfNeeded()
        checkView.layer.cornerRadius = checkView.layer.frame.width / 2
        checkView.layer.borderWidth = 1
        checkView.layer.borderColor = AppColor.border.cgColor
        checkView.clipsToBounds = true
        
        checkIV = UIImageView(frame: CGRect(origin: CGPoint(x: 0, y: 0), size: checkView.frame.size))
        checkIV.image = UIImage(named: "check-marked")
        checkIV.contentMode = .scaleAspectFill
        checkIV.alpha = 0
        
        checkView.addSubview(checkIV)
        
        titleLabel.font = UIFont.latoBold(size: 14)
        titleLabel.textColor = AppColor.brown
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

    func checked(_ isChecked: Bool) {
        checkIV.alpha = isChecked ? 1.0 : 0.0
    }
    
    func indented(_ level: Int) {
        indentConstraint.constant = baseIndent * CGFloat(level)
    }
}
