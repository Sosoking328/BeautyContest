//
//  ConversationTableViewCell.swift
//  sosokan
//
//  Created by David Nguyentan on 7/10/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material
import JSQMessagesViewController

class ConversationTableViewCell: TableViewCell {

    // MARK: Variable
    static let cellReuseIdentifier = "ConversationTableViewCell"
    static let cellDefaultRowHeight = CGFloat(70)
    
    // MARK: IBOutlet
    @IBOutlet var userImage: UIImageView!
    @IBOutlet var descriptionLabel: UILabel!
    @IBOutlet var lastMessageLabel: UILabel!
    @IBOutlet var timeElapsedLabel: UILabel!
    @IBOutlet var counterLabel: UILabel!
    
    func render(_ conversation: FConversation) {
        userImage.sd_setImage(with: URL(string: conversation.postHeaderImageURL), placeholderImage: UIImage.init(named: "no-avatar"))
        descriptionLabel.text = conversation.postName
        lastMessageLabel.text = conversation.lastMessageSentBy + ": " + conversation.lastMessageContent
        
        let lastMessageAt = conversation.lastMessageTimestamp.toNSDate()
        let timestampLabel = JSQMessagesTimestampFormatter.shared().relativeDate(for: lastMessageAt)
        if timestampLabel == "Today" {
            timeElapsedLabel.text = JSQMessagesTimestampFormatter.shared().time(for: lastMessageAt)
        } else {
            timeElapsedLabel.text = timestampLabel
        }
        
        /*
        if conversation.unreadLastMessageUsers.contains(where: { $0.0 == FIRAuth.auth()?.currentUser?.uid }) {
            self.counterLabel.text = "New".localized()
        }
        else {
            self.counterLabel.text = nil
        }
         */
    }

    
    // MARK: View life circle
    override func awakeFromNib() {
        super.awakeFromNib()
        
        userImage.roundify()
        
        descriptionLabel.font = UIFont.latoBold(size: 14)
        descriptionLabel.textColor = AppColor.textPrimary
        
        lastMessageLabel.font = UIFont.latoRegular(size: 12)
        lastMessageLabel.textColor = AppColor.textSecondary
        
        counterLabel.text = nil
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
}
