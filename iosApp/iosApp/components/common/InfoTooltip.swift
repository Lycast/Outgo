import SwiftUI
import SharedApp

struct InfoTooltip<Content: View>: View {
    let title: String
    let description: String
    let content: Content
    
    @State private var isPresented = false
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing

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
                    Text(title)
                        .font(.headline)
                        .foregroundColor(colors.onSurface)
                    
                    Text(description)
                        .font(.subheadline)
                        .foregroundColor(colors.onSurfaceVariant)
                        .lineLimit(nil)
                        .fixedSize(horizontal: false, vertical: true)
                        .multilineTextAlignment(.leading)
                    
                    Button(action: { isPresented = false }) {
                        Text(CommonLabels.shared.ACTION_CLOSE)
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                    .tint(colors.primary)
                    .padding(.top, spacing.small)
                }
                .padding()
                .frame(width: 300)
                .presentationCompactAdaptation(.popover)
            }
        }
}

// --- Preview ---
#Preview {
    InfoTooltip(
        title: "Titre du Tooltip",
        description: "Ceci est une description détaillée qui explique quelque chose d'important à l'utilisateur. avec beaucoup d'explication inutiles. Pour que cela soit plus clair, on ajoute encore plus de détails. Et voilà, c'est prêt !"
    ) {
        Image(systemName: "info.circle")
            .font(.title)
            .foregroundColor(.blue)
    }
}
