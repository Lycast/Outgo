import SwiftUI

extension Color {
    init(hex: Int64) {
        let a = Double((hex >> 24) & 0xff) / 255.0
        let r = Double((hex >> 16) & 0xff) / 255.0
        let g = Double((hex >> 8) & 0xff) / 255.0
        let b = Double(hex & 0xff) / 255.0
        
        self.init(red: r, green: g, blue: b, opacity: a)
    }
}
