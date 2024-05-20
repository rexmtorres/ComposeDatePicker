package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import io.github.rexmtorres.android.composedatepicker.extension.isEqual

/**
 * Specifies the range of dates that can be selected in [DatePicker][io.github.rexmtorres.android.composedatepicker.datepicker.DatePicker].
 */
class SelectionLimiter(
    private val fromDate: DatePickerDate? = null,
    private val toDate: DatePickerDate? = null
) {
    fun isWithinRange(date: DatePickerDate): Boolean {
        val fromDate = fromDate?.toCalendar()
        val toDate = toDate?.toCalendar()
        val selectedDate = date.toCalendar()

        return when {
            fromDate == null && toDate == null -> {
                return true
            }

            fromDate != null && toDate != null -> {
                (selectedDate.before(toDate) && selectedDate.after(fromDate)) ||
                        (selectedDate.isEqual(toDate) || selectedDate.isEqual(fromDate))
            }

            fromDate != null -> {
                selectedDate.after(fromDate) || selectedDate.isEqual(fromDate)
            }

            toDate != null -> {
                selectedDate.before(toDate) || selectedDate.isEqual(toDate)
            }

            else -> {
                true
            }
        }
    }
}

