import SwiftUI
import SharedApp

struct AddActionTrigger: View {
    // --- Propriétés ---
    let onClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    var body: some View {
        Button(action: onClick) {
            Image("plus_bold")
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 24, height: 24)
                .foregroundColor(colors.textOnBrand)
                .frame(width: 56, height: 56)
                .background(colors.primary)
                .clipShape(RoundedRectangle(cornerRadius: spacing.large))
                .shadow(color: colors.primary.opacity(0.3), radius: 8, x: 0, y: 4)
        }
        .accessibilityLabel(AccessibilityLabels.shared.ADD_EXPENSE)
    }
}

// --- Preview ---
#Preview {
    ZStack {
        Color.gray.opacity(0.1).ignoresSafeArea()
        
        VStack {
            Spacer()
            HStack {
                Spacer()
                AddActionTrigger(onClick: {})
                    .padding()
            }
        }
    }
    .outgoTheme()
}
