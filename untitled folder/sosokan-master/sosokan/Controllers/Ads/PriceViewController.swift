//
//  PriceViewController.swift
//  sosokan
//
//  Created by An Phan on 6/23/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit

class PriceViewController: UIViewController {

    @IBOutlet weak var priceTextField: UITextField!
    @IBOutlet weak var freeButton: UIButton!
    @IBOutlet weak var offerButton: UIButton!
    @IBOutlet weak var priceTitleLabel: UILabel!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var stepLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        offerButton.layer.cornerRadius = 23
        offerButton.titleLabel?.lineBreakMode = .byWordWrapping
        offerButton.titleLabel?.numberOfLines = 2
        offerButton.titleLabel?.textAlignment = .center
        offerButton.addBorderWithColor(AppColor.border, width: 1.0)
        
        freeButton.layer.cornerRadius = 23
        freeButton.addBorderWithColor(AppColor.border, width: 1.0)
        
        priceTextField.layer.cornerRadius = 15
        priceTextField.addBorderWithColor(AppColor.border, width: 1.0)
        priceTextField.font = UIFont.avenirLTStdRoman(size: 100)
        
        nextButton.layer.cornerRadius = 7.0
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - IBActions
    
    @IBAction func textFieldEditingChanged(_ sender: Any) {
        if let text = priceTextField.text, !text.hasPrefix("$") {
            priceTextField.text = "$" + text
        }
    }

    @IBAction func offerButtonTapped(_ button: UIButton) {
        button.isSelected = !button.isSelected
        button.backgroundColor = button.isSelected ? UIColor.navTitleColor() : UIColor.white
        priceTextField.isHidden = false
        priceTitleLabel.isHidden = false
        
        freeButton.isSelected = false
        freeButton.backgroundColor = UIColor.white
    }
    
    @IBAction func freeButtonTapped(_ button: UIButton) {
        button.isSelected = !button.isSelected
        button.backgroundColor = button.isSelected ? UIColor.navTitleColor() : UIColor.white
        
        priceTextField.isHidden = true
        priceTitleLabel.isHidden = true
        
        offerButton.isSelected = false
        offerButton.backgroundColor = UIColor.white
    }
}
