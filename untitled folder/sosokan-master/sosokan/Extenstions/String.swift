//
//  String.swift
//  Sosokan

import Foundation

extension String {
    func toTime(withFormat format: String, timeZone: String) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        dateFormatter.timeZone = TimeZone(identifier: timeZone)
        let date = dateFormatter.date(from: self)
        return date
    }
    
    func contains(_ find: String) -> Bool{
        return self.range(of: find) != nil
    }
    
    func containsIgnoringCase(_ find: String) -> Bool{
        return self.range(of: find, options: NSString.CompareOptions.caseInsensitive) != nil
    }
    
//    var toValidEmailAsUserId: String {
//        return self.stringByReplacingOccurrencesOfString(".", withString: "dot")
//    }
    
    var html2AttributedString: NSAttributedString? {
        guard
            let data = data(using: String.Encoding.utf8)
            else { return nil }
        do {
            return try NSAttributedString(data: data, options: [NSDocumentTypeDocumentAttribute:NSHTMLTextDocumentType,NSCharacterEncodingDocumentAttribute:String.Encoding.utf8], documentAttributes: nil)
        } catch let error as NSError {
            print(error.localizedDescription)
            return  nil
        }
    }
    
    var html2String: String {
        return html2AttributedString?.string ?? ""
    }
    
    var isPhoneNumber: Bool {
        let charcterSet  = NSCharacterSet(charactersIn: "+0123456789").inverted
        let inputString = self.components(separatedBy: charcterSet)
        let filtered = inputString.joined(separator: "")
        return  self == filtered
    }
    
    func isValidEmail() -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let result = emailTest.evaluate(with: self)
        return result
    }
    
    func trim() -> String {
        return self.trimmingCharacters(in: CharacterSet.whitespaces)
    }
    
    func replace(_ target: String, withString: String) -> String {
        return self.replacingOccurrences(of: target, with: withString,
            options: NSString.CompareOptions.literal, range: nil)
    }
    
    func toDateTime() -> Date? {
        // Create Date Formatter
        let dateFormatter = DateFormatter()
        
        // Specify Format of String to Parse
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        // Parse into NSDate
        if let dateFromString = dateFormatter.date(from: self) {
            // Return Parsed Date
            return dateFromString
        }
        else {
            dateFormatter.dateFormat = "yyyy-MM-dd"
            if let dateFromString = dateFormatter.date(from: self) {
                // Return Parsed Date
                return dateFromString
            }
        }
        
        return nil
    }
    
    func dayOfWeek() -> String {
        switch self {
        case "Mon":
            return "Monday"
        case "Tue":
            return "Tuesday"
        case "Wed":
            return "Wednesday"
        case "Thu":
            return "Thursday"
        case "Fri":
            return "Friday"
        case "Sat":
            return "Saturday"
        case "Sun":
            return "Sunday"
        default:
            return "Unknown"
        }
    }
    
    func heightWithConstrainedWidth(_ width: CGFloat, font: UIFont) -> CGFloat {
        let rect = NSString(string: self).boundingRect(with: CGSize(width: width, height: CGFloat(MAXFLOAT)), options: .usesLineFragmentOrigin, attributes: [NSFontAttributeName: font], context: nil)
        return ceil(rect.height)
    }
    
    func widthWithConstrainedHeight(_ height: CGFloat, font: UIFont) -> CGFloat {
        let rect = NSString(string: self).boundingRect(with: CGSize(width: CGFloat(MAXFLOAT), height: height), options: .usesLineFragmentOrigin, attributes: [NSFontAttributeName: font], context: nil)
        return ceil(rect.width)
    }
    
    var isAlphabetical: Bool {
        return range(of: "^[a-zA-Z]", options: .regularExpression) != nil
    }
    
    func htmlDecoded()->String {
        
        guard (self != "") else { return self }
        
        var newStr = self
        
        let entities = [
            "&quot;"    : "\"",
            "&amp;"     : "&",
            "&apos;"    : "'",
            "&lt;"      : "<",
            "&gt;"      : ">",
            ]
        
        for (name,value) in entities {
            newStr = newStr.replacingOccurrences(of: name, with: value)
        }
        return newStr
    }
}
