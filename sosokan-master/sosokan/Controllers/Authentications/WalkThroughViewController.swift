//
//  WalkThroughViewController.swift
//  sosokan
//
//  Created by An Phan on 10/2/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Localize_Swift

class WalkThroughViewController: UIViewController {

    @IBOutlet weak var slideShow: ImageSlideshow!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var skipButton: UIButton!
    @IBOutlet weak var blurImageView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        blurImageView.image = UIImage(named: "cn_tutorial1")
        let visualEffectView = UIVisualEffectView(effect: UIBlurEffect(style: .dark))
        visualEffectView.frame = view.frame
        blurImageView.addSubview(visualEffectView)
        
        prepareCarouselView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        nextButton.setTitle("Next".localized(), for: .normal)
        skipButton.setTitle("Skip".localized(), for: .normal)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        setupLanguage()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func nextButtonTapped(_ sender: UIButton) {
        if slideShow.currentItemIndex == 4 {
            dismiss(animated: true, completion: nil)
        }
        else {
            slideShow.setScrollViewPage(slideShow.currentItemIndex + 1, animated: true)
        }
    }
    
    @IBAction func skipButtonTapped(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    fileprivate func setupLanguage() {
        let alertVC = UIAlertController(title: "SoSoKan",
                                        message: "Please select your language",
                                        preferredStyle: .alert)
        let engAction = UIAlertAction(title: "English", style: .default, handler: { (alertAction) in
            Localize.setCurrentLanguage("en")
            self.prepareCarouselView()
        })
        let chineseAction = UIAlertAction(title: "中文", style: .default, handler: { (alertAction) in
            Localize.setCurrentLanguage("zh-Hans")
            self.prepareCarouselView()
        })
        
        alertVC.addAction(engAction)
        alertVC.addAction(chineseAction)
        
        UIApplication.topViewController()?.present(alertVC, animated: true, completion: nil)
    }
    
    fileprivate func prepareCarouselView() {
        // Update language setting
        skipButton.setTitle("Skip".localized(), for: .normal)
        
        // Prepare carousel
        slideShow.contentScaleMode = .scaleAspectFill
        slideShow.pageControlPosition = .insideScrollView
        slideShow.pageControl.currentPageIndicatorTintColor = UIColor(red: 15/255.0, green: 172/255.0, blue: 205/255.0, alpha: 1)
        slideShow.pageControl.pageIndicatorTintColor = UIColor.lightGray
        slideShow.circular = false
        slideShow.bringSubview(toFront: nextButton)
        slideShow.bringSubview(toFront: skipButton)
        slideShow.didScrollToIndex = { index in
            if index == 4 {
                self.nextButton.setTitle("Start".localized(), for: .normal)
            }
            else {
                self.nextButton.setTitle("Next".localized(), for: .normal)
            }
        }
        
        var imageSources = [ImageSource]()
        for index in 1...5 {
            let imageName = IS_ENGLISH ? "eng-tutorial\(index)" : "cn-tutorial\(index)"
            imageSources.append(ImageSource(image: UIImage(named: imageName)!))
        }
        slideShow.setImageInputs(imageSources)
    }
}
