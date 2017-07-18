//
//  PickerView.swift
//  Sosokan

import Foundation

protocol CountryCodePickerViewDelegate {
    func pickerView(_ pickerView: CountryCodePickerView, selectedValue: RingcaptchaCountry?)
}

class CountryCodePickerView: UIView {
    
    var delegate: CountryCodePickerViewDelegate?
    var textField: UITextField?
    var countries = NSMutableArray()
    var selectedCountry: RingcaptchaCountry?
    
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var pickerView: UIPickerView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var doneButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        cancelButton.setTitle("Cancel".localized(), for: .normal)
        doneButton.setTitle("Done".localized(), for: .normal)
        titleLabel.text = "Select country".localized()
    }
    
    func reloadPicker() {
        for country in countries {
            if let code = (country as AnyObject).isoCode, code == selectedCountry?.isoCode {
                let index = countries.index(of: country)
                pickerView.selectRow(index, inComponent: 0, animated: false)
                break
            }
        }
    }
    
    @IBAction func cancelButtonDidTouched(_ sender: AnyObject) {
        textField?.resignFirstResponder()
    }
    
    @IBAction func doneButtonDidTouched(_ sender: AnyObject) {
        textField?.resignFirstResponder()
        
        delegate?.pickerView(self, selectedValue: selectedCountry)
    }
}

extension CountryCodePickerView: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return countries.count
    }
}

extension CountryCodePickerView: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if let category = countries[row] as? RingcaptchaCountry {
            return category.name
        }
        
        return ""
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if let country = countries[row] as? RingcaptchaCountry {
            selectedCountry = country
        }
    }
}
