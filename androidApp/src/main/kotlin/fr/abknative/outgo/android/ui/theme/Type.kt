package fr.abknative.outgo.android.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.core.ui.DesignTypography

data class OutgoTypography(
    val title: TextStyle,      // EXTRALARGE / Bold
    val subtitle: TextStyle,   // LARGE / SemiBold
    val body: TextStyle,       // MEDIUM / Regular
    val caption: TextStyle,    // SMALL / Regular
    val label: TextStyle       // EXTRA_SMALL / Medium
)

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val OutgoTypographyInstance = OutgoTypography(
    title = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = DesignTypography.EXTRALARGE.sp
    ),
    subtitle = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = DesignTypography.LARGE.sp
    ),
    body = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = DesignTypography.MEDIUM.sp
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = DesignTypography.SMALL.sp
    ),
    label = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = DesignTypography.EXTRA_SMALL.sp
    )
)