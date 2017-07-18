//
//  UIImage.swift
//  sosokan

extension UIImage {
    func getUIImageFromUIView(_ myUIView:UIView) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(myUIView.frame.size, true, 0);
        let context:CGContext = UIGraphicsGetCurrentContext()!;
        context.translateBy(x: -myUIView.frame.origin.x, y: -myUIView.frame.origin.y);
        myUIView.layer.render(in: context);
        let renderedImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()!;
        UIGraphicsEndImageContext();
        return renderedImage;
    }
    
    class func image(fromColor color: UIColor) -> UIImage {
        let rect = CGRect(x: 0, y: 0, width: 1, height: 1)
        UIGraphicsBeginImageContext(rect.size)
        let context = UIGraphicsGetCurrentContext()
        context?.setFillColor(color.cgColor)
        context?.fill(rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return img!
    }
    
    var uncompressedPNGData: Data      { return UIImagePNGRepresentation(self)!        }
    var highestQualityJPEGNSData: Data { return UIImageJPEGRepresentation(self, 1.0)!  }
    var highQualityJPEGNSData: Data    { return UIImageJPEGRepresentation(self, 0.75)! }
    var mediumQualityJPEGNSData: Data  { return UIImageJPEGRepresentation(self, 0.5)!  }
    var lowQualityJPEGNSData: Data     { return UIImageJPEGRepresentation(self, 0.25)! }
    var lowestQualityJPEGNSData:Data   { return UIImageJPEGRepresentation(self, 0.0)!  }
}
