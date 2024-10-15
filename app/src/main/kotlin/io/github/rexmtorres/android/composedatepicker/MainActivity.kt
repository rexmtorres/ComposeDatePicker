package io.github.rexmtorres.android.composedatepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rexmtorres.android.composedatepicker.datepicker.DatePicker
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.DatePickerDate
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.SelectionLimiter
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.datepicker.ui.model.DatePickerConfiguration
import io.github.rexmtorres.android.composedatepicker.theme.Size
import io.github.rexmtorres.android.composedatepicker.theme.Size.medium
import io.github.rexmtorres.android.composedatepicker.timepicker.TimePicker
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import io.github.rexmtorres.android.composedatepicker.ui.theme.ComposeDatePickerTheme
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDatePickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    val dateFormatter = remember {
        SimpleDateFormat.getDateInstance(DateFormat.FULL)
    }

    val timeFormatter = remember {
        SimpleDateFormat.getTimeInstance(DateFormat.SHORT)
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedTime by remember {
        mutableStateOf("")
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = selectedDate)

        DatePicker(
            locale = Locale.US,
            selectionLimiter = SelectionLimiter.fromDatePickerDates(
                fromDate = DatePickerDate.currentDate,
                //toDate = DatePickerDate.currentDate.addDays(4)
                disabledDates = disabledDates
            ),
            onDateSelected = { date ->
                println("onDateSelected: date = $date")
                selectedDate = dateFormatter.format(date.toDate(discardTime = true))
            },
            onMonthPageChange = { first, last ->
                println("onMonthPageChange: first = $first | last = $last")
            },
            configuration = DatePickerConfiguration
                .Builder()
                .uppercaseHeaderText(true)
                .uppercaseDaysNameText(true)
                .monthYearHeader { month, year ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = Size.extraLarge)
                    ) {
                        Text(
                            text = "$month $year"
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = stringResource(id = R.string.rightArrow),
                            modifier = Modifier
                                .padding(start = medium)
                        )
                    }
                }
                .build()
        )

        HorizontalDivider(
            modifier = Modifier.padding(32.dp)
        )

        Text(text = selectedTime)

        TimePicker(
            locale = Locale.KOREAN,
            is24Hour = false,
            uppercaseAmPm = true,
            minuteGap = MinuteGap.ONE,
            onTimeSelected = { time ->
                selectedTime = timeFormatter.format(time.toDate(discardDate = true))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    ComposeDatePickerTheme {
        Content()
    }
}