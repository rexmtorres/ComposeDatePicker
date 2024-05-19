package io.github.rexmtorres.android.composedatepicker.datepicker.data

import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.Month
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.FRIDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.MONDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.SATURDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.SUNDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.THURSDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.TUESDAY
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days.WEDNESDAY
import io.github.rexmtorres.android.composedatepicker.extension.isLeapYear
import java.util.Calendar
import java.util.Locale

internal object Constant {
    private const val REPEAT_COUNT: Int = 200

    val days = listOf(
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    )

    val years = List(REPEAT_COUNT) {
        it + Calendar.getInstance()[Calendar.YEAR] - (REPEAT_COUNT / 2)
    }

    fun getMonthNames(
        locale: Locale = Locale.getDefault()
    ): List<String> {
        val months = getMonthNameMap(locale = locale).map { (_, name) -> name }

        return List(REPEAT_COUNT) { months }.flatten()
    }

    fun getMiddleOfMonth(): Int {
        return 12 * (REPEAT_COUNT / 2)
    }

    fun getMonths(
        year: Int,
        locale: Locale = Locale.getDefault()
    ): List<Month> {
        val months = getMonthNameMap(locale = locale)

        return listOf(
            Month(
                name = months[Calendar.JANUARY]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.JANUARY, year),
                number = Calendar.JANUARY
            ),
            Month(
                name = months[Calendar.FEBRUARY]!!,
                numberOfDays = if (year.isLeapYear()) 29 else 28,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.FEBRUARY, year),
                number = Calendar.FEBRUARY
            ),
            Month(
                name = months[Calendar.MARCH]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.MARCH, year),
                number = Calendar.MARCH
            ),
            Month(
                name = months[Calendar.APRIL]!!,
                numberOfDays = 30,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.APRIL, year),
                number = Calendar.APRIL
            ),
            Month(
                name = months[Calendar.MAY]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.MAY, year),
                number = Calendar.MAY
            ),
            Month(
                name = months[Calendar.JUNE]!!,
                numberOfDays = 30,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.JUNE, year),
                number = Calendar.JUNE
            ),
            Month(
                name = months[Calendar.JULY]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.JULY, year),
                number = Calendar.JULY
            ),
            Month(
                name = months[Calendar.AUGUST]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.AUGUST, year),
                number = Calendar.AUGUST
            ),
            Month(
                name = months[Calendar.SEPTEMBER]!!,
                numberOfDays = 30,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.SEPTEMBER, year),
                number = Calendar.SEPTEMBER
            ),
            Month(
                name = months[Calendar.OCTOBER]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.OCTOBER, year),
                number = Calendar.OCTOBER
            ),
            Month(
                name = months[Calendar.NOVEMBER]!!,
                numberOfDays = 30,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.NOVEMBER, year),
                number = Calendar.NOVEMBER
            ),
            Month(
                name = months[Calendar.DECEMBER]!!,
                numberOfDays = 31,
                firstDayOfMonth = getFirstDayOfMonth(Calendar.DECEMBER, year),
                number = Calendar.DECEMBER
            )
        )
    }

    private fun getFirstDayOfMonth(month: Int, year: Int): Days {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        return Days.get(calendar[Calendar.DAY_OF_WEEK])
    }

    private fun getMonthNameMap(
        locale: Locale = Locale.getDefault()
    ): Map<Int, String> = Calendar.getInstance().getDisplayNames(
        Calendar.MONTH,
        Calendar.LONG,
        locale
    )?.map { (name, value) ->
        value to name
    }?.sortedBy { (value, _) ->
        value
    } ?.toMap() ?: throw IllegalStateException("Failed to get month names")
}