//
//  BottomBarView.swift
//  sosokan
//

import UIKit

class BottomBarView: UIView {
    
    @IBOutlet weak var browseButton: UIButton!
    @IBOutlet weak var homeLabel: UILabel!
    @IBOutlet weak var browseSlashView: UIView!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var browseLabel: UILabel!
    @IBOutlet weak var favoriteSlashView: UIView!
    @IBOutlet weak var messageButton: UIButton!
    @IBOutlet weak var messageLabel: UILabel!
    @IBOutlet weak var messageSlashView: UIView!
    @IBOutlet weak var profileButton: UIButton!
    @IBOutlet weak var profileLabel: UILabel!
    
    var browseButtonAction:(() -> Void)?
    var homeButtonAction:(() -> Void)?
    var messageButtonAction:(() -> Void)?
    var profileButtonAction:(() -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.initialize()
    }
    
    @IBAction func homeButtonTapped(_ sender: Any) {
        homeButtonAction?()
    }
    
    @IBAction func browseButtonTapped(_ sender: Any) {
        browseButtonAction?()
    }
    
    @IBAction func messageButtonTapped(_ sender: Any) {
        messageButtonAction?()
    }
    
    @IBAction func profileButtonTapped(_ sender: Any) {
        profileButtonAction?()
    }
    
    // MARK: Private method
    
    fileprivate func initialize() {
        self.clipsToBounds = true
        
        [self.homeLabel, self.browseLabel, self.messageLabel, self.profileLabel].forEach({
            $0!.font = UIFont.avenirLTStdRoman(size: AppFontsizes.extraSmall)
            $0!.text = $0!.text!.localized()
        })
        
        [self.browseSlashView, self.favoriteSlashView, self.messageSlashView].forEach({
            $0!.backgroundColor = AppColor.border
        })
    }
}
