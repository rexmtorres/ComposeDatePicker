package com.vsnappy1.datepicker.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsnappy1.datepicker.data.Constant
import com.vsnappy1.datepicker.data.model.ComposeDatePickerDate
import com.vsnappy1.datepicker.data.model.Month
import com.vsnappy1.datepicker.ui.model.DatePickerUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DatePickerViewModel : ViewModel() {

    private var _uiState = MutableLiveData(DatePickerUiState())
    val uiState: LiveData<DatePickerUiState> = _uiState
    private lateinit var availableMonths: List<Month>


    init {
        uiState.value?.let {
            availableMonths = Constant.getMonths(it.selectedYear)
        }
    }

    private fun updateCurrentVisibleMonth(month: Int) {
        _uiState.value?.apply {
            _uiState.value = this.copy(currentVisibleMonth = availableMonths[month])
        }
    }

    fun updateSelectedMonthIndex(index: Int) {
        _uiState.value?.apply {
            _uiState.value = this.copy(
                selectedMonthIndex = index,
                currentVisibleMonth = availableMonths[index % 12]
            )
        }
    }

    fun updateSelectedDayAndMonth(day: Int) {
        _uiState.value = _uiState.value?.let {
            _uiState.value?.copy(
                selectedDayOfMonth = day,
                selectedMonth = it.currentVisibleMonth
            )
        }
    }

    fun moveToNextMonth() {
        _uiState.value?.apply {
            if (currentVisibleMonth.number == 11) { // if it is December
                val nextYearIndex = selectedYearIndex + 1
                if(nextYearIndex == years.size) return
                val nextYear = years[nextYearIndex].toInt()
                availableMonths = Constant.getMonths(nextYear)
                _uiState.value = _uiState.value?.copy(
                    selectedYear = nextYear,
                    selectedYearIndex = nextYearIndex,
                    selectedMonthIndex = getAdjustedSelectedMonthIndex(selectedMonthIndex + 1),
                    currentVisibleMonth = availableMonths[0]
                )
            } else {
                _uiState.value = _uiState.value?.copy(
                    currentVisibleMonth = availableMonths[currentVisibleMonth.number + 1],
                    selectedMonthIndex = getAdjustedSelectedMonthIndex(selectedMonthIndex + 1),
                )
            }
        }
    }

    fun moveToPreviousMonth() {
        _uiState.value?.apply {
            if (currentVisibleMonth.number == 0) { // if it is January
                val previousYearIndex = selectedYearIndex - 1
                if(previousYearIndex == -1) return
                val previousYear = years[previousYearIndex].toInt()
                availableMonths = Constant.getMonths(previousYear)
                _uiState.value = _uiState.value?.copy(
                    selectedYear = previousYear,
                    selectedYearIndex = previousYearIndex,
                    selectedMonthIndex = getAdjustedSelectedMonthIndex(selectedMonthIndex - 1),
                    currentVisibleMonth = availableMonths[11]
                )
            } else {
                _uiState.value = _uiState.value?.copy(
                    currentVisibleMonth = availableMonths[currentVisibleMonth.number - 1],
                    selectedMonthIndex = getAdjustedSelectedMonthIndex(selectedMonthIndex - 1),
                )
            }
        }
    }

    private fun getAdjustedSelectedMonthIndex(index: Int) = Constant.monthMiddleIndex + index % 12

    fun updateSelectedYearIndex(index: Int) {
        availableMonths = Constant.getMonths(Constant.years[index])
        _uiState.value = _uiState.value?.copy(
            selectedYearIndex = index,
            selectedYear = Constant.years[index],
            currentVisibleMonth = availableMonths[_uiState.value?.currentVisibleMonth?.number ?: 0]
        )
    }

    fun toggleIsMonthYearViewVisible() {
        if (_uiState.value?.isMonthYearViewVisible == true) {
            viewModelScope.launch {
                delay(250)
                _uiState.value = _uiState.value?.let {
                    it.copy(
                        selectedMonthIndex = getAdjustedSelectedMonthIndex(it.selectedMonthIndex)
                    )
                }
            }
        }
        _uiState.value = _uiState.value?.let {
            it.copy(
                isMonthYearViewVisible = !it.isMonthYearViewVisible,
            )
        }
    }

    fun setDate(date: ComposeDatePickerDate) {
        val yearMin = Constant.years.first()
        val yearMax = Constant.years.last()

        if (date.year < yearMin || date.year > yearMax) {
            throw IllegalArgumentException("Invalid year: ${date.year}, year value must be between $yearMin to $yearMax.")
        }
        if (date.month < 0 || date.month > 11) {
            throw IllegalArgumentException("Invalid month: ${date.month}, month value must be between 0 to 11.")
        }

        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = date.year
        calendar[Calendar.MONTH] = date.month

        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (date.day < 1) {
            throw IllegalArgumentException("Invalid day: ${date.day}, day value must be greater than zero.")
        }
        if (date.day > maxDays) {
            throw IllegalArgumentException("Invalid day: ${date.day}, day value must be less than equal to $maxDays for given month.")
        }

        val index = Constant.years.indexOf(date.year)
        updateSelectedYearIndex(index)
        updateCurrentVisibleMonth(date.month)
        updateSelectedDayAndMonth(date.day)
    }
}