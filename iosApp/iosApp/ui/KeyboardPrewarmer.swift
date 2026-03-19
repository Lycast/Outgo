import SwiftUI
import UIKit

struct KeyboardPrewarmer: UIViewRepresentable {
    func makeUIView(context: Context) -> UITextField {
        let textField = UITextField()
        textField.isHidden = true
        
        DispatchQueue.main.async {
            textField.becomeFirstResponder()
            textField.resignFirstResponder()
        }
        
        return textField
    }
    
    func updateUIView(_ uiView: UITextField, context: Context) {
        // Rien à mettre à jour ici
    }
}
