package io.github.rexmtorres.android.composedatepicker.datepicker.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days

@Stable
@Immutable
data class Month(
    val name: String,
    val numberOfDays: Int,
    val firstDayOfMonth: Days,
    val number: Int
)