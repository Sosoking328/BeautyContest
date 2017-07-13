//
//  IAPHelper.swift
//  sosokan
//
//  Created by An Phan on 8/20/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit

import StoreKit

public typealias ProductIdentifier = String
public typealias ProductsRequestCompletionHandler = (_ success: Bool, _ products: [SKProduct]?) -> ()
public typealias ProductsPurchaseCompletionHandler = (_ canceled: Bool, _ success: Bool, _ identifier: String?) -> Void

open class IAPHelper : NSObject  {
    
    fileprivate let productIdentifiers: Set<ProductIdentifier>
    
    fileprivate var productsRequest: SKProductsRequest?
    fileprivate var productsRequestCompletionHandler: ProductsRequestCompletionHandler?
    fileprivate var productsPurchaseCompletionHandler: ProductsPurchaseCompletionHandler?
    
    static let IAPHelperPurchaseNotification = "IAPHelperPurchaseNotification"
    
    public init(productIds: Set<ProductIdentifier>) {
        self.productIdentifiers = productIds
        
        super.init()
    }
    
    deinit {
        productsPurchaseCompletionHandler = nil
        SKPaymentQueue.default().remove(self)
    }
}

// MARK: - StoreKit API

extension IAPHelper {
    
    public func requestProducts(_ completionHandler: @escaping ProductsRequestCompletionHandler) {
        productsRequest?.cancel()
        productsRequestCompletionHandler = completionHandler
        
        productsRequest = SKProductsRequest(productIdentifiers: productIdentifiers)
        productsRequest!.delegate = self
        productsRequest!.start()
    }
    
    public func buyProduct(_ product: SKProduct, purchasedHandler: @escaping ProductsPurchaseCompletionHandler) {
        print("Buying \(product.productIdentifier)...")
        productsPurchaseCompletionHandler = purchasedHandler
        let payment = SKPayment(product: product)
        
        SKPaymentQueue.default().add(self)
        SKPaymentQueue.default().add(payment)
    }
    
    public class func canMakePayments() -> Bool {
        return SKPaymentQueue.canMakePayments()
    }
    
    public func restorePurchases() {
        SKPaymentQueue.default().restoreCompletedTransactions()
        productsPurchaseCompletionHandler = nil
    }
}

// MARK: - SKProductsRequestDelegate

extension IAPHelper: SKProductsRequestDelegate {
    public func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse) {
        print("Loaded list of products...")
        let products = response.products
        debugPrint(response)
        debugPrint(response.invalidProductIdentifiers.description)
        productsRequestCompletionHandler?(true, products)
        clearRequestAndHandler()
        
        for p in products {
            print("Found product: \(p.productIdentifier) \(p.localizedTitle) \(p.price.floatValue)")
        }
    }
    
    public func request(_ request: SKRequest, didFailWithError error: Error) {
        print("Failed to load list of products.")
        print("Error: \(error.localizedDescription)")
        productsRequestCompletionHandler?(false, nil)
        clearRequestAndHandler()
    }
    
    fileprivate func clearRequestAndHandler() {
        productsRequest = nil
        productsRequestCompletionHandler = nil
    }
}

// MARK: - SKPaymentTransactionObserver

extension IAPHelper: SKPaymentTransactionObserver {
    
    public func paymentQueue(_ queue: SKPaymentQueue, updatedTransactions transactions: [SKPaymentTransaction]) {
        for transaction in transactions {
            switch (transaction.transactionState) {
            case .purchased:
                completeTransaction(transaction)
                break
            case .failed:
                failedTransaction(transaction)
                break
            case .restored:
                restoreTransaction(transaction)
                break
            case .deferred:
                failedTransaction(transaction)
                break
            case .purchasing:
                break
            }
        }
    }
    
    fileprivate func completeTransaction(_ transaction: SKPaymentTransaction) {
        print("completeTransaction...")
        productsPurchaseCompletionHandler?(false, true, transaction.payment.productIdentifier)
        SKPaymentQueue.default().finishTransaction(transaction)
    }
    
    fileprivate func restoreTransaction(_ transaction: SKPaymentTransaction) {
        print("restoreTransaction...")
        productsPurchaseCompletionHandler?(false, true, transaction.original?.payment.productIdentifier)
        SKPaymentQueue.default().finishTransaction(transaction)
    }
    
    fileprivate func failedTransaction(_ transaction: SKPaymentTransaction) {
        print("failedTransaction...")
        
//        if transaction.error!.code != SKError.paymentCancelled.rawValue {
//            print("Transaction Error: \(transaction.error?.localizedDescription)")
//            productsPurchaseCompletionHandler?(false, false, nil)
//        } else {
//            productsPurchaseCompletionHandler?(true, false, nil)
//        }
        
        SKPaymentQueue.default().finishTransaction(transaction)
    }
    
    public func paymentQueue(_ queue: SKPaymentQueue, removedTransactions transactions: [SKPaymentTransaction]) {
        
    }
    
    public func paymentQueueRestoreCompletedTransactionsFinished(_ queue: SKPaymentQueue) {
        
    }
    
    public func paymentQueue(_ queue: SKPaymentQueue, updatedDownloads downloads: [SKDownload]) {
        
    }
    
    public func paymentQueue(_ queue: SKPaymentQueue, restoreCompletedTransactionsFailedWithError error: Error) {
        
    }
}
