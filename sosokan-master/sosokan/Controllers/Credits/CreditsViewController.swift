////
////  CreditsViewController.swift
////  sosokan
////
////  Created by An Phan on 8/27/16.
////  Copyright © 2016 icanstudioz inc. All rights reserved.
////
//
//import UIKit
//import StoreKit
//
//class CreditsViewController: UIViewController {
//
//    @IBOutlet weak var tableView: UITableView!
//    @IBOutlet weak var balanceLabel: UILabel!
//    @IBOutlet weak var titleLabel: UILabel!
//    
//    private var purchaseHelper: IAPHelper?
//    private var products = [SKProduct]() {
//        didSet {
//            tableView.reloadData()
//        }
//    }
//    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        title = "Get Credits".localized()
//        titleLabel.text = "Get Credits".localized()
//        if let user = PFUser.currentUser(), credit = user[UserKey.credit] as? Double {
//            balanceLabel.text = "Balance".localized() + ": $\(credit)"
//        }
//        else {
//            balanceLabel.text = "Balance".localized() + ": $0.00"
//        }
//        
//        AppHelper.showLoadingGIF() // Show hud
//        purchaseHelper = IAPHelper(productIds: [IAPIdentifier.creditTier2, IAPIdentifier.creditTier5,
//            IAPIdentifier.creditTier10, IAPIdentifier.creditTier20])
//        purchaseHelper?.requestProducts({ (success, products) in
//            if success {
//                AppHelper.hideLoadingGIF() // Hide hud
//                if let products = products {
//                    self.products = products.sort({ Double($0.price) < Double($1.price) })
//                }
//            }
//            else {
//                self.showAlertWithActions("Error!".localized(), message: "Try again!".localized(), actions: nil)
//            }
//        })
//    }
//    
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//    
//    @IBAction func backButtonTapped(sender: UIBarButtonItem) {
//        dismissViewControllerAnimated(true, completion: nil)
//    }
//}
//
//// MARK: - UITableViewDataSource
//
//extension CreditsViewController: UITableViewDataSource {
//    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
//        return 1
//    }
//    
//    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        return products.count
//    }
//    
//    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCellWithIdentifier(CreditsTableViewCell.cellID,
//                                                               forIndexPath: indexPath) as! CreditsTableViewCell
//        let product = products[indexPath.row]
//        
//        cell.creditTitleLabel.text = product.localizedTitle
//        cell.getCreditButton.setTitle("$\(product.price)", forState: .Normal)
//        cell.getCreditAction = { button in
//            AppHelper.showLoadingGIF()
//            self.purchaseHelper?.buyProduct(product, purchasedHandler: { (canceled, success, identifier) in
//                AppHelper.hideLoadingGIF() // Hide hud
//                
//                if canceled {
//                    // Do nothing
//                } else if success {
//                    // Update user credit
//                    print(identifier)
//                    if let user = PFUser.currentUser(), identifier = identifier{
//                        var credit = user[UserKey.credit] as? Double ?? 0.0
//                        switch identifier {
//                        case IAPIdentifier.creditTier2:
//                            credit += 1.99
//                        case IAPIdentifier.creditTier5:
//                            credit += 5.49
//                        case IAPIdentifier.creditTier10:
//                            credit += 10.99
//                        case IAPIdentifier.creditTier20:
//                            credit += 24.99
//                        default:
//                            // Do nothing
//                            break
//                        }
//                        user[UserKey.credit] = credit
//                        user.saveInBackgroundWithBlock({ (finished, error) in
//                            if finished {
//                                self.dismissViewControllerAnimated(true, completion: nil)
//                            }
//                            else {
//                                self.showAlertWithActions("Error!".localized(), message: "Try again!".localized(), actions: nil)
//                            }
//                        })
//                    }
//                } else {
//                    self.showAlertWithActions("Error!".localized(), message: "Try again!".localized(), actions: nil)
//                }
//            })
//        }
//        
//        return cell
//    }
//}
//
//// MARK: - UITableViewDelegate
//
//extension CreditsViewController: UITableViewDelegate {
//    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
//        tableView.deselectRowAtIndexPath(indexPath, animated: true)
//    }
//}
