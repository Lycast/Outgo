import SwiftUI
import SharedApp

struct CustomToolbarGroup: ToolbarContent {
    let title: String
    let action: () -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo

    var body: some ToolbarContent {
        ToolbarItem(placement: .keyboard) {
            Button(action: action) {
                HStack(spacing: 0) {
                    Text(title)
                        .font(typo.body)
                        .fontWeight(.bold)
                        .foregroundColor(colors.primary)
                        .frame(maxWidth: .infinity, alignment: .init(horizontal: .trailing, vertical: .center))
                        .padding(.trailing, 24)
                }
            }
            .buttonStyle(.plain)
            .frame(width: UIScreen.main.bounds.width)
        }
    }
}
