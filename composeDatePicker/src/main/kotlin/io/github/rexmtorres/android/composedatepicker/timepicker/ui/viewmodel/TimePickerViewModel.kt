package io.github.rexmtorres.android.composedatepicker.timepicker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rexmtorres.android.composedatepicker.timepicker.data.Constant
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.TimePickerTime
import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import io.github.rexmtorres.android.composedatepicker.timepicker.ui.model.TimePickerUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

internal class TimePickerViewModel : ViewModel() {
    private val _uiState: MutableLiveData<TimePickerUiState> = MutableLiveData(TimePickerUiState())
    val uiState: LiveData<TimePickerUiState> = _uiState

    // When time of the day is manually selected by user we either add or subtract this
    private var hour: Int = 0

    fun setLocale(locale: Locale) {
        _uiState.value?.apply {
            _uiState.value = this.copy(
                locale = locale,
                timesOfDay = Constant.getTimesOfDay(locale = locale),
            )

            _uiState.value?.selectedTimeOfDayIndex?.also { index ->
                hour += if (index == 1) {
                    12
                } else {
                    -12
                }
            }
        }
    }

    fun updateSelectedHourIndex(index: Int) {
        _uiState.value = _uiState.value?.copy(selectedHourIndex = index)

        _uiState.value?.apply {
            if (!is24Hour) {
                viewModelScope.launch {
                    delay(200)
                    _uiState.value = _uiState.value?.copy(
                        selectedTimeOfDayIndex = if ((index + 1 + hour) % 24 >= 12) 1 else 0
                    )
                }
            }
        }
    }

    fun updateSelectedMinuteIndex(index: Int) {
        _uiState.value = _uiState.value?.copy(selectedMinuteIndex = index)
    }

    fun updateSelectedTimeOfDayIndex(index: Int) {
        if (index != _uiState.value?.selectedTimeOfDayIndex) {
            _uiState.value = _uiState.value?.copy(selectedTimeOfDayIndex = index)
            hour += if (index == 1) 12 else -12
        }
    }

    fun getSelectedTime(): TimePickerTime? {
        val time = _uiState.value?.let {
            var hour = it.hours[it.selectedHourIndex].toInt()
            if (!it.is24Hour) {
                hour = hour % 12 + if (it.selectedTimeOfDayIndex == 1) 12 else 0
            }
            TimePickerTime(
                hour,
                it.minutes[it.selectedMinuteIndex].toInt()
            )
        }
        return time
    }

    fun updateUiState(
        timePickerTime: TimePickerTime,
        minuteGap: MinuteGap,
        is24: Boolean,
        locale: Locale
    ) {
        _uiState.value = getUiStateTimeProvided(
            timePickerTime = timePickerTime,
            minuteGap = minuteGap,
            is24 = is24,
            locale = locale
        )
    }

    fun getUiStateTimeProvided(
        timePickerTime: TimePickerTime,
        minuteGap: MinuteGap,
        is24: Boolean,
        locale: Locale
    ): TimePickerUiState {

        if (timePickerTime.hour < 0 || timePickerTime.hour > 23) {
            throw IllegalArgumentException("Invalid hour: ${timePickerTime.hour}, The hour value must be between 0 and 23 inclusive.")
        }
        if (timePickerTime.minute < 0 || timePickerTime.minute > 59) {
            throw IllegalArgumentException("Invalid minute: ${timePickerTime.minute}, The minute value must be between 0 and 59 inclusive.")
        }

        val minute: Int = Constant.getNearestNextMinute(timePickerTime.minute, minuteGap)
        val hour: Int = timePickerTime.hour + if(minute == 0 && timePickerTime.minute != 0) 1 else 0

        return TimePickerUiState(
            locale = locale,
            is24Hour = is24,
            minuteGap = minuteGap,
            hours = Constant.getHours(is24),
            selectedHourIndex = Constant.getMiddleOfHour(is24) + hour,
            minutes = Constant.getMinutes(minuteGap),
            selectedMinuteIndex = Constant.getMiddleOfMinute(minuteGap) + minute / minuteGap.gap,
            timesOfDay = Constant.getTimesOfDay(locale = locale),
            selectedTimeOfDayIndex = if (hour in 12..23) 1 else 0
        )
    }
}