//
//  AdPageViewController.swift
//  sosokan
//
//  Created by An Phan on 10/30/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//

import UIKit
import Firebase

class AdPageViewController: UIPageViewController {
    
    var orderedViewControllers: [AdDetailViewController?] = []
    var currentPost: Post?
    var posts: [Post] = []
    var isNeedLoadMorePosts: Bool = true
    var isLoadingMorePosts: Bool = false
    var isViewingFavoritedPosts: Bool = true
    weak var selectedCategory: Category?
    var userId: String?
    var ref: FIRDatabaseReference!
    weak var currentViewController: AdDetailViewController?
    weak var adPageViewDelegate: AdPageViewControllerDelegate?
    weak var swiperNavigationItem: UINavigationItem?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dataSource = self
        self.delegate = self
        self.ref = FIRDatabase.database().reference()
        
        if let currentPost = self.currentPost, index = self.posts.indexOf(currentPost) {
            let initialViewController = self.getAdDetailViewController(self.posts[index])
            scrollToViewController(initialViewController)
        }
        
        adPageViewDelegate?.adPageViewController(self, didUpdatePageCount: orderedViewControllers.count)
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /**
     Scrolls to the next view controller.
     */
    func scrollToNextViewController() {
        if let visibleViewController = viewControllers?.first as? AdDetailViewController,
            let nextViewController = pageViewController(self, viewControllerAfterViewController: visibleViewController) {
            scrollToViewController(nextViewController)
        }
    }
    
    /**
     Scrolls to the view controller at the given index. Automatically calculates
     the direction.
     
     - parameter newIndex: the new index to scroll to
     */
    func scrollToViewController(index newIndex: Int) {
        guard let firstViewController = viewControllers?.first as? AdDetailViewController else { return }
        if let currentIndex = orderedViewControllers.flatMap({ $0 }).indexOf(firstViewController) {
            let direction: UIPageViewControllerNavigationDirection = newIndex >= currentIndex ? .Forward : .Reverse
            if let nextViewController = orderedViewControllers[newIndex] {
                self.scrollToViewController(nextViewController, direction: direction)
            }
        }
    }
    
    /**
     Scrolls to the given 'viewController' page.
     
     - parameter viewController: the view controller to show.
     */
    private func scrollToViewController(viewController: UIViewController,
                                        direction: UIPageViewControllerNavigationDirection = .Forward) {
        setViewControllers([viewController],
                           direction: direction,
                           animated: true,
                           completion: { (finished) -> Void in
                            // Setting the view controller programmatically does not fire
                            // any delegate methods, so we have to manually notify the
                            // 'tutorialDelegate' of the new index.
                            self.notifyTutorialDelegateOfNewIndex()
        })
    }
    
    /**
     Notifies '_tutorialDelegate' that the current page index was updated.
     */
    private func notifyTutorialDelegateOfNewIndex() {
        
        guard let firstViewController = viewControllers?.first as? AdDetailViewController else {
            return
        }
        if let index = self.posts.map({ $0.id }).indexOf(firstViewController.post.id) {
            debugPrint("index: \(index)")
            let currentPost = self.posts[index]
            self.currentPost = currentPost
            adPageViewDelegate?.adPageViewController(self,
                                                     didUpdatePageIndex: index)
            
            if self.isNeedLoadMorePosts && !self.isLoadingMorePosts {
                if let selectedCategory = self.selectedCategory {
                    if self.posts.count - index < 10 {
                        self.isLoadingMorePosts = true
                        if let newestPost = self.posts.maxElement({ $0.0.createdAt > $0.1.createdAt }) {
                            let oldestPostdescendingTime = -newestPost.createdAt
                            let languageNode = IS_ENGLISH ? FCategoryKey.postsEnglish : FCategoryKey.postsChinese
                            let postsRef = self.ref.child(References.categories).child(selectedCategory.id).child(languageNode).queryOrderedByValue().queryStartingAtValue(oldestPostdescendingTime).queryLimitedToFirst(UInt(POST_LIMITED))
                            postsRef.observeSingleEventOfType(.Value, withBlock: { [weak self] snapshot in
                                guard let `self` = self else {
                                    return
                                }
                                var postIds = (snapshot.value as? JSONType)?.keys.map({ $0 }) ?? []
                                var postsWillAppended = [FPost].init([])
                                for postId in postIds {
                                    let postRef = self.ref.child(References.posts).child(postId)
                                    postRef.observeSingleEventOfType(.Value, withBlock: { snapshot in
                                        if let json = snapshot.value as? JSONType where json.isNotEmpty {
                                            let post = FPost.init(json: json)
                                            if !self.posts.contains({ $0.id == post.id }) {
                                                postsWillAppended.append(post)
                                            }
                                            if postsWillAppended.endIndex == postIds.endIndex {
                                                self.posts.appendContentsOf(postsWillAppended)
                                                self.isLoadingMorePosts = false
                                            }
                                            self.posts = self.posts.sort({ $0.0.createdAt > $0.1.createdAt }) // sort newest first
                                        }
                                        else {
                                            if let index = postIds.indexOf(postId) {
                                                let id = postIds.removeAtIndex(index)
                                                debugPrint("invalid: \(id)")
                                            }
                                        }
                                    })
                                }
                                })

                        }
                    }
                }
                else if let userId = self.userId {
                    var referenceKey: String {
                        if FIRAuth.auth()?.currentUser?.uid == userId {
                            if self.isViewingFavoritedPosts {
                                return UserKey.favorites
                            }
                            else {
                                return UserKey.posts
                            }
                        }
                        else {
                            return UserKey.posts
                        }
                    }
                    let postsRef = self.ref.child(References.users).child(userId).child(referenceKey).queryOrderedByValue().queryLimitedToFirst(UInt(POST_LIMITED))
                    postsRef.observeSingleEventOfType(.Value, withBlock: { [weak self] snapshot in
                        guard let `self` = self else {
                            return
                        }
                        var postIds = (snapshot.value as? JSONType)?.keys.map({ $0 }) ?? []
                        var postsWillAppended = [FPost].init([])
                        for postId in postIds {
                            let postRef = self.ref.child(References.posts).child(postId)
                            postRef.observeSingleEventOfType(.Value, withBlock: { snapshot in
                                if let json = snapshot.value as? JSONType where json.isNotEmpty {
                                    let post = FPost.init(json: json)
                                    debugPrint("will appended: \(post.id)")
                                    postsWillAppended.append(post)
                                    if postsWillAppended.endIndex == postIds.endIndex {
                                        self.posts.appendContentsOf(postsWillAppended)
                                        self.isLoadingMorePosts = false
                                    }
                                }
                                else {
                                    if let index = postIds.indexOf(postId) {
                                        let id = postIds.removeAtIndex(index)
                                        debugPrint("invalid: \(id)")
                                    }
                                }
                                
                            })
                        }
                        })
                }
            }
        }
    }
    
    private func getCurrentAdDetailViewController() -> AdDetailViewController {
        let detailViewController = StoryBoardManager.homeStoryBoard().instantiateViewControllerWithIdentifier("adDetailVCID") as! AdDetailViewController
        detailViewController.post = self.currentPost
        detailViewController.swiperNavigationItem = self.swiperNavigationItem
        return detailViewController
    }
}

// MARK: UIPageViewControllerDataSource

extension AdPageViewController: UIPageViewControllerDataSource {
    
    private func getAdDetailViewController(post: FPost) -> AdDetailViewController {
        let detailViewController = StoryBoardManager.homeStoryBoard().instantiateViewControllerWithIdentifier("adDetailVCID") as! AdDetailViewController
        detailViewController.post = post
        detailViewController.swiperNavigationItem = self.swiperNavigationItem
        return detailViewController
    }
    
    func pageViewController(pageViewController: UIPageViewController,
                            viewControllerBeforeViewController viewController: UIViewController) -> UIViewController? {
        
        guard let currentPost = self.currentPost else {
            return nil
        }
        
        guard let index = self.posts.indexOf(currentPost) else {
            return nil
        }
        
        if index == self.posts.startIndex {
            return nil
        }
        else {
            let previousPost = self.posts[index - 1]
            let previousViewController = getAdDetailViewController(previousPost)
            return previousViewController
        }
    }
    
    func pageViewController(pageViewController: UIPageViewController,
                            viewControllerAfterViewController viewController: UIViewController) -> UIViewController? {
        
        guard let currentPost = self.currentPost else {
            return nil
        }
        
        guard let index = self.posts.indexOf(currentPost) else {
            return nil
        }
        
        if (index + 1) < self.posts.count {
            let nextPost = self.posts[index + 1]
            let nextViewController = getAdDetailViewController(nextPost)
            return nextViewController
        }
        else {
            return nil
        }
    }
    
}

extension AdPageViewController: UIPageViewControllerDelegate {
    
    func pageViewController(pageViewController: UIPageViewController,
                            didFinishAnimating finished: Bool,
                                               previousViewControllers: [UIViewController],
                                               transitionCompleted completed: Bool) {
        notifyTutorialDelegateOfNewIndex()
    }
    
}

protocol AdPageViewControllerDelegate: class {
    
    /**
     Called when the number of pages is updated.
     
     - parameter tutorialPageViewController: the TutorialPageViewController instance
     - parameter count: the total nuadPageViewpages.
     */
    func adPageViewController(adPageViewController: AdPageViewController,
                                    didUpdatePageCount count: Int)
    
    /**
     Called when the current index is updated.
     
     - parameter tutorialPageViewController: the TutorialPageViewController instance
     - parameter index: the index of the currently visible page.
     */
    func adPageViewController(adPageViewController: AdPageViewController,
                                    didUpdatePageIndex index: Int)
}