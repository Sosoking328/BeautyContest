//
//  UIScrollView.swift
//  sosokan
//
//  Created by An Phan on 2/6/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import Foundation

extension UIScrollView {
    
    var isAtTop: Bool {
        return contentOffset.y <= self.verticalOffsetForTop
    }
    
    var isAtBottom: Bool {
        return contentOffset.y >= self.verticalOffsetForBottom
    }
    
    var verticalOffsetForTop: CGFloat {
        let topInset = contentInset.top
        return -topInset
    }
    
    var verticalOffsetForBottom: CGFloat {
        let scrollViewHeight = bounds.height
        let scrollContentSizeHeight = contentSize.height
        let bottomInset = contentInset.bottom
        let scrollViewBottomOffset = scrollContentSizeHeight + bottomInset - scrollViewHeight
        return scrollViewBottomOffset
    }
    
}