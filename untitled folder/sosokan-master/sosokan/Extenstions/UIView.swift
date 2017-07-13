//
//  UIView.swift
//  Sosokan

extension Direction {
    var borderIdentifier: String {
        switch self {
        case .top:
            return "topBorder"
        case .bottom:
            return "bottomBorder"
        case .left:
            return "leftBorder"
        case .right:
            return "rightBorder"
        }
    }
}

extension UIView {
    class func fromNib<T : UIView>(_ nibNameOrNil: String? = nil) -> T {
        let v: T? = fromNib(nibNameOrNil)
        return v!
    }
    
    class func fromNib<T : UIView>(_ nibNameOrNil: String? = nil) -> T? {
        var view: T?
        let name: String
        if let nibName = nibNameOrNil {
            name = nibName
        } else {
            // Most nibs are demangled by practice, if not, just declare string explicitly
            name = "\(T.self)".components(separatedBy: ".").last!
        }
        let nibViews = Bundle.main.loadNibNamed(name, owner: nil, options: nil)
        for v in nibViews! {
            if let tog = v as? T {
                view = tog
            }
        }
        return view
    }
    
    func bordered(withColor color: UIColor, width: CGFloat, radius: CGFloat? = nil) {
        self.layer.borderColor = color.cgColor
        self.layer.borderWidth = width
        if let radius = radius {
            self.layer.cornerRadius = radius
            self.layer.masksToBounds = true
        }
    }
    
    func removeBorders(withDirections directions: [Direction]) {
        for direction in directions {
            self.removeBorder(withDirection: direction)
        }
    }
    
    func removeBorder(withDirection direction: Direction) {
        for layer in self.layer.sublayers?.filter({ $0.name == direction.borderIdentifier }) ?? [] {
            layer.removeFromSuperlayer()
        }
    }
    
    func addBorders(withDirections directions: [Direction], color: UIColor, width: CGFloat) {
        for direction in directions {
            switch direction {
            case .bottom:
                self.addBottomBorderWithColor(color, width: width)
            case .left:
                self.addLeftBorderWithColor(color, width: width)
            case .right:
                self.addRightBorderWithColor(color, width: width)
            case .top:
                self.addTopBorderWithColor(color, width: width)
            }
        }
    }
    
    func addTopBorderWithColor(_ color: UIColor, width: CGFloat) {
        layer.sublayers?.filter() { $0.name == Direction.top.borderIdentifier }.forEach() {
            $0.removeFromSuperlayer()
        }
        
        let border = CALayer()
        border.name = Direction.top.borderIdentifier
        border.backgroundColor = color.cgColor
        border.frame = CGRect(x: 0, y: 0, width: self.frame.size.width, height: width)
        self.layer.addSublayer(border)
    }
    
    func addRightBorderWithColor(_ color: UIColor, width: CGFloat) {
        layer.sublayers?.filter() { $0.name == Direction.right.borderIdentifier }.forEach() {
            $0.removeFromSuperlayer()
        }
        let border = CALayer()
        border.name = Direction.right.borderIdentifier
        border.backgroundColor = color.cgColor
        border.frame = CGRect(x: self.frame.size.width - width, y: 0, width: width, height: self.frame.size.height)
        self.layer.addSublayer(border)
    }
    
    func addBottomBorderWithColor(_ color: UIColor, width: CGFloat) {
        layer.sublayers?.filter() { $0.name == Direction.bottom.borderIdentifier }.forEach() {
            $0.removeFromSuperlayer()
        }
        let border = CALayer()
        border.name = Direction.bottom.borderIdentifier
        border.backgroundColor = color.cgColor
        border.frame = CGRect(x: 0, y: self.frame.size.height - width, width: self.frame.size.width, height: width)
        self.layer.addSublayer(border)
    }
    
    func addLeftBorderWithColor(_ color: UIColor, width: CGFloat) {
        layer.sublayers?.filter() { $0.name == Direction.left.borderIdentifier }.forEach() {
            $0.removeFromSuperlayer()
        }
        let border = CALayer()
        border.name = Direction.left.borderIdentifier
        border.backgroundColor = color.cgColor
        border.frame = CGRect(x: 0, y: 0, width: width, height: self.frame.size.height)
        self.layer.addSublayer(border)
    }
    
    func addBorderWithColor(_ color: UIColor, width: CGFloat) {
        layer.borderColor = color.cgColor
        layer.borderWidth = width
    }
    
    // Rounded corner
    func roundify() {
        clipsToBounds = true
        layer.cornerRadius = frame.height / 2
    }
}
