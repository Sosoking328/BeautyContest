//
//  DescriptionViewController.swift
//  sosokan
//
//  Created by An Phan on 6/23/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import RichEditorView

class DescriptionViewController: UIViewController {

    @IBOutlet weak var catButton: UIButton!
    @IBOutlet weak var conditionLabel: UILabel!
    @IBOutlet weak var conditionValueLabel: UILabel!
    @IBOutlet weak var conditionSlider: UISlider!
    @IBOutlet weak var descTitleLabel: UILabel!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var stepLabel: UILabel!
    @IBOutlet weak var richEdittorContainerView: UIView!
    
    fileprivate let inputViewPadding: CGFloat = 5
    
    lazy var toolbar: RichEditorToolbar = {
        let toolbar = RichEditorToolbar(frame: CGRect(x: 0, y: 0, width: self.view.bounds.width, height: 44))
        toolbar.options = [RichEditorDefaultOption.bold, RichEditorDefaultOption.italic, RichEditorDefaultOption.unorderedList,
                           RichEditorDefaultOption.orderedList, RichEditorDefaultOption.textColor, RichEditorDefaultOption.header(1),
                           RichEditorDefaultOption.header(2), RichEditorDefaultOption.header(3), RichEditorDefaultOption.header(4),
                           RichEditorDefaultOption.header(5), RichEditorDefaultOption.header(6), RichEditorDefaultOption.image,
                           RichEditorDefaultOption.link]
        return toolbar
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        nextButton.layer.cornerRadius = 7.0
        catButton.addBorders(withDirections: [.top, .bottom], color: AppColor.border, width: 1.0)
        
        richEdittorContainerView.layer.cornerRadius = 5.0
        richEdittorContainerView.addBorderWithColor(AppColor.border, width: 1.0)
        
        let frame = CGRect(x: inputViewPadding, y: inputViewPadding,
                           width: richEdittorContainerView.frame.size.width - (inputViewPadding * 2),
                           height: richEdittorContainerView.frame.size.height - (inputViewPadding * 2))
        let editor = RichEditorView(frame: frame)
        editor.inputAccessoryView = toolbar
        editor.delegate = self
        editor.placeholder = "Enter a description for you posting"
        richEdittorContainerView.addSubview(editor)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - IBActions

    @IBAction func catButtonTapped(_ sender: Any) {
        let categoryVC = SelectCategoriesViewController(selectedCategories: [])
        categoryVC.singleSelectCategory = { category in
            self.catButton.setTitle(category.localizeName, for: .normal)
        }
        navigationController?.pushViewController(categoryVC, animated: true)
    }
    
    @IBAction func conditionSliderChangedValue(_ sender: Any) {
    }
}

// MARK: - RichEditorDelegate

extension DescriptionViewController: RichEditorDelegate {
    func richEditor(_ editor: RichEditorView, contentDidChange content: String) {
        debugPrint(content)
    }
}


extension DescriptionViewController: RichEditorToolbarDelegate {
    
}
