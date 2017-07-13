//
//  FeatureCategoriesController.swift
//  sosokan
//

import UIKit
import SDWebImage

class FeatureCategoriesController: UIViewController {
    @IBOutlet weak var collectionView: UICollectionView!
    
    fileprivate var tempSelectedCategories = DataManager.shared.featureCategories
    fileprivate var categories = DataManager.shared.allCategories.filter({ $0.popular > 0 || $0.parentURL == nil})
    
    fileprivate struct Constants {
        struct ReuseIdentifiers {
            static let featureCategoryCell = String(describing: FeatureCategoryCollectionViewCell.self)
        }
    }
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareCollectionView()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = "SOSOKAN"
        self.prepareSaveBarButton()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Helpers
    
    func saveButtonTapped() {
        DataManager.shared.featureCategories = self.tempSelectedCategories
        _ = self.navigationController?.popViewController(animated: true)
    }
    
    // MARK: Private method
    
    fileprivate func prepareCollectionView() {
        let nib = UINib.init(nibName: Constants.ReuseIdentifiers.featureCategoryCell, bundle: nil)
        self.collectionView.register(nib, forCellWithReuseIdentifier: Constants.ReuseIdentifiers.featureCategoryCell)
        self.collectionView.delegate = self
        self.collectionView.backgroundColor = AppColor.white
        self.collectionView.alwaysBounceVertical = true
        self.collectionView.dataSource = self
        self.collectionView.delegate = self
    }
    
    fileprivate func prepareSaveBarButton() {
        let button = UIBarButtonItem(barButtonSystemItem: .save, target: self, action: #selector(saveButtonTapped))
        self.navigationItem.rightBarButtonItem = button
    }
}

// MARK: - UICollectionViewDataSource

extension FeatureCategoriesController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return categories.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.ReuseIdentifiers.featureCategoryCell,
                                                      for: indexPath) as! FeatureCategoryCollectionViewCell
        
        let item = categories[indexPath.row]
        if let itemURL = item.absoluteBackgroundImageURL {
            cell.backgroundImageView.kf.setImage(with: itemURL)
        }
        else if let itemIconURL = Foundation.URL(string: item.localizeIconURL!) {
            cell.backgroundImageView.kf.setImage(with: itemIconURL)
        }
        cell.selectedImageView.image = self.tempSelectedCategories.contains(where: {$0 == item}) ? AppIcons.checkMarkSelectedIcon : AppIcons.checkMarkIcon
        cell.titleLabel.text = item.localizeName
        
        return cell
    }
}

// MARK: - UICollectionViewDelegate

extension FeatureCategoriesController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let category = self.categories[indexPath.row]
        if let index = self.tempSelectedCategories.index(where: {$0 == category}) {
            self.tempSelectedCategories.remove(at: index)
        }
        else {
            self.tempSelectedCategories.append(category)
        }
        self.collectionView.reloadItems(at: [indexPath])
        
        // Save featured categories. \
        DataManager.shared.savedCategories = tempSelectedCategories
    }
}

// MARK: UICollectionViewDelegate

extension FeatureCategoriesController: UICollectionViewDelegateFlowLayout {
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

extension FeatureCategoriesController {
    convenience init() {
        self.init(nibName: "FeatureCategoriesController", bundle: nil)
    }
}
