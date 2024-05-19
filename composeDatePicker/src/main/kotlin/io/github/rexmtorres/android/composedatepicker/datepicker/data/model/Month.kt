package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import com.composedatepicker.datepicker.enums.Days

data class Month(
    val name: String,
    val numberOfDays: Int,
    val firstDayOfMonth: Days,
    val number: Int
)