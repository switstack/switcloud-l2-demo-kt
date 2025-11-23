package com.example.switcloud_l2_demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

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
            "payment_ticket/{success}?tlvStream={tlvStream}",
            arguments = listOf(
                navArgument("success") { type = NavType.BoolType },
                navArgument("tlvStream") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val success = backStackEntry.arguments?.getBoolean("success") ?: false
            val tlvStream = backStackEntry.arguments?.getString("tlvStream")
            PaymentTicketScreen(
                navController = navController,
                success = success,
                tlvStream = tlvStream
            )
        }
    }
}
