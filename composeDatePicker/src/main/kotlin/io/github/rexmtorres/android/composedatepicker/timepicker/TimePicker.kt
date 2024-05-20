package io.github.rexmtorres.android.composedatepicker.timepicker

import android.text.format.DateFormat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.rexmtorres.android.composedatepicker.component.SwipeLazyColumn
import io.github.rexmtorres.android.composedatepicker.extension.noRippleClickable
import io.github.rexmtorres.android.composedatepicker.extension.toDp
import io.github.rexmtorres.android.composedatepicker.theme.Size.extraLarge
import io.github.rexmtorres.android.composedatepicker.theme.Size.medium
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.TimePickerTime
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import io.github.rexmtorres.android.composedatepicker.timepicker.ui.model.TimePickerConfiguration
import io.github.rexmtorres.android.composedatepicker.timepicker.ui.viewmodel.TimePickerViewModel
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Composes a time picker.
 *
 * @param modifier [Modifier] to be applied to the time picker.
 * @param is24Hour Whether to use 24-hour format or not.
 * @param uppercaseAmPm Whether to capitalize AM/PM or not.
 * @param minuteGap The gap between minutes.
 * @param time The initial time.
 * @param configuration [TimePickerConfiguration] that controls the appearance of the date picker.
 * @param id Unique identifier for the time picker.  This is important when displaying multiple
 * time pickers in the same screen.
 * @param locale The locale to use for displaying the AM/PM text.
 * @param onTimeSelected Callback when the time is selected.
 */
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    is24Hour: Boolean? = null,
    uppercaseAmPm: Boolean = false,
    minuteGap: MinuteGap = MinuteGap.ONE,
    time: TimePickerTime = TimePickerTime.currentTime,
    configuration: TimePickerConfiguration = TimePickerConfiguration.Builder().build(),
    id: Int = 1,
    locale: Locale = Locale.getDefault(),
    onTimeSelected: (TimePickerTime) -> Unit,
) {
    val viewModel: TimePickerViewModel = viewModel(key = "TimePickerViewModel$id")
    viewModel.setLocale(locale)

    val is24: Boolean = is24Hour ?: DateFormat.is24HourFormat(LocalContext.current)
    val timePickerUiState =
        viewModel.getUiStateTimeProvided(time, minuteGap, is24, locale)
    val uiState by viewModel.uiState.observeAsState(timePickerUiState)

    LaunchedEffect(key1 = Unit) {
        viewModel.updateUiState(time, minuteGap, is24, locale)
        viewModel.getSelectedTime()?.also(onTimeSelected)
    }

    TimePickerView(
        modifier = modifier,
        hours = uiState.hours,
        selectedHourIndex = uiState.selectedHourIndex,
        onSelectedHourIndexChange = {
            viewModel.updateSelectedHourIndex(it)
        },
        minutes = uiState.minutes,
        selectedMinuteIndex = uiState.selectedMinuteIndex,
        onSelectedMinuteIndexChange = {
            viewModel.updateSelectedMinuteIndex(it)
        },
        timesOfDay = uiState.timesOfDay,
        capitalizeTimesOfDay = uppercaseAmPm,
        locale = locale,
        selectedTimeOfDayIndex = uiState.selectedTimeOfDayIndex,
        onSelectedTimeOfDayIndexChange = {
            viewModel.updateSelectedTimeOfDayIndex(it)
        },
        is24Hour = uiState.is24Hour,
        configuration = configuration,
        onScrollingStopped = {
            viewModel.getSelectedTime()?.also(onTimeSelected)
        }
    )
}

@Composable
private fun TimePickerView(
    modifier: Modifier = Modifier,
    hours: List<String>,
    selectedHourIndex: Int,
    onSelectedHourIndexChange: (Int) -> Unit,
    minutes: List<String>,
    selectedMinuteIndex: Int,
    onSelectedMinuteIndexChange: (Int) -> Unit,
    timesOfDay: List<String>,
    capitalizeTimesOfDay: Boolean,
    locale: Locale,
    selectedTimeOfDayIndex: Int,
    onSelectedTimeOfDayIndexChange: (Int) -> Unit,
    is24Hour: Boolean,
    configuration: TimePickerConfiguration,
    onScrollingStopped: () -> Unit,
) {
    var height by remember { mutableStateOf(configuration.height) }

    val ampm = remember(timesOfDay, capitalizeTimesOfDay, locale) {
        if (capitalizeTimesOfDay) {
            timesOfDay.map { it.uppercase(locale) }
        } else {
            timesOfDay.map { it.lowercase(locale) }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onGloballyPositioned {
                if (it.size.height == 0) return@onGloballyPositioned
                height = it.size.height.toDp() // Update the height
            },
    ) {
        Box(modifier = Modifier.fillMaxWidth())
        Box(
            modifier = Modifier
                .padding(horizontal = medium)
                .fillMaxWidth()
                .height(configuration.selectedTimeAreaHeight)
                .background(
                    color = configuration.selectedTimeAreaColor,
                    shape = configuration.selectedTimeAreaShape
                )
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SwipeLazyColumn(
                modifier = Modifier.weight(if (is24Hour) 0.5f else 0.4f),
                selectedIndex = selectedHourIndex,
                onSelectedIndexChange = onSelectedHourIndexChange,
                items = hours,
                alignment = Alignment.CenterEnd,
                configuration = configuration,
                height = height,
                onScrollingStopped = onScrollingStopped
            )

            SwipeLazyColumn(
                modifier = Modifier.weight(if (is24Hour) 0.5f else 0.2f),
                selectedIndex = selectedMinuteIndex,
                onSelectedIndexChange = onSelectedMinuteIndexChange,
                items = minutes,
                textAlign = if (is24Hour) TextAlign.Start else TextAlign.Center,
                alignment = if (is24Hour) Alignment.CenterStart else Alignment.Center,
                configuration = configuration,
                height = height,
                onScrollingStopped = onScrollingStopped
            )

            if (!is24Hour) {
                SwipeLazyColumn(
                    modifier = Modifier.weight(0.4f),
                    selectedIndex = selectedTimeOfDayIndex,
                    onSelectedIndexChange = onSelectedTimeOfDayIndexChange,
                    items = ampm,
                    alignment = Alignment.CenterStart,
                    configuration = configuration,
                    height = height,
                    isScrollingToSelectedItemEnabled = true,
                    onScrollingStopped = onScrollingStopped
                )
            }
        }
    }
}

@Composable
private fun SwipeLazyColumn(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    items: List<String>,
    textAlign: TextAlign = TextAlign.End,
    alignment: Alignment = Alignment.CenterStart,
    configuration: TimePickerConfiguration,
    isScrollingToSelectedItemEnabled: Boolean = false,
    height: Dp,
    onScrollingStopped: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isAutoScrolling by remember { mutableStateOf(false) }
    val listState = rememberLazyListState(selectedIndex)

    SwipeLazyColumn(
        modifier = modifier,
        selectedIndex = selectedIndex,
        onSelectedIndexChange = onSelectedIndexChange,
        isAutoScrolling = isAutoScrolling,
        height = height,
        isScrollingToSelectedItemEnabled = isScrollingToSelectedItemEnabled,
        numberOfRowsDisplayed = configuration.numberOfTimeRowsDisplayed,
        listState = listState,
        onScrollingStopped = {
            isAutoScrolling = false
            onScrollingStopped()
        }
    ) {
        // we add some empty rows at the beginning and end of list to make it feel that it is a center focused list
        val count = items.size + configuration.numberOfTimeRowsDisplayed - 1

        items(count) {
            SliderItem(
                value = it,
                selectedIndex = selectedIndex,
                items = items,
                configuration = configuration,
                alignment = alignment,
                textAlign = textAlign,
                height = height,
                onItemClick = { index ->
                    coroutineScope.launch {
                        isAutoScrolling = true
                        onSelectedIndexChange(index)
                        listState.animateScrollToItem(index)
                        isAutoScrolling = false
                        onScrollingStopped()
                    }
                }
            )
        }
    }
}

@Composable
private fun SliderItem(
    value: Int,
    selectedIndex: Int,
    items: List<String>,
    onItemClick: (Int) -> Unit,
    alignment: Alignment,
    configuration: TimePickerConfiguration,
    height: Dp,
    textAlign: TextAlign,
) {
    // this gap variable helps in maintaining list as center focused list
    val gap = configuration.numberOfTimeRowsDisplayed / 2
    val isSelected = value == selectedIndex + gap

    val scale by animateFloatAsState(
        targetValue = if (isSelected) {
            configuration.selectedTimeScaleFactor
        } else {
            1f
        },
        label = "SliderItem.scale"
    )

    Box(
        modifier = Modifier
            .height(height / configuration.numberOfTimeRowsDisplayed)
            .padding(
                start = if (alignment == Alignment.CenterStart) extraLarge else 0.dp,
                end = if (alignment == Alignment.CenterEnd) extraLarge else 0.dp
            )
    ) {
        if (value >= gap && value < items.size + gap) {
            Box(modifier = Modifier
                .fillMaxSize()
                .noRippleClickable {
                    onItemClick(value - gap)
                }) {
                Text(
                    text = items[value - gap],
                    modifier = Modifier
                        .align(alignment)
                        .scale(scale),
                    style = if (isSelected) {
                        configuration.selectedTimeTextStyle
                    } else {
                        configuration.timeTextStyle
                    },
                    textAlign = textAlign
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTimePicker() {
    var currentTime by remember {
        mutableStateOf(TimePickerTime.currentTime)
    }

    var confirmed by remember {
        mutableStateOf(false)
    }

    if (!confirmed) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                Button(
                    onClick = {
                        confirmed = true
                    }
                ) {
                    Text(text = "OK")
                }
            },
            text = {
                TimePicker(
                    is24Hour = false,
                    minuteGap = MinuteGap.THIRTY,
                    time = currentTime,
                    uppercaseAmPm = true,
                    onTimeSelected = { time ->
                        currentTime = time
                    }
                )
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                Button(
                    onClick = {
                        confirmed = false
                    }
                ) {
                    Text(text = "Close")
                }
            },
            text = {
                Text(text = "Time: ${currentTime.toDate(discardDate = true)}")
            }
        )
    }
}
