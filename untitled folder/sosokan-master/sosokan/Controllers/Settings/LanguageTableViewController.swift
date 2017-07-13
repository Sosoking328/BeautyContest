//
//  LanguageTableViewController.swift
//  sosokan
//
//  Created by An Phan on 7/22/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Localize_Swift

protocol LanguageTableViewControllerDelegate {
    func tableViewSelectedIndex(_ index: Int)
}

class LanguageTableViewController: UITableViewController {

    var delegate: LanguageTableViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view delegate
    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        // TODO: Hanle to mark ad available again.
        switch indexPath.row {
        case 0:
            cell.textLabel?.text = "🇺🇸 " + "English"
        default:
            cell.textLabel?.text = "🇨🇳 " + "Chinese (Simplified)".localized()
        }
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        dismiss(animated: false, completion: nil)
        
        switch indexPath.row {
        case 0:
            Localize.setCurrentLanguage("en")
        default:
            Localize.setCurrentLanguage("zh-Hans")
        }
        
        delegate?.tableViewSelectedIndex(indexPath.row)
    }

}
