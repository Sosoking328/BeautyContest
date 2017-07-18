//
//  FilterMenuViewController.swift
//  sosokan
//

import UIKit
import Material
import TTRangeSlider

class FilterMenuViewController: UIViewController {
    
    // MARK: Variable
    @IBOutlet weak var searchTextField: UITextField!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var priceRangeSlider: TTRangeSlider!
    @IBOutlet weak var propertyLabel: UILabel!
    @IBOutlet weak var propertyButton: Button!
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var cityButton: Button!
    @IBOutlet weak var categoryLabel: UILabel!
    @IBOutlet weak var categoryButton: Button!
    @IBOutlet weak var cancelButton: Button!
    @IBOutlet weak var applyButton: Button!
    @IBOutlet weak var categoryBorderView: UIView!
    @IBOutlet weak var cityBorderView: UIView!
    @IBOutlet weak var propertyBorderView: UIView!
    @IBOutlet weak var priceBorderView: UIView!
    @IBOutlet weak var allPriceButton: Button!
    
    fileprivate struct Constants {
        static let popupSize: CGSize = CGSize(width: 315, height: 400)
        static let popupTableViewSize: CGSize = CGSize(width: 315, height: 500)
        static let categoryAllName = "All".localized()
    }
    
    var filterOptions: FilterOptions!
    fileprivate var tempFilterOptions: FilterOptions!
    
    // MARK: View life cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.preparePopupFrame()
        self.prepareViewBorders()
        self.prepareCategorySection()
        self.prepareCitySection()
        self.preparePropertySection()
        self.preparePriceLabel()
        self.preparePriceRangeSlider()
        self.prepareAllPriceButton()
        self.prepareCancelButton()
        self.prepareApplyButton()
        self.prepareSearchTextField()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = "Advance search".localized()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    // MARK: - IBActions
    
    @IBAction func selectAllPricesButtonTapped(_ sender: AnyObject) {
        self.tempFilterOptions.selectedPrice = nil
    }
    
    @IBAction func categoryButtonTapped(_ sender: AnyObject) {
        let viewController = CategoriesListViewController.init()
        viewController.title = "Select category"
        viewController.selectedAction = { [weak self] (category) in
            guard let `self` = self else { return }
            self.categoryButton.setTitle(category?.localizeName ?? Constants.categoryAllName, for: .normal)
            self.tempFilterOptions.category = category
        }
        self.popupController?.push(viewController, animated: true)
    }
    
    @IBAction func cityButtonTapped(_ sender: AnyObject) {
        let viewController = LocationViewController(didChangeFilterOptions: nil)
        self.popupController?.push(viewController, animated: true)
    }
    
    @IBAction func propertyButtonTapped(_ sender: AnyObject) {
        let tableViewController = UITableViewController()
        tableViewController.title = "Select property"
        tableViewController.contentSizeInPopup = Constants.popupTableViewSize
        self.popupController?.push(tableViewController, animated: true)
    }
    
    @IBAction func applyButtonTapped(_ sender: AnyObject) {
        self.filterOptions = self.tempFilterOptions
        if let popupController = self.popupController {
            popupController.popViewController(animated: true)
        }
        else {
            _ = self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func cancelButtonTapped(_ sender: AnyObject) {
        if let popupController = self.popupController {
            popupController.popViewController(animated: true)
        }
        else {
            _ = self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func searchTextFieldEditingDidEndOnExit(_ sender: AnyObject) {
        self.searchTextField.resignFirstResponder()
    }
    
    // MARK: Private method
    fileprivate func prepareSearchTextField() {
        let imageView = UIImageView(image: AppIcons.searchIcon.resize(toWidth: 16))
        imageView.frame = CGRect(origin: imageView.bounds.origin, size: CGSize(width: imageView.bounds.width + 20, height: imageView.bounds.height))
        imageView.contentMode = .center
        self.searchTextField.backgroundColor = UIColor.white
        self.searchTextField.leftViewMode = .always
        self.searchTextField.leftView = imageView
        self.searchTextField.clearButtonMode = .always
        self.searchTextField.placeholder = "Search here"
        self.searchTextField.returnKeyType = .done
    }
    
    fileprivate func prepareCancelButton() {
        self.cancelButton.backgroundColor = AppColor.white
        self.cancelButton.setTitleColor(AppColor.orange, for: .normal)
        self.cancelButton.bordered(withColor: AppColor.border, width: 1)
    }
    
    fileprivate func prepareApplyButton() {
        self.applyButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.regular)
        self.applyButton.backgroundColor = AppColor.orange
        self.applyButton.setTitleColor(AppColor.white, for: .normal)
        self.applyButton.bordered(withColor: AppColor.border, width: 1)
    }
    
    fileprivate func preparePopupFrame() {
        self.contentSizeInPopup = Constants.popupSize
    }
    
    fileprivate func prepareCategorySection() {
        self.categoryLabel.text = "Category".localized()
        self.categoryLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        self.categoryButton.setTitle(self.tempFilterOptions.category?.localizeName ?? Constants.categoryAllName, for: .normal)
        self.categoryButton.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 12)
        self.categoryButton.setTitleColor(AppColor.orange, for: .normal)
    }
    
    fileprivate func prepareCitySection() {
        self.cityLabel.text = "City".localized()
        self.cityLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        var title: String
        if let selectedDistance = self.tempFilterOptions.selectedDistance {
            title = "Around me \(selectedDistance) mile(s)"
        }
        else {
            title = self.tempFilterOptions.city?.name ?? "N/A"
        }
        self.cityButton.setTitle(title, for: .normal)

        self.cityButton.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 12)
        self.cityButton.setTitleColor(AppColor.orange, for: .normal)
    }
    
    fileprivate func preparePropertySection() {
        self.propertyLabel.text = "Property".localized()
        self.propertyLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        
        let title = self.tempFilterOptions.property?.name ?? "N/A"
        self.propertyButton.setTitle(title, for: .normal)
        self.propertyButton.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 12)
        self.propertyButton.setTitleColor(AppColor.orange, for: .normal)
    }
    
    fileprivate func preparePriceLabel() {
        self.priceLabel.text = "Price".localized()
        self.priceLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        
        self.allPriceButton.isSelected = self.tempFilterOptions.selectedPrice == nil
        self.allPriceButton.setTitleColor(AppColor.grey, for: .selected)
        self.allPriceButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.verySmall)
        self.allPriceButton.setImage(AppIcons.checkMarkIcon, for: .normal)
        self.allPriceButton.setImage(AppIcons.checkMarkSelectedIcon, for: .selected)
        self.allPriceButton.titleEdgeInsets = UIEdgeInsetsMake(0, 5, 0, 0)
    }
    
    fileprivate func preparePriceRangeSlider() {
        self.priceRangeSlider.lineHeight = 2
        self.priceRangeSlider.labelPadding = 0
        self.priceRangeSlider.minLabelFont = UIFont.latoRegular(size: AppFontsizes.small)
        self.priceRangeSlider.maxLabelFont = UIFont.latoRegular(size: AppFontsizes.small)
        self.priceRangeSlider.tintColor = AppColor.grey
        self.priceRangeSlider.tintColorBetweenHandles = AppColor.orange
        self.priceRangeSlider.step = 50
        self.priceRangeSlider.enableStep = true
        self.priceRangeSlider.handleColor = AppColor.orange
        self.priceRangeSlider.delegate = self
        let formatter = NumberFormatter.init()
        formatter.positivePrefix = "$"
        self.priceRangeSlider.numberFormatterOverride = formatter
        
        self.priceRangeSlider.minValue = 0
        self.priceRangeSlider.maxValue = 50000
        self.priceRangeSlider.selectedMinimum = 0
        self.priceRangeSlider.selectedMaximum = 20000
    }
    
    fileprivate func prepareAllPriceButton() {
        self.allPriceButton.setTitleColor(AppColor.grey, for: .selected)
        self.allPriceButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.verySmall)
        self.allPriceButton.setImage(AppIcons.checkMarkIcon, for: .normal)
        self.allPriceButton.setImage(AppIcons.checkMarkSelectedIcon, for: .selected)
        self.allPriceButton.titleEdgeInsets = UIEdgeInsetsMake(0, 5, 0, 0)
    }
    
    fileprivate func prepareViewBorders() {
        let borderViews = [
            self.categoryBorderView,
            self.cityBorderView,
            self.propertyBorderView,
            self.priceBorderView
        ]
        borderViews.forEach({
            $0!.backgroundColor = AppColor.border
        })
    }
}

// MARK: convenience init
extension FilterMenuViewController {
    convenience init() {
        let nibName = String(describing: FilterMenuViewController.self)
        self.init(nibName: nibName, bundle: nil)
    }
    
    convenience init(filterOptions: FilterOptions) {
        let nibName = String(describing: FilterMenuViewController.self)
        self.init(nibName: nibName, bundle: nil)
        self.filterOptions = filterOptions
        self.tempFilterOptions = filterOptions
    }
}

// MARK: TTRangeSliderDelegate
extension FilterMenuViewController: TTRangeSliderDelegate {
    func rangeSlider(_ sender: TTRangeSlider!, didChangeSelectedMinimumValue selectedMinimum: Float, andMaximumValue selectedMaximum: Float) {
        if sender == self.priceRangeSlider {
            self.tempFilterOptions.selectedPrice = Int.init(selectedMaximum)
        }
    }
}
