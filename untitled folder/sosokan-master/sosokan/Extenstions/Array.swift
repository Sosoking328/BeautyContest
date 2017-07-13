//
//  Array.swift
//  sosokan
//
//  Created by An Phan on 8/25/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import Foundation

extension Array where Element: CategoryMenu {
    func hasChildren(ofMenu menu: CategoryMenu) -> Bool {
        guard let category = menu.category else { return false }
        return self.index(where: { $0.category?.parentURL == category.URL }) != nil
    }
}

extension Array where Element: Category {
    
    func hasChildren(ofCategory category: Category) -> Bool {
        return self.index(where: { $0.parentURL == category.URL }) != nil
    }
    
    func getChildren(ofCategory category: Category, allLevel: Bool) -> [Category] {
        var children: [Category] = self.filter({ $0.parentURL == category.URL })
        if !allLevel {
            return children
        }
        else {
            for child in children {
                children.append(contentsOf: self.getChildren(ofCategory: child, allLevel: allLevel))
            }
            return children
        }
    }
    
    func getParents(ofCategory category: Category, allLevel: Bool) -> [Category] {
        var parents: [Category] = self.filter({ $0.URL == category.parentURL })
        
        if !allLevel {
            return parents
        }
        else {
            for parent in parents {
                parents.append(contentsOf: self.getParents(ofCategory: parent, allLevel: allLevel))
            }
            return parents
        }
    }
    
    func getCategory(withCategoryId id: Int) -> Category? {
        if let index = self.index(where: { $0.id == id }) {
            return self[index]
        }
        return nil
    }
}

extension Array where Element: Hashable {
    func before(_ item: Element) -> Element? {
        if let index = self.index(of: item), index > 0 {
            return self[index - 1]
        }
        return nil
    }
    
    func after(_ item: Element) -> Element? {
        if let index = self.index(of: item), index + 1 < self.count {
            return self[index + 1]
        }
        return nil
    }
    
    func isElementExist(atIndex index: Int) -> Bool {
        return self.count > index
    }
}
/*
extension Array {
    func random(numberOfElements number: Int) -> Array {
        var shuffled = self
        shuffled.shuffleInPlace()
        
        if number >= self.count {
            return shuffled
        }
        else {            
            return shuffled.prefix(number).map({ $0 })
        }
    }
}

extension MutableCollection where Index == Int {
    /// Shuffle the elements of `self` in-place.
    mutating func shuffleInPlace() {
        // empty and single-element collections don't shuffle
        if count < 2 { return }
        
        for i in startIndex ..< endIndex - 1 {
            let j = Int(arc4random_uniform(UInt32(count - i))) + i
            guard i != j else { continue }
            swap(&self[i], &self[j])
        }
    }
}

extension Collection {
    /// Return a copy of `self` with its elements shuffled.
    func shuffle() -> [Iterator.Element] {
        var list = Array(self)
        list.shuffleInPlace()
        return list
    }
}
*/
