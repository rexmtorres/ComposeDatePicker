package io.github.rexmtorres.android.composedatepicker.timepicker.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Calendar
import java.util.Date

@Stable
@Immutable
data class TimePickerTime(
    val hour: Int,
    val minute: Int
) {
    companion object {
        val currentTime: TimePickerTime
            get() {
                val calendar = Calendar.getInstance()

                return TimePickerTime(
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE]
                )
            }
    }
}

fun TimePickerTime.toCalendar(
    discardDate: Boolean = false
): Calendar = Calendar.getInstance().apply {
    if (discardDate) {
        clear()
    }

    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
}

fun TimePickerTime.toDate(
    discardDate: Boolean = false
): Date = toCalendar(discardDate = discardDate).time

fun TimePickerTime.addHours(
    hours: Int,
    discardDate: Boolean = false
): TimePickerTime {
    val calendar = toCalendar(discardDate = discardDate)
    calendar.add(Calendar.HOUR_OF_DAY, hours)

    return TimePickerTime(
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE]
    )
}

fun TimePickerTime.addMinutes(
    minutes: Int,
    discardDate: Boolean = false
): TimePickerTime {
    val calendar = toCalendar(discardDate = discardDate)
    calendar.add(Calendar.MINUTE, minutes)

    return TimePickerTime(
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE]
    )
}
