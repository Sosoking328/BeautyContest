//
//  UITextView.swift
//  sosokan

import Foundation

extension UITextView {
    var getString: String {
        if let text = self.text {
            return text
        }
        
        return ""
    }
}
