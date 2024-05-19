package io.github.rexmtorres.android.composedatepicker.datepicker.data

import java.util.Calendar
import java.util.Locale

sealed interface Days {
    companion object {
        fun get(number: Int): Days {
            return when (number) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                else -> SATURDAY
            }
        }
    }

    val number: Int

    fun shortName(locale: Locale = Locale.getDefault()): String =
        getShortDayName(
            day = number,
            locale = locale
        )

    fun name(locale: Locale = Locale.getDefault()): String =
        getLongDayName(
            day = number,
            locale = locale
        )

    data object SUNDAY : Days {
        override val number: Int = Calendar.SUNDAY
    }

    data object MONDAY : Days {
        override val number: Int = Calendar.MONDAY
    }

    data object TUESDAY : Days {
        override val number: Int = Calendar.TUESDAY
    }

    data object WEDNESDAY : Days {
        override val number: Int = Calendar.WEDNESDAY
    }

    data object THURSDAY : Days {
        override val number: Int = Calendar.THURSDAY
    }

    data object FRIDAY : Days {
        override val number: Int = Calendar.FRIDAY
    }

    data object SATURDAY : Days {
        override val number: Int = Calendar.SATURDAY
    }
}

private fun getLongDayName(
    day: Int,
    locale: Locale = Locale.getDefault()
): String = Calendar.getInstance().let {
    it.set(Calendar.DAY_OF_WEEK, day)
    it.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
        ?: throw IllegalStateException("Failed to get long day name")
}

private fun getShortDayName(
    day: Int,
    locale: Locale = Locale.getDefault()
): String = Calendar.getInstance().let {
    it.set(Calendar.DAY_OF_WEEK, day)
    it.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale)
        ?: throw IllegalStateException("Failed to get short day name")
}
