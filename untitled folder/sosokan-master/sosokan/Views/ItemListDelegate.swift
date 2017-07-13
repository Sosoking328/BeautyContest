//
//  ItemListDelegate.swift
//  sosokan

import Foundation

protocol ItemListDelegate : class {
    func itemListClickFavorite(_ sender: AnyObject)
    func itemListClickShare(_ sender: AnyObject)
    func itemListClickRemove(_ sender: AnyObject?)
}
