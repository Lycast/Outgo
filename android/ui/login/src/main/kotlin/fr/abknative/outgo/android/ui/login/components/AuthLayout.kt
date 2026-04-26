package fr.abknative.outgo.android.ui.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.login.LoginLabels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthLayout(
    title: String,
    onBackClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    titleColor: Color = AppTheme.colors.primary.toColor(),
    googleLabel: String = LoginLabels.GOOGLE_BUTTON,
    appleLabel: String = LoginLabels.APPLE_BUTTON,
    isLoading: Boolean = false,
    isAppleAuthAvailable: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppText(
                        text = LoginLabels.BACK_TITLE,
                        style = AppTheme.typo.title.copy(fontWeight = FontWeight.Medium),
                        color = AppTheme.colors.textSecondary.toColor(),
                    )
                },
                navigationIcon = {
                    IconButton(enabled = !isLoading, onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.caret_left),
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary.toColor()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimens.large)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- En-tête ---
                AppText(
                    text = title,
                    style = AppTheme.typo.subtitle.copy(fontWeight = FontWeight.SemiBold),
                    color = titleColor,
                    textAlign = TextAlign.Center
                )

                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    AppText(
                        text = subtitle,
                        color = AppTheme.colors.textSecondary.toColor(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.big))

                // --- Boutons Sociaux ---
                SocialLoginButton(
                    provider = SocialProvider.GOOGLE,
                    label = googleLabel,
                    onClick = onGoogleClick,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                if (isAppleAuthAvailable) {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                    SocialLoginButton(
                        provider = SocialProvider.APPLE,
                        label = appleLabel,
                        onClick = onAppleClick,
                        enabled = !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f))
                    AppText(text = LoginLabels.OR_LABEL, color = AppTheme.colors.textSecondary.toColor(), modifier = Modifier.padding(horizontal = AppTheme.dimens.small))
                    HorizontalDivider(modifier = Modifier.weight(1f), color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f))
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                content()
            }
        }
    }
}