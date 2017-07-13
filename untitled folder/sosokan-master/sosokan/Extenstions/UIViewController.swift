//
//  UIViewController.swift
//  sosokan

import Foundation
import Then
import Social
import SVProgressHUD
import TSMessages

extension UIViewController {
    
    func toggleMainMenu(sender: Any) {
        AppDelegate.shared().toggleLeftDrawer(sender as AnyObject, animated: true)
    }
    
    func popViewController(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    func showNotification(_ message: String, type: TSMessageNotificationType) {
        TSMessage.showNotification(withTitle: message, type: type)
    }
    
    func presentLoginAlert() {
        let message = "You need to login to your Sosokan account".localized()
        let signUpAction = UIAlertAction(title: "Sign Up".localized(), style: .default) { (alertAction) in
            AppState.setSignIn()
        }
        let cancelAction = UIAlertAction.init(title: "Cancel".localized(), style: .cancel, handler: nil)
        
        UIApplication.topViewController()?.showAlertWithActions("We're sorry".localized(),
                                                                message: message.localized(),
                                                                actions: [cancelAction, signUpAction])
    }
    
    func prepareModalView(_ viewController: UIViewController,
                                         barButton: UIBarButtonItem,
                                         preferredSize: CGSize?,
                                         delegate: UIPopoverPresentationControllerDelegate?) -> UIViewController {
        viewController.modalPresentationStyle = .popover
        if let popover = viewController.popoverPresentationController {
            popover.barButtonItem = barButton
            popover.permittedArrowDirections = .any
            if let preferredSize = preferredSize {
                viewController.preferredContentSize = preferredSize
            }
            if let delegate = delegate {
                popover.delegate = delegate
            }
            popover.delegate = delegate
        }
        return viewController
    }
    
    func prepareModalView(_ viewController: UIViewController,
                                         sourceView: UIView,
                                         sourceRect: CGRect,
                                         preferredSize: CGSize?,
                                         delegate: UIPopoverPresentationControllerDelegate?) -> UIViewController {
        viewController.modalPresentationStyle = .popover
        if let popover = viewController.popoverPresentationController {
            popover.sourceView = sourceView
            popover.sourceRect = sourceRect
            popover.permittedArrowDirections = .any
            if let preferredSize = preferredSize {
                viewController.preferredContentSize = preferredSize
            }
            if let delegate = delegate {
                popover.delegate = delegate
            }
        }
        return viewController
    }

    func showHUD() {
        SVProgressHUD.show()
    }
    
    func dismissHUD(_ delay: TimeInterval? = nil) {
        if let delay = delay {
            SVProgressHUD.dismiss(withDelay: delay)
        }
        else {
            SVProgressHUD.dismiss()
        }
    }
    
    func dismissKeyboad() {
        view.endEditing(true)
    }
    
    func showOkeyMessage(_ title: String?, message: String?, completionHandler: (() -> Void)? = nil, okHandler: (() -> Void)? = nil) {
        
        let alertViewController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alertViewController.addAction(UIAlertAction(title: "Ok".localized(), style: .cancel, handler: nil))
        
        present(alertViewController, animated: true, completion: completionHandler)
    }
    
    func showAlertWithActions(_ title: String?, message: String?, actions: [UIAlertAction]?) {
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        if let uActios = actions {
            for action in uActios {
                alert.addAction(action)
            }
        }
        else {
            alert.addAction(UIAlertAction(title: "Ok".localized(), style: .default, handler: { (alertAction) -> Void in }))
        }
        
        present(alert, animated: true, completion: nil)
    }
    
    func showAlert(_ title: String?,
                         message: String?,
                         style: UIAlertControllerStyle = .alert,
                         actions: [UIAlertAction] = [UIAlertAction(title: "Ok".localized(), style: .cancel, handler: nil)]) {
        
        let alertController = UIAlertController(title: title, message: message, preferredStyle: style)
        
        actions.forEach({ alertController.addAction($0) })
        
        present(alertController, animated: true, completion: nil)
    }
    
    func showErrorMessage(_ message: String?, completionHandler: (() -> Void)? = nil, okHandler: (() -> Void)? = nil) {
        let alertViewController = UIAlertController(title: "Error!".localized(), message: message, preferredStyle: .alert)
        alertViewController.addAction(UIAlertAction(title: "Ok".localized(), style: .cancel, handler: nil))
        
        present(alertViewController, animated: true, completion: completionHandler)
    }
    
    func showSosokanMessage(_ message: String?) {
        let alertViewController = UIAlertController(title: "Sosokan", message: message, preferredStyle: .alert)
        alertViewController.addAction(UIAlertAction(title: "Ok".localized(),
        style: .cancel) { (alertAction) -> Void in })
        
        present(alertViewController, animated: true, completion: nil)
    }
    /*
    // MARK: - Progress
    func showLoading() {
        self.view.showActivityView()
    }
    
    func hideLoading() {
        DispatchQueue.main.async{
            self.view.hideActivityView()
        }
    }
    
    func showLoadingWithMsg(_ msg: String) {
        view.showActivityView(withLabel: msg)
    }
    */
    // MARK: - networkActivityIndicatorVisible
    func showNetworkIndicator() {
        if !UIApplication.shared.isNetworkActivityIndicatorVisible {
            UIApplication.shared.isNetworkActivityIndicatorVisible = true
        }
    }
    
    func hideNetworkIndicator() {
        if UIApplication.shared.isNetworkActivityIndicatorVisible {
            UIApplication.shared.isNetworkActivityIndicatorVisible = false
        }
    }
    /*
    func prepareWhiteNavigationBar() {
        self.navigationController?.navigationBar.setBackgroundImage(nil, for: .default)
        self.navigationController?.navigationBar.shadowImage = nil
        self.navigationController?.navigationBar.backgroundColor = AppColor.white
        self.navigationController?.navigationBar.isTranslucent = false
        self.navigationController?.navigationBar.tintColor = AppColor.textPrimary
        self.navigationController?.navigationBar.barStyle = .default
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: AppColor.orange]
        
        self.setNeedsStatusBarAppearanceUpdate()
        
        self.automaticallyAdjustsScrollViewInsets = true
    }
     */
    
    func isModal() -> Bool {
        
        if let navigationController = self.navigationController{
            if navigationController.viewControllers.first != self{
                return false
            }
        }
        
        if self.presentingViewController != nil {
            return true
        }
        
        if self.navigationController?.presentingViewController?.presentedViewController == self.navigationController  {
            return true
        }
        
        if self.tabBarController?.presentingViewController is UITabBarController {
            return true
        }
        
        return false
    }
}
