//
//  EditPopoverTableViewController.swift
//  sosokan
//
//  Created by An Phan on 7/8/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

protocol EditPopoverTableViewControllerDelegate {
    func tableViewSelectedIndex(_ index: Int)
    func extendExpirationTimeAtIndex(_ index: Int)
}

class EditPopoverTableViewController: UITableViewController {
    
    var delegate: EditPopoverTableViewControllerDelegate?
    var isExpiration = false
    
    fileprivate struct ReuseIdentifiers {
        static let cell: String = "cell"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: ReuseIdentifiers.cell)
        self.tableView.rowHeight = 64
        self.tableView.tableFooterView = UIView.init(frame: CGRect.zero)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ReuseIdentifiers.cell, for: indexPath)
        if isExpiration == true {
            switch indexPath.row {
            case 0:
                cell.textLabel?.text = "Extend 1 month".localized()
            case 1:
                cell.textLabel?.text = "Extend 3 months".localized()
            case 2:
                cell.textLabel?.text = "Extend 6 months".localized()
            default:
                cell.textLabel?.text = "Extend 1 year".localized()
            }
        }
        else {
            switch indexPath.row {
            case 0:
                cell.textLabel?.text = "EXPIRE".localized()
            case 1:
                cell.textLabel?.text = "EDIT".localized()
            case 2:
                cell.textLabel?.text = "DELETE".localized()
            default:
                cell.textLabel?.text = "FLAG".localized()
            }
        }

        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        dismiss(animated: false, completion: nil)
        
        if isExpiration == true {
            delegate?.extendExpirationTimeAtIndex(indexPath.row)
        }
        else {
            delegate?.tableViewSelectedIndex(indexPath.row)
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 4
    }
}
