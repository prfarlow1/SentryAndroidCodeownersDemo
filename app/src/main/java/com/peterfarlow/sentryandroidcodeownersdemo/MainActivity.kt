@file:OptIn(ExperimentalMaterial3Api::class)

package com.peterfarlow.sentryandroidcodeownersdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.peterfarlow.detail.DetailScreen
import com.peterfarlow.sentryandroidcodeownersdemo.ui.theme.SentryAndroidCodeownersDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SentryAndroidCodeownersDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

object Route {
    const val HOME = "home"
    const val DETAIL = "details"
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.HOME) {
        composable(Route.HOME) {
            HomeScreen { cat ->
                navController.navigate("${Route.DETAIL}/${cat.id}")
            }
        }
        composable(
            route = "${Route.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(requireNotNull(backStackEntry.arguments?.getString("id"))) {
                navController.popBackStack()
            }
        }
    }
}



