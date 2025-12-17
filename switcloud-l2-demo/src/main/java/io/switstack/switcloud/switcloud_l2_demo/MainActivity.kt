package io.switstack.switcloud.switcloud_l2_demo

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorEnum
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorEnum.QCOM
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorEnum.STANDALONE
import java.util.Locale.getDefault

class MainActivity : AppCompatActivity() {

    val paymentViewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG) {
            println("${Build.BRAND} ${Build.PRODUCT}")
            println("${Build.BRAND.uppercase()}_${Build.PRODUCT.uppercase()}")
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        paymentViewModel.setupSwitcloudL2(this)
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
    val startDestination = when (FlavorEnum.valueOf(BuildConfig.FLAVOR_version.uppercase(getDefault()))) {
        QCOM -> "shopping_cart"
        STANDALONE -> "payment_entry"
    }

    NavHost(
        navController = navController,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent),
        startDestination = startDestination,
        route = "payment_flow"
    ) {
        composable("shopping_cart") {
            ShoppingCartScreen { total ->
                navController.navigate("payment/$total") {
                    popUpTo("shopping_cart") { inclusive = true }
                }
            }
        }

        composable("payment_entry") {
            AmountEntryScreen() { total ->
                navController.navigate("payment/$total") {
                    popUpTo("payment_entry") { inclusive = true }
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
                isShoppingCart = startDestination == "shopping_cart",
                onPinRequired = {
                    navController.navigate("pin_entry/$amount") {
                        popUpTo("payment/$amount") { inclusive = false }
                    }
                },
                onPaymentVerdict = { success, tlvStream ->
                    paymentViewModel.resetPaymentState()
                    navController.navigate("payment_ticket/$success?tlvStream=$tlvStream") {
                        // Pop up to the start screen to prevent going back to payment
                        popUpTo(startDestination) { inclusive = false }
                    }
                },
                onBackToPreviousClick = {
                    paymentViewModel.resetPaymentState()
                    navController.navigate(startDestination)
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
                onPinValidationClick = { navController.navigate("payment/$amount") }
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
                navController.navigate(startDestination)
            }
        }
    }
}
