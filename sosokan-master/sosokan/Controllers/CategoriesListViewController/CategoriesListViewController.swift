//
//  CategoriesListViewController.swift
//  sosokan
//
import UIKit

class CategoriesListViewController: UIViewController {
    
    // MARK: IBOulet
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: Variable
    var categoryMenus: [CategoryMenu] = []
    var selectedMenu: CategoryMenu?
    var selectedAction: ((Category?) -> Void)?
    
    fileprivate struct Constants {
        
        static let popupSize = CGSize(width: 315, height: 500)
        static let categoryCellHeight = CGFloat.init(50)
        static let categoryAllName = "All".localized()
        
        struct ReuseIdentifier {
            static let categoryMenuCell = String(describing: CategoryTableViewCell.self)
        }
    }
    
    // MARK: View controller lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.preparePopupFrame()
        self.prepareTableView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = self.title ?? "Select category".localized()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: Private method
    
    fileprivate func preparePopupFrame() {
        self.contentSizeInPopup = Constants.popupSize
    }
    
    fileprivate func prepareTableView() {
        let nib = UINib.init(nibName: Constants.ReuseIdentifier.categoryMenuCell, bundle: nil)
        self.tableView.register(nib, forCellReuseIdentifier: Constants.ReuseIdentifier.categoryMenuCell)
        self.tableView.backgroundColor = AppColor.white
        self.tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        self.tableView.separatorStyle = .none
        self.tableView.rowHeight = Constants.categoryCellHeight
        self.tableView.dataSource = self
        self.tableView.delegate = self
        
        // Prepare variables
        let rootCategories = DataManager.shared.allCategories.filter({ $0.parentURL == nil })
        let menus = rootCategories.sorted(by: { $0.0.sort < $0.1.sort }).map({ CategoryMenu.init(category: $0, level: 0) })
        self.categoryMenus = menus
        self.tableView.reloadData()
    }
}

// MARK: UITableViewDataSource
extension CategoriesListViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.categoryMenus.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.ReuseIdentifier.categoryMenuCell, for: indexPath) as! CategoryTableViewCell
        let menu = self.categoryMenus[indexPath.row]
        cell.subCategoryName.text = menu.title.uppercased()
        cell.indented(menu.level)
        if let icon = menu.icon {
            cell.subCatImageView.image = icon
        }
        else if let iconURLString = menu.iconURL, let URL = URL.init(string: iconURLString) {
            cell.subCatImageView.sd_setImage(with: URL)
        }
        if menu.type == .selectable {
            cell.checkButton.setImage(menu == self.selectedMenu ? AppIcons.checkMarkSelectedIcon : nil, for: UIControlState())
        }
        else {
            cell.checkButton.setImage(self.categoryMenus.hasChildren(ofMenu: menu) ? AppIcons.carretUpIcon : AppIcons.carretDownIcon, for: UIControlState())
        }
        cell.setAdCount(menu.number)
        return cell
    }
}

// MARK: UITableViewDelegate
extension CategoriesListViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let menu = self.categoryMenus[indexPath.row]
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
            let allCategories = DataManager.shared.allCategories
            if let index = self.categoryMenus.index(of: menu), menu.type == .expandable {
                guard let menuCategory = menu.category else { return }
                var subMenus = allCategories.flatMap({ (category) -> CategoryMenu? in
                    if category.parentURL == menuCategory.URL {
                        return CategoryMenu.init(category: category, level: menu.level + 1)
                    }
                    return nil
                })
                let subMenuAll = CategoryMenu.init(category: menuCategory,
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
        self.selectedMenu = menu
        self.tableView.reloadData()
        if self.categoryMenus.hasChildren(ofMenu: menu), let index = self.categoryMenus.index(of: menu) {
            self.tableView.scrollToRow(at: IndexPath.init(row: index, section: 0), at: .top, animated: true)
        }
        if menu.type == .selectable {
            self.selectedAction?(menu.category)
            self.popupController?.popViewController(animated: true)
        }
    }
}

// MARK: convenience init
extension CategoriesListViewController {
    convenience init() {
        let nibName = String(describing: CategoriesListViewController.self)
        self.init(nibName: nibName, bundle: nil)
    }
}
