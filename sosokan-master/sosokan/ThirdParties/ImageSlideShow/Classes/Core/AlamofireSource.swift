//
//  AlamofireSource.swift
//  ImageSlideshow
//
//  Created by Petr Zvoníček on 14.01.16.
//
//

import Alamofire

/// Input Source to image using Alamofire
open class AlamofireSource: NSObject, InputSource {
    var url: URL
    var placeHolderImage: UIImage?
    var onCompleted: ((UIImage?) -> Void)?

    public init(urlString: URL,
                 placeHolderImage: UIImage?,
                 onCompleted: ((UIImage?) -> Void)?) {
        self.url = urlString
        self.placeHolderImage = placeHolderImage
        self.onCompleted = onCompleted
        super.init()
    }

    open func setToImageView(_ imageView: UIImageView) {
        imageView.kf.setImage(with: self.url, placeholder: self.placeHolderImage, options: nil, progressBlock: nil) { (image, error, cacheType, url) in
            imageView.image = image ?? #imageLiteral(resourceName: "no-photo")
        }
    }
}
