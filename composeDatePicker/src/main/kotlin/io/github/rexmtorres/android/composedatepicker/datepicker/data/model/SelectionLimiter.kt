package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import io.github.rexmtorres.android.composedatepicker.extension.isEqual
import java.util.Calendar
import java.util.Date

/**
 * Specifies the range of dates that can be selected in [DatePicker][io.github.rexmtorres.android.composedatepicker.datepicker.DatePicker].
 *
 * @param fromDate The earliest date that can be selected.
 * @param toDate The latest date that can be selected.
 * @param disabledDates A list of dates that cannot be selected.
 */
class SelectionLimiter private constructor(
    private val fromDate: DatePickerDate? = null,
    private val toDate: DatePickerDate? = null,
    private val disabledDates: List<DatePickerDate> = emptyList()
) {
    companion object {
        /**
         * Creates a [SelectionLimiter] that imposes no limits.
         */
        fun unlimited(): SelectionLimiter = SelectionLimiter()

        /**
         * Creates a [SelectionLimiter] from the given [DatePickerDate]s.
         *
         * @param fromDate The earliest date that can be selected.
         * @param toDate The latest date that can be selected.
         * @param disabledDates A list of dates that cannot be selected.
         */
        fun fromDatePickerDates(
            fromDate: DatePickerDate? = null,
            toDate: DatePickerDate? = null,
            disabledDates: List<DatePickerDate> = emptyList()
        ): SelectionLimiter = SelectionLimiter(
            fromDate = fromDate,
            toDate = toDate,
            disabledDates = disabledDates
        )

        /**
         * Creates a [SelectionLimiter] from the given [Date]s.
         *
         * @param fromDate The earliest date that can be selected.
         * @param toDate The latest date that can be selected.
         * @param disabledDates A list of dates that cannot be selected.
         */
        fun fromDates(
            fromDate: Date? = null,
            toDate: Date? = null,
            disabledDates: List<Date> = emptyList()
        ): SelectionLimiter = SelectionLimiter(
            fromDate = fromDate?.toDatePickerDate(),
            toDate = toDate?.toDatePickerDate(),
            disabledDates = disabledDates.map { it.toDatePickerDate() }
        )

        /**
         * Creates a [SelectionLimiter] from the given [timestamps][Long]s.
         *
         * @param fromDate The earliest date that can be selected.
         * @param toDate The latest date that can be selected.
         * @param disabledDates A list of dates that cannot be selected.
         */
        fun fromTimestamps(
            fromDate: Long? = null,
            toDate: Long? = null,
            disabledDates: List<Long> = emptyList()
        ): SelectionLimiter = SelectionLimiter(
            fromDate = fromDate?.millisToDatePickerDate(),
            toDate = toDate?.millisToDatePickerDate(),
            disabledDates = disabledDates.map { it.millisToDatePickerDate() }
        )

        /**
         * Creates a [SelectionLimiter] from the given [Calendar]s.
         *
         * @param fromDate The earliest date that can be selected.
         * @param toDate The latest date that can be selected.
         * @param disabledDates A list of dates that cannot be selected.
         */
        fun fromCalendars(
            fromDate: Calendar? = null,
            toDate: Calendar? = null,
            disabledDates: List<Calendar> = emptyList()
        ): SelectionLimiter = SelectionLimiter(
            fromDate = fromDate?.toDatePickerDate(),
            toDate = toDate?.toDatePickerDate(),
            disabledDates = disabledDates.map { it.toDatePickerDate() }
        )
    }

    fun isWithinRange(date: DatePickerDate): Boolean {
        val fromDate = fromDate?.toCalendar()
        val toDate = toDate?.toCalendar()
        val selectedDate = date.toCalendar()

        return when {
            disabledDates.containsElement(date) -> {
                false
            }

            (fromDate == null) && (toDate == null) -> {
                true
            }

            (fromDate != null) && (toDate != null) -> {
                (selectedDate.before(toDate) && selectedDate.after(fromDate)) ||
                        (selectedDate.isEqual(toDate) || selectedDate.isEqual(fromDate))
            }

            (fromDate != null) -> {
                selectedDate.after(fromDate) || selectedDate.isEqual(fromDate)
            }

            else -> {
                selectedDate.before(toDate) || selectedDate.isEqual(toDate)
            }
        }
    }
}

private fun List<DatePickerDate>.containsElement(date: DatePickerDate): Boolean {
    println("disabledDates: $this")
    println("date: $date")

    return any { elem ->
        println("elem: $elem")

        (elem.year == date.year) && (elem.month == date.month) && (elem.day == date.day)
    }
}
