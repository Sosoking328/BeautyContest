//
//  FAQCollectionViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/11/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class FAQCollectionViewCell: UICollectionViewCell {
    
    static let cellReuseIdentifier = "FAQTableViewCell"
    static let expand: CGFloat = 264
    static let collapse: CGFloat = 64
    
    @IBOutlet weak var backgroundSubView: UIView!
    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var seperatorView: UIView!
    @IBOutlet weak var carretIcon: UIImageView!
    
    var onButtonTapped : (() -> Void)?
    
    @IBAction func buttonTapped(_ sender: UIButton) {
        onButtonTapped?()
    }
}
