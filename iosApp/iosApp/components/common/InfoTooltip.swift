import SwiftUI
import SharedApp

struct InfoTooltip<Content: View>: View {
    let title: String
    let description: String
    let content: Content
    
    @State private var isPresented = false
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoTypography) private var typo

    init(title: String, description: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.description = description
        self.content = content()
    }

    var body: some View {
            Button(action: { isPresented = true }) {
                content
            }
            .buttonStyle(.plain)
            .popover(isPresented: $isPresented) {
                VStack(alignment: .leading, spacing: spacing.medium) {

                    // --- Titre ---
                    Text(title)
                        .font(typo.body)
                        .fontWeight(.semibold)
                        .foregroundColor(colors.textPrimary)
                    
                    // --- Description ---
                    Text(description)
                        .font(typo.caption)
                        .foregroundColor(colors.textSecondary)
                        .lineLimit(nil)
                        .fixedSize(horizontal: false, vertical: true)
                        .multilineTextAlignment(.leading)
                    
                    // --- Bouton Fermer ---
                    Button(action: { isPresented = false }) {
                        Text(CommonLabels.shared.ACTION_CLOSE.uppercased())
                            .font(typo.label)
                            .fontWeight(.bold)
                            .padding(.vertical, 8)
                            .frame(maxWidth: .infinity)
                            .background(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(colors.primary.opacity(0.3), lineWidth: 1)
                            )
                            .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                    .foregroundColor(colors.primary)
                    .padding(.top, spacing.medium)
                }
                .padding(spacing.large)
                .frame(width: 280)
                .background(colors.surface200)
                .presentationCompactAdaptation(.popover)
            }
        }
}

// --- Preview ---
#Preview {
    ZStack {
        Color.gray.opacity(0.1).ignoresSafeArea()
        
        InfoTooltip(
            title: "Solde prévisionnel",
            description: "Ce montant correspond à ce qu'il vous restera une fois toutes vos charges du mois payées."
        ) {
            Image(systemName: "info.circle.fill")
                .foregroundColor(.blue)
                .font(.title2)
        }
    }
    .outgoTheme()
}
