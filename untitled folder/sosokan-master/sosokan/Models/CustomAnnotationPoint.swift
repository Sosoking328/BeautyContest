//
//  CustomAnnotationPoint.swift
//  sosokan
//
//  Created by An Phan on 9/13/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import MapKit

class CustomAnnotationPoint: MKPointAnnotation {
    var post: Post?
    
    convenience init(post: Post?) {
        self.init()
        self.post = post
    }
}
