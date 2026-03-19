import SwiftUI
import SharedApp

struct OutgoTypography {
    let title: Font
    let subtitle: Font
    let body: Font
    let caption: Font
    let label: Font
}

extension AppTheme {
    static func typography(theme: OutgoTheme) -> OutgoTypography {
        return OutgoTypography(
            title: .custom(theme.fontBold, size: CGFloat(DesignTypography.shared.EXTRALARGE)),
            subtitle: .custom(theme.fontMedium, size: CGFloat(DesignTypography.shared.LARGE)),
            body: .custom(theme.fontRegular, size: CGFloat(DesignTypography.shared.MEDIUM)),
            caption: .custom(theme.fontRegular, size: CGFloat(DesignTypography.shared.SMALL)),
            label: .custom(theme.fontMedium, size: CGFloat(DesignTypography.shared.EXTRA_SMALL))
        )
    }
}
