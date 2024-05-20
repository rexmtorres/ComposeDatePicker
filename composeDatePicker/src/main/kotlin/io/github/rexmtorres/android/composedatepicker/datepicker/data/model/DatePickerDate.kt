package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Calendar
import java.util.Date

/**
 * Represents the date used by [DatePicker][io.github.rexmtorres.android.composedatepicker.datepicker.DatePicker].
 *
 * There are several extension functions to convert between [DatePickerDate] and [Calendar], [Date]
 * and [Long] (date in milliseconds).  Please refer to the following extensions:
 * - [Calendar.toDatePickerDate]
 * - [Date.toDatePickerDate]
 * - [Long.millisToDatePickerDate]
 * - [DatePickerDate.toCalendar]
 * - [DatePickerDate.toDate]
 * - [DatePickerDate.toMillis]
 *
 * There are also extension functions to add days, months and years to a [DatePickerDate].
 * Please see [DatePickerDate.addDays], [DatePickerDate.addMonths], [DatePickerDate.addYears].
 */
@Stable
@Immutable
data class DatePickerDate(
    val year: Int,
    val month: Int,
    val day: Int
) {
    companion object {
        /**
         * Returns the current date.
         */
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

/**
 * Converts a [Calendar] to a [DatePickerDate]
 */
fun Calendar.toDatePickerDate(): DatePickerDate = DatePickerDate(
    get(Calendar.YEAR),
    get(Calendar.MONTH),
    get(Calendar.DAY_OF_MONTH)
)

/**
 * Converts a [Date] to a [DatePickerDate]
 */
fun Date.toDatePickerDate(): DatePickerDate = Calendar.getInstance().let {
    it.time = this@toDatePickerDate

    DatePickerDate(
        it[Calendar.YEAR],
        it[Calendar.MONTH],
        it[Calendar.DAY_OF_MONTH]
    )
}

/**
 * Converts a [Long], representing the date in milliseconds, to a [DatePickerDate].
 */
fun Long.millisToDatePickerDate(): DatePickerDate = Date(this).toDatePickerDate()

/**
 * Converts a [DatePickerDate] to a [Calendar]
 *
 * @param discardTime If true, the time part of the resulting [Calendar] will be discarded.  Else,
 * it will be set to the current time.
 */
fun DatePickerDate.toCalendar(
    discardTime: Boolean = false
): Calendar = Calendar.getInstance().apply {
    if (discardTime) {
        clear()
    }

    set(year, month, day)
}

/**
 * Converts a [DatePickerDate] to a [Date]
 *
 * @param discardTime If true, the time part of the resulting [Date] will be discarded.  Else,
 * it will be set to the current time.
 */
fun DatePickerDate.toDate(
    discardTime: Boolean = false
): Date = toCalendar(discardTime = discardTime).time

/**
 * Converts a [DatePickerDate] to a [Long], representing the date in milliseconds.
 *
 * @param discardTime If true, the time part of the resulting date representation will be discarded.
 * Else, it will be set to the current time.
 */
fun DatePickerDate.toMillis(
    discardTime: Boolean = false
): Long = toDate(discardTime = discardTime).time

/**
 * Returns a new [DatePickerDate] with the specified [days] added.
 */
fun DatePickerDate.addDays(days: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.DAY_OF_MONTH, days)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}

/**
 * Returns a new [DatePickerDate] with the specified [months] added.
 */
fun DatePickerDate.addMonths(months: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.MONTH, months)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}

/**
 * Returns a new [DatePickerDate] with the specified [years] added.
 */
fun DatePickerDate.addYears(years: Int): DatePickerDate {
    val calendar = toCalendar()
    calendar.add(Calendar.YEAR, years)

    return DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}
