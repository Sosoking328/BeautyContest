//
//  MediaOptionViewController.swift
//  sosokan
//
//  Created by An Phan on 6/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import DKImagePickerController
import CLImageEditor

class MediaOptionViewController: UIViewController{
    
    // MARK: - IBOutlet
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var cameraButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var photoLabel: UIButton!
    @IBOutlet weak var videoLabel: UIButton!
    @IBOutlet weak var bgTopContraint: NSLayoutConstraint!
    @IBOutlet weak var photoWidthConstraint: NSLayoutConstraint!
    @IBOutlet weak var photoHeightContraint: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        titleLabel.font = UIFont.avenirLTStdBook(size: 20)
        if UIDevice.current.deviceType == .iPhone35 || UIDevice.current.deviceType == .iPhone40 {
            bgTopContraint.constant = 0
            photoHeightContraint.constant = 120
            photoWidthConstraint.constant = 120
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    // MARK: - IBAction
    @IBAction func closePageTaped(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func addPhotoTapped(_ sender: Any) {
        let pickerController = DKImagePickerController()
        pickerController.singleSelect = true
        pickerController.showsCancelButton = true
        pickerController.assetType = .allPhotos
        pickerController.showsEmptyAlbums = true
        
        pickerController.didSelectAssets = { [weak self] (assets: [DKAsset]) in
            guard let `self` = self else { return }
            
            if assets.isEmpty {
                // TODO: Display error
            }
            else {
                for asset in assets {
                    asset.fetchFullScreenImageWithCompleteBlock({ (image, info) in
                        if let imageEditorVC = CLImageEditor(image: image, delegate: self) {
                            
                            // Adjust clipping tool.
                            let ratios = [["value1": 1, "value2": 1, "titleFormat": "%.1f : %.1f"]]
                            let tool = imageEditorVC.toolInfo.subToolInfo(withToolName: "CLClippingTool", recursive: false)
                            tool?.optionalInfo["ratios"] = ratios
                            tool?.optionalInfo["swapButtonHidden"] = true
                            tool?.available = true
                            tool?.dockedNumber = -1
                            
                            // Hide other tools.
                            ["CLFilterTool", "CLAdjustmentTool", "CLEffectTool", "CLBlurTool", "CLRotateTool", "CLDrawTool", "CLResizeTool", "CLToneCurveTool"].forEach({ (name) in
                                let tool = imageEditorVC.toolInfo.subToolInfo(withToolName: name, recursive: false)
                                tool?.available = false
                            })
                            
                            self.present(imageEditorVC, animated: true, completion: nil)
                        }

                    })
                }
            }
        }
        
        present(pickerController, animated: true, completion: nil)
    }
    
    @IBAction func addVideoTapped(_ sender: Any) {
    }
}

// MARK: - CLImageEditorDelegate

extension MediaOptionViewController: CLImageEditorDelegate {
    func imageEditor(_ editor: CLImageEditor!, didFinishEdittingWith image: UIImage!) {
        editor.dismiss(animated: true, completion: nil)
        let addMorePhotoVC = StoryBoardManager.viewController("Post",
                                                              viewControllerName: "AddMorePhotoViewController") as! AddMorePhotoViewController
        addMorePhotoVC.firstImage = image
        navigationController?.pushViewController(addMorePhotoVC, animated: true)
        
    }
}
