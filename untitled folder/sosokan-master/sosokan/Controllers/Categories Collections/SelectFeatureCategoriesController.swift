//
//  SelectFeatureCategoriesController.swift
//  sosokan
//

import UIKit
import SDWebImage
import Material

class SelectFeatureCategoriesController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var doneButton: Button!
    
    fileprivate let categories = DataManager.shared.allCategories.filter({ $0.popular > 0 }).sorted(by: { $0.0.popular > $0.1.popular })
    fileprivate var selectedCategories: [Category] = []
    
    fileprivate struct Constants {
        struct ReuseIdentifiers {
            static let featureCategoryCell = String.init(describing: FeatureCategoryCollectionViewCell.self)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareCollectionView()
        self.prepareDoneButton()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = "SOSOKAN"
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func doneButtonTapped(_ sender: AnyObject) {
        DataManager.shared.featureCategories = self.selectedCategories
        AppState.setHome()
    }
    
    // MARK: Private method
    
    fileprivate func prepareDoneButton() {
        self.doneButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.regular)
        self.doneButton.backgroundColor = AppColor.orange
        self.doneButton.setTitleColor(AppColor.white, for: .normal)
        self.doneButton.bordered(withColor: AppColor.border, width: 1)
    }
    
    fileprivate func prepareCollectionView() {
        let nib = UINib.init(nibName: Constants.ReuseIdentifiers.featureCategoryCell, bundle: nil)
        self.collectionView.register(nib, forCellWithReuseIdentifier: Constants.ReuseIdentifiers.featureCategoryCell)
        self.collectionView.delegate = self
        self.collectionView.backgroundColor = AppColor.white
        self.collectionView.alwaysBounceVertical = true
    }
}

// MARK: - UICollectionViewDataSource

extension SelectFeatureCategoriesController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return categories.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.ReuseIdentifiers.featureCategoryCell,
                                                                         for: indexPath) as! FeatureCategoryCollectionViewCell
        let item = categories[indexPath.row]
            
        cell.backgroundImageView.sd_setImage(with: item.absoluteLocalizeIconURL as URL!, placeholderImage: AppIcons.bigStarIcon)
        cell.selectedImageView.image = self.selectedCategories.contains(item) ? AppIcons.checkMarkSelectedIcon : AppIcons.checkMarkIcon
        cell.titleLabel.text = item.localizeName
        
        return cell
    }
}

extension SelectFeatureCategoriesController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let category = self.categories[indexPath.item]
        if let index = self.selectedCategories.index(of: category) {
            self.selectedCategories.remove(at: index)
        }
        else {
            self.selectedCategories.append(category)
        }
        self.collectionView.reloadItems(at: [indexPath])
    }
}

// MARK: UICollectionViewDelegate

extension SelectFeatureCategoriesController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = collectionView.frame.width / 2
        let height = width / 4 * 3
        return CGSize(width: width, height: height)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
}

// MARK: Convenience init
extension SelectFeatureCategoriesController {
    convenience init() {
        let nibName = String(describing: SelectFeatureCategoriesController.self)
        self.init(nibName: nibName, bundle: nil)
    }
}
