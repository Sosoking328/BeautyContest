//
//  FAQViewController.swift
//  sosokan
//
//  Created by David Nguyentan on 6/9/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Material

struct FAQTab {
    var question: String
    var answer: String
    
    init(question: String, answer: String) {
        self.question = question
        self.answer = answer
    }
    
    static func getFAQs() -> [FAQTab] {
        let fileName = IS_ENGLISH ? "FAQ.plist" : "FAQ-Chinese.plist"
        guard let path = Bundle.main.path(forResource: fileName, ofType: nil) else { return [] }
        guard let content = NSArray(contentsOfFile: path) else { return [] }
        
        let faqs = content.map({ (entry) -> FAQTab? in
            if let entry = entry as? [String: AnyObject] {
                guard let question = entry["question"] as? String else { return nil }
                guard let answer = entry["answer"] as? String else { return nil }
                
                return FAQTab(question: question, answer: answer)
            }
            
            return nil
        })
        
        return faqs.flatMap { $0 }
    }
}

class FAQViewController: UIViewController {
    
    
    // MARK: - IBOutlets
    @IBOutlet weak var topView: UIView!
    @IBOutlet weak var topViewConstraint: NSLayoutConstraint!
    @IBOutlet weak var searchBarTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var searchBarShadowView: UIView!
    @IBOutlet weak var searchBarCornerView: UIView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var textField: UITextField!
    
    
    // MARK: - Variables
    let colorForBackground = UIColor(hexString: "F8F8F8")
    let colorForHighlightedText = UIColor(hexString: "FFAA4A")
    let separatorColor = UIColor(red: 55/255, green: 50/255, blue: 48/255, alpha: 0.1)
    let shadowColor = UIColor(white: 0, alpha: 1)
    let shadowOffset = CGSize(width: 0, height: 1)
    let shadowOpacity: Float = 0.12
    let shadowRadius: CGFloat = 2
    let cornerRadius: CGFloat = 5
    let topViewLongHeight: CGFloat = 256
    let topViewSortHeight: CGFloat = 156
    let FAQs = FAQTab.getFAQs()
    var isSearching = false {
        didSet {
            if !isSearching {
                FAQResults = FAQs
            }
        }
    }
    var FAQResults = [FAQTab]() {
        didSet {
            collectionView.reloadData()
        }
    }
    var selectedIndexPath: IndexPath? {
        didSet {
            collectionView.reloadData()
        }
    }
    
    // MARK: - IBActions
    
    @IBAction func textFieldDidEnd(_ sender: UITextField) {
        selectedIndexPath = nil
        isSearching = true
        
        let searchText = sender.text ?? ""
        
        FAQResults = FAQs.filter { (faq) -> Bool in
            let content = faq.question + " " + faq.answer
            
            return ((content as NSString).range(of: searchText, options: NSString.CompareOptions.caseInsensitive).location) != NSNotFound
        }
        expandTopView()
    }
    
    @IBAction func textFieldDidCancel(_ sender: UITextField) {
        sender.text = nil
        selectedIndexPath = nil
        isSearching = false
    }
    
    @IBAction func textFieldDidBeginEdit(_ sender: UITextField) {
        collapseTopView()
    }
    
    // MARK: - View life circle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // setup view
        let tapToDismissSearch = UITapGestureRecognizer(target: self, action: #selector(dismissTextField))
        view.addGestureRecognizer(tapToDismissSearch)
        view.backgroundColor = colorForBackground
        
        let leftBarButton = UIBarButtonItem(image: AppIcons.backIcon, style: .plain, target: self, action: #selector(handleLeftBarButton))
        navigationItem.leftBarButtonItem = leftBarButton
        
        // setup subviews
        searchBarShadowView.backgroundColor = UIColor.clear
        searchBarShadowView.layer.shadowColor = shadowColor.cgColor
        searchBarShadowView.layer.shadowOffset = shadowOffset
        searchBarShadowView.layer.shadowOpacity = shadowOpacity
        searchBarShadowView.layer.shadowRadius = shadowRadius
        
        searchBarCornerView.layer.cornerRadius = cornerRadius
        searchBarCornerView.layer.masksToBounds = true
        
        let imageView = UIImageView(image: UIImage(named: "searchIcon"))
        imageView.frame = CGRect(origin: imageView.bounds.origin, size: CGSize(width: imageView.bounds.width + 20, height: imageView.bounds.height))
        imageView.contentMode = .center
        textField.backgroundColor = UIColor.white
        textField.leftViewMode = .always
        textField.leftView = imageView
        textField.borderStyle = .none
        textField.delegate = self
        
        collectionView.backgroundColor = UIColor.clear
        collectionView.dataSource = self
        collectionView.delegate = self
        
        // setup variables
        
        FAQResults = FAQs
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        setupNavigationbar()
        setText()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        dismissKeyboad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Functions
    
    func handleLeftBarButton() {
        _ = navigationController?.popViewController(animated: true)
    }
    
    func setText() {
        title = "FAQ".localized()
        textField.placeholder = "Search here".localized()
    }
    
    func setupNavigationbar() {
        navigationController?.navigationBar.barStyle = .black
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.tintColor = Color.white
        navigationController?.navigationBar.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.0)
        navigationController?.navigationBar.isTranslucent = true
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.white]
    }
    
    func dismissTextField() {
        textField.endEditing(true)
        expandTopView()
    }
    
    func endSearching() {
        selectedIndexPath = nil
        isSearching = false
        textField.text = nil
        dismissTextField()
    }
    
    func expandTopView() {
        topViewConstraint.constant = topViewLongHeight
        UIView.animate(withDuration: 0.55, animations: {
            [self.topView, self.view].forEach { $0?.setNeedsLayout(); $0?.layoutIfNeeded() }
        }) 
    }
    
    func collapseTopView() {
        topViewConstraint.constant = topViewSortHeight
        UIView.animate(withDuration: 0.75, animations: {
            [self.topView, self.view].forEach { $0?.setNeedsLayout(); $0?.layoutIfNeeded() }
        }) 
    }
}

// MARK: - UICollectionViewDataSource
extension FAQViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
         return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return FAQResults.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FAQCollectionViewCell.cellReuseIdentifier, for: indexPath) as! FAQCollectionViewCell
        let FAQ = FAQResults[indexPath.row]
        
        cell.contentView.subviews.forEach { $0.autoresizingMask = [.flexibleWidth, .flexibleHeight, .flexibleTopMargin, .flexibleBottomMargin, .flexibleLeftMargin, .flexibleRightMargin] }
        
        cell.button.titleLabel?.font = UIFont.latoBold(size: 17)
        cell.button.titleLabel?.numberOfLines = 3
        cell.button.titleLabel?.lineBreakMode = .byWordWrapping
        cell.button.setTitle(FAQ.question, for: UIControlState())
        cell.button.setTitleColor(UIColor.darkText, for: UIControlState())
        
        cell.textView.text = FAQ.answer
        cell.textView.textContainerInset = UIEdgeInsets(top: 25, left: 20, bottom: 25, right: 20)
        
        cell.contentView.backgroundColor = UIColor.white
        cell.contentView.layer.cornerRadius = cornerRadius
        cell.contentView.layer.masksToBounds = true
        
        cell.layer.shadowColor = shadowColor.cgColor
        cell.layer.shadowOpacity = shadowOpacity
        cell.layer.shadowOffset = shadowOffset
        cell.layer.shadowRadius = shadowRadius
        cell.layer.masksToBounds = false
        cell.layer.shadowPath = UIBezierPath(roundedRect: cell.bounds, cornerRadius: cell.contentView.layer.cornerRadius).cgPath
        
        if let selected = selectedIndexPath, selected == indexPath {
            cell.button.setTitleColor(colorForHighlightedText, for: UIControlState())
            cell.carretIcon.image = UIImage(named: "carretIconUp")
            cell.seperatorView.backgroundColor = separatorColor
            cell.carretIcon.tintColor = colorForHighlightedText
        } else {
            cell.button.setTitleColor(UIColor.darkText, for: UIControlState())
            cell.seperatorView.backgroundColor = UIColor.clear
            cell.carretIcon.image = UIImage(named: "carretIcon")
            cell.carretIcon.tintColor = UIColor.darkText
        }
        
        return cell
    }
}

// MARK: - UICollectionViewDelegate
extension FAQViewController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        let cell = cell as! FAQCollectionViewCell
        
        cell.onButtonTapped = {
            if let selected = self.selectedIndexPath, selected == indexPath {
                self.selectedIndexPath = nil
            } else {
                self.selectedIndexPath = indexPath
            }
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension FAQViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = collectionView.bounds.width - 40
        
        if let selected = selectedIndexPath, selected == indexPath {
            return CGSize(width: width, height: FAQCollectionViewCell.expand)
        }
        
        return CGSize(width: width, height: FAQCollectionViewCell.collapse)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 15
    }
}

// MARK: UITextFieldDelegate
extension FAQViewController: UITextFieldDelegate {
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        endSearching()
        
        return false
    }
}
