package com.eeema.android.rickandmortyapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eeema.android.rickandmortyapp.R

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    )
)

val Typography.splashFont: FontFamily
    @Composable
    get() = FontFamily(Font(R.font.encoded_sans_sc_semibold, FontWeight.SemiBold))
