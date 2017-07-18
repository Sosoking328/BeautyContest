//
//  Constants.swift
//  sosokan
//
//  Created by An Phan on 6/16/16.
//  Copyright © 2016 icanstudioz inc. All rights reserved.
//


import Foundation
import Material
import Localize_Swift

let POST_LIMITED: Int = 35
let CATEGROGY_ALL_ID: String = "sosokanCategoryAll"
let CATEGORY_COUPON_DISCOUNT_ID = "sosokanCategoryCouponDiscount"
let ERROR_CODES = (400 ..< 500)

let KEY_CREATED_AT = "createdAt"
let KEY_OBJECT_ID = "objectId"

var IS_ENGLISH: Bool {
    return Localize.currentLanguage() == "en"
}

let SUBSCRIPTION_PREFIX = "Category_"

let BUNDLE_IDENTIFIER = Bundle.main.bundleIdentifier!

struct References {
    static let users = "users"
    static let images = "images"
    static let categories = "categories"
    static let posts = "advertises"
    static let conversations = "conversations"
    static let messages = "messages"
    static let favorites = "favorites"
    static let postMetas = "advertiseMetas"
    static let videos = "videos"
    static let geoFire = "geoFire"
    static let postRates = "postRates"
    static let userTokens = "userTokens"
    static let userNotificationNumbers = "userNotificationNumbers"
}

struct Topics {
    static let topics = "/topics/"
}

struct Keys {
    static let internetConnectionOfflineCode: Int = -1009
    static let customErrorCode: Int = -1
    static let kCount: String = "count"
    static let kNext: String = "next"
    static let kPrevious: String = "previous"
    static let kResults: String = "results"
    static let kLimit: String = "limit"
    static let kOffset: String = "offset"
    static let kLanguage: String = "language"
    
    static let defaultDateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
    static let nanoSecondDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SZ"
    static let defaultTimeZone = "UTC"
    
    // User
    static let id: String = "Keys.User.id"
    static let csrfToken: String = "Keys.User.csrfToken"
    static let accessToken: String = "Keys.User.accessToken"
    static let username: String = "Keys.User.username"
    static let email: String = "Keys.User.email"
    static let phone: String = "Keys.User.phone"
    static let country: String = "Keys.User.country"
    
    // For recently searched
    static let recentlyKeyword = "Sosokan.Keys.Keyword"
    
    struct SpecialCategory {
        #if DEBUG
        static let all: Int = 6268
        static let trending: String = "Trending"
        #else
        static let all: Int = 6813
        static let trending: String = "Trending"
        #endif
    }
    
    struct Wechat {
        static let appId = "wx79982ec7495cf6b0"
        static let appSecret = "61355a79aeead68b6d306b12e703aa36"
    }
  
    struct RequestCode {
        static let deleteCommentSucceed: Int = 204
        static let createCommentSucceed: Int = 201
        static let updateCommentSucceed: Int = 200
        static let getUserIdSucceed: Int = 200
        static let getUserTokenSucceed: Int = 200
        static let getPostIdSucceed: Int = 200
        static let getCommentsSucceed: Int = 200
        static let loginWithPhoneNumberSucceed: Int = 200
        static let getCategoriesSucceed: Int = 200
        static let getPostsSucceed: Int = 200
        static let getPostSucceed: Int = 200
        static let getWechatTokensSucceed: Int = 200
        static let getWechatInfoSucceed: Int = 200
        static let getFirebaseCustomTokenSucceed: Int = 200
        static let getSplashDealsSucceed: Int = 200
        static let getSplashVideosSucceed: Int = 200
        static let getUserProfileSucceed: Int = 200
    }
    
    struct RingCaptcha {
        static let appKey = "ymo7o5ara1y3y2ymato2"
        static let apiKey = "925f2dcf5370e76a26be65e7e3c0a7b4a1094a27"
    }
    
    struct AppStateFlag {
        static let appLaunchFirstTime = "Keys.AppStateFlag.appLaunchFirstTime"
        static let noLaunchReady = "Keys.AppStateFlag.noLaunchReady"
    }
}

struct SosokanError {
    static let network = "com.sosokan.error"
}

enum Direction {
    case top, left, bottom, right
}

struct AppIcons {
    static let smallStarIcon: UIImage! = UIImage(named: "star-icon")
    static let bigStarIcon: UIImage! = UIImage(named: "icon-sharing")
    static let thumbUp: UIImage! = UIImage(named: "thumbUp")
    static let thumbUpSelected: UIImage! = UIImage(named: "thumbUpSelected")
    static let userIcon: UIImage! = UIImage(named: "no-avatar")
    static let heartIcon: UIImage! = UIImage(named: "heartIcon")
    static let heartSelectedIcon: UIImage! = UIImage(named: "heart-icon-selected")
    static let writeCommentIcon: UIImage! = UIImage(named: "writeCommentIcon")
    static let flagIcon: UIImage! = UIImage(named: "flag")
    static let trashIcon: UIImage! = UIImage(named: "trashIcon")
    static let angleDownIcon: UIImage! = UIImage(named: "angleDown")
    static let checkMarkIcon: UIImage! = UIImage(named: "check-mark")
    static let checkMarkSelectedIcon: UIImage! = UIImage(named: "check-marked")
    static let closeIcon: UIImage! = UIImage(named: "close-icon")
    static let searchIcon: UIImage! = UIImage(named: "searchIcon")
    static let viewByListIcon: UIImage! = UIImage(named: "viewByListIcon")
    static let viewByGridIcon: UIImage! = UIImage(named: "viewByGridIcon")
    static let carretUpIcon: UIImage! = UIImage(named: "carretIconUp")
    static let carretDownIcon: UIImage! = UIImage(named: "carretIcon")
    static let leftBarMenuIcon: UIImage! = UIImage(named: "left-menu")
    static let locationBarIcon: UIImage! = UIImage(named: "locationBarIcon")
    static let imagePlaceHolder: UIImage! = UIImage(named: "no-photo")
    static let plusIcon: UIImage! = UIImage(named: "plusIcon")
    static let backIcon: UIImage! = UIImage(named: "back-icon")
    static let eyeIcon: UIImage! = UIImage(named: "eyeIcon")
    static let favoriteIcon: UIImage! = UIImage(named: "favoriteIcon")
    static let shareIcon: UIImage! = UIImage(named: "shareIcon")
    static let reportIcon: UIImage! = UIImage(named: "reportIcon")
    static let commentIcon: UIImage! = UIImage(named: "commentIcon")
    static let sendCommentIcon: UIImage! = UIImage(named: "sendCommentIcon")
    static let messageBottomBarIcon: UIImage! = UIImage(named: "messageBottomBarIcon")
    static let bannerPlaceHolder = UIImage(named: "bannerPlaceHolderImage")
    static let adDefault = UIImage(named: "ad-default")
}

struct AppFontsizes {
    static let regular: CGFloat = 14
    static let small: CGFloat = 12
    static let verySmall: CGFloat = 10
    static let extraSmall: CGFloat = 8
    static let big: CGFloat = 16
    static let veryBig: CGFloat = 18
}

struct Constants {
    static let buttonCornerRadius: CGFloat = 26
    static let defaultAnimationDuration: TimeInterval = 0.35
    static let longAnimationDuration: TimeInterval = 2
    static let signUpMethodsContainerViewHeight: CGFloat = 300
    static let videoFormat: String = "mp4"
}

struct Messages {
    
    // Camera
    static let cameraTitle         = "Camera"
    static let cameraDescription   = "Choose an option!"
    static let cameraTakePhoto     = "Take a Photo"
    static let cameraRoll          = "Choose from Camera Roll"
    
    // Alert
    static let alertOk     = "Ok"
    static let alertCancel = "Cancel"
    static let errorTitle  = "We're sorry"
    static let appName     = "Sosokan"
    static let occurredError = "Occurred error. Please try again!"
    static let internetConnectionOffline = "The Internet connection appears to be offline"
}

struct AppColor {
    static let blackColor = UIColor.black
    static let darkText = UIColor.darkText
    static let orange = UIColor(hexString: "FF6200")
    static let textPrimary = Color.darkText.primary
    static let textSecondary = Color.darkText.secondary
    static let border = Color.darkText.dividers
    static let white = UIColor.white
    static let brown = UIColor(hexString: "373230")
    static let clear = UIColor.clear
    static let sliderIndicator = UIColor(hexString: "0faccd")
    static let red = Color.red.base
    static let grey = Color.grey.base
    static let popupBackgroundColor = UIColor(white: 0, alpha: 0.7)
}

struct Notifications {
    static let createdNewAd        = "sosokan.createdNewAd"
    static let didLoggedIn         = "sosokan.didLoggedIn"
    static let updatedUserAvatar   = "sosokan.updatedUserAvatar"
    static let deletedAd           = "sosokan.deletedAd"
    static let markExpiredAd       = "sosokan.expiredAd"
    static let didSignedOut        = "sosokan.didSignedOut"
    static let updatedUserInfo     = "sosokan.updatedUserInfo"
    static let receivedNewMessage  = "sosokan.receivedNewMessage"
    static let messageChanged      = "sosokan.messageChanged"
    static let newSubscriptionPost = "sosokan.newSubscriptionPost"
    static let subscriptionChanged = "sosokan.subscriptionChanged"
    static let toggleFilterView    = "sosokan.toggleFilterView"
    static let toggleLeftMenu      = "sosokan.toggleLeftMenu"
    static let categoryIconDidLoad = "sosokan.categoryIconDidLoad"
    static let reloadPosts         = "sosokan.reloadPosts"
    static let refreshPosts        = "sosokan.refreshPosts"
    static let languageChanged        = "sosokan.languageChanged"
    static let statusBarTouched = "sosokan.statusBarTouched"
    static let loggedInWithWechat = "sosokan.loggedInWithWechat"
    static let scrollDown = "sosokan.scrollDown"
    static let scrollUp = "sosokan.scrollUp"
    static let fetchedCurrentUserProfile = "sosokan.fetchedCurrentUserProfile"
    static let fetchedCategories = "sosokan.fetchedCategories"
}

struct IAPIdentifier {
    static let creditTier2 = BUNDLE_IDENTIFIER + ".creditTier2"
    static let creditTier5 = BUNDLE_IDENTIFIER + ".creditTier4.5"
    static let creditTier10 = BUNDLE_IDENTIFIER + ".creditTier10"
    static let creditTier20 = BUNDLE_IDENTIFIER + ".creditTier20"
}

let VIDEO_TYPES = ["mp4"]
