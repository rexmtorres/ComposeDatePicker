package io.github.rexmtorres.android.composedatepicker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rexmtorres.android.composedatepicker.datepicker.DatePicker
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.DatePickerDate
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.SelectionLimiter
import io.github.rexmtorres.android.composedatepicker.datepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.timepicker.TimePicker
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.TimePickerTime
import io.github.rexmtorres.android.composedatepicker.timepicker.data.model.toDate
import io.github.rexmtorres.android.composedatepicker.timepicker.enums.MinuteGap
import io.github.rexmtorres.android.composedatepicker.ui.theme.ComposeDatePickerTheme
import java.text.DateFormat
import java.text.SimpleDateFormat
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = selectedDate)

        DatePicker(
            locale = Locale.JAPANESE,
            selectionLimiter = SelectionLimiter(
                fromDate = DatePickerDate.currentDate,
                //toDate = DatePickerDate.currentDate.addDays(4)
            ),
            onDateSelected = { year, month, day ->
                val date = DatePickerDate(
                    year = year,
                    month = month,
                    day = day
                ).toDate(discardTime = true)

                selectedDate = dateFormatter.format(date)
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(32.dp)
        )

        Text(text = selectedTime)

        TimePicker(
            locale = Locale.KOREAN,
            is24Hour = false,
            minuteGap = MinuteGap.ONE,
            onTimeSelected = { hour, minute ->
                val currentTime = TimePickerTime(
                    hour = hour,
                    minute = minute
                ).toDate(discardDate = true)

                selectedTime = timeFormatter.format(currentTime)
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