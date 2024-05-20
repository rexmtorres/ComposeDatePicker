package io.github.rexmtorres.android.composedatepicker.timepicker.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import io.github.rexmtorres.android.composedatepicker.timepicker.data.DefaultTimePickerConfig
import io.github.rexmtorres.android.composedatepicker.timepicker.ui.model.TimePickerConfiguration.Builder

/**
 * Additional configurations for customizing the appearance of the date picker.
 *
 * Use [Builder] to create a builder instance and then call [Builder.build] to create the instance
 * of this class.
 *
 * See [DefaultTimePickerConfig] for the default configurations.
 *
 * @property height The height of the time picker.
 * @property timeTextStyle The text style for the time text.
 * @property selectedTimeTextStyle The text style for the selected time text.
 * @property numberOfTimeRowsDisplayed The number of rows of time to display.
 * @property selectedTimeScaleFactor The scale factor for the selected time area.
 * @property selectedTimeAreaHeight The height of the selected time area.
 * @property selectedTimeAreaColor The color of the selected time area.
 * @property selectedTimeAreaShape The shape of the selected time area.
 */
class TimePickerConfiguration private constructor(
    val height: Dp,
    val timeTextStyle: TextStyle,
    val selectedTimeTextStyle: TextStyle,
    val numberOfTimeRowsDisplayed: Int,
    val selectedTimeScaleFactor: Float,
    val selectedTimeAreaHeight: Dp,
    val selectedTimeAreaColor: Color,
    val selectedTimeAreaShape: Shape
) {
    class Builder {
        private var height: Dp = DefaultTimePickerConfig.height
        private var timeTextStyle: TextStyle = DefaultTimePickerConfig.timeTextStyle
        private var selectedTimeTextStyle: TextStyle = DefaultTimePickerConfig.selectedTimeTextStyle
        private var numberOfTimeRowsDisplayed: Int =
            DefaultTimePickerConfig.numberOfTimeRowsDisplayed
        private var selectedTimeScaleFactor: Float = DefaultTimePickerConfig.selectedTimeScaleFactor
        private var selectedTimeAreaHeight: Dp = DefaultTimePickerConfig.selectedTimeAreaHeight
        private var selectedTimeAreaColor: Color = DefaultTimePickerConfig.selectedTimeAreaColor
        private var selectedTimeAreaShape: Shape = DefaultTimePickerConfig.selectedTimeAreaShape

        fun height(height: Dp) =
            apply { this.height = height }

        fun timeTextStyle(textStyle: TextStyle) =
            apply { this.timeTextStyle = textStyle }

        fun selectedTimeTextStyle(textStyle: TextStyle) =
            apply { this.selectedTimeTextStyle = textStyle }

        fun selectedTimeScaleFactor(scaleFactor: Float) =
            apply { this.selectedTimeScaleFactor = scaleFactor }

        fun numberOfTimeRowsDisplayed(count: Int) =
            apply { this.numberOfTimeRowsDisplayed = count }

        fun selectedTimeAreaHeight(height: Dp) =
            apply { this.selectedTimeAreaHeight = height }

        fun selectedTimeAreaColor(color: Color) =
            apply { this.selectedTimeAreaColor = color }

        fun selectedTimeAreaShape(shape: Shape) =
            apply { this.selectedTimeAreaShape = shape }

        fun build(): TimePickerConfiguration {
            return TimePickerConfiguration(
                height = height,
                timeTextStyle = timeTextStyle,
                selectedTimeTextStyle = selectedTimeTextStyle,
                numberOfTimeRowsDisplayed = numberOfTimeRowsDisplayed,
                selectedTimeScaleFactor = selectedTimeScaleFactor,
                selectedTimeAreaHeight = selectedTimeAreaHeight,
                selectedTimeAreaColor = selectedTimeAreaColor,
                selectedTimeAreaShape = selectedTimeAreaShape,
            )
        }
    }
}
