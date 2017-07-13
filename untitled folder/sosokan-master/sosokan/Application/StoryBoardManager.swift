//
//  StoryBoardManager.swift
//  sosokan

import UIKit

class StoryBoardManager: NSObject {
    
    // Type methods
    class func storyBoard(_ name: String) -> UIStoryboard{
        return UIStoryboard(name: name, bundle: nil)
    }
    
    class func viewController(_ storyBoardName: String, viewControllerName: String) -> UIViewController {
        let storyboard = storyBoard(storyBoardName)
        return storyboard.instantiateViewController(withIdentifier: viewControllerName)
    }
    
    class func mainStoryBoard() -> UIStoryboard {
        return storyBoard("Main")
    }

    class func homeStoryBoard() -> UIStoryboard {
        return storyBoard("Home")
    }
    
    class func postStoryBoard() -> UIStoryboard {
        return storyBoard("Post")
    }
    
    class func favoriteStoryBoard() -> UIStoryboard {
        return storyBoard("Favorite")
    }
    
    class func drawersStoryBoard() -> UIStoryboard {
        return storyBoard("Drawers")
    }
    
    class func userStoryBoard() -> UIStoryboard {
        return storyBoard("User")
    }
}
