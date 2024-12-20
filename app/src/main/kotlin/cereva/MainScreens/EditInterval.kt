package cereva.MainScreens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.app.TimePickerDialog
import android.widget.TimePicker
import cereva.ui.theme.DarkGreen
import cereva.ui.theme.LightGreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditIntervalsDialog(
    context: Context,
    intervals: MutableList<Map<String, Any>>,
    isMultipleIntervalsEnabled: Boolean,
    setIsMultipleIntervalsEnabled: (Boolean) -> Unit,
    onSaveIntervals: () -> Unit,
    onCancel: () -> Unit
) {
    var updatedIntervals by remember { mutableStateOf(intervals) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var selectedStartTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedEndTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }
    var intervalBeingEditedIndex by remember { mutableStateOf(-1) }

    // Time Picker Dialogs
    if (showStartTimePicker) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedStartTime = LocalTime.of(hourOfDay, minute)
                showStartTimePicker = false
                showEndTimePicker = true
            },
            selectedStartTime.hour,
            selectedStartTime.minute,
            true
        )
        LaunchedEffect(Unit) {
            timePickerDialog.show()
        }
    }

    if (showEndTimePicker) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedEndTime = LocalTime.of(hourOfDay, minute)
                showEndTimePicker = false
                if (intervalBeingEditedIndex == -1) {
                    // Adding new interval
                    updatedIntervals.add(mapOf("start" to selectedStartTime, "end" to selectedEndTime))
                    println("Saved interval: Start - $selectedStartTime, End - $selectedEndTime")
                } else {
                    // Editing existing interval
                    updatedIntervals[intervalBeingEditedIndex] = mapOf("start" to selectedStartTime, "end" to selectedEndTime)
                    println("Updated interval: Start - $selectedStartTime, End - $selectedEndTime")
                }
            },
            selectedEndTime.hour,
            selectedEndTime.minute,
            true
        )
        LaunchedEffect(Unit) {
            timePickerDialog.show()
        }
    }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                "Intervals",
                color = DarkGreen // Light accent color for title
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                // Displaying intervals
                updatedIntervals.forEachIndexed { index, interval ->
                    val start = interval["start"] as LocalTime
                    val end = interval["end"] as LocalTime
                    ListItem(
                        headlineContent = {
                            Text("Start: ${start.format(DateTimeFormatter.ofPattern("hh:mm a"))} - End: ${end.format(DateTimeFormatter.ofPattern("hh:mm a"))}", color = Color.White)
                        },
                        modifier = Modifier.clickable {
                            intervalBeingEditedIndex = index
                            selectedStartTime = start
                            selectedEndTime = end
                            showStartTimePicker = true
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Switch for enabling/disabling multiple intervals
                Switch(
                    checked = isMultipleIntervalsEnabled,
                    onCheckedChange = { value ->
                        setIsMultipleIntervalsEnabled(value)
                        if (!value) {
                            updatedIntervals = updatedIntervals.take(1).toMutableList() // Keep only the first interval when multiple intervals are disabled
                            println("Multiple intervals disabled. Keeping only the first interval.")
                        }
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFBB86FC), uncheckedThumbColor = Color.Gray)
                )

                // Show button to add interval if multiple intervals are enabled
                if (isMultipleIntervalsEnabled) {
                    Button(
                        onClick = {
                            if (updatedIntervals.size < 3) {
                                // Show time picker for adding a new interval
                                intervalBeingEditedIndex = -1 // Indicate new interval
                                showStartTimePicker = true
                            } else {
                                Toast.makeText(context, "You can only add up to 3 intervals", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB86FC)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Interval", color = Color.Black)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (validateIntervals(updatedIntervals)) {
                    // Save intervals to SharedPreferences
                    saveIntervalsToSharedPreferences(context, updatedIntervals)
                    onSaveIntervals()
                    Toast.makeText(context, "Intervals saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please ensure all intervals are valid", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Save", color = LightGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

// Function to validate if intervals are valid (start time is before end time)
@RequiresApi(Build.VERSION_CODES.O)
fun validateIntervals(intervals: List<Map<String, Any>>): Boolean {
    return intervals.all {
        val start = it["start"] as LocalTime
        val end = it["end"] as LocalTime
        start.isBefore(end)
    }
}

// Function to save intervals to SharedPreferences
@RequiresApi(Build.VERSION_CODES.O)
fun saveIntervalsToSharedPreferences(context: Context, intervals: List<Map<String, Any>>) {
    val sharedPreferences = context.getSharedPreferences("IntervalsPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Convert each interval to Long (nanoOfDay) for storing
    intervals.forEachIndexed { index, interval ->
        val startTime = interval["start"] as LocalTime
        val endTime = interval["end"] as LocalTime
        editor.putLong("start_time_$index", startTime.toNanoOfDay())
        editor.putLong("end_time_$index", endTime.toNanoOfDay())
    }
    editor.apply()
    println("Intervals saved to SharedPreferences: $intervals")
}

// Preview function
@Preview
@Composable
fun PreviewEditIntervalsDialog() {
    val intervals = mutableListOf<Map<String, Any>>(
        mapOf("start" to LocalTime.now(), "end" to LocalTime.now().plusHours(1))
    )
    var isMultipleIntervalsEnabled by remember { mutableStateOf(true) }

    EditIntervalsDialog(
        context = LocalContext.current,
        intervals = intervals,
        isMultipleIntervalsEnabled = isMultipleIntervalsEnabled,
        setIsMultipleIntervalsEnabled = { isMultipleIntervalsEnabled = it },
        onSaveIntervals = { },
        onCancel = { }
    )
}
