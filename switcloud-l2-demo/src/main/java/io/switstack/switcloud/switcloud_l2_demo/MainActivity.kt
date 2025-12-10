package io.switstack.switcloud.switcloud_l2_demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.findActivity

class MainActivity : AppCompatActivity() {

    val paymentViewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        paymentViewModel.initializeSwitcloudL2(this)
        setContent {
            Switcloudl2demoktTheme {
                MyApp(paymentViewModel)
            }
        }
    }

    override fun onDestroy() {
        paymentViewModel.cleanupSwitcloudL2()
        super.onDestroy()
    }
}

@Composable
fun MyApp(paymentViewModel: PaymentViewModel) {
    val navController = rememberNavController()
    LocalContext.current.findActivity()?.let { activity ->
        NavHost(
            navController = navController,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent),
            startDestination = "shopping_cart",
            route = "payment_flow"
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
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("payment_flow")
                }

                val amount = backStackEntry.arguments?.getString("amount") ?: "0"

                PaymentScreen(paymentViewModel = paymentViewModel,
                              amount = amount,
                              onPaymentVerdict = { success, tlvStream ->
                                  paymentViewModel.resetPaymentState()
                                  navController.navigate("payment_ticket/$success?tlvStream=$tlvStream") {
                                      // Pop up to the shopping cart screen to prevent going back to payment
                                      popUpTo("shopping_cart") { inclusive = false }
                                  }
                              },
                              onBackToCartClick = {
                                  paymentViewModel.resetPaymentState()
                                  navController.navigate("shopping_cart")
                              },
                              onCancelClick = {
                                  paymentViewModel.cancelPayment()
                                  navController.navigate("shopping_cart")
                              }
                )
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
}
