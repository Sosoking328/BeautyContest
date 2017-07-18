//
//  AddMorePhotoViewController.swift
//  sosokan
//
//  Created by An Phan on 6/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import DKImagePickerController
import CLImageEditor

class AddMorePhotoViewController: UIViewController {

    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var firstImageView: UIImageView!
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var trashButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var stepLabel: UILabel!
    
    var firstImage: UIImage?
    var selectedImages = [UIImage]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.isTranslucent = false
        title = "Add More Photo"
        
        //self.titleTextField.layer.cornerRadius = 6
        self.nextButton.layer.cornerRadius = 7
        firstImageView.layer.borderColor = UIColor.navTitleColor().cgColor
        firstImageView.layer.borderWidth = 2.0
        
        // Do any additional setup after loading the view.
        
        if let image = firstImage {
            firstImageView.image = image
            selectedImages.append(image)
        }

        nextButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 25)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - IBActions
    
    @IBAction func firstImageViewTapped(_ sender: Any) {
        addMorePhotoTapped(nil)
    }
    
    @IBAction func trashButtonTapped(_ sender: Any) {
        selectedImages.removeFirst()
        firstImageView.image = selectedImages.first
        collectionView.reloadData()
        
        if selectedImages.count == 0 {
            trashButton.isHidden = true
            firstImageView.image = #imageLiteral(resourceName: "photo-icon")
            firstImageView.isUserInteractionEnabled = true
        }
    }
    
    @IBAction func addMorePhotoTapped(_ sender: Any?) {
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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

// MARK: - UICollectionViewDataSource

extension AddMorePhotoViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return selectedImages.count == 5 ? 4 : selectedImages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AddPhotoCollectionViewCell.idenfitier,
                                                      for: indexPath) as! AddPhotoCollectionViewCell
        if selectedImages.count > 1 && indexPath.item < selectedImages.count - 1 {
            let image = selectedImages[indexPath.item + 1]
            cell.photoImageView.image = image
            cell.photoButton.isHidden = true
            cell.deleteButton.isHidden = false
        }
        
        cell.deleteButtonAction = {
            self.selectedImages.remove(at: indexPath.item + 1)
            self.collectionView.reloadData()
        }
        
        return cell
    }
}

// MARK: - ELCImagePickerControllerDelegate

extension AddMorePhotoViewController: CLImageEditorDelegate {
    func imageEditor(_ editor: CLImageEditor!, didFinishEdittingWith image: UIImage!) {
        editor.dismiss(animated: true, completion: nil)
        
        if selectedImages.count == 0 { firstImageView.image = image }
        
        selectedImages.append(image)
        collectionView.reloadData()
    }
}
