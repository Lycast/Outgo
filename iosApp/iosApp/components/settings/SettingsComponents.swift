import SwiftUI
import SharedApp

// --- La Section (Conteneur avec titre et Card) ---
struct SettingsSection<Content: View>: View {
    let title: String
    let content: Content

    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo

    init(title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: spacing.small) {
            // Titre de section en majuscules
            Text(title.uppercased())
                .font(typo.label)
                .fontWeight(.bold)
                .foregroundColor(colors.primary)
                .padding(.leading, spacing.medium)

            // La "Card" (VStack avec fond et bordure)
            VStack(spacing: 0) {
                content
            }
            .background(colors.surface50)
            .clipShape(RoundedRectangle(cornerRadius: spacing.medium))
            .overlay(
                RoundedRectangle(cornerRadius: spacing.medium)
                    .stroke(colors.textSecondary.opacity(0.1), lineWidth: 1)
            )
        }
    }
}

// --- Contenu de ligne partagé (Icon + Textes) ---
private struct SettingsRowContent: View {
    let icon: String
    let title: String
    let subtitle: String

    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    
    var body: some View {
        HStack(spacing: spacing.medium) {
            
            CircleIcon(
                iconName: icon,
                tintColor: colors.primary,
                containerColor: colors.surface100
            )
            
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(typo.body)
                    .fontWeight(.medium)
                    .foregroundColor(colors.textPrimary)
                
                Text(subtitle)
                    .font(typo.caption)
                    .foregroundColor(colors.textSecondary)
            }
        }
    }
}

// --- Ligne Cliquable (Navigation/Action) ---
struct SettingsRowClickable: View {
    let icon: String
    let title: String
    let subtitle: String
    let onClick: () -> Void

    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors

    var body: some View {
        Button(action: onClick) {
            HStack {
                SettingsRowContent(icon: icon, title: title, subtitle: subtitle)
                
                Spacer()
                
                Image("caret_right")
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundColor(colors.textSecondary)
                    .accessibilityLabel(SettingsLabels.shared.CHEVRON_DESC)
            }
            .padding(spacing.medium)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

// --- Ligne avec Toggle (Mode Sombre, etc.) ---
struct SettingsRowToggle: View {
    let icon: String
    let title: String
    let subtitle: String
    @Binding var isChecked: Bool

    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors

    var body: some View {
        HStack {
            SettingsRowContent(icon: icon, title: title, subtitle: subtitle)
            
            Spacer()
            
            Toggle("", isOn: $isChecked)
                .labelsHidden()
                .tint(colors.primary)
        }
        .padding(spacing.medium)
    }
}
