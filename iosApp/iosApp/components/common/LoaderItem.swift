import SwiftUI

struct LoaderItem: View {
    var body: some View {
        VStack {
            ProgressView()
                .progressViewStyle(.linear)
                .frame(maxWidth: .infinity)
        }
        .padding(32)
        .frame(maxWidth: .infinity)
    }
}

// --- Preview ---
#Preview {
    LoaderItem()
}
