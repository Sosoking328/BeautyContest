//
//  InputSource.swift
//  ImageSlideshow
//
//  Created by Petr Zvoníček on 14.01.16.
//
//

import Foundation

@objc public protocol InputSource {
    func setToImageView(_ imageView: UIImageView);
}

open class ImageSource: NSObject, InputSource {
    var image: UIImage
    
    public init(image: UIImage) {
        self.image = image
    }
    
    public init?(imageString: String) {
        guard let image = UIImage.init(named: imageString) else { return nil }
        self.image = image
        super.init()
    }
    
    @objc open func setToImageView(_ imageView: UIImageView) {
        imageView.image = image
    }
}
