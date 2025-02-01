package com.example.pic_a_day_frontend

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pic_a_day_frontend.ui.theme.PicaDayFrontendTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

class HomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicaDayFrontendTheme {
                HomePageUI()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomePageUI() {
        val navController = rememberNavController()
//        val username1 = this@HomePage.getPreferences(Context.MODE_PRIVATE).getString("username", null)
        val username = getSharedPreferences("Pic-a-Day", Context.MODE_PRIVATE).getString("username", null)
        val token = getSharedPreferences("Pic-a-Day", Context.MODE_PRIVATE).getString("token", null)
        println("Token is $token")
        val bottomTabList = listOf(
            BottomNavigationItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavigationItem(
                title = "Week",
                selectedIcon = Icons.Filled.DateRange,
                unselectedIcon = Icons.Outlined.DateRange,
            ),
            BottomNavigationItem(
                title = "Year",
                selectedIcon = Icons.Filled.DateRange,
                unselectedIcon = Icons.Outlined.DateRange
            ),
            BottomNavigationItem(
                title = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
            ),
        )
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomTabList.forEachIndexed { index, bottomNavigationItem ->
                        NavigationBarItem(
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedTabIndex = index
                                navController.navigate(bottomNavigationItem.title)
                                      },
                            icon = {
                                Icon(
                                    imageVector = if(selectedTabIndex == index) bottomNavigationItem.selectedIcon
                                        else bottomNavigationItem.unselectedIcon,
                                    contentDescription = "Selected Icon",
                                    tint = if(selectedTabIndex == index) Color.Blue else Color.Black,
                                    modifier = Modifier.size(40.dp)
                                ) },
                            label = { Text(bottomNavigationItem.title) }

                        )
                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = "Home",
            ) {
                composable("Home") {
                    HomeScreen(username!!, token!!) // Replace with your composable
                }
                composable("Week") {
                    WeekScreen() // Replace with your composable
                }
                composable("Year") {
                    YearScreen() // Replace with your composable
                }
                composable("Settings") {
                    SettingScreen() // Replace with your composable
                }
            }

        }
    }

    @Composable
    fun WeekScreen() {
        Text("Week Screen")
    }

    @Composable
    fun YearScreen() {
        Text("Year Screen")
    }

    @Composable
    fun SettingScreen() {
        Text("Settings Screen")
    }
}