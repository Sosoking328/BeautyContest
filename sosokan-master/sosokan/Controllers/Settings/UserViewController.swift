////
////  UserViewController.swift
////  sosokan
////
////  Created by An Phan on 7/25/16.
////  Copyright © 2016 icanstudioz inc. All rights reserved.
////
//
//import UIKit
//import Localize_Swift
//import TSMessages
//
//class UserViewController: UIViewController {
//
//    @IBOutlet weak var tableView: UITableView!
//    
//    var users = [PFUser]()
//    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        // Do any additional setup after loading the view.
//        prepareNavigationbar()
//        setText()
//        
//        AppHelper.showLoadingGIF()
//        let userQuery = PFQuery(className:"_User")
//        userQuery.findObjectsInBackgroundWithBlock { (objects: [PFObject]?, error: NSError?) -> Void in
//            AppHelper.hideLoadingGIF()
//            if error == nil {
//                if let users = objects as? [PFUser] {
//                    self.users = users
//                    self.tableView.reloadData()
//                }
//            } else {
//                print("\(error?.userInfo)")
//            }
//        }
//    }
//
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//    
//    @IBAction func backButtonTapped(sender: UIBarButtonItem) {
//        navigationController?.popViewControllerAnimated(true)
//    }
//
//    /*
//    // MARK: - Navigation
//
//    // In a storyboard-based application, you will often want to do a little preparation before navigation
//    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
//        // Get the new view controller using segue.destinationViewController.
//        // Pass the selected object to the new view controller.
//    }
//    */
//    // MARK: - Methods
//    
//    func setText() {
//        title = "Users".localized()
//    }
//    
//    // MARK: - Private methods
//    
//    private func prepareNavigationbar() {
//        // Appearance
//        navigationController?.navigationBar.barStyle = .Default   // Change status bar to light mode.
//        navigationController?.navigationBar.setBackgroundImage(nil, forBarMetrics: .Default)
//        navigationController?.navigationBar.shadowImage = nil
//        navigationController?.navigationBar.barTintColor = AppColor.white
//        navigationController?.navigationBar.tintColor = AppColor.textPrimary
//        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.orange]
//        navigationController?.navigationBar.translucent = false
//        setNeedsStatusBarAppearanceUpdate()
//    }
//}
//
//// MARK: - UITableViewDataSource
//
//extension UserViewController: UITableViewDataSource {
//    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
//        return 1
//    }
//    
//    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        return users.count
//    }
//    
//    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCellWithIdentifier("userCellID",
//                                                               forIndexPath: indexPath) as! UserTableViewCell
//        let user = users[indexPath.row]
//        cell.nameLabel.text = user.username
//        
//        if let avatar = user["profile_picture"] as? PFFile {
//            avatar.getDataInBackgroundWithBlock { (imageData: NSData?, error:NSError?) -> Void in
//                if error == nil {
//                    if let imageData = imageData {
//                        cell.avtImageView.image = UIImage(data: imageData)
//                    }
//                }
//            }
//        }
//        else {
//            cell.avtImageView.image = UIImage(named: "no-avatar")
//        }
//        
//        cell.activeButtonDidTapped = { button in
//            if button.selected {
//                user.setObject(true, forKey: "actived")
//                user.saveInBackground()
//            }
//            else {
//                user.setObject(false, forKey: "actived")
//                user.saveInBackground()
//            }
//        }
//        
//        return cell
//    }
//}
//
//// MARK: - UITableViewDelegate
//
//extension UserViewController: UITableViewDelegate {
//    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
//        tableView.deselectRowAtIndexPath(indexPath, animated: true)
//    }
//}
