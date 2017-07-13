//
//  StatesListViewController.swift
//  sosokan
//

import UIKit
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}


class StatesListViewController: UIViewController {
    
    // MARK: IBOulet
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: Variable
    fileprivate var didSelectState: ((State?) -> Void)?
    fileprivate let states: [State] = State.availableStates(SupportedLanguage.current()).sorted(by: { $0.0.code < $0.1.code })
    
    fileprivate struct Constants {
        
        static let popupSize = CGSize(width: 315, height: 500)
        
        struct ReuseIdentifiers {
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


// MARK: - UITableViewDataSource

extension StatesListViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.states.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell: UITableViewCell! = tableView.dequeueReusableCell(withIdentifier: Constants.ReuseIdentifiers.stateTableCell)
        let state = self.states[indexPath.row]
        if cell == nil {
            cell = UITableViewCell.init(style: .value1, reuseIdentifier: Constants.ReuseIdentifiers.stateTableCell)
        }
        cell.textLabel?.text = "\(state.code ?? "") - \(state.name ?? "")"
        return cell
    }
}


// MARK: - UITableViewDelegate

extension StatesListViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let state = self.states[indexPath.row]
        
        didSelectState?(state)
        
        if let popupController = self.popupController {
            popupController.popViewController(animated: true)
        }
        else {
            _ = navigationController?.popViewController(animated: true)
        }
    }
}

// MARK: - convenience init
extension StatesListViewController {
    convenience init(didSelectState: ((State?) -> Void)?) {
        self.init(nibName: "StatesListViewController", bundle: nil)
        self.didSelectState = didSelectState
    }
}
