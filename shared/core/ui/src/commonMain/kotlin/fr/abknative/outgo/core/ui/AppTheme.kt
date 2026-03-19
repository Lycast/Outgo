package fr.abknative.outgo.core.ui

/**
 * MODÈLE SÉMANTIQUE (La Recette)
 * C'est ce contrat que tes composants UI vont lire.
 */
data class OutgoTheme(
    val isDark: Boolean,
    val primary: Long,
    val secondary: Long,
    val tertiary: Long,
    val background: Long,
    val surface50: Long,
    val surface100: Long,
    val surface200: Long,
    val textPrimary: Long,
    val textSecondary: Long,
    val textOnBrand: Long,
    val error: Long,

    val fontBold: String = "Poppins-Bold",
    val fontMedium: String = "Poppins-Medium",
    val fontRegular: String = "Poppins-Regular"
)


/**
 * DESIGN COLOR (Le Mapping)
 * Le "Flip" intelligent entre le mode Clair et Sombre.
 */
object DesignColor {
    val Light = OutgoTheme(
        isDark = false,
        primary = ColorPalette.PRIMARY,
        secondary = ColorPalette.SECONDARY,
        tertiary = ColorPalette.TERTIARY,
        background = ColorPalette.PRIMARY_50,
        surface50 = ColorPalette.WHITE,
        surface100 = ColorPalette.PRIMARY_100,
        surface200 = ColorPalette.PRIMARY_150,
        textPrimary = ColorPalette.TEXT_MAIN_LIGHT,
        textSecondary = ColorPalette.TEXT_MUTED_LIGHT,
        textOnBrand = ColorPalette.WHITE,
        error = ColorPalette.RED_ERROR
    )

    val Dark = OutgoTheme(
        isDark = true,
        primary = ColorPalette.PRIMARY,
        secondary = ColorPalette.SECONDARY,
        tertiary = ColorPalette.TERTIARY,
        background = ColorPalette.BLACK,       // Fond noir pur (OLED)
        surface50 = ColorPalette.PRIMARY_950,
        surface100 = ColorPalette.PRIMARY_900,
        surface200 = ColorPalette.PRIMARY_850,
        textPrimary = ColorPalette.TEXT_MAIN_DARK,
        textSecondary = ColorPalette.TEXT_MUTED_DARK,
        textOnBrand = ColorPalette.WHITE,
        error = ColorPalette.RED_ERROR
    )
}

