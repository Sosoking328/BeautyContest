//
//  FollowingViewController.swift
//  sosokan
//
//  Created by An Phan on 12/20/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import UIScrollView_InfiniteScroll
import CHTCollectionViewWaterfallLayout
import FBSDKShareKit
import SwiftyJSON

class FollowingViewController: UIViewController {
    
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var collectionView: UICollectionView!
    
    fileprivate struct Constants {
        fileprivate struct ReuseIdentifiers {
            static let followUserCollectionViewCell = String(describing: FollowUserCollectionViewCell.self)
        }
    }
    
    fileprivate var userId: Int!
    fileprivate var userProfiles: [Profile] = []
    fileprivate let request = "http://sosokan-staging.herokuapp.com/api/userprofiles/?limit=20&offset=0"

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareSearchBar()
        self.prepareCollectionView()
        self.prepareMenuBarButton()
        
        self.fetchUserProfiles()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = "FOLLOWING".localized()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        
        self.searchBar.resignFirstResponder()
    }
    
    // MARK: Method
    
    func menuBarButtonTapped(_ sender: AnyObject) {
        AppDelegate.shared().toggleLeftDrawer(self, animated: true)
    }
    
    // MARK: Private method
    
    fileprivate func fetchUserProfiles() {
        self.showNetworkIndicator()
        /*
        self.request.requestJSON { [weak self] (request, response, results) in
            guard let `self` = self else { return }
            self.hideNetworkIndicator()
            if let error = results.error {
                debugPrint(error)
                self.showErrorMessage(Messages.occurredError)
            }
            else {
                let json = JSON(results.value!)
                let userProfilesData = json[Keys.kResults].array ?? []
                let userProfiles = userProfilesData.flatMap({ Profile(json: $0) })
                self.userProfiles = userProfiles
                self.collectionView.reloadData()
            }
        }
         */
    }
    
    fileprivate func prepareSearchBar() {
        self.searchBar.placeholder = "Search Who You are Following"
        self.searchBar.backgroundColor = AppColor.clear
        self.searchBar.barTintColor = AppColor.white
        self.searchBar.isTranslucent = false
        self.searchBar.backgroundImage = UIImage.init()
        
        let textField = self.searchBar.value(forKey: "_searchField") as? UITextField
        textField?.borderStyle = .none
        textField?.layer.borderColor = AppColor.border.cgColor
        textField?.layer.borderWidth = 1
        textField?.layer.cornerRadius = 8
    }
    
    fileprivate func prepareCollectionView() {
        let nib = UINib.init(nibName: Constants.ReuseIdentifiers.followUserCollectionViewCell, bundle: nil)
        self.collectionView.register(nib, forCellWithReuseIdentifier: Constants.ReuseIdentifiers.followUserCollectionViewCell)
        self.collectionView.backgroundColor = AppColor.white
        self.collectionView.dataSource = self
        self.collectionView.delegate = self
        self.collectionView.keyboardDismissMode = .onDrag
        
        let layout = UICollectionViewFlowLayout.init()
        layout.minimumLineSpacing = 8
        layout.minimumInteritemSpacing = 0
        self.collectionView.collectionViewLayout = layout
    }
    
    fileprivate func prepareMenuBarButton() {
        let leftBarButton = UIBarButtonItem(image: AppIcons.leftBarMenuIcon,
                                            style: .plain,
                                            target: self, action: #selector(self.menuBarButtonTapped(_:)))
        self.navigationItem.leftBarButtonItem = leftBarButton
    }
}

extension FollowingViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.userProfiles.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: Constants.ReuseIdentifiers.followUserCollectionViewCell, for: indexPath) as! FollowUserCollectionViewCell
        let profile = self.userProfiles[indexPath.item]
        cell.render(profile)
        return cell
    }
}

// MARK: CHTCollectionViewDelegateWaterfallLayout
extension FollowingViewController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: IndexPath) -> CGSize {
        let width = collectionView.frame.width / 3 
        let height = width * 3.8 / 3
        return CGSize(width: width, height: height)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let profile = self.userProfiles[indexPath.item]
        let viewController = UserProfileViewController.init(profileId: profile.id)
        self.navigationController?.show(viewController, sender: nil)
    }
}

// MARK: Convenience init
extension FollowingViewController {
    convenience init(userId: Int) {
        let nibName = String(describing: FollowingViewController.self)
        self.init(nibName: nibName, bundle: nil)
        self.userId = userId
    }
}
