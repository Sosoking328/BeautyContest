//
//  CodeVerificationViewController.swift
//  sosokan
//
//  Created by An Phan on 5/22/17.
//  Copyright © 2017 Sosokan Technology, Inc. All rights reserved.
//

import UIKit
import TSMessages

class CodeVerificationViewController: UIViewController {

    // MARK: - IBOutlets
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    @IBOutlet weak var codeTextField: UITextField!
    @IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var phoneTitleLabel: UILabel!
    @IBOutlet weak var codeTitleTextField: UITextField!
    
    var phone = ""
    var countryCode = ""
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let leftBarButton = UIBarButtonItem(image: AppIcons.backIcon,
                                            style: .plain,
                                            target: self,
                                            action: #selector(handleLeftBarButton))
        navigationItem.leftBarButtonItem = leftBarButton
        
        // Custom fonts
        titleLabel.font = UIFont.avenirLTStdBook(size: 21)
        phoneTitleLabel.font = UIFont.avenirLTStdBook(size: 17)
        codeTitleTextField.font = UIFont.avenirLTStdBook(size: 17)
        phoneLabel.font = UIFont.avenirLTStdBook(size: 17)
        codeTextField.font = UIFont.avenirLTStdBook(size: 17)
        submitButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 18)
        
        submitButton.layer.cornerRadius = Constants.buttonCornerRadius
        
        prepareContent()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        navigationController?.setNavigationBarHidden(false, animated: false)
        setText()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - IBActions
    
    @IBAction func submitButtonTapped(_ sender: Any) {
        guard let code = codeTextField.text, !code.isEmpty else {
            // TODO: Display error message
            return
        }
        
        showHUD()
        UserService.shared.logIn(phone, countryCode: countryCode, verifyCode: code) { (json, error) in
            self.dismissHUD()
            if let error = error {
                debugPrint(error)
                TSMessage.showNotification(withTitle: "Incorrect Code", type: .error)
            }
            else {
                AppState.setHome()
            }
        }
    }
    
    // MARK: - Helper methods
    
    func handleLeftBarButton() {
        _ = navigationController?.popViewController(animated: true)
    }
    
    func setText() {
        titleLabel.text = "Verification code has been sent to your phone".localized()
        codeTitleTextField.text = "Code".localized()
        codeTextField.placeholder = "Enter code".localized()
        phoneTitleLabel.text = "Phone".localized()
        submitButton.setTitle("SUBMIT".localized(), for: .normal)
    }
    
    // MARK: - Private methods
    fileprivate func prepareContent() {
        phoneLabel.text = countryCode + " " + phone
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

// MARK: - convenience init
extension CodeVerificationViewController {
    convenience init(phone: String, countryCode: String) {
        self.init(nibName: String(describing: CodeVerificationViewController.self), bundle: nil)
        self.phone = phone
        self.countryCode = countryCode
    }
}

