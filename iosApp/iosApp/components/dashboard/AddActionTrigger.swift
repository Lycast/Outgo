import SwiftUI
import SharedApp

struct AddActionTrigger: View {
    // --- Propriétés ---
    let onClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        Button(action: onClick) {
            Image("plus_bold")
                .renderingMode(.template)
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
            
                .frame(width: 56, height: 56)
                .background(colors.primary)
                .clipShape(Circle())
                .shadow(color: Color.black.opacity(0.2), radius: 4, x: 0, y: 4)
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
}
