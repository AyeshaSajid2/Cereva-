@file:OptIn(ExperimentalMaterial3Api::class)

package cereva.MainScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cereva.alarms.showNotification

@Composable
fun HomePage(navController: NavController, context: Context) {
    var selectedDays by remember { mutableStateOf(listOf<String>()) }
    var interval by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(DialogType.None) }
    var intervals by remember { mutableStateOf(mutableListOf<Map<String, Any>>()) }
    var isMultipleIntervalsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Set Reminders",
            style = TextStyle(color = Color.White, fontSize = 24.sp)
        )

        RoundedButton("Days") { isDialogOpen = DialogType.Days }
        RoundedButton("Interval") { isDialogOpen = DialogType.Interval }
        RoundedButton("Frequency") { isDialogOpen = DialogType.Frequency }

        Button(
            onClick = {
                navController.navigate("detail")
            }, // Navigation to Detail Screen
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Detail Screen")
        }

        RoundedButton("Save Reminder") {
            showNotification(context)
            isDialogOpen = DialogType.None
        }

        when (isDialogOpen) {
            DialogType.Days -> DaySelectionDialog(
                context = context,
//                selectedDays = selectedDays,
                onSave = { selectedDays = it },
                onDismiss = { isDialogOpen = DialogType.None }
            )
            DialogType.Interval -> EditIntervalsDialog(
                context = context,
                intervals = intervals,
                isMultipleIntervalsEnabled = isMultipleIntervalsEnabled,
                setIsMultipleIntervalsEnabled = { isEnabled -> isMultipleIntervalsEnabled = isEnabled },
                onSaveIntervals = {
                    Toast.makeText(context, "Intervals saved successfully", Toast.LENGTH_SHORT).show()
                    isDialogOpen = DialogType.None
                },
                onCancel = {
                    isDialogOpen = DialogType.None
                }
            )
            DialogType.Frequency -> FrequencySelectionScreen(
                context = context,
                onDismiss = { isDialogOpen = DialogType.None }
            )
            DialogType.None -> {}
            else -> {}
        }
    }
}




@Composable
fun RoundedButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .width(200.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.White, fontSize = 16.sp)
        )
    }
}

enum class DialogType {
    None, Days, Interval, Frequency, Details
}
