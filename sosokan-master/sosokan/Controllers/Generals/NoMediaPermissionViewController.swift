//
//  NoMediaPermissionViewController.swift
//  Sosokan

import UIKit

class NoMediaPermissionViewController: UIViewController {

    @IBOutlet weak var switchBgView: UIView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var firstItemLabel: UILabel!
    @IBOutlet weak var secondItemLabel: UILabel!
    @IBOutlet weak var thirdItemLabel: UILabel!
    @IBOutlet weak var fourthItemLabel: UILabel!
    @IBOutlet weak var appNameLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        switchBgView?.addBorderWithColor(UIColor.rgbColor(red: 183, green: 183, blue: 183), width: 1)
        switchBgView?.layer.cornerRadius = 8.0
        
        navigationController?.navigationBar.isTranslucent = false
        navigationController?.navigationBar.barStyle = .black
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = AppColor.orange
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.white]
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Cancel".localized(),
                                                            style: .plain,
                                                            target: self,
                                                            action: #selector(self.dismissVC))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        setText()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func dismissVC() {
        self.dismiss(animated: true, completion: nil)
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    // MARK: - Methods
    
    func setText() {
        titleLabel.text = "It looks like you have turned off permissions needed to attach media.".localized()
        firstItemLabel.text = "Open Settings".localized()
        secondItemLabel.text = "Tap Privacy".localized()
        thirdItemLabel.text = "Tap Camera".localized()
        fourthItemLabel.text = "Make sure Sosokan is turned ON".localized()
    }
}
