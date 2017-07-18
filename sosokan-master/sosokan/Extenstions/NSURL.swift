//
//  NSURL.swift
//  sosokan
//
//  Created by An Phan on 1/12/17.
//  Copyright © 2017 icanstudioz inc. All rights reserved.
//

import Foundation
import RxCocoa
import RxSwift
import Alamofire

typealias RequestResponse = (NSHTTPURLResponse?, AnyObject?)

extension NSURL {
    
    func delete(parameters parameters: JSONType?, encoding: ParameterEncoding, headers: [String: String]?) -> Observable<(NSHTTPURLResponse?, AnyObject?)> {
        return Observable.create { (observer: AnyObserver<(NSHTTPURLResponse?, AnyObject?)>) -> Disposable in
            
            Alamofire.request(.DELETE, self, parameters: parameters, encoding: .JSON, headers: headers)
                .responseJSON(completionHandler: { (response) in
                    if let error = response.result.error {
                        observer.on(.Error(error))
                    }
                    else {
                        observer.on(.Next(response.response, response.result.value))
                        observer.on(.Completed)
                    }
                })
            
            return NopDisposable.instance
        }
    }
    
    func put(parameters parameters: JSONType?, encoding: ParameterEncoding, headers: [String: String]?) -> Observable<(NSHTTPURLResponse?, AnyObject?)> {
        return Observable.create { (observer: AnyObserver<(NSHTTPURLResponse?, AnyObject?)>) -> Disposable in
            
            Alamofire.request(.PUT, self, parameters: parameters, encoding: .JSON, headers: headers)
                .responseJSON(completionHandler: { (response) in
                    if let error = response.result.error {
                        observer.on(.Error(error))
                    }
                    else {
                        observer.on(.Next(response.response, response.result.value))
                        observer.on(.Completed)
                    }
                })
            
            return NopDisposable.instance
        }
    }
    
    func fetchImage() -> Driver<UIImage?> {
        let observer = Observable.create { (observer: AnyObserver<UIImage?>) -> Disposable in
            
            if let image = DataManager.sharedInstance.getCacheImage(self) {
                observer.on(.Next(image))
                observer.onCompleted()
            }
            else {
                UIImage.contentsOfURL(self, completion: { (image, error) in
                    if let error = error {
                        observer.onError(error)
                    }
                    else {
                        observer.on(.Next(image))
                        observer.onCompleted()
                    }
                })
            }
            
            return NopDisposable.instance
        }
        
        return observer.asDriver(onErrorJustReturn: nil)
    }
    
    func get(clearCookies clearCookies: Bool, parameters: JSONType?, encoding: ParameterEncoding, headers: [String: String]?) -> Observable<(NSHTTPURLResponse?, AnyObject?)> {
        return Observable.create { (observer: AnyObserver<(NSHTTPURLResponse?, AnyObject?)>) -> Disposable in
            
            if clearCookies {
                self.clearCookies()
            }
            
            Alamofire.request(.GET, self, parameters: parameters, encoding: .JSON, headers: headers)
                .responseJSON(completionHandler: { (response) in
                    if let error = response.result.error {
                        observer.on(.Error(error))
                    }
                    else {
                        observer.on(.Next(response.response, response.result.value))
                        observer.on(.Completed)
                    }
                })
            
            return NopDisposable.instance
        }
    }
    
    func post(clearCookies clearCookies: Bool, parameters: JSONType?, encoding: ParameterEncoding, headers: [String: String]?) -> Observable<(NSHTTPURLResponse?, AnyObject?)> {
        return Observable.create { (observer: AnyObserver<(NSHTTPURLResponse?, AnyObject?)>) -> Disposable in
            
            if clearCookies {
                self.clearCookies()
            }
            
            Alamofire.request(.POST, self, parameters: parameters, encoding: .JSON, headers: headers)
                .responseJSON(completionHandler: { (response) in
                    if let error = response.result.error {
                        observer.on(.Error(error))
                    }
                    else {
                        observer.on(.Next(response.response, response.result.value))
                        observer.on(.Completed)
                    }
                })
            
            return NopDisposable.instance
        }
    }
    
    func put(clearCookies clearCookies: Bool, parameters: JSONType?, encoding: ParameterEncoding, headers: [String: String]?) -> Observable<(NSHTTPURLResponse?, AnyObject?)> {
        return Observable.create { (observer: AnyObserver<(NSHTTPURLResponse?, AnyObject?)>) -> Disposable in
            
            if clearCookies {
                self.clearCookies()
            }
            
            Alamofire.request(.PUT, self, parameters: parameters, encoding: .JSON, headers: headers)
                .responseJSON(completionHandler: { (response) in
                    if let error = response.result.error {
                        observer.on(.Error(error))
                    }
                    else {
                        observer.on(.Next(response.response, response.result.value))
                        observer.on(.Completed)
                    }
                })
            
            return NopDisposable.instance
        }
    }
    
    func clearCookies() {
        let storage = NSHTTPCookieStorage.sharedHTTPCookieStorage()
        for cookie in storage.cookiesForURL(self) ?? [] {
            storage.deleteCookie(cookie)
        }
    }
}