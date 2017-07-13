//
//  SelectCategoriesViewController.swift
//  sosokan
//

import UIKit

class SelectCategoriesViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    var rootCategories: [Category] = DataManager.shared.allCategories.filter({ $0.popular > 0 || $0.parentURL == nil})
    var selectedCategories: [Category] = []
    
    var singleSelectCategory:((Category) -> Void)?
    
    fileprivate struct Constants {
        static let defaultPadding: CGFloat = 8
        static let defaultButtonHeight: CGFloat = 34
    }
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
     
        // Navigation bar
        title = "Browse".localized()
        if isModal() {
            let closeButton = UIBarButtonItem(image: #imageLiteral(resourceName: "close-icon"), style: .plain, target: self, action: #selector(dismissVC))
            navigationItem.leftBarButtonItem = closeButton
            
            let doneButton = UIBarButtonItem(image: #imageLiteral(resourceName: "check-marked"), style: .plain, target: self, action: #selector(performBrowsePosts))
            navigationItem.rightBarButtonItem = doneButton
        }
        else {
            let backButton = UIBarButtonItem(image: UIImage(named: "back_button"), style: .plain, target: self, action: #selector(back))
            navigationItem.leftBarButtonItem = backButton
        }
        
        collectionView.register(UINib(nibName: BrowseCategoryCollectionViewCell.identifier, bundle: nil),
                                forCellWithReuseIdentifier: BrowseCategoryCollectionViewCell.identifier)
        collectionView.register(UINib(nibName: BrowseParentCategoryCollectionViewCell.identifier, bundle: nil),
                                forCellWithReuseIdentifier: BrowseParentCategoryCollectionViewCell.identifier)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func dismissVC() {
        dismiss(animated: true, completion: nil)
    }
    
    func performBrowsePosts() {
        
    }
    
    func back() {
        navigationController?.popViewController(animated: true)
    }
    // MARK: fileprivate method
    
}

// MARK: - UICollectionViewDataSource

extension SelectCategoriesViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return rootCategories.count
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        let parentCategory = rootCategories[section]
        let subCategories = DataManager.shared.allCategories.filter({$0.parentURL == parentCategory.URL})
        
        return subCategories.count + 1
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let parentCategory = rootCategories[indexPath.section]
        
        if indexPath.item == 0 {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: BrowseParentCategoryCollectionViewCell.identifier,
                                                          for: indexPath) as! BrowseParentCategoryCollectionViewCell
            cell.catButton.setTitle(parentCategory.localizeName.uppercased(), for: .normal)
            cell.catButton.isSelected = selectedCategories.contains(parentCategory)
            
            // Click on parent category button.
            cell.catButtonAction = { button in
                
                if self.isModal() { // Multiple selection, the case user selects many categories for filtering
                    // Get list of sub-categories for current category.
                    let subCategories = DataManager.shared.allCategories.filter({$0.parentURL == parentCategory.URL})
                    if let index = self.selectedCategories.index(where: {$0 == parentCategory}) {
                        // Case: unselect parent category.
                        self.selectedCategories.remove(at: index)
                        
                        // Unselect all sub-categories.
                        for subCat in subCategories {
                            if let index = self.selectedCategories.index(where: {$0 == subCat}) {
                                self.selectedCategories.remove(at: index)
                            }
                        }
                    }
                    else {
                        self.selectedCategories.append(parentCategory)
                        
                        // Auto select all sub-categories.
                        for subCat in subCategories {
                            if !self.selectedCategories.contains(subCat) {
                                self.selectedCategories.append(subCat)
                            }
                        }
                    }
                    collectionView.reloadSections([indexPath.section])
                }
                else { // Single select, the case user selects a category to post a new ad.
                    self.singleSelectCategory?(parentCategory)
                    self.back()
                }
            }
            
            return cell
        }
        else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: BrowseCategoryCollectionViewCell.identifier,
                                                          for: indexPath) as! BrowseCategoryCollectionViewCell
            let subCategory = DataManager.shared.allCategories.filter({$0.parentURL == parentCategory.URL})[indexPath.item - 1]
            cell.catButton.setTitle(subCategory.localizeName, for: .normal)
            cell.setCatButtonSelected(isSelected: selectedCategories.contains(subCategory))
            
            // Click on one of sub-category button.
            cell.catButtonAction = { button in
                
                if self.isModal() { // Multiple selection, the case user selects many categories for filtering
                    // If the sub-category is selected, remove it. Otherwise add it to the selected list.
                    if let index = self.selectedCategories.index(where: {$0 == subCategory}) {
                        self.selectedCategories.remove(at: index)
                    }
                    else {
                        self.selectedCategories.append(subCategory)
                    }
                    collectionView.reloadSections([indexPath.section])
                }
                else { // Single select, the case user selects a category to post a new ad.
                    self.singleSelectCategory?(subCategory)
                    self.back()
                }
                
            }
            
            return cell
        }
    }
}

// MARK: - UICollectionViewDelegateFlowLayout

extension SelectCategoriesViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        if indexPath.item == 0 {
            return CGSize(width: collectionView.frame.size.width , height: 35.0)
        }
        else {
            let parentCategory = rootCategories[indexPath.section]
            let subCategory = DataManager.shared.allCategories.filter({$0.parentURL == parentCategory.URL})[indexPath.item - 1]
            
            let width = subCategory.localizeName.widthWithConstrainedHeight(35.0, font: UIFont.avenirLTStdRoman(size: 15))
            
            return CGSize(width: width + BrowseCategoryCollectionViewCell.buttonPadding , height: 35.0)
        }
    }
}

// MARK: convenience init

extension SelectCategoriesViewController {
    convenience init(selectedCategories: [Category]) {
        self.init(nibName: "SelectCategoriesViewController", bundle: nil)
        self.selectedCategories = selectedCategories
    }
}
