//
//  ModelController.swift
//  sosokan
//

import UIKit

/*
 A controller object that manages a simple model -- a collection of month names.
 
 The controller serves as the data source for the page view controller; it therefore implements pageViewController:viewControllerBeforeViewController: and pageViewController:viewControllerAfterViewController:.
 It also implements a custom method, viewControllerAtIndex: which is useful in the implementation of the data source methods, and in the initial configuration of the application.
 
 There is no need to actually create view controllers for each page in advance -- indeed doing so incurs unnecessary overhead. Given the data model, these methods create, configure, and return a new view controller on demand.
 */


class ModelController: NSObject, UIPageViewControllerDataSource {

    var simplifiedPosts: [SimplifiedPost]!

    func viewControllerAtIndex(_ index: Int) -> PostDetailViewController? {
        // Return the data view controller for the given index.
        if (self.simplifiedPosts.count == 0) || (index >= self.simplifiedPosts.count) {
            return nil
        }

        // Create a new view controller and pass suitable data.
        let postDetailViewController = PostDetailViewController.init()
        postDetailViewController.simplifiedPost = self.simplifiedPosts[index]
        return postDetailViewController
    }

    func indexOfViewController(_ viewController: PostDetailViewController) -> Int {
        // Return the index of the given data view controller.
        // For simplicity, this implementation uses a static array of model objects and the view controller stores the model object; you can therefore use the model object to identify the index.
        return self.simplifiedPosts.index(where: { $0 == viewController.simplifiedPost }) ?? NSNotFound
    }

    // MARK: - Page View Controller Data Source

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        var index = self.indexOfViewController(viewController as! PostDetailViewController)
        if (index == 0) || (index == NSNotFound) {
            return nil
        }
        
        index -= 1
        return self.viewControllerAtIndex(index)
    }

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        var index = self.indexOfViewController(viewController as! PostDetailViewController)
        if index == NSNotFound {
            return nil
        }
        
        index += 1
        if index == self.simplifiedPosts.count {
            return nil
        }
        return self.viewControllerAtIndex(index)
    }

}

