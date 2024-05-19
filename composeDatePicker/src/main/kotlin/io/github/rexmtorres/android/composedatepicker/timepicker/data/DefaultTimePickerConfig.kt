package io.github.rexmtorres.android.composedatepicker.timepicker.data

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composedatepicker.theme.Size
import com.composedatepicker.theme.grayLight

class DefaultTimePickerConfig private constructor() {

    companion object {
        val height: Dp = 200.dp
        val timeTextStyle: TextStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black.copy(alpha = 0.5f)
        )
        val selectedTimeTextStyle: TextStyle = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black
        )
        const val numberOfTimeRowsDisplayed: Int = 7
        const val selectedTimeScaleFactor: Float = 1.2f
        val selectedTimeAreaHeight: Dp = 35.dp
        val selectedTimeAreaColor: Color = grayLight.copy(alpha = 0.2f)
        val selectedTimeAreaShape: Shape = RoundedCornerShape(Size.medium)
    }
}