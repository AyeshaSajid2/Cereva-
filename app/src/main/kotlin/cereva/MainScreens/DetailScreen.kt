package cereva.MainScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cereva.utills.PreferencesManager
import cereva.utills.PreferencesManager2

@Composable
fun DetailScreen() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // Fetch selected days, frequency, and intervals
    val selectedDays = remember { preferencesManager.getSelectedDays() }
    val frequency = remember { preferencesManager.getFrequency() }
    val intervals = remember { preferencesManager.getIntervals() }

    // Log the values to debug
    Log.d("DetailScreen", "Selected Days: $selectedDays")
    Log.d("DetailScreen", "Frequency: $frequency")
    Log.d("DetailScreen", "Intervals: $intervals")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Days Container
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Days:",
                    style = TextStyle(fontSize = 20.sp, color = Color.White)
                )
                // Display the selected days as a comma-separated string
                BasicText(
                    text = if (selectedDays.isNotEmpty()) selectedDays.joinToString(", ") else "No days selected",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                )
            }
        }

        // Frequency Container
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Frequency:",
                    style = TextStyle(fontSize = 20.sp, color = Color.White)
                )
                BasicText(
                    text = "$frequency times per day",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                )
            }
        }

        // Slot 1, Slot 2, Slot 3 Containers
        intervals.forEachIndexed { index, interval ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Slot ${index + 1}:",
                        style = TextStyle(fontSize = 20.sp, color = Color.White)
                    )
                    BasicText(
                        text = "Start: ${interval["start"]}, End: ${interval["end"]}",
                        style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}
