import SwiftUI

struct OutgoIcon: View {
    let iconName: String
    var size: CGFloat = 24
    var opacity: Double = 1
    
    var body: some View {
        Image(iconName)
            .resizable()
            .renderingMode(.template)
            .aspectRatio(contentMode: .fit)
            .frame(width: size, height: size)
            .foregroundColor(.primary.opacity(opacity))
            .frame(width: 36, height: 36, alignment: .center)
            .contentShape(Rectangle())
    }
}
