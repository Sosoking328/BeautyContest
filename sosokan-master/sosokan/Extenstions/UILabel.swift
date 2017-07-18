//
//  UILabel.swift
//  sosokan
//

import Foundation

extension UILabel {
    
    fileprivate static let calculationLabel = UILabel.init()
    
    class func dynamicLabelHeight(maxWidth width: CGFloat, font: UIFont, content: String) -> CGFloat {
        self.calculationLabel.frame = CGRect.init(origin: CGPoint.zero, size: CGSize.init(width: width, height: CGFloat.greatestFiniteMagnitude))
        self.calculationLabel.font = font
        self.calculationLabel.text = content
        self.calculationLabel.numberOfLines = 2
        self.calculationLabel.lineBreakMode = .byWordWrapping
        self.calculationLabel.sizeToFit()
        return self.calculationLabel.frame.height
    }
    
    class func dynamicLabelHeight(maxWidth width: CGFloat, font: UIFont, content: String, attributes: [String : AnyObject]) -> CGFloat {
        self.calculationLabel.frame = CGRect(x: 0, y: 0, width: width, height: CGFloat.greatestFiniteMagnitude)
        self.calculationLabel.preferredMaxLayoutWidth = self.calculationLabel.bounds.width
        self.calculationLabel.font = font
        self.calculationLabel.text = nil
        self.calculationLabel.attributedText = NSAttributedString.init(string: content, attributes: attributes)
        self.calculationLabel.numberOfLines = 2
        self.calculationLabel.lineBreakMode = .byWordWrapping
        self.calculationLabel.sizeToFit()
        return self.calculationLabel.frame.height
    }
}
