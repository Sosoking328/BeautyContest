//
//  BrowseParentCategoryCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/15/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class BrowseParentCategoryCollectionViewCell: UICollectionViewCell {

    static let identifier = "BrowseParentCategoryCollectionViewCell"
    static let buttonPadding: CGFloat = 10.0

    var catButtonAction:((UIButton) -> Void)?
    
    @IBOutlet weak var catButton: UIButton!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @IBAction func catButtonTapped(_ button: UIButton) {
        button.isSelected = !button.isSelected
        catButtonAction?(button)
    }
}
