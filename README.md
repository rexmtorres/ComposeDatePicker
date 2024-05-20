# ComposeDatePicker
An Android Jetpack Compose library that provides a Composable Date Picker / Time Picker functionality.
This is a fork of [vsnappy1/ComposeDatePicker](https://github.com/vsnappy1/composedatepicker)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.rexmtorres.android/ComposeDatePicker.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.rexmtorres.android%22%20AND%20a:%ComposeDatePicker%22)

## Dependencies Setup
```kotlin
dependencies {
    //..
    implementation("io.github.rexmtorres.android:composedatepicker:1.0.1")
}
```

## Usage
Adding a date picker or time picker is incredibly easy, requiring just two lines of code.

```kotlin
DatePicker(
    onDateSelected = { year, month, day ->
    
    }
)
```
![DatePicker Gif](doc/date_picker_01.gif)
![DatePicker Screenshot 01](doc/date_picker_02.jpg)



```kotlin
TimePicker(
    onTimeSelected = { hour, minute ->

    }
)
```
![TimePicker Gif](doc/time_picker_01.gif)
![TimePicker Screenshot 01](doc/time_picker_02.jpg)


## Customization
The date and time picker offer extensive customization options, allowing users to modify the 
TextStyle, Color, Size, Shape, and other elements to align with their preferred theme.

### Date Picker
___
#### Set Custom Date

```kotlin
import java.util.Calendar

DatePicker(
    onDateSelected = { year, month, day ->

    },
    date = DatePickerDate(
        year = 2023,
        month = Calendar.JANUARY,
        day = 5
    )
)
```
Please note that the **_year_** should be within a range of <current_year> ± 100 (inclusive). Additionally, for the **_month_**, 
please keep in mind that 0 represents January, while 11 corresponds to December.  It is better to use the `java.util.Calendar` month constants (e.g.: `Calendar.JANUARY`, `Calendar.DECEMBER`) to specify the month.

#### Set Selection Limit

```kotlin
import java.util.Calendar

DatePicker(
    onDateSelected = { year, month, day ->

    },
    selectionLimiter = SelectionLimiter(
        fromDate = DatePickerDate(
            year = 2023,
            month = Calendar.APRIL,
            day = 7
        ),
        toDate = DatePickerDate(
            year = 2023,
            month = Calendar.MAY,
            day = 21
        )
    )
)
```

#### Customize the Appearance
```kotlin
DatePicker(
    modifier = Modifier.padding(16.dp),
    onDateSelected = { year, month, day ->

    },
    configuration = DatePickerConfiguration.Builder()
        .height(height = 300.dp)
        .dateTextStyle(DefaultDatePickerConfig.dateTextStyle.copy(color = Color(0xFF333333)))
        .selectedDateTextStyle(textStyle = TextStyle(Color(0xFFFFFFFF)))
        .selectedDateBackgroundColor(color = Color(0xFF64DD17))
        .build()
)
```
In addition to **_dateTextStyle_**, **_selectedDateTextStyle_**, and **_selectedDateBackgroundColor_**, there are a total of 
20 attributes available for users to customize the appearance of the date picker.

#### Set the Locale
```kotlin
import java.util.Locale

DatePicker(
    locale = Locale.CHINESE,
    onDateSelected = { year, month, day ->

    },
)
```
![DatePicker Screenshot 02](doc/date_picker_03.png)

<br>

### Time Picker
___
#### Set Custom Time
```kotlin
TimePicker(
    onTimeSelected = { hour, minute ->

    },
    time = TimePickerTime(
        hour = 12,
        minute = 45
    )
)
```

#### Set Is24Hour & MinuteGap
```kotlin
TimePicker(
    onTimeSelected = { hour, minute ->

    },
    is24Hour = true,
    minuteGap = MinuteGap.FIVE
)
```
The interval between consecutive items in the minute list is determined by the **_minuteGap_** parameter. When minuteGap is set to MinuteGap.FIVE, the minutes in the time picker will be displayed in increments of 5, such as 00, 05, 10,..., 55. The default value for minuteGap is MinuteGap.ONE, which means the minutes will be displayed in sequential order from 00 to 59.


#### Customize the Appearance
```kotlin
TimePicker(
    modifier = Modifier
        .padding(16.dp)
        .background(Color(0xFF1B5E20), RoundedCornerShape(8.dp)),
    onTimeSelected = { hour, minute ->

    },
    configuration = TimePickerConfiguration.Builder()
        .numberOfTimeRowsDisplayed(count = 5)
        .selectedTimeScaleFactor(scaleFactor = 1.4f)
        .build()
)
```
There are a total of 8 attributes available for users to customize the appearance of the time picker.

#### Set the Locale
```kotlin
import java.util.Locale

TimePicker(
    locale = Locale.KOREAN,
    onTimeSelected = { hour, minute ->

    },
)
```
![TimePicker Screenshot 02](doc/time_picker_03.png)


## Troubleshot

* If multiple date/time pickers are used, a unique **_id_** parameter should be included in the function call for each composable.

* When adjusting the height of a date/time picker, it is recommended to use **_TimePickerConfiguration.Builder().height()_** instead of **_Modifier.height()_** to ensure smooth rendering.
