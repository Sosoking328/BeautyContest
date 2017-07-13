//
//  ShareTableViewController.swift
//  sosokan
//
//  Created by An Phan on 6/26/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import LocationPicker
import MapKit

class ShareViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var finishButton: UIButton!
    @IBOutlet weak var stepLabel: UILabel!
    
    let shareItems = ["Facebook", "Twitter", "WeChat"]
    
    fileprivate var selectedLocation: Location? {
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        finishButton.layer.cornerRadius = 7.0
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "pushLocationPickerVCSegue" {
            let locationPicker = segue.destination as! LocationPickerViewController
            locationPicker.showCurrentLocationButton = true
            locationPicker.useCurrentLocationAsHint = true
            locationPicker.showCurrentLocationInitially = true
            locationPicker.mapType = .standard
            locationPicker.completion = {
                self.selectedLocation = $0
            }
            
            // Add select current location button
            let button = UIButton.init()
            button.setTitle("Use my location".localized(), for: .normal)
            button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
            button.titleLabel?.font = UIFont.avenirLTStdRoman(size: 15)
            button.backgroundColor = AppColor.orange
            button.layer.cornerRadius = 5
            button.addTarget(self, action: #selector(myLocationButtonTapped), for: .touchUpInside)
            button.sizeToFit()
            locationPicker.view.layoutSubviews()
            let viewFrame = UIScreen.main.bounds
            var buttonFrame = button.frame
            buttonFrame.origin.x = (viewFrame.width - button.frame.size.width) / 2
            buttonFrame.origin.y = viewFrame.height - button.frame.size.height - 16
            button.frame = buttonFrame
            locationPicker.view.addSubview(button)
            locationPicker.view.bringSubview(toFront: button)
        }
    }
    
    func myLocationButtonTapped() {
        if let location = DataManager.shared.currentLocation {
            CLGeocoder().reverseGeocodeLocation(location, completionHandler: { (placemarks, error) -> Void in
                let placeArray = placemarks
                var placeMark: CLPlacemark!
                placeMark = placeArray?[0]
                var name: String = ""
                if let locationName = placeMark.addressDictionary?["Name"] as? NSString {
                    name = locationName as String
                }
                self.selectedLocation = Location(name: name, placemark: placeMark)
                self.navigationController?.popViewController(animated: true)
            })
        }
    }
    
    @IBAction func finishButtonTapped(_ sender: Any) {
    }
}

extension ShareViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return section == 0 ? 3 : 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellID = indexPath.section == 0 ? "ShareCellID" : "LocationCellID"
        let cell = tableView.dequeueReusableCell(withIdentifier: cellID, for: indexPath) as! CreateAdTableViewCell
        cell.selectionStyle = indexPath.section == 1 ? .default : .none
        
        if indexPath.section == 0 {
            cell.titleLabel.text = shareItems[indexPath.row]
        }
        else if let location = selectedLocation {
            cell.titleLabel.text = location.address
        }
        
        return cell
    }
}

// MARK: - UITableViewDelegate

extension ShareViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView: ShareTableHeaderView = ShareTableHeaderView.fromNib()
        headerView.titleLabel.text = section == 0 ? "Sharing" : "Item location"
        
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return section == 0 ? 50 : 70
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if indexPath.section == 1 {
            
        }
    }
}
