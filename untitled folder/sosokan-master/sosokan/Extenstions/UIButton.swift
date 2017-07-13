//
//  UIButton.swift
//  Sosokan

extension UIButton {
    var compressedSize: CGSize {
        sizeToFit()
        return frame.size
    }
}

extension UIButton {
    func activated(_ activated: Bool) {
        isEnabled = activated
        alpha = activated ? 1.0 : 0.5
    }
}
