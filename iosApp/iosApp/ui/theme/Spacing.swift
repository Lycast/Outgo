import SwiftUI
import SharedApp


struct OutgoSpacing {
    let none: CGFloat = CGFloat(DesignSpacing.shared.NONE)
    let extraSmall: CGFloat = CGFloat(DesignSpacing.shared.EXTRA_SMALL)
    let small: CGFloat = CGFloat(DesignSpacing.shared.SMALL)
    let medium: CGFloat = CGFloat(DesignSpacing.shared.MEDIUM)
    let large: CGFloat = CGFloat(DesignSpacing.shared.LARGE)
    let extraLarge: CGFloat = CGFloat(DesignSpacing.shared.EXTRA_LARGE)
    let big: CGFloat = CGFloat(DesignSpacing.shared.BIG)
}

private struct SpacingEnvironmentKey: EnvironmentKey {
    static let defaultValue = OutgoSpacing()
}

extension EnvironmentValues {
    var spacing: OutgoSpacing {
        get { self[SpacingEnvironmentKey.self] }
        set { self[SpacingEnvironmentKey.self] = newValue }
    }
}
