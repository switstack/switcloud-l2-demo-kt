package io.switstack.switcloud.switcloud_l2_demo

import LockScreenOrientation
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorModeEnum.CONNECTED
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorModeEnum.STANDALONE
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorTargetEnum.QUALCOMM
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorMode
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget

class MainActivity : AppCompatActivity() {

    val paymentViewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            println("${Build.BRAND} ${Build.PRODUCT}")
            println("${Build.BRAND.uppercase()}_${Build.PRODUCT.uppercase()}")
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val inserts = WindowCompat.getInsetsController(window, window.decorView)
        inserts.hide(android.view.WindowInsets.Type.systemBars())

        paymentViewModel.setupSwitcloudL2(this)
        setContent {
            Switcloudl2demoktTheme {
                // Call the orientation locker here
                LockScreenOrientation()

                MyApp(paymentViewModel)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val widthInch = with(newBase.resources.configuration) { screenWidthDp / densityDpi }
        super.attachBaseContext(
            // in case of a large screen width in qualcomm and standalone configuration
            // the dpi will be set at 240 to display with a nice arrangement
            if (getFlavorTarget() == QUALCOMM && getFlavorMode() == STANDALONE && widthInch > 10) {
                newBase.createConfigurationContext(
                    newBase.resources.configuration.apply {
                        densityDpi = 240
                    }
                )
            } else {
                newBase
            }
        )
    }

    override fun onDestroy() {
        paymentViewModel.cleanupSwitcloudL2()
        super.onDestroy()
    }
}

@Composable
fun MyApp(paymentViewModel: PaymentViewModel) {
    val navController = rememberNavController()
    val startDestination = when (getFlavorMode()) {
        CONNECTED -> "shopping_cart"
        STANDALONE -> "payment_entry"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = "payment_flow"
    ) {
        composable("shopping_cart") {
            paymentViewModel.resetPaymentState()
            ShoppingCartScreen { total ->
                navController.navigate("payment/$total") {
                    popUpTo("shopping_cart") { inclusive = false }
                }
            }
        }

        composable("payment_entry") {
            paymentViewModel.resetPaymentState()
            AmountEntryScreen() { total ->
                navController.navigate("payment/$total") {
                    popUpTo("payment_entry") { inclusive = false }
                }
            }
        }

        composable(
            "payment/{amount}",
            arguments = listOf(navArgument("amount") { type = NavType.StringType })
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            PaymentScreen(
                paymentViewModel = paymentViewModel,
                amount = amount,
                onPinRequired = {
                    navController.navigate("pin_entry/$amount") {
                        popUpTo("payment/$amount") { inclusive = true }
                    }
                },
                onPaymentVerdict = { success, tlvStream ->
                    navController.navigate("payment_ticket/$success?tlvStream=$tlvStream") {
                        // Pop up to the start screen to prevent going back to payment
                        popUpTo(startDestination) { inclusive = false }
                    }
                    paymentViewModel.resetPaymentState()
                },
                onBackToPreviousClick = {
                    navController.navigate(startDestination)
                    paymentViewModel.resetPaymentState()
                },
                onCancelClick = {
                    paymentViewModel.cancelPayment()
                }
            )
        }

        composable(
            "pin_entry/{amount}",
            arguments = listOf(navArgument("amount") { type = NavType.StringType })
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"

            PinEntryScreen(
                paymentViewModel = paymentViewModel,
                onPinVerified = {
                    navController.navigate("payment/$amount") {
                        // Pop up to the start screen to prevent going back to payment
                        popUpTo(startDestination) { inclusive = true }
                    }
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
                navController.navigate(startDestination) {
                    // Pop up to the start screen to prevent going back to payment
                    popUpTo(startDestination) { inclusive = true }
                }
            }
        }
    }
}
