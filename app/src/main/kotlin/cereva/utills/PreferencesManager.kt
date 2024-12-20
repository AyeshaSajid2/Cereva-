package cereva.utills

import android.content.Context
import android.util.Log
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

    // Get the selected days from shared preferences
    fun getSelectedDays(): List<String> {
        val selectedDays = sharedPreferences.getStringSet("selected_days", emptySet())?.toList() ?: emptyList()
        Log.d("PreferencesManager", "Fetched selected days: $selectedDays")
        return selectedDays
    }

    // Get the frequency from shared preferences
    fun getFrequency(): Int {
        val frequency = sharedPreferences.getInt("frequency", 1)  // Default to 1
        Log.d("PreferencesManager", "Fetched frequency: $frequency")
        return frequency
    }

    // Get the intervals (slots) from shared preferences
    fun getIntervals(): List<Map<String, String>> {
        val intervals = mutableListOf<Map<String, String>>()
        val keys = sharedPreferences.all.keys.filter { it.startsWith("start_time_") } // Filter keys for intervals

        keys.forEachIndexed { index, key ->
            val startNano = sharedPreferences.getLong("start_time_$index", -1L)
            val endNano = sharedPreferences.getLong("end_time_$index", -1L)

            if (startNano != -1L && endNano != -1L) {
                val startTime = LocalTime.ofNanoOfDay(startNano)
                val endTime = LocalTime.ofNanoOfDay(endNano)

                // Add the interval as a map of strings
                intervals.add(mapOf(
                    "start" to startTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    "end" to endTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                ))

                Log.d("PreferencesManager", "Fetched interval $index: Start - $startTime, End - $endTime")
            }
        }

        return intervals
    }

}
