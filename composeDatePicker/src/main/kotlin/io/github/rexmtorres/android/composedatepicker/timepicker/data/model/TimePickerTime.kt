package io.github.rexmtorres.android.composedatepicker.timepicker.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Calendar
import java.util.Date

/**
 * Represents the time used by [TimePicker][io.github.rexmtorres.android.composedatepicker.timepicker.TimePicker].
 *
 * There are several extension functions to convert between [TimePickerTime] and [Calendar] and
 * [Date].  Please see [Calendar.toTimePickerTime], [Date.toTimePickerTime],
 * [Long.millisToTimePickerTime], [TimePickerTime.toCalendar], [TimePickerTime.toDate].
 *
 * There are also extension functions to add hours and minutes to a [TimePickerTime].
 * Please see [TimePickerTime.addHours], [TimePickerTime.addMinutes].
 */
@Stable
@Immutable
data class TimePickerTime(
    val hour: Int,
    val minute: Int
) {
    companion object {
        /**
         * Returns the current time.
         */
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

/**
 * Converts a [Calendar] to a [TimePickerTime].
 */
fun Calendar.toTimePickerTime(): TimePickerTime = TimePickerTime(
    hour = get(Calendar.HOUR_OF_DAY),
    minute = get(Calendar.MINUTE)
)

/**
 * Converts a [Date] to a [TimePickerTime].
 */
fun Date.toTimePickerTime(): TimePickerTime = Calendar.getInstance().let {
    it.time = this@toTimePickerTime

    TimePickerTime(
        hour = it.get(Calendar.HOUR_OF_DAY),
        minute = it.get(Calendar.MINUTE)
    )
}

/**
 * Converts a [Long], representing the time in milliseconds, to a [TimePickerTime].
 */
fun Long.millisToTimePickerTime(): TimePickerTime = Date(this).toTimePickerTime()

/**
 * Converts a [TimePickerTime] to a [Calendar].
 *
 * @param discardDate If true, the date part of the resulting [Calendar] will be discarded.  Else,
 * it will be set to the current date.
 */
fun TimePickerTime.toCalendar(
    discardDate: Boolean = false
): Calendar = Calendar.getInstance().apply {
    if (discardDate) {
        clear()
    }

    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
}

/**
 * Converts a [TimePickerTime] to a [Date].
 *
 * @param discardDate If true, the date part of the resulting [Date] will be discarded.  Else, it
 * will be set to the current date.
 */
fun TimePickerTime.toDate(
    discardDate: Boolean = false
): Date = toCalendar(discardDate = discardDate).time

/**
 * Returns a new [TimePickerTime] with the specified [hours] added.
 */
fun TimePickerTime.addHours(
    hours: Int,
): TimePickerTime {
    val calendar = toCalendar()
    calendar.add(Calendar.HOUR_OF_DAY, hours)

    return TimePickerTime(
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE]
    )
}

/**
 * Returns a new [TimePickerTime] with the specified [minutes] added.
 */
fun TimePickerTime.addMinutes(
    minutes: Int,
): TimePickerTime {
    val calendar = toCalendar()
    calendar.add(Calendar.MINUTE, minutes)

    return TimePickerTime(
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE]
    )
}
