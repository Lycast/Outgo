import SwiftUI

struct CircleIcon: View {
    let iconName: String
    let tintColor: Color
    let containerColor: Color
    
    var body: some View {
        ZStack {
            // Le cercle de fond (Container)
            Circle()
                .fill(containerColor)
                .frame(width: 40, height: 40)
            
            // L'icône
            Image(iconName)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(width: 20, height: 20)
                .foregroundColor(tintColor)
        }
    }
}
