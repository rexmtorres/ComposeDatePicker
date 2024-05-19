package io.github.rexmtorres.android.composedatepicker.timepicker.data.model

import java.util.Calendar

class TimePickerTime(val hour: Int, val minute: Int) {

    companion object DefaultTime {
        fun getTime(): TimePickerTime {
            return TimePickerTime(
                Calendar.getInstance()[Calendar.HOUR_OF_DAY],
                Calendar.getInstance()[Calendar.MINUTE]
            )
        }
    }
}


