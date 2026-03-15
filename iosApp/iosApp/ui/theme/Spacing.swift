import SwiftUI
import SharedApp


struct OutgoSpacing {
    let none: CGFloat = CGFloat(DesignSpacing.shared.None)
    let extraSmall: CGFloat = CGFloat(DesignSpacing.shared.ExtraSmall)
    let small: CGFloat = CGFloat(DesignSpacing.shared.Small)
    let medium: CGFloat = CGFloat(DesignSpacing.shared.Medium)
    let large: CGFloat = CGFloat(DesignSpacing.shared.Large)
    let extraLarge: CGFloat = CGFloat(DesignSpacing.shared.ExtraLarge)
    let big: CGFloat = CGFloat(DesignSpacing.shared.Big)
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
