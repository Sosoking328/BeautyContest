//
//  UITextField.swift
//  sosokan

extension UITextField {
    var getString: String {
        if let text = self.text {
            return text
        }
        
        return ""
    }
    
}
