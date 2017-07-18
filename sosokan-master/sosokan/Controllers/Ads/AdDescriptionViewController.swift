//
//  AdDescriptionViewController.swift
//  sosokan
//
//  Created by An Phan on 8/29/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class AdDescriptionViewController: UIViewController /*ZSSRichTextEditor*/ {
    /*
    @IBOutlet weak var doneButton: UIBarButtonItem!
    
    var finishedComposedDescription: ((String) -> Void)?
    var desc: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        doneButton.title = "Done".localized()
        
        self.enabledToolbarItems = [ZSSRichTextEditorToolbarBold, ZSSRichTextEditorToolbarItalic, ZSSRichTextEditorToolbarUnorderedList, ZSSRichTextEditorToolbarOrderedList, ZSSRichTextEditorToolbarTextColor, ZSSRichTextEditorToolbarH1, ZSSRichTextEditorToolbarH2, ZSSRichTextEditorToolbarH3, ZSSRichTextEditorToolbarH4, ZSSRichTextEditorToolbarH5, ZSSRichTextEditorToolbarH6, ZSSRichTextEditorToolbarInsertImageFromDevice, ZSSRichTextEditorToolbarInsertLink]
        if let htmlString = desc {
            setHTML(htmlString)
        }
        
        /*
        let button = UIButton.init()
        button.setTitle("AAA", forState: .Normal)
        button.sizeToFit()
        button.rx_action = Action.init(workFactory: { input in
            self.insertHTML("<iframe width=\"300\" height=\"300\" src=\"https://www.youtube.com/embed/450p7goxZqg\" frameborder=\"0\" allowfullscreen></iframe>")

            return .empty()
        })
        self.addCustomToolbarItemWithButton(button)
        */
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        prepareNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    // MARK: - Private methods
    fileprivate func prepareNavigationBar() {
        // Navigation
        title = "Description".localized()
        navigationController?.navigationBar.isTranslucent = false
        navigationController?.navigationBar.barStyle = .default
        navigationController?.navigationBar.setBackgroundImage(nil, for: .default)
        navigationController?.navigationBar.shadowImage = nil
        navigationController?.navigationBar.tintColor = UIColor.mainColor()
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.orange]
        setNeedsStatusBarAppearanceUpdate()
    }
    
    // MARK: - IBActions
    @IBAction func doneButtonTapped(_ sender: UIBarButtonItem) {
        finishedComposedDescription?(getHTML())
        _ = navigationController?.popViewController(animated: true)
    }
     */
}
