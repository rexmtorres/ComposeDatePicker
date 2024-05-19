package io.github.rexmtorres.android.composedatepicker.timepicker.enums

enum class MinuteGap(
    val gap: Int,
    val intervalCount: Int
) {
    ONE(1, 60),
    FIVE(5, 12),
    TEN(10, 6),
    FIFTEEN(15, 4),
    THIRTY(30, 2)
}
