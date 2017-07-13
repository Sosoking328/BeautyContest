//
//  BaseObjectProtocol.swift
//  sosokan
//
//  Created by An Phan on 10/19/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation

protocol BaseObjectProtocol {
    var id: String { get }
    var createdAt: TimeInterval { get }
    var updatedAt: TimeInterval { get set }
}
