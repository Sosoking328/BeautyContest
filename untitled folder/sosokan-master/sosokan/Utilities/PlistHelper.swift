//
//  PlistHelper.swift
//  sosokan
//
//  Created by David Nguyentan on 7/3/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

struct AvailableSettingKey {
    static let filter = "Filter"
    static let filterViewType = "View type"
    static let filterDistance = "Distance"
    static let filterSortOrder = "Sort order"
}

class PlistHelper: NSObject {
    var fileName: String
    var settingFilePath: String
    var results: NSMutableDictionary?
    
    fileprivate let documentPath: String = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
    fileprivate let fileManager = FileManager.default
    
    init(fileName: String) {
        self.fileName = fileName
        settingFilePath = (documentPath as NSString).appendingPathComponent(fileName)
        
        if !fileManager.fileExists(atPath: settingFilePath) { // File isn't existing
            let defaultSetting = Bundle.main.path(forResource: fileName, ofType: nil)
            
            do {
                try fileManager.copyItem(atPath: defaultSetting!, toPath: settingFilePath)
                
                self.results = NSMutableDictionary(contentsOfFile: defaultSetting!)
            } catch let error as NSError {
                fatalError(error.localizedDescription)
            }
        } else {
            results = NSMutableDictionary(contentsOfFile: settingFilePath)
        }
        
        super.init()
    }
}

extension PlistHelper {
    func getValue(_ key: String) -> AnyObject? {
        return results?.value(forKey: key) as AnyObject?
    }
    
    func setValue(_ key: String, value: AnyObject) {
        results?.setValue(value, forKey: key)
        results?.write(toFile: settingFilePath, atomically: false)
    }
}
