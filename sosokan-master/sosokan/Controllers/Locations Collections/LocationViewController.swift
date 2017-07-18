//
//  LocationViewController.swift
//  sosokan
//

import UIKit
import STPopup
import TTRangeSlider
import MapKit

class LocationViewController: UIViewController {

    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var applyButton: UIButton!
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var cityButton: UIButton!
    @IBOutlet weak var stateLabel: UILabel!
    @IBOutlet weak var stateButton: UIButton!
    @IBOutlet weak var stateBorderView: UIView!
    @IBOutlet weak var cityBorderView: UIView!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var distanceSlider: TTRangeSlider!
    @IBOutlet weak var enableDistanceSwitch: UISwitch!
    
    fileprivate struct Constants {
        
        static let popupSize = CGSize(width: 315, height: 500)
        static let defaultAnimationInterval: TimeInterval = 0.5
        
        struct ReuseIdentifiers {
            static let cell = "LocationPickerViewController.Constants.ReuseIdentifiers.cell"
        }
    }
    
    fileprivate var didChangeFilterOptions: ((FilterOptions) -> Void)?
    fileprivate var filterOptions = DataManager.shared.filterOptions
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        title = "Location".localized()
        
        preparePopupFrame()
        prepareCancelButton()
        prepareApplyButton()
        prepareStateSection()
        prepareCitySection()
        prepareBorderViews()
        prepareDistanceSection()
        prepareMapView()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: IBAction
    
    @IBAction func stateButtonTapped(_ sender: AnyObject) {
        let viewController = StatesListViewController(didSelectState: didSelectState)
        viewController.title = "Select state"
        popupController?.push(viewController, animated: true)
    }
    
    @IBAction func cityButtonTapped(_ sender: AnyObject) {
        let availableCities = City.availableCities(SupportedLanguage.current())
        let cities = availableCities.filter({ $0.stateName == filterOptions.state?.name }).sorted(by: { $0.0.name < $0.1.name })
        let viewController = CitiesListViewController(cities: cities, didSelectCity: didSelectCity)
        viewController.title = "Select city"
        popupController?.push(viewController, animated: true)
    }
    
    @IBAction func cancelButtonTapped(_ sender: Any) {
        popupController?.popViewController(animated: true)
    }
    
    @IBAction func applyButtonTapped(_ sender: Any) {
        didChangeFilterOptions?(filterOptions)
        popupController?.popViewController(animated: true)
    }
    
    @IBAction func enableDistanceSwitchValueChanged(_ sender: Any) {
        let enabled = (sender as? UISwitch)?.isOn ?? false
        filterOptions.selectedDistance = enabled ? Int(distanceSlider.selectedMaximum) : nil
        
        UIView.animate(withDuration: Constants.defaultAnimationInterval) { 
            self.distanceSlider.alpha = enabled ? 1 : 0
        }
    }
    
    // MARK: Private method
    
    fileprivate func didSelectLocation(_ location: CLLocation?) {
        mapView.removeAnnotations(mapView.annotations)
        
        guard let location = location else { return }
        
        let span = MKCoordinateSpanMake(0.05, 0.05)
        let region = MKCoordinateRegion(center: location.coordinate, span: span)
        mapView.setRegion(region, animated: true)
        
        let geoCoder = CLGeocoder()
        geoCoder.reverseGeocodeLocation(location, completionHandler: { (placemarks, error) -> Void in
            let annotation = MKPointAnnotation()
            annotation.coordinate = location.coordinate
            annotation.title = "Me"
            self.mapView.addAnnotation(annotation)
        })
    }
    
    fileprivate func didSelectCity(_ city: City?) {
        cityButton.setTitle(city?.name ?? "N/A", for: .normal)
        
        filterOptions.city = city
        
        if let location = city?.location {
            filterOptions.currentLocation = location
        }
        else if let longitude = city?.longitude,
            let lattidude = city?.latitude {
            filterOptions.currentLocation = CLLocation(latitude: lattidude, longitude: longitude)
        }
        
        didSelectLocation(filterOptions.currentLocation)
    }
    
    fileprivate func didSelectState(_ state: State?) {
        stateButton.setTitle(state?.name ?? "N/A", for: .normal)
        
        filterOptions.state = state
        
        if filterOptions.city?.stateName != state?.name {
            let availableCities = City.availableCities(SupportedLanguage.current()).sorted(by: { $0.0.name < $0.1.name })
            if let index = availableCities.index(where: { $0.stateName == state?.name }) {
                let city = availableCities[index]
                didSelectCity(city)
            }
            else {
                didSelectCity(nil)
            }
        }
    }
    
    fileprivate func prepareMapView() {
        mapView.delegate = self
        didSelectLocation(filterOptions.currentLocation)
    }
    
    fileprivate func prepareDistanceSection() {
        let formatter = NumberFormatter.init()
        distanceSlider.numberFormatterOverride = formatter
        distanceSlider.lineHeight = 2
        distanceSlider.labelPadding = 0
        distanceSlider.tintColor = AppColor.grey
        distanceSlider.tintColorBetweenHandles = AppColor.orange
        distanceSlider.step = 10
        distanceSlider.enableStep = true
        distanceSlider.handleColor = AppColor.orange
        distanceSlider.delegate = self
        distanceSlider.minValue = 0
        distanceSlider.maxValue = 5000
        distanceSlider.selectedMinimum = 0
        distanceSlider.selectedMaximum = Float(filterOptions.selectedDistance ?? 1000)
        distanceSlider.alpha = filterOptions.selectedDistance == nil ? 0 : 1
        
        distanceLabel.text = "Distance (mile)".localized()
        distanceLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        
        enableDistanceSwitch.setOn(filterOptions.selectedDistance != nil, animated: false)
    }
    
    fileprivate func prepareCitySection() {
        cityLabel.text = "City".localized()
        cityLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        
        let title = filterOptions.city?.name ?? "N/A"
        cityButton.setTitle(title, for: .normal)
        cityButton.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 12)
        cityButton.setTitleColor(AppColor.orange, for: .normal)
        
        //        if let myCity = DataManager.sharedInstance.myCity,
        //            let myLocation = myCity.location, myCity.name == selectedCity?.name {
        //            currentLocation = myLocation
        //        }
        //        else if let longitude = selectedCity?.longitude,
        //            let lattidude = selectedCity?.latitude {
        //            currentLocation = CLLocation(latitude: lattidude, longitude: longitude)
        //        }
    }
    
    fileprivate func prepareStateSection() {
        stateLabel.text = "State".localized()
        stateLabel.font = UIFont.latoBold(size: AppFontsizes.regular)
        
        let title = filterOptions.state?.name ?? "N/A"
        stateButton.setTitle(title, for: .normal)
        stateButton.contentEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 12)
        stateButton.setTitleColor(AppColor.orange, for: .normal)
        
        //        if selectedCity.stateName != selectedState.name {
        //            let availableCities = City.availableCities(SupportedLanguage.current()).sorted(by: { $0.0.name < $0.1.name })
        //            if let index = availableCities.index(where: { $0.stateName == selectedState.name }) {
        //                selectedCity = availableCities[index]
        //            }
        //            else {
        //                selectedCity = nil
        //            }
        //        }
    }
    
    fileprivate func preparePopupFrame() {
        contentSizeInPopup = Constants.popupSize
    }
    
    fileprivate func prepareCancelButton() {
        cancelButton.backgroundColor = AppColor.white
        cancelButton.setTitleColor(AppColor.orange, for: .normal)
        cancelButton.bordered(withColor: AppColor.border, width: 1)
    }
    
    fileprivate func prepareApplyButton() {
        applyButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.regular)
        applyButton.backgroundColor = AppColor.orange
        applyButton.setTitleColor(AppColor.white, for: .normal)
        applyButton.bordered(withColor: AppColor.border, width: 1)
    }
    
    fileprivate func prepareBorderViews() {
        let borderViews = [
            cityBorderView,
            stateBorderView
        ]
        borderViews.forEach({
            $0!.backgroundColor = AppColor.border
        })
    }
}

// MARK: - convenience init
extension LocationViewController {
    convenience init(didChangeFilterOptions: ((FilterOptions) -> Void)?) {
        self.init(nibName: "LocationViewController", bundle: nil)
        self.didChangeFilterOptions = didChangeFilterOptions
    }
}

// MARK: TTRangeSliderDelegate
extension LocationViewController: TTRangeSliderDelegate {
    func rangeSlider(_ sender: TTRangeSlider!, didChangeSelectedMinimumValue selectedMinimum: Float, andMaximumValue selectedMaximum: Float) {
        filterOptions.selectedDistance = Int(selectedMaximum)
    }
}

// MARK: - MKMapViewDelegate
extension LocationViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        // Don't want to show a custom image if the annotation is the user's location.
        guard !annotation.isKind(of: MKUserLocation.self) else {
            return nil
        }
        
        // Better to make this class property
        let annotationIdentifier = "AnnotationIdentifier"
        
        var annotationView: MKAnnotationView?
        if let dequeuedAnnotationView = mapView.dequeueReusableAnnotationView(withIdentifier: annotationIdentifier) {
            annotationView = dequeuedAnnotationView
            annotationView?.annotation = annotation
        }
        else {
            let av = MKAnnotationView(annotation: annotation, reuseIdentifier: annotationIdentifier)
            annotationView = av
        }
        
        if let annotationView = annotationView {
            // Configure your annotation view here
            annotationView.canShowCallout = true
            annotationView.image = UIImage(named: "pin-map")
        }
        
        return annotationView
    }
}
