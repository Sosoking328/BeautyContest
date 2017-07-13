//
//  AdLayout.swift
//  sosokan
//
//  Created by An Phan on 7/3/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

protocol AdLayoutDelegate {
    func collectionView(_ collectionView:UICollectionView, heightForPhotoAtIndexPath indexPath:IndexPath,
                        withWidth:CGFloat) -> CGFloat
    func collectionView(_ collectionView: UICollectionView,
                        heightForDescAtIndexPath indexPath: IndexPath, withWidth width: CGFloat) -> CGFloat
}

class AdLayout: UICollectionViewLayout {
    
    var delegate: AdLayoutDelegate!
    var numberOfColumns = 2
    var cellPadding: CGFloat = 6.0
    var otherItemHeight: CGFloat = 60.0
    fileprivate var cache = [AdLayoutAttributes]()
    fileprivate var contentHeight: CGFloat  = 0.0
    fileprivate var contentWidth: CGFloat {
        let insets = collectionView!.contentInset
        return collectionView!.bounds.width - (insets.left + insets.right)
    }
    
    override func prepare() {
        cache.removeAll()
        contentHeight = 0.0
        if cache.isEmpty {
            let columnWidth = contentWidth / CGFloat(numberOfColumns)
            var xOffset = [CGFloat]()
            for column in 0 ..< numberOfColumns {
                xOffset.append(CGFloat(column) * columnWidth )
            }
            var column = 0
            var yOffset = [CGFloat](repeating: 0, count: numberOfColumns)
            
            for item in 0 ..< collectionView!.numberOfItems(inSection: 0) {
                
                let indexPath = IndexPath(item: item, section: 0)
                let width = columnWidth - cellPadding * 2
                let photoHeight = delegate.collectionView(collectionView!, heightForPhotoAtIndexPath: indexPath,
                                                          withWidth:width)
                let annotationHeight = delegate.collectionView(collectionView!,
                                                               heightForDescAtIndexPath: indexPath, withWidth: width)
                let height = cellPadding +  photoHeight + annotationHeight + cellPadding + otherItemHeight
                let frame = CGRect(x: xOffset[column], y: yOffset[column], width: columnWidth, height: height)
                let insetFrame = frame.insetBy(dx: cellPadding, dy: cellPadding)
                
                let attributes = AdLayoutAttributes(forCellWith: indexPath)
                attributes.photoHeight = photoHeight
                attributes.frame = insetFrame
                cache.append(attributes)
                
                contentHeight = max(contentHeight, frame.maxY)
                yOffset[column] = yOffset[column] + height
                
                column = column >= (numberOfColumns - 1) ? 0 : column + 1
            }
        }
    }
    
    override var collectionViewContentSize : CGSize {
        return CGSize(width: contentWidth, height: contentHeight)
    }
    
    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        if cache.count > indexPath.row {
            return cache[indexPath.row]
        }
        else {
            return nil
        }
    }
    
    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        
        var layoutAttributes = [UICollectionViewLayoutAttributes]()
        
        for attributes in cache {
            if attributes.frame.intersects(rect) {
                layoutAttributes.append(attributes)
            }
        }
        return layoutAttributes
    }
    
    override class var layoutAttributesClass : AnyClass {
        return AdLayoutAttributes.self
    }
}
