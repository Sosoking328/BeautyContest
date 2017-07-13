//
//  InvitationTableViewController.swift
//  sosokan
//
//  Created by An Phan on 7/12/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import FBSDKShareKit
import TSMessages
import MessageUI

class InvitationTableViewController: UITableViewController {

    let contentSharing = "I'm using Sosokan. A COMMUNITY DRIVEN CLASSIFIEDS APP. You can find and post local classifieds and forums for your every needs such as jobs, housing, for sale, personals, services, local community, and events. And the best of it, it is FREE! Start marketing on Sosokan! Download it at below link:"
    let appStoreLink = "https://itunes.apple.com/pg/app/sosokan/id1023454229?mt=8"
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.tableFooterView = UIView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        setText()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view delegate
    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        switch indexPath.section {
        case 0:
            cell.textLabel?.text = "Nearby".localized()
        default:
            break
        }
    }
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        switch indexPath.section {
        case 0:
            break
        case 1:
            if indexPath.row == 0 { // Facebook
                let content = FBSDKAppInviteContent()
                content.appLinkURL = URL(string: "https://fb.me/1711769919072313")
                FBSDKAppInviteDialog.show(from: self, with: content, delegate: self)
            }
            else if indexPath.row == 1 {    // WeChat
                let message = WXMediaMessage()
                message.title = "Sosokan - A community driven classifieds app"
                message.description = contentSharing.localized()
                message.setThumbImage(UIImage(named: "icon-sharing"))
                
                let webpageObject = WXWebpageObject()
                webpageObject.webpageUrl = appStoreLink
                message.mediaObject = webpageObject
                
                let req = SendMessageToWXReq()
                req.message = message
                req.bText = false
                req.scene = 1
                
                WXApi.send(req)
            }
            else if indexPath.row == 2 {    // Contact
                if MFMessageComposeViewController.canSendText() {
                    let messageVC = MFMessageComposeViewController()
                    messageVC.messageComposeDelegate = self
                    messageVC.body = contentSharing.localized() + "\n\(appStoreLink)"
                    self.present(messageVC, animated: true, completion: nil)
                }
            }
            else if indexPath.row == 3 {    // Email
                if MFMailComposeViewController.canSendMail() {
                    let mailVC = MFMailComposeViewController()
                    mailVC.mailComposeDelegate = self
                    mailVC.setMessageBody(contentSharing.localized() + "\n\(appStoreLink)", isHTML: false)
                    mailVC.setSubject("Someone sent an invitation to you on Sosokan".localized())
                    present(mailVC, animated: true, completion: nil)
                }
            }
        default:
            break
        }
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 0:
            return "CONNECT NEARBY FRIENDS".localized()
        default:
            return "SELECT INVITATION MODE".localized()
        }
    }
    
    // MARK: - Methods
    
    func setText() {
        title = "CONNECT".localized()
    }
}

// MARK: - FBSDKAppInviteDialogDelegate

extension InvitationTableViewController: FBSDKAppInviteDialogDelegate {
    func appInviteDialog(_ appInviteDialog: FBSDKAppInviteDialog!, didCompleteWithResults results: [AnyHashable: Any]!) {
        if let completed = results?["didComplete"] as? Bool, completed == true {
            if let cancel = results?["completionGesture"] as? String, cancel == "cancel" {
                TSMessage.showNotification(in: self,
                                           title: "You cancelled the invitation!".localized(),
                                           subtitle: "",
                                           type: .message)
            }
            else {
                TSMessage.showNotification(in: self,
                                           title: "The invitation was sent!".localized(),
                                           subtitle: "",
                                           type: .success)
            }
        }
        else {
            TSMessage.showNotification(in: self,
                                           title: "You cancelled the invitation!".localized(),
                                           subtitle: "",
                                           type: .message)
        }
    }
    
    func appInviteDialog(_ appInviteDialog: FBSDKAppInviteDialog!, didFailWithError error: Error!) {
        TSMessage.showNotification(in: self,
                                           title: "The invitation was failed!".localized(),
                                           subtitle: "",
                                           type: .error)
    }
}

// MARK: - MFMailComposeViewControllerDelegate

extension InvitationTableViewController: MFMailComposeViewControllerDelegate {
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        var message: String?
        switch result {
        case MFMailComposeResult.cancelled:
            message = "Mail have been cancelled"
        case MFMailComposeResult.saved:
            message = "Mail have been saved"
            TSMessage.showNotification(in: self, title: message, subtitle: "", type: .message)
        case MFMailComposeResult.sent:
            message = "Mail have been sent"
            TSMessage.showNotification(in: self, title: message, subtitle: "", type: .message)
        case MFMailComposeResult.failed:
            message = "Mail have been sent fail"
            TSMessage.showNotification(in: self, title: message, subtitle: "", type: .error)
        }
        
        dismiss(animated: true, completion: nil)
    }
}

// MARK: - MFMessageComposeViewControllerDelegate

extension InvitationTableViewController: MFMessageComposeViewControllerDelegate {
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        var message: String?
        switch (result) {
        case MessageComposeResult.cancelled:
            message = "SMS have been cancelled"
        case MessageComposeResult.failed:
            message = "SMS have been sent fail"
            TSMessage.showNotification(in: self, title: message, subtitle: "", type: .error)
        case MessageComposeResult.sent:
            message = "SMS have been sent"
            TSMessage.showNotification(in: self, title: message, subtitle: "", type: .message)
        }
        dismiss(animated: true, completion: nil)
    }
}
