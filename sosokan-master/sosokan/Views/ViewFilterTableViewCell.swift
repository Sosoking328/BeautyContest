//
//  ViewFilterTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 6/25/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

class ViewFilterTableViewCell: UITableViewCell {

    // Use as global variable
    static let cellRuseIdentifier = "ViewFilterTableViewCell"
    
    // Define for control this view
    let defaultButtonIconColor = UIColor.rgbaColor(red: 55, green: 50, blue: 48, alpha: 0.3)
    let selectedButtonIconColor = AppColor.orange
    var button1Action: () -> Void = {}
    var button2Action: () -> Void = {}
    var buttonGroup: [Button] {
        return [rightButton1, rightButton2]
    }
    
    // MARK: - IBOutlets
    @IBOutlet weak var leftLabel: UILabel!
    @IBOutlet weak var rightButton1: Button!
    @IBOutlet weak var rightButton2: Button!
    
    // MARK: - IBActions
    @IBAction func rightButton1Tapped(_ sender: Button) {
        // Change view apperance
        setActiveButton(sender)
        
        // Action from controller
        button1Action()
    }
    
    @IBAction func rightButton2Tapped(_ sender: Button) {
        // Change view apperance
        setActiveButton(sender)
        
        // Action from controller
        button2Action()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Setup label on left
        leftLabel.font = UIFont.latoBold(size: 14)
        leftLabel.textColor = AppColor.textPrimary
        
        // Setup buttons
        let listImage = UIImage(named: "viewByListIcon")?.withRenderingMode(.alwaysTemplate)
        rightButton1.setImage(listImage, for: .normal)
        rightButton2.setImage(listImage, for: .highlighted)
        
        let gridImage = UIImage(named: "viewByGridIcon")?.withRenderingMode(.alwaysTemplate)
        rightButton2.setImage(gridImage, for: .normal)
        rightButton2.setImage(gridImage, for: .highlighted)
        
        buttonGroup.forEach { (button) in
            button.tintColor = defaultButtonIconColor
            button.imageView?.contentMode = .scaleAspectFit
            button.setTitle(nil, for: .normal)
            button.backgroundColor = UIColor.rgbaColor(red: 55, green: 50, blue: 48, alpha: 0.05)
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        buttonGroup.forEach { $0.layer.cornerRadius = 3 }
    }
    
    // MAKR: Methods
    func setActiveButton(_ button: Button) {
        buttonGroup.forEach { $0.tintColor = defaultButtonIconColor }
        button.tintColor = selectedButtonIconColor
    }
}
