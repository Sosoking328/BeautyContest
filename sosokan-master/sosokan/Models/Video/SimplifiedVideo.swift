//
//  SimplifiedVideo.swift
//  sosokan
//

import Foundation

func ==(left: SimplifiedVideo?, right: SimplifiedVideo?) -> Bool {
    return left?.videoURL == right?.videoURL
        && left?.imageURL == right?.imageURL
}

struct SimplifiedVideo {
    var videoURL: String
    var imageURL: String?
    
    init(videoURL: String,
         imageURL: String?) {
        self.videoURL = videoURL
        self.imageURL = imageURL
    }
}