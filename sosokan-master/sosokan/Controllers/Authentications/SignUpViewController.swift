//
//  SignUpViewController.swift
//  sosokan

import UIKit
import CoreTelephony
import FBSDKLoginKit
import Material
import SwiftyJSON
import STPopup
import Localize_Swift
import CoreLocation

class SignUpViewController: VideoSplashViewController {
    
    // MARK: - IBOutlets
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var countryCodeTextField: UITextField!
    @IBOutlet weak var phoneTextField: UITextField!
    @IBOutlet weak var verifyButton: UIButton!
    @IBOutlet weak var loginViaFBButton: UIButton!
    @IBOutlet weak var loginViaWeChatButton: UIButton!
    @IBOutlet weak var signUpMethodsContainerView: UIView!
    @IBOutlet weak var signUpMethodsContainerViewTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var signUpButton: UIButton!
    @IBOutlet weak var skipButton: UIButton!
    @IBOutlet weak var signUpLabel: UILabel!
    @IBOutlet weak var orLabel: UILabel!
    @IBOutlet weak var phoneTitleLabel: UILabel!
    @IBOutlet weak var splashImageView: UIImageView!
    @IBOutlet weak var termLabel: UILabel!
    @IBOutlet weak var andSymboyLabel: UILabel!
    @IBOutlet weak var termButton: UIButton!
    @IBOutlet weak var pivacyButton: UIButton!
    @IBOutlet weak var andSymboyCenterConstraint: NSLayoutConstraint!
    @IBOutlet weak var backButton: UIButton!
    
    fileprivate let splashImage = UIImage(named: "splash")
    
    /*
     .flatMap({ _ in self.presentFacebookLoginPage(withPermissions: ["public_profile", "email", "user_friends"]) })
     .filter({ !$0.isCancelled })
     .flatMap({ _ in self.fetchFacebookUserProfile(withParameters: ["fields": "id, name, first_name, last_name, picture.type(large), email"]) })
     */
    
    // Location permission
    var locationHandler: LocationHandler = {
        var locationHandler = LocationHandler()
        return locationHandler
    }()
    
    // MARK: - View life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        requestLocationPermission()

        prepareSplashImageView()
        prepareBackgroundVideo()
        prepareViewsBackgroundColor()
        prepareSignUpMethodsContainerView()
        prepareWechatLoginButton()
        prepareSignUpButton()
        prepareCountryCodeTextField()
        prepareTermAndServiceSection()
        
        // FIXME: Deep link not work
        
        verifyButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 15)
        verifyButton.layer.cornerRadius = 16.0
        
        skipButton.addBorderWithColor(UIColor.white, width: 2.0)
        skipButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 18)
        
        loginViaFBButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 18)
        loginViaFBButton.layer.cornerRadius = Constants.buttonCornerRadius
        
//        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(fetchWechatToken(_:)), name: Notifications.loggedInWithWechat, object: nil)
        
        // Fetch splash 
        fetchSplashVideos()
        
        // Fetch categories list
        CategoryService.shared.fetchCategoriesWithCompletionHandler { (json, error) in
            // Do nothing
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        UIApplication.shared.isStatusBarHidden = true
        navigationController?.setNavigationBarHidden(true, animated: false)
        setText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        if !isAppAlreadyLaunchedOnce() {
            setupLanguage()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        UIApplication.shared.isStatusBarHidden = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override var prefersStatusBarHidden : Bool {
        return true
    }
    
    override func movieReadyToPlay() {
        super.movieReadyToPlay()
        
        let delayTime = DispatchTime.now() + Double(Int64(1 * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC)
        DispatchQueue.main.asyncAfter(deadline: delayTime) {
            self.setHiddenSplashImage(true, animated: true)
        }
    }
    
    // MARK: - IBActions
    
    @IBAction func signUpButtonTapped(_ sender: AnyObject) {
        toggleSignUpMethodsContainerView(true)
    }
    
    @IBAction func skipButtonTapped(_ sender: AnyObject) {
        presentHomePage()
    }
    
    @IBAction func backButtonTapped(_ sender: AnyObject) {
        phoneTextField.resignFirstResponder()
        countryCodeTextField.resignFirstResponder()
        toggleSignUpMethodsContainerView(true)
    }
    
    @IBAction func textFieldDidChangedValue(_ textField: UITextField) {
        
        // Only enable VERIFY button if user types a right phone.
        if let phone = textField.text, phone.isPhoneNumber {
            verifyButton.activated(true)
        }
        else {
            verifyButton.activated(false)
        }
    }
    
    @IBAction func verifyButtonTapped(_ sender: AnyObject) {
        let fullPhone = countryCodeTextField.getString + getPhoneNumber()
        
        if fullPhone.isPhoneNumber {
            countryCodeTextField.resignFirstResponder()
            phoneTextField.resignFirstResponder()
            showNetworkIndicator()
            showHUD()
//            setHiddenAllViews(true)
            let ringcaptchaAPI = RingcaptchaAPI(appKey: Keys.RingCaptcha.appKey, andAPIKey: Keys.RingCaptcha.apiKey)
            
            ringcaptchaAPI?.sendCaptchaCode(toNumber: fullPhone, with: SMS, delegate: self)
        }
        else {
            showNotification("The phone number you entered is not valid".localized(), type: .error)
        }
    }
    
    @IBAction func loginFBButtonTapped(_ sender: AnyObject) {
    }
    
    @IBAction func loginWXButtonTapped(_ sender: AnyObject) {
        if WXApi.isWXAppInstalled() {
            self.presentWechatLoginPage()
            self.presentHomePage()
        }
    }
    
    @IBAction func privacyButtonTapped(_ sender: AnyObject) {
        let name = SupportedLanguage.current() == SupportedLanguage.english ? "Privacy Policy" : "Privacy_Policy_Chinese"
        if let path = Bundle.main.path(forResource: name, ofType: "html", inDirectory: nil) {
            let viewController = WebViewViewController()
            viewController.url = URL(fileURLWithPath: path)
            viewController.title = "Privacy Policy".localized()
            let navigationController = UINavigationController(rootViewController: viewController)
            
            self.show(navigationController, sender: nil)
        }
    }
    
    @IBAction func termButtonTapped(_ sender: AnyObject) {
        let name = SupportedLanguage.current() == SupportedLanguage.english ? "Terms and Conditions" : "Terms_Condition_Chinese"
        if let path = Bundle.main.path(forResource: name, ofType: "html", inDirectory: nil) {
            
            let viewController = WebViewViewController()
            viewController.url = URL(fileURLWithPath: path)
            viewController.title = "Terms and Conditions".localized()
            let navigationController = UINavigationController(rootViewController: viewController)
            
            self.show(navigationController, sender: nil)
        }
    }
    
    // MARK: - Helpers
    /*
    func fetchWechatToken(notification: NSNotification) {
        if let code = notification.object as? String where !code.isEmpty {
            let request = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=\(Keys.Wechat.appId)&secret=\(Keys.Wechat.appSecret)&code=\(code)&grant_type=authorization_code"
            return request.requestJSON()
                .map { (response, data) in
                    if response.statusCode == Keys.RequestCode.getWechatTokensSucceed {
                        let json = JSON.init(data)
                        if let token = json["access_token"].string where !token.isEmpty,
                            let userId = json["openid"].string where !userId.isEmpty {
                            return (token, userId)
                        }
                        return nil
                    }
                    return nil
            }
        }
    }
    */
    
    func setText() {
        signUpButton.setTitle("LOGIN/SIGN UP".localized(), for: .normal)
        skipButton.setTitle("SKIP".localized(), for: .normal)
        verifyButton.setTitle("VERIFY".localized(), for: .normal)
        loginViaFBButton.setTitle("LOG IN USING FACEBOOK".localized(), for: .normal)
        loginViaWeChatButton.setTitle("LOG IN USING WECHAT".localized(), for: .normal)
        phoneTextField.placeholder = "Type your phone number".localized()
        signUpLabel.text = "SIGN UP".localized()
        orLabel.text = "OR".localized()
        phoneTitleLabel.text = "VERIFY BY PHONE".localized()
        prepareTermAndServiceSection()
    }
    
    // MARK: - Private methods
    
    fileprivate func requestLocationPermission() {
        // Enable location service.
        locationHandler.delegate = self
        locationHandler.startLocationUpdate()
    }
    
    fileprivate func isAppAlreadyLaunchedOnce() -> Bool {
        let defaults = UserDefaults.standard
        if let _ = defaults.string(forKey: "isAppAlreadyLaunchedOnce") {
            return true
        }
        defaults.setValue("isAppAlreadyLaunchedOnce", forKey: "isAppAlreadyLaunchedOnce")
        
        return false
    }
    
    fileprivate func setupLanguage() {
        let alertVC = UIAlertController(title: "SoSoKan",
                                        message: "Please select your language",
                                        preferredStyle: .alert)
        let engAction = UIAlertAction(title: "English", style: .default, handler: { (alertAction) in
            Localize.setCurrentLanguage("en")
            self.setText()
        })
        let chineseAction = UIAlertAction(title: "中文", style: .default, handler: { (alertAction) in
            Localize.setCurrentLanguage("zh-Hans")
            self.setText()
        })
        
        alertVC.addAction(engAction)
        alertVC.addAction(chineseAction)
        
        self.present(alertVC, animated: true, completion: nil)
    }
    /*
    private func fetchFacebookUserProfile(withParameters parameters: [String: String]) -> Observable<FacebookUserInfo> {
        let observable = Observable.create { (observer: AnyObserver<FacebookUserInfo?>) -> Disposable in
            FBSDKGraphRequest(graphPath: "me", parameters: parameters).startWithCompletionHandler({ (_, data, error) in
                if let error = error {
                    observer.onError(error)
                }
                else if let data = data {
                    let json  = JSON.init(data)
                    if let id = json["id"].string, let token = FBSDKAccessToken.currentAccessToken().tokenString {
                        let email = json["email"].string
                        let firstName = json["first_name"].string
                        let lastName = json["last_name"].string
                        let imageURL = "https://graph.facebook.com/" + id + "/picture?type=large"
                        let info = FacebookUserInfo.init(id: id, email: email, firstName: firstName, lastName: lastName, imageURL: imageURL, token: token)
                        observer.onNext(info)
                    }
                    else {
                        observer.onNext(nil)
                    }
                }
                else {
                    observer.onNext(nil)
                }
            })
            return NopDisposable.instance
        }
        return observable.errorOnNil()
    }
    */
    fileprivate func setHiddenSplashImage(_ hidden: Bool, animated: Bool) {
        if animated {
            UIView.animate(withDuration: Constants.longAnimationDuration, animations: {
                self.splashImageView.alpha = hidden ? 0 : 1
            })
        }
        else {
            self.splashImageView.alpha = hidden ? 0 : 1
        }
    }
    
    fileprivate func setHiddenViewsExceptSplashImage(_ hidden: Bool, animated: Bool) {
        if animated {
            UIView.animate(withDuration: Constants.defaultAnimationDuration, animations: { 
                self.containerView.alpha = hidden ? 0 : 1
            })
        }
        else {
            self.containerView.alpha = hidden ? 0 : 1
        }
    }
    
    fileprivate func fetchSplashVideos() {
        showNetworkIndicator()
        SplashVideo.getVideos(SupportedLanguage.current()) { (json, error) in
            self.hideNetworkIndicator()
            if let error = error {
                debugPrint(error)
            }
            else {
                if let videos = json?.array?.flatMap({ SplashVideo(json: $0) }), videos.count > 0 {
                    DataManager.shared.splashVideo = videos.first
                    if let URL = videos.first!.absoluteURL {
                        if URL.pathExtension.lowercased() == Constants.videoFormat.lowercased() {
                            self.contentURL = URL
                        }
                        else {
                            self.splashImageView.kf.setImage(with: URL, placeholder: self.splashImage, completionHandler: { [unowned self] (image, _, _, _) in
                                guard let image = image else { return }
                                UIView.transition(with: self.splashImageView,
                                                  duration: Constants.defaultAnimationDuration,
                                                  options: UIViewAnimationOptions.transitionCrossDissolve,
                                                  animations: { self.splashImageView.image = image },
                                                  completion: nil)
                            })
                        }
                    }
                }
            }
        }
    }
    
    fileprivate func prepareSplashImageView() {
        self.splashImageView.image = self.splashImage
        self.splashImageView.contentMode = .scaleAspectFill
    }
    
    fileprivate func prepareBackgroundVideo() {
        self.videoFrame = UIScreen.main.bounds
        self.fillMode = .resizeAspectFill
        self.alwaysRepeat = true
        self.sound = false
        self.alpha = 1
        self.backgroundColor = AppColor.clear
    }
    
    fileprivate func prepareViewsBackgroundColor() {
        self.view.backgroundColor = AppColor.clear
        self.scrollView.backgroundColor = AppColor.clear
        self.containerView.backgroundColor = AppColor.clear
    }
    
    fileprivate func isSignUpMethodsContainerViewShowing() -> Bool {
        return self.signUpMethodsContainerViewTopConstraint.constant == Constants.signUpMethodsContainerViewHeight
    }
    
    fileprivate func toggleSignUpMethodsContainerView(_ animated: Bool) {
        let constraint = self.signUpMethodsContainerViewTopConstraint
        if animated {
            UIView.animate(withDuration: Constants.defaultAnimationDuration, animations: {
                constraint?.constant = constraint?.constant == 0 ? Constants.signUpMethodsContainerViewHeight : 0
                self.view.layoutIfNeeded()
            }) 
        }
        else {
            constraint?.constant = constraint?.constant == 0 ? Constants.signUpMethodsContainerViewHeight : 0
        }
    }
    
    fileprivate func prepareSignUpMethodsContainerView() {
        self.signUpMethodsContainerViewTopConstraint.constant = 0
    }
    
    fileprivate func splashPopupEnabled() -> Bool {
        let userDefaults = UserDefaults.standard
        if let value = userDefaults.value(forKey: Keys.AppStateFlag.noLaunchReady)  { // Not first time install app
            let noLaunchReady = value as? Bool ?? false
            userDefaults.set(false, forKey: Keys.AppStateFlag.noLaunchReady)
            return noLaunchReady
        }
        userDefaults.set(false, forKey: Keys.AppStateFlag.noLaunchReady)
        return false
    }
    
    fileprivate func prepareCountryCodeTextField() {
        // Phone icon view on the left
        let leftView = UIImageView(frame: CGRect(x: 0.0, y: 0.0, width: 35, height: self.countryCodeTextField.frame.height))
        leftView.contentMode = .center
        leftView.image = UIImage(named: "phone-icon")
        self.countryCodeTextField.leftView = leftView
        self.countryCodeTextField.leftViewMode = .always
        self.countryCodeTextField.font = UIFont.avenirLTStdLight(size: 15)
        
        // Country picker view
        let picker: CountryCodePickerView = UIView.fromNib()
        picker.countries = Ringcaptcha.getSupportedCountries()
        picker.textField = self.countryCodeTextField
        picker.delegate = self
        self.countryCodeTextField.inputView = picker
        
        // Load list of countries
        let isoCode = self.getIsoCountryCode()
        for country in picker.countries {
            if let code = (country as AnyObject).isoCode, code == isoCode?.uppercased() {
                self.countryCodeTextField.text = "+\((country as AnyObject).dialingCode)"
                picker.selectedCountry = country as? RingcaptchaCountry
                picker.reloadPicker()
                break
            }
        }
    }
    
    fileprivate func prepareSignUpButton() {
        self.signUpButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 18)
        self.signUpButton.layer.cornerRadius = Constants.buttonCornerRadius
    }
    
    fileprivate func presentWechatLoginPage() {
        let request = SendAuthReq.init()
        request.scope = "snsapi_userinfo"
        request.state = "sosokan"
        WXApi.send(request)
    }
    
    fileprivate func prepareWechatLoginButton() {
        self.loginViaWeChatButton.titleLabel?.font = UIFont.avenirLTStdRoman(size: 18)
        self.loginViaWeChatButton.layer.cornerRadius = Constants.buttonCornerRadius
    }

    fileprivate func presentHomePage() {
        setHiddenViewsExceptSplashImage(true, animated: true)
        // TODO: Double check why call it on the previous method
//        setHiddenAllViews(true)
        
        AppState.setHome()
    }
    
    fileprivate func setHiddenAllViews(_ hidden: Bool) {
        if hidden {
            self.view.subviews.forEach({ $0.isHidden = $0 != self.splashImageView })
        }
        else {
            self.view.subviews.forEach({ $0.isHidden = false })
        }
    }
    
    fileprivate func prepareTermAndServiceSection() {
        
        [self.termLabel, self.andSymboyLabel].forEach { (label) in
            label?.textColor = Color.grey.lighten2
            label?.font = UIFont.avenirLTStdLight(size: 13)
        }
        
        [self.termButton, self.pivacyButton].forEach { (button) in
            button?.setTitleColor(AppColor.white, for: UIControlState())
            button?.titleLabel?.font = UIFont.avenirLTStdRoman(size: 15)
        }
        
        self.andSymboyLabel.text = "&"
        
        self.termLabel.text = "By browsing, signing up or logging in. I hereby agree to Sosokan's".localized()
        
        self.termButton.setTitle("Terms and Conditions".localized(), for: .normal)
        
        self.pivacyButton.setTitle("Privacy Policy".localized(), for: .normal)
        self.termButton.setNeedsLayout()
        self.termButton.layoutIfNeeded()
        self.pivacyButton.setNeedsLayout()
        self.pivacyButton.layoutIfNeeded()
        
        let termButtonWidth = self.termButton.frame.width
        let pivacyButtonWidth = self.pivacyButton.frame.width
        self.andSymboyCenterConstraint.constant = (termButtonWidth - pivacyButtonWidth) / 2
        
    }
    
    fileprivate func getIsoCountryCode() -> String! {
        let networkInfo = CTTelephonyNetworkInfo()
        let carrier = networkInfo.subscriberCellularProvider
        if let countryCode = carrier?.isoCountryCode {
            return countryCode
        }
        
        return ""
    }
    
    fileprivate func getPhoneNumber() -> String {
        if let phone = phoneTextField.text, phone.hasPrefix("0") {
            return String(phone.characters.dropFirst())
        }
        
        return phoneTextField.text ?? ""
    }
}

// MARK: - RingcaptchaAPIDelegate

extension SignUpViewController: RingcaptchaAPIDelegate {
    func didFinishCodeRequest(_ rsp: RingcaptchaResponse!) {
        print(rsp)
        self.dismissHUD()
        
        let codeVerificationVC = CodeVerificationViewController(phone: getPhoneNumber(),
                                                                countryCode: countryCodeTextField.getString)
        navigationController?.pushViewController(codeVerificationVC, animated: true)
    }
    
    func didFinishCodeRequestWithError(_ err: Error!) {
        let message = err.code == Keys.internetConnectionOfflineCode ? Messages.internetConnectionOffline : Messages.occurredError
        self.hideNetworkIndicator()
        self.dismissHUD()
        self.setHiddenAllViews(false)
        self.showErrorMessage(message)
    }
}

// MARK: - CountryCodePickerViewDelegate
extension SignUpViewController: CountryCodePickerViewDelegate {
    func pickerView(_ pickerView: CountryCodePickerView, selectedValue: RingcaptchaCountry?) {
        if let country = selectedValue {
            countryCodeTextField.text = "+\(country.dialingCode!)"
        }
    }
}

// MARK: - Convenience init
extension SignUpViewController {
    convenience init() {
        let nibName = String(describing: SignUpViewController.self)
        self.init(nibName: nibName, bundle: nil)
    }
}

// MARK: - LocationHandlerDelegate

extension SignUpViewController: LocationHandlerDelegate {
    func locationDetected(_ location: CLLocation) {
        DataManager.shared.currentLocation = location
    }
    
    func locationDiscoveryFailed(_ error: NSError) {
        debugPrint("Location failed with error: %@", error)
    }
}
