package io.github.rexmtorres.android.composedatepicker.timepicker.ui.model

import io.github.rexmtorres.android.composedatepicker.timepicker.data.Constant
import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import java.util.Calendar
import java.util.Locale

internal data class TimePickerUiState(
    val locale: Locale = Locale.getDefault(),
    val is24Hour: Boolean = false,
    val minuteGap: MinuteGap = MinuteGap.FIVE,
    val hours: List<String> = Constant.getHours(is24Hour),
    val selectedHourIndex: Int = Constant.getMiddleOfHour(is24Hour) +
            Calendar.getInstance()[Calendar.HOUR_OF_DAY] +
            if (
                Constant.getNearestNextMinute(
                    minute = Calendar.getInstance()[Calendar.MINUTE],
                    minuteGap = minuteGap
                ) == 0 && Calendar.getInstance()[Calendar.MINUTE] != 0
            ) {
                1
            } else {
                0
            },
    val minutes: List<String> = Constant.getMinutes(minuteGap),
    val selectedMinuteIndex: Int = Constant.getMiddleOfMinute(minuteGap) +
            Constant.getNearestNextMinute(
                minute = Calendar.getInstance()[Calendar.MINUTE],
                minuteGap = minuteGap
            ) / minuteGap.gap,
    val timesOfDay: List<String> = Constant.getTimesOfDay(locale),
    val selectedTimeOfDayIndex: Int =
        if (
            (
                    hours[selectedHourIndex].toInt() +
                            if (is24Hour) {
                                0
                            } else {
                                (12 * Calendar.getInstance()[Calendar.AM_PM])
                            }
                    ) in 12..23
        ) {
            1
        } else {
            0
        },
)
