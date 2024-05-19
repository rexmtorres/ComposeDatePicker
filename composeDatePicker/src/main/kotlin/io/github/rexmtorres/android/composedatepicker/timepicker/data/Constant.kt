package io.github.rexmtorres.android.composedatepicker.timepicker.data

import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import java.util.Calendar
import java.util.Locale

internal object Constant {
    private const val REPEAT_COUNT: Int = 200

    private fun findHours(is24Hour: Boolean): List<String> {
        return if (is24Hour) {
            (0..23).map { it.toString() }
        } else {
            (1..12).map { it.toString() }
        }
    }

    fun getHours(is24Hour: Boolean): List<String> {
        val hours = findHours(is24Hour = is24Hour)
        return List(REPEAT_COUNT) { hours }.flatten()
    }

    fun getMiddleOfHour(is24Hour: Boolean): Int {
        return if (is24Hour) {
            24 * (REPEAT_COUNT / 2)
        } else {
            12 * (REPEAT_COUNT / 2) - 1
        }
    }

    private fun findMinutes(minuteGap: MinuteGap): List<String> {
        var value = 0

        val list = mutableListOf<String>()

        while (value < 60) {
            list.add(value.toString().padStart(2, '0'))
//            list.add("${if (value < 10) "0" else ""}${value}")
            value += minuteGap.gap
        }

        return list
    }

    fun getMinutes(minuteGap: MinuteGap): List<String> {
        val minutes = findMinutes(minuteGap = minuteGap)
        return List(REPEAT_COUNT) { minutes }.flatten()
    }

    fun getMiddleOfMinute(minuteGap: MinuteGap): Int {
        return minuteGap.intervalCount * (REPEAT_COUNT / 2)
    }

    fun getNearestNextMinute(minute: Int, minuteGap: MinuteGap): Int {
        if (minuteGap.gap == 1) {
            return minute
        }

        var value = 0

        while (value < minute) {
            value += minuteGap.gap
        }

        return if (value >= 60) {
            0
        } else {
            value
        }
    }

    fun getTimesOfDay(
        locale: Locale = Locale.getDefault()
    ): List<String> = Calendar.getInstance().getDisplayNames(
        Calendar.AM_PM,
        Calendar.LONG,
        locale
    )?.map { (name, value) ->
        value to name
    }?.sortedBy { (value, _) ->
        value
    }?.map { (_, name) ->
        name
    } ?: throw IllegalStateException("Failed to get AM/PM names")
}
