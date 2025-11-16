package com.example.switcloud_l2_demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Switcloudl2demoktTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "shopping_cart") {
        composable("shopping_cart") {
            ShoppingCartScreen(navController = navController)
        }

        composable(
            "payment/{amount}",
            arguments = listOf(navArgument("amount") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("amount")?.let {
                PaymentScreen(
                    navController = navController,
                    amount = it
                )
            }
        }

        composable(
            "payment_ticket/{paymentResult}",
            arguments = listOf(navArgument("paymentResult") { type = NavType.StringType })
        ) { backStackEntry ->
            PaymentTicketScreen(
                navController = navController,
                paymentResult = backStackEntry.arguments?.getString("paymentResult")
            )
        }
    }
}
