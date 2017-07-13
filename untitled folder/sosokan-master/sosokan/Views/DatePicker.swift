//
//  DatePicker.swift
//  sosokan

import UIKit

protocol DatePickerDelegate {
    func datePicker(_ datePicker: DatePicker, selectedDate: Date)
}

class DatePicker: UIView {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var doneButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    
    let appDelegate = UIApplication.shared.delegate as! AppDelegate
    
    var delegate: DatePickerDelegate?
    var selectedDate: Date?
    var textField: UITextField?
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func reloadDatePicker() {
        cancelButton.setTitle("Cancel".localized(), for: .normal)
        doneButton.setTitle("Done".localized(), for: .normal)
        titleLabel.text = "Select Date".localized()
        if let date = selectedDate {
            datePicker.date = date
        }
    }
    
    @IBAction func cancelButtonDidTouched(_ sender: AnyObject) {
        textField?.resignFirstResponder()
    }
    
    @IBAction func doneButtonDidTouched(_ sender: AnyObject) {
        textField?.resignFirstResponder()
        if let date = selectedDate {
            delegate?.datePicker(self, selectedDate: date)
        }
        else {
            delegate?.datePicker(self, selectedDate: Date())
        }
    }
    
    @IBAction func datePickerChangedValue(_ datePicker: UIDatePicker) {
        print(datePicker.date)
        selectedDate = datePicker.date
    }
}
