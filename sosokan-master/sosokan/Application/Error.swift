//
//  Error.swift
//  sosokan
//
//  Created by An Phan on 5/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import Foundation

extension Error {
    var code: Int { return (self as NSError).code }
    var domain: String { return (self as NSError).domain }
}
