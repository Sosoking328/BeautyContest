//
//  CommentOptionsTableViewController.swift
//  sosokan
//
//  Created by A on 2/12/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit

typealias CommentOptionsTableCell = (UIImage?, String, () -> Void)

class CommentOptionsTableViewController: UITableViewController {
    
    fileprivate struct Constants {
        static let optionCell = "optionCell"
        static let iconSize = CGFloat.init(22)
    }
    
    var options: [CommentOptionsTableCell] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: Constants.optionCell)
        self.tableView.rowHeight = 44
        self.tableView.isScrollEnabled = false
        self.tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        self.tableView.separatorStyle = .none
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return options.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.optionCell, for: indexPath)
        let option = self.options[indexPath.row]
        
        cell.imageView?.image = option.0?.resize(toWidth: Constants.iconSize)?.withRenderingMode(.alwaysTemplate)
        cell.imageView?.tintColor = AppColor.grey
        cell.textLabel?.text = option.1
        cell.textLabel?.textColor = AppColor.textPrimary
        
        if options.count != 1 && option.0 == self.options.last?.0 && option.1 == self.options.last?.1 {
            cell.removeBorder(withDirection: .bottom)
        }
        else {
            cell.addBorders(withDirections: [.bottom], color: AppColor.border, width: 1)
        }

        return cell
    }

    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let option = self.options[indexPath.row]
        option.2()
        self.dismiss(animated: true, completion: nil)
    }
    
}
