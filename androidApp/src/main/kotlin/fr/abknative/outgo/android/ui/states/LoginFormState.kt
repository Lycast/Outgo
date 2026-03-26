package fr.abknative.outgo.android.ui.states

import androidx.compose.runtime.*

class LoginFormState(
    initialEmail: String = "",
    initialPassword: String = ""
) {
    var email by mutableStateOf(initialEmail)
    var password by mutableStateOf(initialPassword)

    val isValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}

@Composable
fun rememberLoginFormState(
    initialEmail: String = "",
    initialPassword: String = ""
): LoginFormState {
    return remember(initialEmail, initialPassword) {
        LoginFormState(initialEmail, initialPassword)
    }
}