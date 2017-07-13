//
//  Sequence.swift
//  sosokan
//

import UIKit

extension Sequence where Iterator.Element: UIView {
    func filterViewsContainedIn(frame: CGRect) -> [UIView] {
        let containedViews = filter({ frame.contains($0.frame) })
        return containedViews
    }
}
