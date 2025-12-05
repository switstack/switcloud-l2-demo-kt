package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@Composable
fun PaymentScreen(paymentViewModel: PaymentViewModel,
                  amount: String,
                  onPaymentSuccess: (String) -> Unit,
                  onPaymentFailed: () -> Unit,
                  onCancelClick: () -> Unit
) {
    val context = LocalContext.current

    val uiState by paymentViewModel.uiState.collectAsStateWithLifecycle()

    PaymentScreenContent(amount, uiState.initialized, onCancelClick)

    LaunchedEffect(uiState.initialized) {
        if (uiState.initialized) {
            paymentViewModel.processPayment(amount)
        }
    }
}

@Composable
fun PaymentScreenContent(amount: String, ready: Boolean, onCancelClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Shopping Cart",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$$amount",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                !ready -> {
                    CircularProgressIndicator(modifier = Modifier.size(75.dp))
                    Text(modifier = Modifier.padding(top = 32.dp),
                         text = "Loading in progress...",
                         color = MaterialTheme.colorScheme.onBackground,
                         style = MaterialTheme.typography.titleLarge)
                }

                ready -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_contactless),
                        contentDescription = "EMVCo contactless logo",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.width(150.dp)
                    )
                    Text(text = "Tap here to pay",
                         color = MaterialTheme.colorScheme.onBackground,
                         style = MaterialTheme.typography.titleLarge)
                }
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        Column {
            Action(buttonText = "Cancel",
                   buttonType = ButtonType.Tonal,
                   onClick = onCancelClick)
            Footer()
        }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000", false) { }
    }
}