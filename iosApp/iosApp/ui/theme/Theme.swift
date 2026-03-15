import SwiftUI
import SharedApp

// --- Définition des schémas de couleurs ---
struct OutgoColorScheme {
    let primary: Color
    let secondary: Color
    let tertiary: Color
    let background: Color
    let surface: Color
    let onSurface: Color
    let onSurfaceVariant: Color
    let error: Color
}

struct DashboardColors {
    let budget: Color
    let charges: Color
    let remainingPay: Color
    let remainingLive: Color
    let warning: Color
}

// --- Générateur de Thème ---
struct AppTheme {
    static func colorScheme(isDark: Bool) -> OutgoColorScheme {
        if isDark {
            return OutgoColorScheme(
                primary: Color(hex: DesignColors.Dark.shared.Primary),
                secondary: Color(hex: DesignColors.Dark.shared.Secondary),
                tertiary: Color(hex: DesignColors.Dark.shared.Tertiary),
                background: Color(hex: DesignColors.Dark.shared.Background),
                surface: Color(hex: DesignColors.Dark.shared.Surface),
                onSurface: Color(hex: DesignColors.Dark.shared.OnSurface),
                onSurfaceVariant: Color(hex: DesignColors.Dark.shared.onSurfaceVariant),
                error: Color(hex: DesignColors.Dark.shared.Error)
            )
        } else {
            return OutgoColorScheme(
                primary: Color(hex: DesignColors.Light.shared.Primary),
                secondary: Color(hex: DesignColors.Light.shared.Secondary),
                tertiary: Color(hex: DesignColors.Light.shared.Tertiary),
                background: Color(hex: DesignColors.Light.shared.Background),
                surface: Color(hex: DesignColors.Light.shared.Surface),
                onSurface: Color(hex: DesignColors.Light.shared.OnSurface),
                onSurfaceVariant: Color(hex: DesignColors.Light.shared.onSurfaceVariant),
                error: Color(hex: DesignColors.Light.shared.Error)
            )
        }
    }
    
    static func dashboardColors(isDark: Bool) -> DashboardColors {
        if isDark {
            return DashboardColors(
                budget: Color(hex: DesignColors.Dark.shared.BarBudget),
                charges: Color(hex: DesignColors.Dark.shared.BarCharges),
                remainingPay: Color(hex: DesignColors.Dark.shared.BarRemainingPay),
                remainingLive: Color(hex: DesignColors.Dark.shared.BarRemainingLive),
                warning: Color(hex: DesignColors.Dark.shared.BarWarning)
            )
        } else {
            return DashboardColors(
                budget: Color(hex: DesignColors.Light.shared.BarBudget),
                charges: Color(hex: DesignColors.Light.shared.BarCharges),
                remainingPay: Color(hex: DesignColors.Light.shared.BarRemainingPay),
                remainingLive: Color(hex: DesignColors.Light.shared.BarRemainingLive),
                warning: Color(hex: DesignColors.Light.shared.BarWarning)
            )
        }
    }
}

// --- Création des clés d'Environnement ---
private struct ColorSchemeKey: EnvironmentKey {
    static let defaultValue = AppTheme.colorScheme(isDark: false)
}

private struct DashboardColorsKey: EnvironmentKey {
    static let defaultValue = AppTheme.dashboardColors(isDark: false)
}

// --- Raccourcis SwiftUI ---
extension EnvironmentValues {
    var outgoColors: OutgoColorScheme {
        get { self[ColorSchemeKey.self] }
        set { self[ColorSchemeKey.self] = newValue }
    }
    
    var dashboardColors: DashboardColors {
        get { self[DashboardColorsKey.self] }
        set { self[DashboardColorsKey.self] = newValue }
    }
}

// --- Le composant d'injection ---
struct OutgoThemeModifier: ViewModifier {
    @Environment(\.colorScheme) private var systemColorScheme
    
    var forcedDarkMode: Bool?

    func body(content: Content) -> some View {
        let isDark = forcedDarkMode ?? (systemColorScheme == .dark)
        
        content
            .environment(\.outgoColors, AppTheme.colorScheme(isDark: isDark))
            .environment(\.dashboardColors, AppTheme.dashboardColors(isDark: isDark))
            .background(AppTheme.colorScheme(isDark: isDark).background)
    }
}

extension View {
    func outgoTheme(isDark: Bool? = nil) -> some View {
        self.modifier(OutgoThemeModifier(forcedDarkMode: isDark))
    }
}
