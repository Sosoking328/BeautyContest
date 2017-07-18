//
//  ShareWithFacebook.swift
//  sosokan
//
//  Created by An Phan on 12/14/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import FBSDKShareKit

struct CustomActivityIdentifier {
    static let shareWithFacebook = "sosokan.shareWithFacebook"
}

class ShareWithFacebook: UIActivity {
    
    var customAction: (() -> Void)?
    
    override class var activityCategory : UIActivityCategory {
        return .share
    }
    
    override var activityType: UIActivityType? {
        return UIActivityType(rawValue: CustomActivityIdentifier.shareWithFacebook)
    }
    
//    override var activityType : String? {
//        return CustomActivityIdentifier.shareWithFacebook
//    }
    
    override var activityTitle : String? {
        return "Facebook"
    }
    
    override func canPerform(withActivityItems activityItems: [Any]) -> Bool {
        NSLog("%@", #function)
        return true
    }
    
    override func prepare(withActivityItems activityItems: [Any]) {
        NSLog("%@", #function)
    }
    
    override var activityViewController : UIViewController? {
        NSLog("%@", #function)
        return nil
    }
    
    override func perform() {
        // Todo: handle action:
        NSLog("%@", #function)
        
        self.customAction?()
        self.activityDidFinish(true)
    }
    
    override var activityImage : UIImage? {
        return UIImage(named: "facebookActivityIcon")
    }
}
