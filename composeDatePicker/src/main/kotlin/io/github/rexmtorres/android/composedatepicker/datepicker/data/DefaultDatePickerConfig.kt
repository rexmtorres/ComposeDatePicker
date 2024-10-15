package io.github.rexmtorres.android.composedatepicker.datepicker.data

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.rexmtorres.android.composedatepicker.R
import io.github.rexmtorres.android.composedatepicker.extension.noRippleClickable
import io.github.rexmtorres.android.composedatepicker.theme.Size
import io.github.rexmtorres.android.composedatepicker.theme.Size.extraLarge
import io.github.rexmtorres.android.composedatepicker.theme.Size.large
import io.github.rexmtorres.android.composedatepicker.theme.Size.medium
import io.github.rexmtorres.android.composedatepicker.theme.black
import io.github.rexmtorres.android.composedatepicker.theme.blue
import io.github.rexmtorres.android.composedatepicker.theme.grayDark
import io.github.rexmtorres.android.composedatepicker.theme.grayLight
import io.github.rexmtorres.android.composedatepicker.theme.red
import io.github.rexmtorres.android.composedatepicker.theme.white

@Suppress("ConstPropertyName")
object DefaultDatePickerConfig  {
    val height: Dp = 260.dp

    // Header configuration
    val headerHeight: Dp = 35.dp
    const val uppercaseHeaderText: Boolean = false
    val headerTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        color = black
    )
    val monthYearHeader: @Composable (month: String, year: Int) -> Unit = { month, year ->
        Text(
            text = "$month $year",
            modifier = Modifier
                .padding(start = extraLarge)
        )
    }
    val previousArrow: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            contentDescription = stringResource(id = R.string.leftArrow),
            tint = black,
            modifier = Modifier
                .size(35.dp)
        )
    }
    val nextArrow: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.rightArrow),
            tint = black,
            modifier = Modifier
                .padding(end = medium)
                .size(35.dp)
        )
    }

    // Date view configuration
    val daysNameTextStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        color = grayDark
    )
    const val uppercaseDaysNameText: Boolean = false
    val dateTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        color = black
    )
    val selectedDateTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        color = white
    )
    val sundayTextColor: Color = red
    val disabledDateColor: Color = grayLight
    val selectedDateBackgroundSize: Dp = 32.dp
    val selectedDateBackgroundColor: Color = blue
    val selectedDateBackgroundShape: Shape = CircleShape

    // Month Year view configuration
    val monthYearTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        color = black.copy(alpha = 0.5f)
    )
    val selectedMonthYearTextStyle: TextStyle = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.W600,
        color = black.copy(alpha = 1f)
    )
    const val numberOfMonthYearRowsDisplayed: Int = 7
    const val selectedMonthYearScaleFactor: Float = 1.2f
    val selectedMonthYearAreaHeight: Dp = 35.dp
    val selectedMonthYearAreaColor: Color = grayLight.copy(alpha = 0.2f)
    val selectedMonthYearAreaShape: Shape = RoundedCornerShape(Size.medium)
}
