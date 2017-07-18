//
//  UIFont.swift
//  FoodTracker

import UIKit

/**
* Custom category of UIFont to load the custom fonts according to texts.
*/

extension UIFont {
    
    /// useful macros
    
    class func latoBold(size s: CGFloat) -> UIFont {
        return UIFont(name: "Lato-Bold", size: s)!
    }
    
    class func latoRegular(size s: CGFloat) -> UIFont {
        return UIFont(name: "Lato", size: s)!
    }
    
    class func openSansSemibold(size s: CGFloat) -> UIFont {
        return UIFont(name: "OpenSans-Semibold", size: s)!
    }
    
    class func avenirLTStdBook(size s: CGFloat) -> UIFont {
        return UIFont(name: "AvenirLTStd-Book", size: s)!
    }
    
    class func avenirLTStdLight(size s: CGFloat) -> UIFont {
        return UIFont(name: "AvenirLTStd-Light", size: s)!
    }
    
    class func avenirLTStdRoman(size s: CGFloat) -> UIFont {
        return UIFont(name: "AvenirLTStd-Roman", size: s)!
    }
}
