//
//  ReportViewController.swift
//  sosokan
//
//  Created by An Phan on 1/11/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import SwiftyJSON
import Alamofire

class ReportViewController: UIViewController {
    
    // MARK: IBOutlet
    
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: Variable
    
    fileprivate var reportMenuItems: [ReportMenuItem] = []
    fileprivate var selectedReportMenuItem: ReportMenuItem?
    fileprivate var reportReasons: [ReportReason] = []
    fileprivate var reportContent: String?
    
    var postId: Int!
    
    fileprivate struct Constants {
        static let selectCell = "ReportMenuItem"
        static let textCell = "TextViewTableViewCell"
        static let textCellHeight: CGFloat = 100
        static let estimatedCellHeight: CGFloat = 50
    }
    
    // MARK: View controller life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        prepareSendBarButton()
        prepareTableView()
        
        fetchReasons()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        parepareNavigationBar()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Method
    func rightBarButtonTapped(sender: Any) {
        guard let reportReason = selectedReportMenuItem?.reason,
            let currentUser = User.currentUser()
            else { return }
        (sender as? UIBarButtonItem)?.isEnabled = false
        ReportService.shared.sendReport(post: postId, user: currentUser.id, reason: reportReason.id, content: reportContent, onSuccess: {
            let okAction = UIAlertAction(title: "Ok".localized(), style: .default, handler: { (action) in
                _ = self.navigationController?.popViewController(animated: true)
            })
            let actions = [okAction]
            self.showAlert("Sosokan", message: "Thank you for your support!".localized(), style: .alert, actions: actions)
        }, onError: { (error) in
            (sender as? UIBarButtonItem)?.isEnabled = true
            self.showErrorMessage(Messages.occurredError)
        })
        //        let selected = selectedReportMenuItem.asObservable().map({ $0.value != nil })
        //        let excuting = Variable.false)
        //        let enabled = Observable.combineLatest(selected, excuting.asObservable()) { (selected, excuting) -> Bool in
        //            if excuting {
        //                return false
        //            }
        //            return selected
        //        }
        //
        //        rightBarButton.rx_action = Action.enabledIf: enabled, workFactory: { input in
        //
        //            if let reason = selectedReportMenuItem.value?.reason,
        //                currentUser = DataManager.sharedInstance.currentUser.value {
        //
        //                let postId = fetchPostId(withFirebaseId: firebasePostId).errorOnNil()
        //                let userId = fetchUserId(withFirebaseId: String(currentUser.id)).errorOnNil()
        //                let userToken = userId.flatMap({ fetchUserToken(withUserId: $0) }).errorOnNil()
        //
        //                showNetworkIndicator()
        //                navigationItem.leftBarButtonItem?.enabled = false
        //                excuting.value = true
        //
        //                Observable.combineLatest(postId, userId, userToken, resultSelector: { ($0.0, $0.1, $0.2) })
        //                    .debug()
        //                    .flatMap({ sendReport(withPostId: $0.0, userId: $0.1, reasonId: reason.id, content: reportContent, userToken: $0.2) } )
        //                    .subscribe(onNext: { [weak self] (response, json) in
        //                        guard let `self` = self else { return }
        //                        debugPrint(response)
        //                        debugPrint(json)
        //                        if let code = response?.statusCode where !ERROR_CODES.contains(code) {
        //                            showAlert(title: "Sosokan", message: "Thank you for your support!".localized(), style: .Alert, actions: [
        //                                UIAlertAction.title: "Ok".localized(), style: .Default, handler: { (action) in
        //                                    if let navigation = navigationController {
        //                                        navigation.popViewControllerAnimated(true)
        //                                    }
        //                                    else {
        //                                        dismissViewControllerAnimated(true, completion: nil)
        //                                    }
        //                                })
        //                                ])
        //                        }
        //                        else {
        //                            showErrorMessage(Messages.occurredError)
        //                        }
        //                        navigationItem.leftBarButtonItem?.enabled = true
        //                        hideNetworkIndicator()
        //                        }, onError: { [weak self] (error) in
        //                            guard let `self` = self else { return }
        //                            excuting.value = false
        //                            showErrorMessage(Messages.occurredError)
        //                            navigationItem.leftBarButtonItem?.enabled = true
        //                            hideNetworkIndicator()
        //                        })
        //                    .addDisposableTo(rx_disposeBag)
        //            }
        //            
        //            return .empty()
        //        })

    }
    
    // MARK: File's private method
    
    fileprivate func parepareNavigationBar() {
        title = "Report this listing".localized()
        
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.white]
        navigationController?.navigationBar.tintColor = AppColor.white
        navigationController?.navigationBar.barTintColor = AppColor.orange
        navigationController?.navigationBar.isTranslucent = false
    }
    
    fileprivate func prepareTableView() {
        let nib = UINib(nibName: Constants.textCell, bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: Constants.textCell)
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: Constants.selectCell)
        tableView.tableFooterView = UIView(frame: CGRect.zero)
        tableView.backgroundColor = UIColor(hexString: "f3f3f3")
        tableView.tableFooterView = UIView(frame: CGRect.zero)
        tableView.backgroundColor = AppColor.white
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    fileprivate func prepareSendBarButton() {
        let rightBarButton = UIBarButtonItem(title: "Send".localized(),
                                             style: .plain,
                                             target: self, action: #selector(rightBarButtonTapped(sender:)))
        rightBarButton.tintColor = AppColor.white
        rightBarButton.isEnabled = false
        navigationItem.rightBarButtonItem = rightBarButton
    }
    
    fileprivate func fetchReasons() {
        showNetworkIndicator()
        ReportService.shared.fetchReasons(onSuccess: { (reasons) in
            self.hideNetworkIndicator()
            self.reportReasons = reasons ?? []
            self.reportMenuItems = self.reportReasons.map({ ReportMenuItem(reason: $0, title: $0.reason, type: .select) })
            self.tableView.reloadData()
        }, onError: { (error) in
            self.hideNetworkIndicator()
            self.showErrorMessage(Messages.occurredError)
        })
    }
    
    fileprivate func reasonTextViewValueChanged(_ reportContent: String) {
        self.reportContent = reportContent
    }
}

// MARK: - UITableViewDataSource

extension ReportViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return reportReasons.count
        case 1:
            return 1
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: Constants.selectCell, for: indexPath)
            let reportMenuItem = reportMenuItems[indexPath.row]
            cell.textLabel?.text = reportMenuItem.title
            if let selectedReportMenuItem = selectedReportMenuItem, selectedReportMenuItem == reportMenuItem {
                cell.accessoryType = .checkmark
            }
            else {
                cell.accessoryType = .none
            }
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier:Constants.textCell, for: indexPath) as! TextViewTableViewCell
            cell.placeHolderLabel.text = "Why are you reporting this listing? (Optional)"
            cell.textView.text = ""
            cell.textViewValueChanged = reasonTextViewValueChanged
            return cell
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate

extension ReportViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return Constants.estimatedCellHeight
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 1 {
            return Constants.textCellHeight
        }
        return UITableViewAutomaticDimension
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 0
        }
        return 15
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.section {
        case 0:
            let menuItem = reportMenuItems[indexPath.row]
            selectedReportMenuItem = menuItem
            tableView.reloadData()
            navigationItem.rightBarButtonItem?.isEnabled = true
        default:
            guard let cell = tableView.cellForRow(at: indexPath) as? TextViewTableViewCell
                else { return }
            cell.textView.becomeFirstResponder()
        }
    }
}

// MARK: - Convenience init

extension ReportViewController {
    convenience init(postId: Int) {
        self.init(nibName: "ReportViewController", bundle: nil)
        self.postId = postId
    }
}
