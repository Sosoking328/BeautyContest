//
//  BrowseCategoryCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/14/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class BrowseCategoryCollectionViewCell: UICollectionViewCell {

    static let identifier = "BrowseCategoryCollectionViewCell"
    static let buttonPadding: CGFloat = 10.0
    
    var catButtonAction:((UIButton) -> Void)?
    
    @IBOutlet weak var catButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        catButton.layer.borderWidth = 1.0
        catButton.layer.borderColor = UIColor.gray.cgColor
    }

    // MARK: - IBActions
    
    @IBAction func catButtonTapped(_ button: UIButton) {
        button.isSelected = !button.isSelected
        button.backgroundColor = button.isSelected ? UIColor.navTitleColor() : UIColor.white
        button.layer.borderColor = button.isSelected ? UIColor.navTitleColor().cgColor : UIColor.gray.cgColor
        catButtonAction?(button)
    }
    
    func setCatButtonSelected(isSelected: Bool) {
        catButton.isSelected = isSelected
        
        catButton.backgroundColor = catButton.isSelected ? UIColor.navTitleColor() : UIColor.white
        catButton.layer.borderColor = catButton.isSelected ? UIColor.navTitleColor().cgColor : UIColor.gray.cgColor
    }
}
