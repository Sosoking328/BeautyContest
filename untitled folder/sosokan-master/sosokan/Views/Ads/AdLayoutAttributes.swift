//
//  AdLayoutAttributes.swift
//  sosokan
//
//  Created by An Phan on 7/4/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

class AdLayoutAttributes: UICollectionViewLayoutAttributes {
    var photoHeight: CGFloat = 0.0

    override func copy(with zone: NSZone?) -> Any {
        let copy = super.copy(with: zone) as! AdLayoutAttributes
        copy.photoHeight = photoHeight
        return copy
    }
    
    override func isEqual(_ object: Any?) -> Bool {
        if let attributes = object as? AdLayoutAttributes {
            if( attributes.photoHeight == photoHeight  ) {
                return super.isEqual(object)
            }
        }
        return false
    }
}
