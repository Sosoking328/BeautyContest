//
//  WebViewViewController.swift
//  sosokan
//
//  Created by An Phan on 12/3/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class WebViewViewController: UIViewController {

    @IBOutlet weak var webView: UIWebView!
    
    var url: URL?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        webView.loadRequest(URLRequest(url: url!))
        
        if isModal() {
            let closeBarButton = UIBarButtonItem(title: "Done".localized(),
                                                 style: .plain,
                                                 target: self,
                                                 action: #selector(dismissVC))
            navigationItem.rightBarButtonItem = closeBarButton
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Helpers
    func dismissVC() {
        self.dismiss(animated: true, completion: nil)
    }
}
