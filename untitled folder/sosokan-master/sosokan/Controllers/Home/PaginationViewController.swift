//
//  PaginationViewController.swift
//  sosokan
//

import UIKit
import PagingMenuController
import STPopup
import Material
import PageMenu

class PaginationViewController: UIViewController {
    
    @IBOutlet weak var navigationTableView: UITableView!
    @IBOutlet weak var navigationTableViewBottomConstaint: NSLayoutConstraint!
    @IBOutlet weak var bottomBarContainerView: UIView!
    @IBOutlet weak var bottomBarContainerViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var plusButton: UIButton!
    
    
    var filterOptions: FilterOptions!
    var viewType: ViewType!
    var mainCategory: Category!
    
    fileprivate var viewTypeBarButton: UIBarButtonItem!
    fileprivate var categoryMenus = [CategoryMenu]()
    fileprivate var subcategories = [Category]()
    fileprivate var bottomBarView: BottomBarView!
    fileprivate var navigationTitleLabel: UILabel!
    fileprivate var pageMenu: CAPSPageMenu?
    fileprivate var viewControllers = [UIViewController]()
    
    fileprivate struct Constants {
        static let animationDuration = TimeInterval(0.5)
        static let categoryAllName = "All".localized()
        static let menuItemAllTitle = "ALL".localized()
        static let menuItemAddTitle = "+ ADD".localized()
        static let menuItemHeight = CGFloat(35)
        static let menuMargin = CGFloat(0)
        static let menuItemMargin = CGFloat(10)
        static let bottomBarHeight = CGFloat(50)
        
        struct ReuseIdentifier {
            static let categoryTableViewCell = String(describing: CategoryTableViewCell.self)
            static let postCollectionViewCell = String(describing: AdCollectionViewCell.self)
            static let popularCollectionViewCell = String(describing: PopularCategoryCollectionViewCell.self)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        subcategories = DataManager.shared.allCategories.filter({ $0.parentURL == mainCategory.URL }).sorted(by: { $0.0.localizeName < $0.1.localizeName })
        
        if subcategories.count > 0 {
            categoryMenus = CategoryMenu.createMenus(subcategories, needMenuAll: true)
        }
        
        prepareNavigationTableView()
        prepareViewTypeBarButton()
        prepareBottomBarView()
        observerNotitications()
        
        viewControllers = getViewControllers(fromCategories: [mainCategory] + subcategories)
        
        renderPageMenu(controllers: viewControllers)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = mainCategory.localizeName
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    deinit {
        removeObservers()
    }
    
    // MARK: - IBActions
    
    @IBAction func plusButtonTapped(_ sender: AnyObject) {
        presentLoginAlert()
    }
    
    // MARK: Method
    
    func viewTypeBarButtonDidTouch(_ sender: AnyObject?) {
        self.viewType = self.viewType! == .list ? .grid : .list
        self.viewTypeBarButton.image = self.viewType.defaultBarButtonIcon
        self.viewControllers.forEach { (viewController) in
            if let postsCollectionViewController = viewController as? PostsCollectionViewController {
                postsCollectionViewController.changePostsCollectionViewLayout(self.viewType)
            }
        }
    }
    
    func scrollViewScrollsUp(sender: Any) {
        UIView.animate(withDuration: Constants.animationDuration, animations: {
            self.bottomBarContainerViewBottomConstraint.constant = 0
            self.view.setNeedsLayout()
            self.view.layoutIfNeeded()
        })
    }
    
    func scrollViewScrollsDown(sender: Any) {
        UIView.animate(withDuration: Constants.animationDuration, animations: {
            self.bottomBarContainerViewBottomConstraint.constant = -Constants.bottomBarHeight
            self.view.setNeedsLayout()
            self.view.layoutIfNeeded()
        })
    }
    
    func statusBarTapped(sender: Any) {
        self.navigationTableView.scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: true)
    }
    
    func toggleSideMenu() {
        AppDelegate.shared().toggleLeftDrawer(self, animated: true)
    }
    
    func navTitleTapped() {
        toggleNavigationTableView(animated: true)
    }
    
    // MARK: Private method
    
    fileprivate func observerNotitications() {
        let center = NotificationCenter.default
        center.addObserver(self, selector: #selector(self.scrollViewScrollsUp(sender:)),
                           name: NSNotification.Name(Notifications.scrollUp), object: nil)
        center.addObserver(self, selector: #selector(self.scrollViewScrollsDown(sender:)),
                           name: NSNotification.Name(Notifications.scrollDown), object: nil)
        center.addObserver(self, selector: #selector(self.statusBarTapped(sender:)),
                           name: NSNotification.Name(Notifications.statusBarTouched), object: nil)
    }
    
    fileprivate func removeObservers() {
        let center = NotificationCenter.default
        center.removeObserver(self)
    }
    
    fileprivate func preparePlusButton(){
        self.plusButton.sendSubview(toBack: self.view)
        self.plusButton.setTitle(nil, for: UIControlState())
    }
    
    fileprivate func prepareBottomBarView() {
        self.bottomBarView = BottomBarView.fromNib()
        self.bottomBarView.frame = CGRect(x: 0, y: 0, width: self.bottomBarContainerView.frame.width, height: self.bottomBarContainerView.frame.height)
        self.bottomBarView.addTopBorderWithColor(AppColor.border, width: 1)
        self.bottomBarContainerView.addSubview(self.bottomBarView)
        
        bottomBarView.browseButtonAction = {    // Open categories gird page
            let categoryVC = SelectCategoriesViewController(selectedCategories: [])
            let categoryNC = UINavigationController(rootViewController: categoryVC)
            self.present(categoryNC, animated: true, completion: nil)
        }
        bottomBarView.homeButtonAction = {
            self.navigationController?.popViewController(animated: true)
        }
        bottomBarView.profileButtonAction = {
            let profileViewController = StoryBoardManager.viewController("Profile",
                                                                         viewControllerName: "ProfileViewControllerNav")
            AppDelegate.shared().centerViewController = profileViewController
        }
    }
    
    fileprivate func toggleNavigationTableView(animated: Bool) {
        if animated {
            self.navigationTitleLabel.isUserInteractionEnabled = false
            UIView.animate(withDuration: Constants.animationDuration, animations: {
                let constant = self.navigationTitleLabel.tag == 0 ? 0 : self.view.frame.height
                self.navigationTableViewBottomConstaint.constant = constant
                self.view.layoutIfNeeded()
                }, completion: { _ in
                    self.navigationTitleLabel.isUserInteractionEnabled = true
                    self.navigationTitleLabel.tag = self.navigationTitleLabel.tag == 0 ? 1 : 0
            })
        }
        else {
            let constant = self.navigationTitleLabel.tag == 0 ? 0 : self.view.frame.height
            self.navigationTableViewBottomConstaint.constant = constant
            self.navigationTitleLabel.tag = self.navigationTitleLabel.tag == 0 ? 1 : 0
        }
    }
    
    fileprivate func prepareNavigationTableView() {
        self.navigationTableViewBottomConstaint.constant = UIScreen.main.bounds.height
        
        self.view.bringSubview(toFront: self.navigationTableView)
        
        let nib = UINib(nibName: Constants.ReuseIdentifier.categoryTableViewCell, bundle: nil)
        self.navigationTableView.register(nib, forCellReuseIdentifier: Constants.ReuseIdentifier.categoryTableViewCell)
        self.navigationTableView.tableFooterView = UIView(frame: CGRect.zero)
        self.navigationTableView.separatorStyle = .none
        self.navigationTableView.rowHeight = self.view.frame.height / 7
        self.navigationTableView.dataSource = self
        self.navigationTableView.delegate = self
    }
    
    fileprivate func getViewControllers(fromCategories categories: [Category]) -> [UIViewController] {
        let controllers = categories.map { (category) -> UIViewController in
            let viewController = PostsCollectionViewController(category: category,
                                                               viewType: viewType,
                                                               parentNavigationController: navigationController)
            viewController.title = category.localizeName
            
            return viewController
        }
        return controllers
    }
    
    fileprivate func renderPageMenu(controllers: [UIViewController]) {
        self.pageMenu?.view.removeFromSuperview()
        
        // FIXME: Tweak to fixing the bug wrong orientation
        UIDevice.current.setValue(UIApplication.shared.statusBarOrientation.rawValue, forKey: "orientation")
        
        let parameters: [CAPSPageMenuOption] = [
            .viewBackgroundColor(AppColor.white),
            .scrollMenuBackgroundColor(AppColor.white),
            .selectionIndicatorColor(AppColor.orange),
            .menuItemSeparatorColor(AppColor.clear),
            .menuMargin(15),
            .menuItemMargin(0),
            .menuHeight(33),
            .selectedMenuItemLabelColor(AppColor.orange),
            .unselectedMenuItemLabelColor(AppColor.textSecondary),
            .useMenuLikeSegmentedControl(false),
            .menuItemWidthBasedOnTitleTextWidth(true),
            .menuItemSeparatorWidth(80)
        ]
        let pageMenuFrame = CGRect(x: 0.0, y: 0.0, width: self.view.frame.width, height: self.view.frame.height)
        self.pageMenu = CAPSPageMenu(viewControllers: controllers,
                                     frame: pageMenuFrame,
                                     pageMenuOptions: parameters)
        self.pageMenu?.delegate = self
        self.view.addSubview(self.pageMenu!.view)
        self.view.sendSubview(toBack: self.pageMenu!.view)
    }
    
    /*
    fileprivate func getPostsRequest(withFilterOptions options: FilterOptions) -> String? {
        var request = APIEndPoint.posts.path
        request += "?language=\(SupportedLanguage.current().code)"
        if let distance = options.selectedDistance,
            let location = options.currentLocation {
            request += "&dist=\(distance)"
            request += "&point=\(location.coordinate.longitude),\(location.coordinate.latitude)"
        }
        else if let city = options.city {
            request += "&city=\(city.name)"
            request += "&dist=1"
        }
        if let category = options.category, category.id != Keys.SpecialCategory.all {
            request += "&category=\(category.id)"
        }
        return request.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
    }
    */
    
    fileprivate func prepareToggleMenuBarButton() {
        let menuButton = UIBarButtonItem(image: AppIcons.leftBarMenuIcon, style: .plain, target: self, action: #selector(toggleSideMenu))
        let spacerBarButton = UIBarButtonItem(barButtonSystemItem: .fixedSpace, target: nil, action: nil)
        spacerBarButton.width = -10
        
        var barButtons = self.navigationItem.leftBarButtonItems ?? []
        barButtons.append(spacerBarButton)
        barButtons.append(menuButton)
        
        self.navigationItem.leftBarButtonItems = barButtons
    }
    
    fileprivate func prepareNavigationTitle() {
        self.navigationTitleLabel = UILabel()
        self.navigationTitleLabel.textAlignment = .center
        self.navigationTitleLabel.isUserInteractionEnabled = true
        self.navigationTitleLabel.tag = 0
        self.navigationTitleLabel.textColor = AppColor.orange
        self.navigationTitleLabel.font = UIFont.latoBold(size: 17)
        self.navigationTitleLabel.text = "ALL".localized() + " ▾"
        self.navigationTitleLabel.sizeToFit()
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(navTitleTapped))
        self.navigationTitleLabel.addGestureRecognizer(tap)
        
//        self.filterOptions.asDriver()
//            .map({ $0.category })
//            .distinctUntilChanged()
//            .map({ $0?.localizeName ?? Constants.categoryAllName })
//            .driveNext { [weak self] (name) in
//                guard let `self` = self else { return }
//                self.navigationTitleLabel.text = "\(name) ▾".uppercaseString
////                self.pagingMenuController?.menuView?.currentMenuItemView.titleLabel.text = name.uppercaseString
//                self.navigationTitleLabel.sizeToFit()
//            }
//            .addDisposableTo(self.rx_disposeBag)
        
        self.navigationItem.titleView = self.navigationTitleLabel
    }
    
    fileprivate func prepareViewTypeBarButton() {
        self.viewTypeBarButton = UIBarButtonItem(image: self.viewType.defaultBarButtonIcon,
                                                 style: .plain,
                                                 target: self,
                                                 action: #selector(self.viewTypeBarButtonDidTouch(_:)))
//        self.viewTypeBarButton.imageInsets = UIEdgeInsetsMake(0, 0, 0, -35)
        
        let spacerBarButton = UIBarButtonItem(barButtonSystemItem: .fixedSpace, target: nil, action: nil)
        spacerBarButton.width = -10
        
        var barButtons = self.navigationItem.rightBarButtonItems ?? []
        barButtons.append(spacerBarButton)
        barButtons.append(self.viewTypeBarButton)
        
        self.navigationItem.rightBarButtonItems = barButtons
    }
}

// MARK: Convenience init
extension PaginationViewController {
    convenience init(filterOptions: FilterOptions, viewType: ViewType, mainCategory: Category) {
        self.init(nibName: "PaginationViewController", bundle: nil)
        self.filterOptions = filterOptions
        self.viewType = viewType
        self.mainCategory = mainCategory
    }
}

// MARK: - UITableViewDataSource

extension PaginationViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return categoryMenus.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.ReuseIdentifier.categoryTableViewCell,
                                                               for: indexPath) as! CategoryTableViewCell
        let menu = categoryMenus[indexPath.row]
        cell.subCategoryName.text = menu.title.uppercased()
        cell.indented(menu.level)
        if let icon = menu.icon {
            cell.subCatImageView.image = icon
        }
        else if let iconURLString = menu.iconURL, let URL = URL(string: iconURLString) {
            cell.subCatImageView.sd_setImage(with: URL)
        }
        if menu.type == .selectable {
            cell.checkButton.setImage(menu.category?.id == self.filterOptions.category?.id ? AppIcons.checkMarkSelectedIcon : nil, for: UIControlState())
        }
        else {
            cell.checkButton.setImage(self.categoryMenus.hasChildren(ofMenu: menu) ? AppIcons.carretUpIcon : AppIcons.carretDownIcon, for: UIControlState())
        }
        cell.setAdCount(menu.number)
        
        return cell
    }
}

// MARK: - UITableViewDelegate

extension PaginationViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let menu = categoryMenus[indexPath.row]
        if self.categoryMenus.hasChildren(ofMenu: menu) {
            self.categoryMenus.forEach({
                if $0.level > menu.level, let index = self.categoryMenus.index(of: $0) {
                    self.categoryMenus.remove(at: index)
                }
            })
        }
        else {
            self.categoryMenus.forEach({
                if $0.level > menu.level, let index = self.categoryMenus.index(of: $0) {
                    self.categoryMenus.remove(at: index)
                }
            })
            if let index = self.categoryMenus.index(of: menu), menu.type == .expandable {
                guard let menuCategory = menu.category else { return }
                var subMenus = DataManager.shared.allCategories.flatMap({ (category) -> CategoryMenu? in
                    if category.parentURL == menuCategory.URL {
                        return CategoryMenu(category: category, level: menu.level + 1)
                    }
                    return nil
                })
                let subMenuAll = CategoryMenu(category: menuCategory,
                                                   title: Constants.categoryAllName + " " + menuCategory.localizeName,
                                                   number: menuCategory.localizeNumberOfPosts,
                                                   level: menu.level + 1,
                                                   iconURL: nil,
                                                   icon: AppIcons.bigStarIcon,
                                                   type: .selectable)
                subMenus.insert(subMenuAll, at: 0)
                self.categoryMenus.insert(contentsOf: subMenus, at: index + 1)
            }
        }
        if self.categoryMenus.hasChildren(ofMenu: menu), let index = self.categoryMenus.index(of: menu) {
            self.navigationTableView.scrollToRow(at: IndexPath(row: index, section: 0), at: .top, animated: true)
        }
        if menu.type == .selectable {
            self.toggleNavigationTableView(animated: false)
            self.filterOptions.category = menu.category
        }
        
        tableView.reloadData()
        
        var navigationTitle = "ALL".localized() + " ▾"
        if let categoryName = menu.category?.localizeName {
            navigationTitle = "\(categoryName) ▾".uppercased()
        }
        self.navigationTitleLabel.text = navigationTitle
        self.navigationTitleLabel.sizeToFit()
    }
}

// MARK: PagingMenuControllerDelegate
extension PaginationViewController: CAPSPageMenuDelegate {
    func willMoveToPage(_ controller: UIViewController, index: Int) {
        
    }
    
    func didMoveToPage(_ controller: UIViewController, index: Int) {
        
    }
}
