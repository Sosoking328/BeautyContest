//
//  CreateAdOptionsViewController.swift
//  sosokan
//
//  Created by An Phan on 8/27/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

enum PremiumFeature {
    case featuredPost
    case video
    case richText
    case standOut
    
    var creditCost: Double {
        return 0.00
    }
}

class CreateAdOptionsViewController: UIViewController {

    @IBOutlet weak var closeButton: UIButton!
    @IBOutlet weak var doneButton: UIButton!
    @IBOutlet weak var totalCreditLabel: UILabel!
    @IBOutlet weak var balanceLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var titleLabel: UILabel!
    
    let items: [(String, PremiumFeature)] = [
        ("Feature my ad in the listings (Free!)".localized(), .featuredPost),
        ("Enable video upload (Free!)".localized(), .video),
        ("Enable Rich Text editing (Free!)".localized(), .richText),
        ("Bold the title of my listing (Free!)".localized(), .standOut)
    ]
    let detailItems = ["Orange border around item", "Header image is replaced by video",
                       "Color text and images in the listing", "Bold the title of my listing and stand out"]
    
    var purchasedPremiumFeatures: [PremiumFeature] = []
    var premiumFeatures: [PremiumFeature] = [] {
        didSet {
            balanceLabel?.text = String(format: "Balance".localized() + ": $%.2f", remainingCredit - totalFee)
            totalCreditLabel?.text = String(format: "Total credits".localized() + ": $%.2f", totalFee)
        }
    }
    var totalFee: Double {
        return premiumFeatures.reduce(0.0, { $0.0 + (purchasedPremiumFeatures.contains($0.1) ? 0 : $0.1.creditCost) })
    }
    var remainingCredit: Double {
        return 0
//        guard let currentUser = PFUser.currentUser(), credit = currentUser[UserKey.credit] as? Double else { return 0 }
//        return credit
    }
    var didSelectDoneButton: ((_ totalFee: Double, _ premiumFeatures: [PremiumFeature]) -> Void)!
    
    // MARK: - View life cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        totalCreditLabel.text = "Total credits".localized() + ": $0.0"
        doneButton.setTitle("Done".localized(), for: .normal)
        titleLabel.text = "Upgrade your ad and stand out!\nSpecial Promotion!Now FREE.".localized()
        tableView.layer.cornerRadius = 10.0
        doneButton.activated(true)
        
        balanceLabel.text = String(format: "Balance".localized() + ": $%.2f", remainingCredit - totalFee)
        totalCreditLabel.text = String(format: "Total credits".localized() + ": $%.2f", totalFee)

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func closeButtonTapped(_ sender: UIButton) {
        dismiss(animated: false, completion: nil)
    }

    @IBAction func doneButtonTapped(_ sender: UIButton) {
        dismiss(animated: false) {
            _ = Array.init(Set.init(self.premiumFeatures + self.purchasedPremiumFeatures))
//            self.didSelectDoneButton(totalFee: self.totalFee, premiumFeatures: finalPremiumFeatures)
        }
    }
}

// MARK: - UITableViewDataSource

extension CreateAdOptionsViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "featureAdCellID", for: indexPath) as! CreateAdTableViewCell
        let item = items[indexPath.row]
    
        cell.titleLabel?.text = item.0
        cell.selectionStyle = .none
        cell.featureAdButton.isUserInteractionEnabled = false
        cell.featureAdButton.isSelected = premiumFeatures.contains(item.1) || purchasedPremiumFeatures.contains(item.1)
        cell.descLabel.text = detailItems[indexPath.row].localized()
        switch item.1 {
        case .video, .richText:
            cell.isUserInteractionEnabled = false
        default:
            cell.isUserInteractionEnabled = !purchasedPremiumFeatures.contains(item.1)
        }
        
        return cell
    }
}

// MARK: - UITableViewDelegate

extension CreateAdOptionsViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let item = items[indexPath.row]
        
        if let index = premiumFeatures.index(of: item.1) {
            let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .destructive, handler: nil)
            
            switch item.1 {
            case .featuredPost:
                let title = "Are you sure to remove FEATURED".localized()
                let message = "The orange border around item will be removed".localized()
                let okAction = UIAlertAction(title: "Ok".localized(), style: .default, handler: { (_) in
                    self.premiumFeatures.remove(at: index)
                    DispatchQueue.main.async {
                        tableView.reloadRows(at: [indexPath], with: .automatic)
                    }
                })
                
                DispatchQueue.main.async(execute: {
                    self.showAlertWithActions(title, message: message, actions: [cancelAction, okAction])
                })
            case .video:
                let title = "Please confirm".localized()
                let message = "The post's video will be removed".localized()
                let okAction = UIAlertAction(title: "Ok".localized(), style: .default, handler: { (_) in
                    self.premiumFeatures.remove(at: index)
                    DispatchQueue.main.async {
                        tableView.reloadRows(at: [indexPath], with: .automatic)
                    }
                })
                
                DispatchQueue.main.async(execute: {
                    self.showAlertWithActions(title, message: message, actions: [cancelAction, okAction])
                })
            case .richText:
                let title = "Please confirm".localized()
                let message = "The post's rich text will be removed".localized()
                let okAction = UIAlertAction(title: "Ok".localized(), style: .default, handler: { (_) in
                    self.premiumFeatures.remove(at: index)
                    
                    DispatchQueue.main.async {
                        tableView.reloadRows(at: [indexPath], with: .automatic)
                    }
                })
                
                DispatchQueue.main.async(execute: {
                    self.showAlertWithActions(title, message: message, actions: [cancelAction, okAction])
                })
            case .standOut:
                let title = "Please confirm".localized()
                let message = "This post may not on top too long".localized()
                let okAction = UIAlertAction(title: "Ok".localized(), style: .default, handler: { (_) in
                    self.premiumFeatures.remove(at: index)
                    
                    tableView.reloadRows(at: [indexPath], with: .automatic)
                })
                
                DispatchQueue.main.async(execute: { 
                    self.showAlertWithActions(title, message: message, actions: [cancelAction, okAction])
                })
            }
        }
        else {
            if remainingCredit < totalFee + item.1.creditCost {
                DispatchQueue.main.async(execute: {
                    self.showOkeyMessage("Balance is not enough".localized(), message: "Please buy more credits to continue".localized())
                })
            }
            else {
                premiumFeatures.append(item.1)
                DispatchQueue.main.async(execute: {
                    tableView.reloadRows(at: [indexPath], with: .automatic)
                })
            }
        }
    }
}
