package io.switstack.switcloud.switcloud_l2_demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
    NavHost(
        navController = navController,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent),
        startDestination = "shopping_cart"
    ) {
        composable("shopping_cart") {
            ShoppingCartScreen { total ->
                navController.navigate("payment/$total") {
                    popUpTo("shopping_cart") { inclusive = true }
                }
            }
        }

        composable(
            "payment/{amount}",
            arguments = listOf(navArgument("amount") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("amount")?.let {
                PaymentScreen(
                    amount = it,
                    onPaymentSuccess = { tlvStream ->
                        navController.navigate("payment_ticket/true?tlvStream=$tlvStream") {
                            // Pop up to the shopping cart screen to prevent going back to payment
                            popUpTo("shopping_cart") { inclusive = false }
                        }
                    },
                    onPaymentFailed = {
                        navController.navigate("payment_ticket/false") {
                            // Pop up to the shopping cart screen to prevent going back to payment
                            popUpTo("shopping_cart") { inclusive = false }
                        }
                    }
                ) {
                    navController.navigate("shopping_cart")
                }
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
                success = success,
                tlvStream = tlvStream
            ) {
                navController.navigate("shopping_cart")
            }
        }
    }
}
