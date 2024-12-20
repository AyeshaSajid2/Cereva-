package cereva.utills

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import cereva.MainScreens.DetailScreen
import cereva.MainScreens.HomePage

@Composable
fun MyAppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current // Access the context here

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController, context = context)
        }
        composable("detail") { DetailScreen() }
    }
}
