package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Calendar
import java.util.Date

@Stable
@Immutable
data class DatePickerDate(
    val year: Int,
    val month: Int,
    val day: Int
) {
    companion object {
        val currentDate: DatePickerDate
            get() {
                val calendar = Calendar.getInstance()

                return DatePickerDate(
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                )
            }
    }
}

fun DatePickerDate.toCalendar(
    discardTime: Boolean = false
): Calendar = Calendar.getInstance().apply {
    if (discardTime) {
        clear()
    }

    set(year, month, day)
}

fun DatePickerDate.toDate(
    discardTime: Boolean = false
): Date = toCalendar(discardTime = discardTime).time

fun DatePickerDate.addDays(days: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.DAY_OF_MONTH, days)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}

fun DatePickerDate.addMonths(months: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.MONTH, months)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}

fun DatePickerDate.addYears(years: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.YEAR, years)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}
