import SwiftUI
import SharedApp

// --- Définition des schémas de couleurs ---
struct OutgoColorScheme {
    let isDark: Bool
    let primary: Color
    let secondary: Color
    let tertiary: Color

    let background: Color
    let surface50: Color
    let surface100: Color
    let surface200: Color

    let textPrimary: Color
    let textSecondary: Color
    let textOnBrand: Color

    let error: Color
}

// --- Générateur de Thème ---
struct AppTheme {
    static func colorScheme(isDark: Bool) -> OutgoColorScheme {

        let theme = isDark ? DesignColor.shared.Dark : DesignColor.shared.Light

        return OutgoColorScheme(
            isDark: theme.isDark,
            primary: Color(hex: theme.primary),
            secondary: Color(hex: theme.secondary),
            tertiary: Color(hex: theme.tertiary),
            background: Color(hex: theme.background),
            surface50: Color(hex: theme.surface50),
            surface100: Color(hex: theme.surface100),
            surface200: Color(hex: theme.surface200),
            textPrimary: Color(hex: theme.textPrimary),
            textSecondary: Color(hex: theme.textSecondary),
            textOnBrand: Color(hex: theme.textOnBrand),
            error: Color(hex: theme.error)
        )
    }
}

// --- Création des clés d'Environnement ---
private struct ColorSchemeKey: EnvironmentKey {
    static let defaultValue = AppTheme.colorScheme(isDark: false)
}

private struct TypographyKey: EnvironmentKey {
    static let defaultValue = AppTheme.typography(theme: DesignColor.shared.Light)
}

// --- Raccourcis SwiftUI ---
extension EnvironmentValues {
    var outgoColors: OutgoColorScheme {
        get { self[ColorSchemeKey.self] }
        set { self[ColorSchemeKey.self] = newValue }
    }
}

extension EnvironmentValues {
    var outgoTypography: OutgoTypography {
        get { self[TypographyKey.self] }
        set { self[TypographyKey.self] = newValue }
    }
}

// --- Le composant d'injection ---
struct OutgoThemeModifier: ViewModifier {
    @Environment(\.colorScheme) private var systemColorScheme
    var forcedDarkMode: Bool?

    func body(content: Content) -> some View {
        let isDark = forcedDarkMode ?? (systemColorScheme == .dark)
        let theme = isDark ? DesignColor.shared.Dark : DesignColor.shared.Light
        
        let outgoColors = AppTheme.colorScheme(isDark: isDark)
        let outgoTypography = AppTheme.typography(theme: theme)

        content
            .environment(\.outgoColors, outgoColors)
            .environment(\.outgoTypography, outgoTypography)
            .environment(\.spacing, OutgoSpacing())
            .background(outgoColors.background)
            .preferredColorScheme(isDark ? .dark : .light)
    }
}

extension View {
    func outgoTheme(isDark: Bool? = nil) -> some View {
        self.modifier(OutgoThemeModifier(forcedDarkMode: isDark))
    }
}

extension View {
    func outgoFont(_ font: KeyPath<OutgoTypography, Font>, color: Color) -> some View {
        self.font(AppTheme.typography(theme: DesignColor.shared.Light)[keyPath: font])
            .foregroundColor(color)
    }
}
