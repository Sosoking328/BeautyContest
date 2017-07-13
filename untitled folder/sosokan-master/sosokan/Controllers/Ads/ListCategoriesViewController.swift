//
//  ListCategoriesViewController.swift
//  sosokan
//
//  Created by An Phan on 8/5/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//


import UIKit
import RxCocoa
import RxSwift
import RxOptional
import RxDataSources
import NSObject_Rx

class ListCategoriesViewController: UIViewController {
    
    // MARK: IBOulet
    @IBOutlet weak var categoriesTableView: UITableView!
    
    // MARK: Variable
    let cellIdentifier = "categoryCellID"
    var categoryMenuItems: Variable<[CategoryMenuItem]> = Variable.init([])
    var selectedCategoryMenuItem: Variable<CategoryMenuItem?> = Variable.init(nil)
    var currentCategory: Variable<FCategory?> = Variable(nil)
    var didSelectedCategory: ((category: FCategory) -> Void)?
    
    // MARK: View controller lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareCategoriesTableView()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        self.prepareNavigationBar()
        self.setText()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Methods
    
    func setText() {
        title = "Select a category".localized()
    }
    
    func popViewController() {
        navigationController?.popViewControllerAnimated(true)
    }
    
    // MARK: Private method
    
    private func prepareCategoriesTableView() {
        
        let reuserIdentifier = "categoryCellID"
        
        // Preapre navigation menu
        self.categoriesTableView.registerNib(UINib(nibName: String(CategoryTableViewCell), bundle: nil), forCellReuseIdentifier: reuserIdentifier)
        self.categoriesTableView.backgroundColor = AppColor.white
        self.categoriesTableView.tableFooterView = UIView.init(frame: CGRect.zero)
        self.categoriesTableView.separatorStyle = .None
        self.categoriesTableView.rowHeight = 60
        
        // Config cells
        let dataSource = RxTableViewSectionedReloadDataSource<SectionModel<String, CategoryMenuItem>>.init()
        dataSource.configureCell = { _, tableView, indexPath, menuItem in
            let cell = tableView.dequeueReusableCellWithIdentifier(reuserIdentifier, forIndexPath: indexPath) as! CategoryTableViewCell
            
            cell.subCategoryName.text = menuItem.title.uppercaseString
            cell.indented(menuItem.level ?? 1)
            
            if let icon = menuItem.icon {
                cell.subCatImageView.image = icon
            }
            else {
                if let URL = menuItem.category.icon?.absoluteImageURL {
                    cell.subCatImageView.sd_setImageWithURL(URL, placeholderImage: nil)
                }
                else {
                    cell.subCatImageView.image = nil
                }
            }
            
            if menuItem.type == .Selectable {
                if menuItem == self.selectedCategoryMenuItem.value {
                    cell.checkButton.setImage(CategoryMenuAccessoryIcon.selected, forState: .Normal)
                }
                else {
                    cell.checkButton.setImage(nil, forState: .Normal)
                }
            }
            else {
                if let firstSubMenuItem = menuItem.subCategoryMenuItems.first where self.categoryMenuItems.value.contains(firstSubMenuItem) {
                    cell.checkButton.setImage(CategoryMenuAccessoryIcon.carretUp, forState: .Normal)
                }
                else {
                    cell.checkButton.setImage(CategoryMenuAccessoryIcon.carretDown, forState: .Normal)
                }
            }
            
            cell.subCategoryName.font = UIFont.latoBold(size: 18)
            
            cell.setAdCount(menuItem.number ?? 0)
            
            return cell
        }
        
        // Bind data to navigation menu
        self.categoryMenuItems.asObservable()
            .map({ [SectionModel.init(model: "", items: $0)] })
            .bindTo(self.categoriesTableView.rx_itemsWithDataSource(dataSource))
            .addDisposableTo(self.rx_disposeBag)
        
        // Handle cell did touch
        self.categoriesTableView.rx_itemSelected
            .map({ self.categoryMenuItems.value[$0.row] })
            .subscribeNext({ [weak self] menuItem in
                guard let `self` = self else { return }
                
                if menuItem.category.id == CATEGROGY_ALL_ID {
                    if menuItem != self.selectedCategoryMenuItem.value {
                        let hightLevelMenuItems = self.categoryMenuItems.value.filter({ $0.level > menuItem.level })
                        if !hightLevelMenuItems.isEmpty {
                            for _menuItem in hightLevelMenuItems {
                                self.removeMenuItem(_menuItem)
                            }
                        }
                    }
                }
                else {
                    if menuItem.type == .Selectable {
                        let hightLevelMenuItems = self.categoryMenuItems.value.filter({ $0.level > menuItem.level })
                        if !hightLevelMenuItems.isEmpty {
                            for _menuItem in hightLevelMenuItems {
                                self.removeMenuItem(_menuItem)
                            }
                        }
                    }
                    else {
                        if let selected = self.selectedCategoryMenuItem.value {
                            let parents = self.getParentMenuItems(selected, multipleLevel: true)
                            let topMenuItem = parents.maxElement({ $0.0.level > $0.1.level }) ?? selected
                            let children = self.getChildMenuItems(topMenuItem, multipleLevel: true)
                            let sibling = [topMenuItem] + children
                            
                            if !sibling.contains(menuItem) {
                                let hightLevelMenuItems = self.categoryMenuItems.value.filter({ $0.level > menuItem.level })
                                if !hightLevelMenuItems.isEmpty {
                                    for _menuItem in hightLevelMenuItems {
                                        self.removeMenuItem(_menuItem)
                                    }
                                }
                            }
                        }
                        
                        var alreadyExpanded: Bool = false
                        let children = self.getChildMenuItems(menuItem, multipleLevel: true)
                        for child in children {
                            if self.categoryMenuItems.value.contains(child) {
                                alreadyExpanded = true
                                break
                            }
                        }
                        
                        if alreadyExpanded {
                            let hightLevelMenuItems = self.categoryMenuItems.value.filter({ $0.level > menuItem.level })
                            if !hightLevelMenuItems.isEmpty {
                                for _menuItem in hightLevelMenuItems {
                                    self.removeMenuItem(_menuItem)
                                }
                            }
                            
                            let sameLevelMenuItems = self.categoryMenuItems.value.filter({ $0.level == menuItem.level })
                            if !sameLevelMenuItems.isEmpty {
                                for _menuItem in sameLevelMenuItems {
                                    self.removeLowerMenuItems(_menuItem)
                                }
                            }
                        }
                        else {
                            let appendedMenuItems = self.fetchAppendedMenuItems(menuItem)
                            menuItem.subCategoryMenuItems = appendedMenuItems
                            if let index = self.categoryMenuItems.value.indexOf(menuItem) {
                                self.categoryMenuItems.value.insertContentsOf(appendedMenuItems, at: index + 1)
                            }
                        }
                    }
                }
                
                self.selectedCategoryMenuItem.value = menuItem
                })
            .addDisposableTo(self.rx_disposeBag)
        
        // Animation after select menu item
        self.selectedCategoryMenuItem.asObservable()
            .filterNil()
            .subscribeNext({ [weak self] menuItem in
                guard let `self` = self else { return }
                
                self.categoriesTableView.reloadData()
                
                if menuItem.type == .Expandable {
                    var alreadyExpanded: Bool = false
                    let children = self.getChildMenuItems(menuItem, multipleLevel: true)
                    for child in children {
                        if self.categoryMenuItems.value.contains(child) {
                            alreadyExpanded = true
                            break
                        }
                    }
                    
                    if alreadyExpanded {
                        if let index = self.categoryMenuItems.value.indexOf(menuItem) {
                            let indexPath = NSIndexPath.init(forRow: index, inSection: 0)
                            if let _ = self.categoriesTableView.cellForRowAtIndexPath(indexPath) {
                                self.categoriesTableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
                            }
                        }
                    }
                }
                })
            .addDisposableTo(self.rx_disposeBag)
        
        // Action after select menu item
        self.selectedCategoryMenuItem.asObservable()
            .filterNil()
            .filter({ $0.type == .Selectable })
            .subscribeNext({ [weak self] menuItem in
                guard let `self` = self else { return }
                self.currentCategory.value = menuItem.category
                })
            .addDisposableTo(self.rx_disposeBag)
        
        self.currentCategory.asObservable()
            .filterNil()
            .subscribeNext({ [weak self] category in
                guard let `self` = self else { return }
                self.didSelectedCategory?(category: category)
                self.popViewController()
            })
            .addDisposableTo(self.rx_disposeBag)
        
        // Prepare variables
        let allCategories = DataManager.sharedInstance.categories.value
        let rootCategories = allCategories.filter({ $0.id != CATEGROGY_ALL_ID }).filter({ $0.deepLevel == 1 }).sort({ $0.0.sortedOrder < $0.1.sortedOrder })
        let menuItems = rootCategories.map({ CategoryMenuItem.init(category: $0) })
        self.categoryMenuItems.value = menuItems
    }
    
    private func fetchAppendedMenuItems(menuItem: CategoryMenuItem) -> [CategoryMenuItem] {
        var result: [CategoryMenuItem] = []
        
        var appendedCategories = DataManager.sharedInstance.getChildCategories(menuItem.category, onMultipleDeepLevel: false)
        if IS_ENGLISH {
            appendedCategories = appendedCategories.sort({ $0.0.nameInEnglish < $0.1.nameInEnglish })
        }
        else {
            appendedCategories = appendedCategories.sort({ $0.0.nameInChinese < $0.1.nameInChinese })
        }
        result = appendedCategories.map({ CategoryMenuItem.init(category: $0) })
        
        /*
        let title = "All".localized() + " " + menuItem.title
        let number = IS_ENGLISH ? menuItem.category.postsEnglish.count : menuItem.category.postsChinese.count
        let icon = DefaultIcon.starIcon
        let allMenuItem = CategoryMenuItem.init(category: menuItem.category, title: title, number: number, level: menuItem.level + 1, icon: icon, type: .Selectable)
        
        result.insert(allMenuItem, atIndex: 0)
        */
        
        return result
    }
    
    private func getParentMenuItems(menuItem: CategoryMenuItem, multipleLevel: Bool) -> [CategoryMenuItem] {
        var parents = self.categoryMenuItems.value.filter({ _menuItem -> Bool in
            let one = _menuItem.category.id == menuItem.category.parentId
            let two = (_menuItem.category.id == menuItem.category.id) && (_menuItem.type == .Expandable) && (_menuItem != menuItem)
            return one || two
        })
        
        if !multipleLevel {
            return parents
        }
        else {
            for parent in parents {
                parents.appendContentsOf(self.getParentMenuItems(parent, multipleLevel: multipleLevel))
            }
            return parents
        }
    }
    
    private func getChildMenuItems(menuItem: CategoryMenuItem, multipleLevel: Bool) -> [CategoryMenuItem] {
        var children = self.categoryMenuItems.value.filter({ _menuItem -> Bool in
            let one = _menuItem.category.parentId == menuItem.category.id
            let two = (_menuItem.category.id == menuItem.category.id) && (_menuItem.type == .Selectable) && (_menuItem != menuItem)
            return one || two
        })
        
        if !multipleLevel {
            return children
        }
        else {
            for parent in children {
                children.appendContentsOf(self.getChildMenuItems(parent, multipleLevel: multipleLevel))
            }
            return children
        }
    }
    
    private func removeMenuItem(menuItem: CategoryMenuItem) {
        if let index = self.categoryMenuItems.value.indexOf(menuItem) {
            self.categoryMenuItems.value.removeAtIndex(index)
        }
    }
    
    private func removeLowerMenuItems(menuItem: CategoryMenuItem) {
        defer {
            menuItem.subCategoryMenuItems.removeAll()
        }
        let subMenuItems = menuItem.subCategoryMenuItems
        for menuItem in subMenuItems {
            self.removeMenuItem(menuItem)
            for _menuItem in menuItem.subCategoryMenuItems {
                self.removeLowerMenuItems(_menuItem)
            }
        }
    }
    
    private func prepareNavigationBar() {
        // Appearance
        navigationController?.navigationBar.barStyle = .Default   // Change status bar to black mode.
        navigationController?.navigationBar.setBackgroundImage(nil, forBarMetrics: .Default)
        navigationController?.navigationBar.shadowImage = nil
        navigationController?.navigationBar.barTintColor = AppColor.white
        navigationController?.navigationBar.tintColor = AppColor.textPrimary
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.orange]
        navigationController?.navigationBar.translucent = false
        setNeedsStatusBarAppearanceUpdate()
        
        let leftBarButton = UIBarButtonItem(image: UIImage(named: "back"), style: .Plain, target: self, action: #selector(popViewController))
        navigationItem.leftBarButtonItem = leftBarButton
    }
}