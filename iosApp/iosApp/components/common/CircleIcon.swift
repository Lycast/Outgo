import SwiftUI

struct CircleIcon: View {
    let iconName: String
    
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        ZStack {
            Circle()
                .fill(colors.onSurface.opacity(0.05))
                .frame(width: 40, height: 40)
            
            Image(iconName)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(width: 22, height: 22)
                .foregroundColor(colors.primary)
        }
    }
}
