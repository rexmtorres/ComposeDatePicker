package io.github.rexmtorres.android.composedatepicker.datepicker.ui.model

import io.github.rexmtorres.android.composedatepicker.datepicker.data.Constant
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.Month
import java.util.Calendar
import java.util.Locale

internal data class DatePickerUiState(
    val locale: Locale = Locale.getDefault(),
    val selectedYear: Int = Calendar.getInstance()[Calendar.YEAR],
    val selectedYearIndex: Int = Constant.years.size / 2,
    val selectedMonth: Month = Constant.getMonths(selectedYear, locale)[Calendar.getInstance()[Calendar.MONTH]],
    val selectedMonthIndex: Int = Constant.getMiddleOfMonth() + selectedMonth.number,
    val currentVisibleMonth: Month = selectedMonth,
    val selectedDayOfMonth: Int = Calendar.getInstance()[Calendar.DAY_OF_MONTH],
    val years: List<String> = Constant.years.map { "$it" }.toList(),
    val months: List<String> = Constant.getMonthNames(locale),
    val isMonthYearViewVisible: Boolean = false
)
