package fr.abknative.outgo.core.api.theme

/**
* Palette de couleurs "brutes" de l'application.
* On utilise des Long (format 0xFFRRGGBB) pour que ce soit compatible partout.
*/
object Palette {
    val BrandPrimary = 0xFF285ABF   // Bleu profond
    val BrandSecondary = 0xFFC7A63A // Or/Accent
    val BrandTertiary = 0xFF7B77C9  // Violet/Lavande

    // HERO SECTION BAR COLOR
    val BarBudget = 0xFFB0BEC5
    val BarCharges = 0xFF42A5C5
    val BarRemainingPay = 0xFFFFCA28
    val BarRemainingLive = 0xFF66BB6A
    val BarWarning = 0xFFEF5350


    // Light Mode
    val BgLight = 0xFFF7F7F8       // Fond principal très légèrement teinté (respire mieux que le blanc pur)
    val SurfaceLight = 0xFFFFFFFF  // Cartes en blanc pur (ressortent sur le BgLight)

    // Dark Mode
    val BgDark = 0xFF000000        // Noir profond (standard Material)
    val SurfaceDark = 0xFF101010   // Gris anthracite très subtil. Crée un relief sans agresser l'œil.

    // --- GRIS DE CONTENU (Textes secondaires et Bordures) ---

    // Light Mode
    val TextVariantLight = 0xFF757575 // Gris moyen/foncé. Parfaitement lisible sur du blanc.

    // Dark Mode
    val TextVariantDark = 0xFFAAAAAA  // Gris clair doux. Ne "brille" pas trop sur le fond noir.


    val RedError = 0xFFB00020
}

/**
 * Couleurs Sémantiques : On définit comment les couleurs sont utilisées
 * selon le thème (Clair ou Sombre).
 */
object DesignColors {
    object Light {
        val Primary = Palette.BrandPrimary
        val Secondary = Palette.BrandSecondary
        val Tertiary = Palette.BrandTertiary

        val Background = Palette.BgLight
        val Surface = Palette.SurfaceLight
        val OnSurface = Palette.BgDark
        val onSurfaceVariant = Palette.TextVariantLight
        val Error = Palette.RedError

        // Mapping spécifique Hero
        val BarBudget = Palette.BarBudget
        val BarCharges = Palette.BarCharges
        val BarRemainingPay = Palette.BarRemainingPay
        val BarRemainingLive = Palette.BarRemainingLive
        val BarWarning = Palette.BarWarning
    }

    object Dark {
        val Primary = Palette.BrandPrimary
        val Secondary = Palette.BrandSecondary
        val Tertiary = Palette.BrandTertiary

        val Background = Palette.BgDark
        val Surface = Palette.SurfaceDark
        val OnSurface = Palette.BgLight
        val onSurfaceVariant = Palette.TextVariantDark
        val Error = Palette.RedError

        // Mapping spécifique Hero
        val BarBudget = Palette.BarBudget
        val BarCharges = Palette.BarCharges
        val BarRemainingPay = Palette.BarRemainingPay
        val BarRemainingLive = Palette.BarRemainingLive
        val BarWarning = Palette.BarWarning
    }
}

/**
 * Échelle d'espacements standardisée.
 * On utilise des Int qui représenteront des "dp".
 */
object DesignSpacing {
    val None = 0
    val ExtraSmall = 2
    val Small = 4
    val Medium = 8
    val Large = 16
    val ExtraLarge = 24
    val Big = 32
}