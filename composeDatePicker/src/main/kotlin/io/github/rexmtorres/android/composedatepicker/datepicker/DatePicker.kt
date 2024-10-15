package io.github.rexmtorres.android.composedatepicker.datepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.rexmtorres.android.composedatepicker.R
import io.github.rexmtorres.android.composedatepicker.component.AnimatedFadeVisibility
import io.github.rexmtorres.android.composedatepicker.component.SwipeLazyColumn
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Constant
import io.github.rexmtorres.android.composedatepicker.datepicker.data.Days
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.DatePickerDate
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.Month
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.SelectionLimiter
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.datepicker.ui.model.DatePickerConfiguration
import io.github.rexmtorres.android.composedatepicker.datepicker.ui.model.DatePickerUiState
import io.github.rexmtorres.android.composedatepicker.datepicker.ui.viewmodel.DatePickerViewModel
import io.github.rexmtorres.android.composedatepicker.extension.noRippleClickable
import io.github.rexmtorres.android.composedatepicker.extension.spToDp
import io.github.rexmtorres.android.composedatepicker.extension.toDp
import io.github.rexmtorres.android.composedatepicker.theme.Size.medium
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

/**
 * Composes a date picker.
 *
 * @param modifier [Modifier] to be applied to the date picker.
 * @param date The initial date.
 * @param selectionLimiter [SelectionLimiter] that indicates the range of selectable dates.
 * @param configuration [DatePickerConfiguration] that controls the appearance of the date picker.
 * @param id Unique identifier for the date picker.  This is important when displaying multiple
 * date pickers in the same screen.
 * @param locale The locale to use for displaying the month and day names.
 * @param onMonthPageChange Callback invoked when the month changes as a result of clicking on the
 * previous/next month arrows or selecting the month/year from the header.
 * @param onDateSelected Callback invoked when a date is selected.
 */
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    date: DatePickerDate = DatePickerDate.currentDate,
    selectionLimiter: SelectionLimiter = SelectionLimiter.unlimited(),
    configuration: DatePickerConfiguration = DatePickerConfiguration.Builder().build(),
    id: Int = 1,
    locale: Locale = Locale.getDefault(),
    onMonthPageChange: (firstDayOfMonth: DatePickerDate, lastDayOfMonth: DatePickerDate) -> Unit = { _, _ -> },
    onDateSelected: (DatePickerDate) -> Unit
) {
    val viewModel: DatePickerViewModel = viewModel(key = "DatePickerViewModel$id")
    viewModel.setLocale(locale)

    val initialYear = date.year

    val uiState by viewModel.uiState.observeAsState(
        DatePickerUiState(
            locale = locale,
            selectedYear = initialYear,
            selectedMonth = Constant.getMonths(initialYear, locale)[date.month],
            selectedDayOfMonth = date.day
        )
    )

    LaunchedEffect(key1 = uiState.currentVisibleMonth, key2 = uiState.selectedYear) {
        val month = uiState.currentVisibleMonth

        val firstDayOfMonth = DatePickerDate(
            year = uiState.selectedYear,
            month = month.number,
            day = 1
        )

        val lastDayOfMonth = DatePickerDate(
            year = uiState.selectedYear,
            month = month.number,
            day = month.numberOfDays
        )

        onMonthPageChange(firstDayOfMonth, lastDayOfMonth)
    }

    // Key is Unit because I want this to run only once not every time when is composable is recomposed.
    LaunchedEffect(key1 = Unit) { viewModel.setDate(date) }

    var height by remember { mutableStateOf(configuration.height) }

    Box(
        modifier = modifier.onGloballyPositioned {
            if (it.size.height == 0) {
                return@onGloballyPositioned
            }

            // Update the height
            height = it.size.height.toDp() - configuration.headerHeight
        }
    ) {
        // TODO add sliding effect when next or previous arrow is pressed
        CalendarHeader(
            title = "${uiState.currentVisibleMonth.name} ${uiState.selectedYear}",
            onMonthYearClick = { viewModel.toggleIsMonthYearViewVisible() },
            onNextClick = {
                viewModel.moveToNextMonth()
            },
            onPreviousClick = { viewModel.moveToPreviousMonth() },
            isPreviousNextVisible = !uiState.isMonthYearViewVisible,
            themeColor = configuration.selectedDateBackgroundColor,
            configuration = configuration,
        )

        Box(
            modifier = Modifier
                .padding(top = configuration.headerHeight)
                .height(height)
        ) {
            AnimatedFadeVisibility(
                visible = !uiState.isMonthYearViewVisible
            ) {
                DateView(
                    currentYear = uiState.selectedYear, //initialYear,
                    currentVisibleMonth = uiState.currentVisibleMonth,
                    selectedYear = uiState.selectedYear,
                    selectedMonth = uiState.selectedMonth,
                    selectedDayOfMonth = uiState.selectedDayOfMonth,
                    selectionLimiter = selectionLimiter,
                    height = height,
                    onDaySelected = {
                        viewModel.updateSelectedDayAndMonth(it)
                        onDateSelected(
                            DatePickerDate(
                                year = uiState.selectedYear,
                                month = uiState.selectedMonth.number,
                                day = uiState.selectedDayOfMonth
                            )
                        )
                    },
                    configuration = configuration,
                    locale = locale
                )
            }

            AnimatedFadeVisibility(
                visible = uiState.isMonthYearViewVisible
            ) {
                MonthAndYearView(
                    modifier = Modifier.align(Alignment.Center),
                    selectedMonth = uiState.selectedMonthIndex,
                    onMonthChange = {
                        viewModel.updateSelectedMonthIndex(it)
                    },
                    selectedYear = uiState.selectedYearIndex,
                    onYearChange = {
                        println("onYearChange: year = $it")

                        viewModel.updateSelectedYearIndex(it)
                    },
                    years = uiState.years,
                    months = uiState.months,
                    height = height,
                    configuration = configuration
                )
            }
        }
    }

    // Call onDateSelected when composition is completed
    LaunchedEffect(key1 = Unit) {
        onDateSelected(
            DatePickerDate(
                year = uiState.selectedYear,
                month = uiState.selectedMonth.number,
                day = uiState.selectedDayOfMonth
            )
        )
    }
}

@Composable
private fun MonthAndYearView(
    modifier: Modifier = Modifier,
    selectedMonth: Int,
    onMonthChange: (Int) -> Unit,
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    years: List<String>,
    months: List<String>,
    height: Dp,
    configuration: DatePickerConfiguration
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = medium)
                .fillMaxWidth()
                .height(configuration.selectedMonthYearAreaHeight)
                .background(
                    color = configuration.selectedMonthYearAreaColor,
                    shape = configuration.selectedMonthYearAreaShape
                )
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SwipeLazyColumn(
                modifier = Modifier.weight(0.5f),
                selectedIndex = selectedMonth,
                onSelectedIndexChange = onMonthChange,
                items = months,
                height = height,
                configuration = configuration
            )

            SwipeLazyColumn(
                modifier = Modifier.weight(0.5f),
                selectedIndex = selectedYear,
                onSelectedIndexChange = onYearChange,
                items = years,
                alignment = Alignment.CenterEnd,
                height = height,
                configuration = configuration
            )
        }
    }
}

@Composable
private fun SwipeLazyColumn(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    items: List<String>,
    alignment: Alignment = Alignment.CenterStart,
    configuration: DatePickerConfiguration,
    height: Dp
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
        numberOfRowsDisplayed = configuration.numberOfMonthYearRowsDisplayed,
        listState = listState,
        onScrollingStopped = {}
    ) {
        // I add some empty rows at the beginning and end of list to make it feel that it is a center focused list
        val count = items.size + configuration.numberOfMonthYearRowsDisplayed - 1

        items(count) {
            SliderItem(
                value = it,
                selectedIndex = selectedIndex,
                items = items,
                configuration = configuration,
                alignment = alignment,
                height = height,
                onItemClick = { index ->
                    onSelectedIndexChange(index)
                    coroutineScope.launch {
                        isAutoScrolling = true
                        onSelectedIndexChange(index)
                        listState.animateScrollToItem(index)
                        isAutoScrolling = false
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
    configuration: DatePickerConfiguration,
    height: Dp
) {
    // this gap variable helps in maintaining list as center focused list
    val gap = configuration.numberOfMonthYearRowsDisplayed / 2
    val isSelected = value == selectedIndex + gap
    val scale by animateFloatAsState(
        targetValue = if (isSelected) {
            configuration.selectedMonthYearScaleFactor
        } else {
            1f
        },
        label = "SliderItem.scale",
    )

    Box(
        modifier = Modifier
            .height(height / (configuration.numberOfMonthYearRowsDisplayed))
    ) {
        if (value >= gap && value < items.size + gap) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        onItemClick(value - gap)
                    },
                contentAlignment = if (alignment == Alignment.CenterEnd) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                configuration.selectedMonthYearTextStyle.fontSize
                Box(
                    modifier = Modifier.width(
                        configuration.selectedMonthYearTextStyle.fontSize.spToDp(LocalDensity.current) * 5
                    )
                ) {
                    Text(
                        text = items[value - gap],
                        modifier = Modifier
                            .align(alignment)
                            .scale(scale),
                        style = if (isSelected) configuration.selectedMonthYearTextStyle
                        else configuration.monthYearTextStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun DateView(
    currentYear: Int,
    selectedYear: Int,
    currentVisibleMonth: Month,
    selectedDayOfMonth: Int?,
    selectionLimiter: SelectionLimiter,
    selectedMonth: Month,
    height: Dp,
    configuration: DatePickerConfiguration,
    locale: Locale,
    modifier: Modifier = Modifier,
    onDaySelected: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = modifier
    ) {
        items(
            items = Constant.days
        ) {
            DateViewHeaderItem(
                day = it,
                configuration = configuration,
                locale = locale
            )
        }

        // since I may need few empty cells because every month starts with a different day(Monday, Tuesday, ..)
        // that's way I add some number X into the count
        val count =
            currentVisibleMonth.numberOfDays + currentVisibleMonth.firstDayOfMonth.number - 1

        val topPaddingForItem = getTopPaddingForItem(
            count,
            height - configuration.selectedDateBackgroundSize * 2, // because I don't want to count first two rows
            configuration.selectedDateBackgroundSize
        )

        items(count) {
            // to create empty boxes
            if (it < currentVisibleMonth.firstDayOfMonth.number - 1) {
                return@items
            }

            DateViewBodyItem(
                value = it,
                currentYear = currentYear,
                currentVisibleMonth = currentVisibleMonth,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
                selectionLimiter = selectionLimiter,
                onDaySelected = onDaySelected,
                topPaddingForItem = topPaddingForItem,
                configuration = configuration,
            )
        }
    }
}

@Composable
private fun DateViewBodyItem(
    value: Int,
    currentYear: Int,
    currentVisibleMonth: Month,
    selectedYear: Int,
    selectedMonth: Month,
    selectedDayOfMonth: Int?,
    selectionLimiter: SelectionLimiter,
    onDaySelected: (Int) -> Unit,
    topPaddingForItem: Dp,
    configuration: DatePickerConfiguration,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(
                top = if (value < 7) {
                    0.dp // I don't want first row to have any padding
                } else {
                    topPaddingForItem
                }
            )
    ) {
        val day = value - currentVisibleMonth.firstDayOfMonth.number + 2

        val isSelected = (selectedDayOfMonth == day) &&
                (selectedMonth == currentVisibleMonth) &&
                (selectedYear == currentYear)
        val isWithinRange = selectionLimiter.isWithinRange(
            DatePickerDate(
                selectedYear,
                currentVisibleMonth.number,
                day
            )
        )

        val backgroundColor by animateColorAsState(
            targetValue = if (isSelected) {
                configuration.selectedDateBackgroundColor
            } else {
                Color.Transparent
            },
            animationSpec = tween(
                durationMillis = 400,
            ),
            label = "DateViewBodyItem.backgroundColor"
        )

        val textColor by animateColorAsState(
            targetValue = if (isSelected) {
                if (isWithinRange) {
                    configuration.selectedDateTextStyle.color
                } else {
                    configuration.disabledDateColor
                }
            } else {
                if (isWithinRange) {
                    if (value % 7 == 0) {
                        configuration.sundayTextColor
                    } else {
                        configuration.dateTextStyle.color
                    }
                } else {
                    configuration.disabledDateColor
                }
            },
            animationSpec = tween(
                durationMillis = 400,
            ),
            label = "DateViewBodyItem.textColor"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .requiredSize(configuration.selectedDateBackgroundSize)
                .clip(configuration.selectedDateBackgroundShape)
                .noRippleClickable(enabled = isWithinRange) { onDaySelected(day) }
                .background(backgroundColor)
        ) {
            Text(
                text = "$day",
                textAlign = TextAlign.Center,
                style = if (isSelected) {
                    configuration.selectedDateTextStyle.copy(color = textColor)
                } else {
                    configuration.dateTextStyle.copy(color = textColor)
                },
            )
        }
    }
}

@Composable
private fun DateViewHeaderItem(
    configuration: DatePickerConfiguration,
    day: Days,
    locale: Locale
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .size(configuration.selectedDateBackgroundSize)
    ) {
        Text(
            text = day.shortName(locale).let {
                if (configuration.uppercaseDaysNameText) {
                    it.uppercase()
                } else {
                    it
                }
            },
            textAlign = TextAlign.Center,
            style = configuration.daysNameTextStyle.copy(
                color = if (day.number == 1) {
                    configuration.sundayTextColor
                } else {
                    configuration.daysNameTextStyle.color
                }
            ),
        )
    }
}

// Not every month has same number of weeks, so to maintain the same size for calender I add padding if there are less weeks
private fun getTopPaddingForItem(
    count: Int,
    height: Dp,
    size: Dp
): Dp {
    val numberOfRowsVisible = ceil(count.toDouble() / 7) - 1
    val result =
        (height - (size * numberOfRowsVisible.toInt()) - medium) / numberOfRowsVisible.toInt()
    return if (result > 0.dp) result else 0.dp
}

@Composable
private fun CalendarHeader(
    modifier: Modifier = Modifier,
    title: String,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onMonthYearClick: () -> Unit,
    isPreviousNextVisible: Boolean,
    configuration: DatePickerConfiguration,
    themeColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(configuration.headerHeight)
    ) {
        val textColor by animateColorAsState(
            targetValue = if (isPreviousNextVisible) {
                configuration.headerTextStyle.color
            } else {
                themeColor
            },
            animationSpec = tween(durationMillis = 400, delayMillis = 100),
            label = "CalendarHeader.textColor"
        )

        Text(
            text = title.let {
                if (configuration.uppercaseHeaderText) {
                    it.uppercase()
                } else {
                    it
                }
            },
            style = configuration.headerTextStyle.copy(color = textColor),
            modifier = modifier
                .padding(start = medium)
                .noRippleClickable { onMonthYearClick() }
                .align(Alignment.CenterStart),
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            AnimatedFadeVisibility(visible = isPreviousNextVisible) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .noRippleClickable { onPreviousClick() }
                ) {
                    configuration.previousArrow()
                }
            }

            Spacer(modifier = Modifier.width(medium))

            AnimatedFadeVisibility(visible = isPreviousNextVisible) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .noRippleClickable { onNextClick() }
                ) {
                    configuration.nextArrow()
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewDatePicker() {
    var currentDate by remember {
        mutableStateOf(DatePickerDate.currentDate)
    }

    val now = Date()

    val disabledDates = remember {
        val calendar = Calendar.getInstance().apply {
            time = now
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        listOf(
            DatePickerDate(year, month, day + 2),
            DatePickerDate(year, month, day + 6),
            DatePickerDate(year, month, day + 8),
        )
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
                DatePicker(
                    configuration = DatePickerConfiguration
                        .Builder()
                        //.selectedDateBackgroundShape(RoundedCornerShape(4.dp))
                        //.height(220.dp)
                        .build(),
                    selectionLimiter = SelectionLimiter.fromDatePickerDates(
                        fromDate = DatePickerDate.currentDate,
                        //toDate = DatePickerDate.currentDate.addDays(4)
                        disabledDates = disabledDates
                    ),
                    onDateSelected = { date ->
                        currentDate = date
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
                Text(text = "Date: ${currentDate.toDate(discardTime = true)}")
            }
        )
    }
}