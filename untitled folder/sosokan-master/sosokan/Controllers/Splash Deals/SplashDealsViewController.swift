//
//  SplashDealsViewController.swift
//  sosokan
//

import UIKit
import Material

class SplashDealsViewController: UIViewController {
    
    @IBOutlet weak var slider: ImageSlideshow!
//    @IBOutlet weak var doneButton: MaterialButton!
    
    var splashDeals = [SplashDeal]()
    
    fileprivate struct Constants {
        static let popupSize = CGSize(width: 315, height: 315)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.preparePopupFrame()
        self.prepareSlider()
        
        title = "Hot deals"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
    }
    
    // MARK: Private method
    fileprivate func preparePopupFrame() {
        self.contentSizeInPopup = Constants.popupSize
    }
    
    fileprivate func prepareSlider() {
        let placeHolder = AppIcons.imagePlaceHolder
        let inputSources = self.splashDeals.map({ SDWebImageSource.init(url: $0.absoluteURL, placeholder: placeHolder!) })
        self.slider.setImageInputs(inputSources)
        self.slider.pageControl.alpha = inputSources.count > 1 ? 1 : 0
    }
    
    /*
    private func prepareDoneButton() {
        self.doneButton.titleLabel?.font = UIFont.latoBold(size: AppFontsizes.regular)
        self.doneButton.backgroundColor = AppColor.orange
        self.doneButton.setTitleColor(AppColor.white, forState: .Normal)
        self.doneButton.bordered(withColor: AppColor.border, width: 1)
        self.doneButton.setTitle("Done".localized(), forState: .Normal)
        self.doneButton.rx_tap.asDriver()
            .driveNext { [weak self] (_) in
                guard let `self` = self else { return }
                self.popupController?.popViewControllerAnimated(true)
            }
            .addDisposableTo(self.rx_disposeBag)
    }
     */
}

// MARK: - convenience init
extension SplashDealsViewController {
    convenience init() {
        let nibName = String(describing: SplashDealsViewController.self)
        self.init(nibName: nibName, bundle: nil)
    }
}
