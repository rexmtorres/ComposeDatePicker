package io.github.rexmtorres.android.composedatepicker.timepicker.enums

/**
 * Used by [TimePicker][io.github.rexmtorres.android.composedatepicker.timepicker.TimePicker] to
 * determine the minute selection.
 */
enum class MinuteGap(
    val gap: Int,
    val intervalCount: Int
) {
    /**
     * All minutes, from 0 to 59 are displayed.
     */
    ONE(1, 60),

    /**
     * Every 5 minutes, from 0 to 55 are displayed.
     */
    FIVE(5, 12),

    /**
     * Every 10 minutes, from 0 to 50 are displayed.
     */
    TEN(10, 6),

    /**
     * Every 15 minutes, from 0 to 45 are displayed.
     */
    FIFTEEN(15, 4),

    /**
     * 0 and 30 are displayed.
     */
    THIRTY(30, 2)
}
