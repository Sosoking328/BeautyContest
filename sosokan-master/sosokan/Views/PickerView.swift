////
////  PickerView.swift
////  Sosokan
//
//import Foundation
//import Localize_Swift
//
//protocol PickerViewDelegate {
//    func pickerView(pickerView: PickerView, selectedValue: AnyObject?)
//}
//
//class PickerView: UIView {
//    
//    var delegate: PickerViewDelegate?
//    var textField: UITextField?
//    var categories = [Category]()
//    var selectedCategory: AnyObject?
//    
//    @IBOutlet weak var pickerView: UIPickerView!
//    @IBOutlet weak var titleLabel: UILabel!
//    @IBOutlet weak var cancelButton: UIButton!
//    @IBOutlet weak var doneButton: UIButton!
//    
//    override func awakeFromNib() {
//        super.awakeFromNib()
//        
//        cancelButton.setTitle("Cancel".localized(), forState: .Normal)
//        doneButton.setTitle("Done".localized(), forState: .Normal)
//        titleLabel.text = "Select category".localized()
//    }
//    
//    func reloadPicker() {
//        /*
//        if let category = selectedCategory {
//            let index = NSArray(array: categories).indexOfObject(category)
//            pickerView.selectRow(index, inComponent: 0, animated: false)
//        }
//         */
//    }
//    
//    @IBAction func cancelButtonDidTouched(sender: AnyObject) {
//        textField?.resignFirstResponder()
//    }
//    
//    @IBAction func doneButtonDidTouched(sender: AnyObject) {
//        delegate?.pickerView(self, selectedValue: selectedCategory)
//        textField?.resignFirstResponder()
//    }
//}
//
//extension PickerView: UIPickerViewDataSource {
//    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
//        return 1
//    }
//    
//    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
//        return categories.count
//    }
//}
//
//extension PickerView: UIPickerViewDelegate {
//    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
//        let category = categories[row]
//        
//        return IS_ENGLISH ? category.name : category.name_c
//    }
//    
//    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
//        let category = categories[row]
//        
//        selectedCategory = category
//    }
//}