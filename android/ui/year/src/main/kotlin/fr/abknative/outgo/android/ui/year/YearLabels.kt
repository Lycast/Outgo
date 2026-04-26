package fr.abknative.outgo.android.ui.year

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object YearLabels {
    val SHOWCASE_TITLE @Composable get() = stringResource(Res.string.year_showcase_title)
    val SHOWCASE_SUBTITLE @Composable get() = stringResource(Res.string.year_showcase_subtitle)

    val ESSENTIAL_TITLE @Composable get() = stringResource(Res.string.year_essential_title)
    val ESSENTIAL_BADGE @Composable get() = stringResource(Res.string.year_essential_badge)
    val ESSENTIAL_DESC @Composable get() = stringResource(Res.string.year_essential_desc)
    val ESSENTIAL_FEATURES @Composable get() = listOf(
        stringResource(Res.string.year_essential_feature_1),
        stringResource(Res.string.year_essential_feature_2),
        stringResource(Res.string.year_essential_feature_3)
    )

    val PREMIUM_TITLE @Composable get() = stringResource(Res.string.year_premium_title)
    val PREMIUM_BADGE @Composable get() = stringResource(Res.string.year_premium_badge)
    val PREMIUM_DESC @Composable get() = stringResource(Res.string.year_premium_desc)
    val PREMIUM_FEATURES @Composable get() = listOf(
        stringResource(Res.string.year_premium_feature_1),
        stringResource(Res.string.year_premium_feature_2),
        stringResource(Res.string.year_premium_feature_3)
    )
    val PREMIUM_ENDING @Composable get() = stringResource(Res.string.year_premium_feature_4)
    val NOTIFY_ME_ACTION @Composable get() = stringResource(Res.string.year_notify_me_action)

    val PLACEHOLDER_TITLE @Composable get() = stringResource(Res.string.year_placeholder_title)

    const val URL_FORM = "https://docs.google.com/forms/d/e/1FAIpQLSfxRBuUn4epj2eL4qNXZTpLfqNlP9qmPGZCNrtlh2GsJB3zAQ/viewform?usp=dialog"
}