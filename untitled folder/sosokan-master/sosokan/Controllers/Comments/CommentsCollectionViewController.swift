//
//  CommentsCollectionViewController.swift
//  sosokan
//

import UIKit
import Material

class CommentsCollectionViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var commentContainerView: UIView!
    @IBOutlet weak var commentContainerViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var commentTextField: UITextField!
    @IBOutlet weak var sendCommentButton: Button!
    
    var commentsRequest = ""
    var nextCommentsRequest = ""
    var totalComments = 0
    var comments = [CommentObject]()
    
    fileprivate struct Constants {
        static let animationDuration = TimeInterval.init(0.5)
        
        struct ReuseIdentifiers {
            static let commentContentCell = String.init(describing: CommentContentTableViewCell.self)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.prepareCommentContainerView()
        self.prepareSendCommentButton()
        self.prepareCommentTableView()
        self.prepareTotalCommentsBarButton()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        title = "Comments".localized()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: IBAction
    
    @IBAction func commentTextFieldValueChanged(_ sender: AnyObject) {
        let text = (sender as? UITextField)?.text ?? ""
        self.sendCommentButton.isEnabled = !text.isEmpty
    }
    
    // MARK: Private method
    
    fileprivate func prepareCommentContainerView() {
        self.view.bringSubview(toFront: self.commentContainerView)
        self.commentContainerView.addTopBorderWithColor(AppColor.border, width: 1)
        
        /*
        let notificationCenter = NSNotificationCenter.defaultCenter()
        notificationCenter.rx_notification(UIKeyboardWillShowNotification)
            .subscribeNext { [weak self] (notification) in
                guard let `self` = self else { return }
                if self.commentTextField.isFirstResponder() {
                    guard let userInfo = notification.userInfo else { return }
                    guard let keyboardFrame = userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue else { return }
                    let keyboardRectangle = keyboardFrame.CGRectValue()
                    let duration = userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSTimeInterval ?? Constants.animationDuration
                    UIView.animateWithDuration(duration, animations: {
                        let constant = keyboardRectangle.height
                        self.commentContainerViewBottomConstraint.constant = constant
                        self.view.setNeedsLayout()
                        self.view.layoutIfNeeded()
                    })
                }
            }
            .addDisposableTo(self.rx_disposeBag)
        
        notificationCenter.rx_notification(UIKeyboardWillHideNotification)
            .subscribeNext { [weak self] (notification) in
                guard let `self` = self else { return }
                if self.commentTextField.isFirstResponder() {
                    guard let userInfo = notification.userInfo else { return }
                    let duration = userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSTimeInterval ?? Constants.animationDuration
                    UIView.animateWithDuration(duration, animations: {
                        self.commentContainerViewBottomConstraint.constant = 0
                        self.view.layoutIfNeeded()
                    })
                }
            }
            .addDisposableTo(self.rx_disposeBag)
         */
    }
    
    fileprivate func prepareSendCommentButton() {
        self.sendCommentButton.setImage(AppIcons.sendCommentIcon.tint(with: AppColor.grey), for: .disabled)
        self.sendCommentButton.setImage(AppIcons.sendCommentIcon.tint(with: AppColor.orange), for: .normal)
    }
    
    fileprivate func prepareCommentTableView() {
        let nib = UINib.init(nibName: Constants.ReuseIdentifiers.commentContentCell, bundle: nil)
        self.tableView.register(nib, forCellReuseIdentifier: Constants.ReuseIdentifiers.commentContentCell)
        self.tableView.separatorStyle = .none
        self.tableView.keyboardDismissMode = .onDrag
        self.tableView.estimatedRowHeight = 200
        self.tableView.rowHeight = UITableViewAutomaticDimension
        self.tableView.dataSource = self
    }
    
    fileprivate func presentCommentOptionsModalView(_ comment: CommentObject,
                                                        sourceView: UIView,
                                                        sourceRect: CGRect) {
        let nibName = String(describing: CommentOptionsTableViewController.self)
        let viewController = CommentOptionsTableViewController.init(nibName: nibName, bundle: nil)
        let reportAction: () -> Void = { [weak self] (_) in
            guard let `self` = self else { return }
            // TODO: Handle later
        }
        var options: [CommentOptionsTableCell] = [
            CommentOptionsTableCell(AppIcons.flagIcon, "Report", reportAction)
        ]
        let isMyComment = comment.id == DataManager.shared.currentUser!.id
        if isMyComment {
            let deleteAction: () -> Void = { [weak self] (_) in
                guard let `self` = self else { return }
                /*
                 guard let currentUser = DataManager.sharedInstance.currentUser else { return }
                 self.showNetworkIndicator()
                 self.fetchUserId(withFirebaseId: currentUser.id)
                 .debug()
                 .errorOnNil()
                 .flatMap({ self.fetchUserToken(withUserId: $0) })
                 .errorOnNil()
                 .flatMap({ self.deleteComment(comment: comment, userToken: $0) })
                 .errorOnNil()
                 .subscribe(onNext: { [weak self] (removedComment) in
                 guard let `self` = self else { return }
                 self.handleCommentsChange(deletedComment: removedComment)
                 self.hideNetworkIndicator()
                 }, onError: { [weak self] (error) in
                 guard let `self` = self else { return }
                 self.showErrorMessage(Messages.occurredError)
                 self.hideNetworkIndicator()
                 })
                 .addDisposableTo(self.rx_disposeBag)
                 self.selectedComment = nil
                 */
            }
            options.append(CommentOptionsTableCell(AppIcons.trashIcon, "Delete", deleteAction))
            
            let editAction: () -> Void = { [weak self] (_) in
                guard let `self` = self else { return }
                /*
                 self.selectedComment = comment
                 self.commentTextField.becomeFirstResponder()
                 */
            }
            options.append(CommentOptionsTableCell(AppIcons.writeCommentIcon, "Edit", editAction))
        }
        viewController.options = options
        viewController.modalPresentationStyle = .popover
        if let popover = viewController.popoverPresentationController {
            popover.sourceView = sourceView
            popover.sourceRect = sourceRect
            popover.permittedArrowDirections = .any
            viewController.preferredContentSize = CGSize(width: 200, height: 132)
            popover.delegate = self
        }
        
        self.present(viewController, animated: true, completion: nil)
    }
    
    fileprivate func prepareTotalCommentsBarButton() {
        let button = UIBarButtonItem.init(title: "Total: " + self.totalComments.string(), style: .plain, target: nil, action: nil)
        self.navigationItem.rightBarButtonItem = button
    }
}

// MARK: - convenience init
extension CommentsCollectionViewController {
    convenience init(commentsRequest: String?, nextCommentsRequest: String?, totalComments: Int?) {
        let nibName = String(describing: CommentsCollectionViewController.self)
        self.init(nibName: nibName, bundle: nil)
        self.commentsRequest = commentsRequest ?? ""
        self.nextCommentsRequest = nextCommentsRequest ?? ""
        self.totalComments = totalComments ?? 0
    }
}

// MARK: - UIPopoverPresentationControllerDelegate
extension CommentsCollectionViewController: UIPopoverPresentationControllerDelegate {
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle {
        return .none
    }
}

// MARK: UITableViewDataSource
extension CommentsCollectionViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.comments.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Constants.ReuseIdentifiers.commentContentCell, for: indexPath) as! CommentContentTableViewCell
        let comment = self.comments[indexPath.row]
        cell.selectionStyle = .none
        cell.render(userAvatarURL: comment.userAvatarURL,
                    userDisplayName: comment.userDisplayName,
                    createdAt: comment.createdAt,
                    commentContent: comment.content,
                    numberOfFavorites: comment.numberOfFavorites,
                    actionsTitle: "Actions",
                    indentationLevel: 0,
                    isLiked: false)
        cell.setHiddenBorderBottomView(comment == self.comments.last)
        /*
        let tap = UITapGestureRecognizer.init(target: cell, action: Selector.init())
        tap.rx_event.asDriver()
            .driveNext({ [weak self] (_) in
                guard let `self` = self else { return }
                // TODO: tap to view user profile
                })
            .addDisposableTo(cell.rx_reusableDisposeBag)
        cell.userAvatarImageView.userInteractionEnabled = true
        cell.userAvatarImageView.addGestureRecognizer(tap)
        cell.actionsButton.rx_tap.asDriver()
            .driveNext({ [weak self] (_) in
                guard let `self` = self else { return }
                self.presentCommentOptionsModalView(comment: comment, sourceView: cell.contentView, sourceRect: cell.actionsButton.frame)
                })
            .addDisposableTo(cell.rx_reusableDisposeBag)
        cell.favoriteButton.rx_tap.asDriver()
            .driveNext({ [weak self] (_) in
                guard let `self` = self else { return }
                // TODO: tap to like/unlike comment
                })
            .addDisposableTo(cell.rx_reusableDisposeBag)
         */
        return cell
    }
}
