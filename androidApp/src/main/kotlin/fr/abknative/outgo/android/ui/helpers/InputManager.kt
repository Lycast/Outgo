package fr.abknative.outgo.android.ui.helpers

import androidx.compose.runtime.*

/**
 * Manages the input logic for a name field.
 * Caps the length and can be extended for further formatting (like auto-capitalization).
 */

class NameInputManager(
    initialValue: String,
    private val maxLength: Int = 30,
    private val onValidChange: (String) -> Unit
) {
    var text by mutableStateOf(initialValue)
        private set

    fun onTextChange(newValue: String) {
        if (newValue.length <= maxLength) {
            text = newValue
            onValidChange(newValue)
        }
    }
}

@Composable
fun rememberNameInputManager(
    initialValue: String,
    onValidChange: (String) -> Unit
): NameInputManager {
    return remember(initialValue) {
        NameInputManager(initialValue, onValidChange = onValidChange)
    }
}


class AmountInputManager(
    initialValue: String,
    private val maxDigitsBeforeComma: Int = 7,
    private val onValidChange: (String) -> Unit
) {
    var text by mutableStateOf(initialValue)
        private set

    fun onTextChange(newValue: String) {
        val sanitized = newValue.replace(',', '.')

        val isValidFormat = sanitized.isEmpty() || sanitized.matches(Regex("""^\d{0,$maxDigitsBeforeComma}(\.?\d{0,2})$"""))

        if (isValidFormat) {
            text = sanitized
            onValidChange(sanitized)
        }
    }
}

@Composable
fun rememberAmountInputManager(
    initialValue: String,
    onValidChange: (String) -> Unit
): AmountInputManager {
    return remember(initialValue) {
        AmountInputManager(initialValue, onValidChange = onValidChange)
    }
}