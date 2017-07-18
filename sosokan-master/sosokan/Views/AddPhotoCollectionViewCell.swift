//
//  AddPhotoCollectionViewCell.swift
//  sosokan
//
//  Created by An Phan on 6/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class AddPhotoCollectionViewCell: UICollectionViewCell {
    
    static let idenfitier = "AddPhotoCollectionViewCell"
    
    @IBOutlet weak var deleteButton: UIButton!
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var photoImageView: UIImageView!
    
    var deleteButtonAction:(() -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        contentView.layer.cornerRadius = 7
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        photoImageView.image = nil
        photoButton.isHidden = false
        deleteButton.isHidden = true
    }
    
    @IBAction func deleteButtonTapped(_ sender: Any) {
        deleteButtonAction?()
    }
}
