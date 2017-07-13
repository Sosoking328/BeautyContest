//
//  NSDate.swift
//  Sosokan



//extension Date: Comparable { }

extension Date {
    static public func < (first: Date, second: Date) -> Bool {
        return first.compare(second) == .orderedAscending
    }
    
    static public func > (first: Date, second: Date) -> Bool {
        return first.compare(second) == .orderedDescending
    }
    
    static public func <= (first: Date, second: Date) -> Bool {
        let cmp = first.compare(second)
        
        return cmp == .orderedAscending || cmp == .orderedSame
    }
    
    static public func >= (first: Date, second: Date) -> Bool {
        let cmp = first.compare(second)
        
        return cmp == .orderedDescending || cmp == .orderedSame
    }
    
    static public func == (first: Date, second: Date) -> Bool {
        return first.compare(second) == .orderedSame
    }
    
    static func getTimestamp() -> TimeInterval {
        return Date.init().timeIntervalSince1970
    }
    
    var monthFirstFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "MM/dd/yyyy"
        
        return formatter
    }
    
    var yearFirstFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        
        return formatter
    }
    
    var month3CharsFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        
        return formatter
    }
    
    var month3CharYearFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "MMM yyyy"
        
        return formatter
    }
    
    var monthFullCharMiddleFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "dd MMMM yyyy"
        
        return formatter
    }
    
    var dayOfWeekFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "EEEE"
        
        return formatter
    }
    
    var hourDayFormatter: DateFormatter {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = "MM/dd/yyyy hh:mm:ss"
        
        return formatter
    }
    
    func monthFirstDate() -> String {
        return monthFirstFormatter.string(from: self)
    }
    
    func yearFirstDate() -> String {
        return yearFirstFormatter.string(from: self)
    }
    
    func monthMiddleWith3Chars() -> String {
        return month3CharsFormatter.string(from: self)
    }
    
    func monthFirst3Chars() -> String {
        return month3CharYearFormatter.string(from: self)
    }
    
    func monthFullCharMiddle() -> String {
        return monthFullCharMiddleFormatter.string(from: self)
    }
    
    func dayOfWeek() -> String {
        return dayOfWeekFormatter.string(from: self)
    }
    
    func hourDay() -> String {
        return hourDayFormatter.string(from: self)
    }
    
    func toString(_ dateStyle: DateFormatter.Style = .medium, timeStyle: DateFormatter.Style = .medium) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = dateStyle
        dateFormatter.timeStyle = timeStyle
        return dateFormatter.string(from: self)
    }
}
