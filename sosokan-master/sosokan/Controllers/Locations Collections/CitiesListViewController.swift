//
//  CitiesListViewController.swift
//  sosokan
//

import UIKit

class CitiesListViewController: UIViewController {
    
    // MARK: IBOulet
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: Variable
    fileprivate var didSelectCity: ((City?) -> Void)?
    fileprivate var cities = [City]()
    
    fileprivate struct Constants {
        
        static let popupSize = CGSize(width: 315, height: 500)
        
        struct ReuseIdentifiers {
            static let cityTableCell = "CitiesViewController.Constants.ReuseIdentifiers.cityTableCell"
            static let stateTableCell = "CitiesViewController.Constants.ReuseIdentifiers.stateTableCell"
        }
    }
    
    // MARK: View controller lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.preparePopupFrame()
        self.prepareTableView()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
    }
    
    // MARK: Private methods
    
    fileprivate func preparePopupFrame() {
        self.contentSizeInPopup = Constants.popupSize
    }
    
    fileprivate func prepareTableView() {
        self.tableView.backgroundColor = AppColor.white
        self.tableView.tableFooterView = UIView.init(frame: CGRect.zero)
        self.tableView.dataSource = self
        self.tableView.delegate = self
    }
}

// MARK: UITableViewDataSource
extension CitiesListViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return cities.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell: UITableViewCell! = tableView.dequeueReusableCell(withIdentifier: Constants.ReuseIdentifiers.cityTableCell)
        if cell == nil {
            cell = UITableViewCell.init(style: .value1, reuseIdentifier: Constants.ReuseIdentifiers.cityTableCell)
        }
        
        let item = cities[indexPath.row]
        cell.textLabel?.text = item.name
        
        return cell
    }
}

// MARK: UITableViewDelegate
extension CitiesListViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let city = cities[indexPath.row]
        
        didSelectCity?(city)
        
        if let popupController = self.popupController {
            popupController.popViewController(animated: true)
        }
        else {
            _ = navigationController?.popViewController(animated: true)
        }
    }
}

// MARK: - convenience init
extension CitiesListViewController {
    convenience init(cities: [City], didSelectCity: @escaping ((City?) -> Void)) {
        self.init(nibName: "CitiesListViewController", bundle: nil)
        self.cities = cities
        self.didSelectCity = didSelectCity
    }
}
