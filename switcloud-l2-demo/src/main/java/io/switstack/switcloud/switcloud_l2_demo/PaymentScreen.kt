package io.switstack.switcloud.switcloud_l2_demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.utils.findActivity
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2Exception
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = viewModel(),
    amount: String,
    onPaymentSuccess: (String) -> Unit,
    onPaymentFailed: () -> Unit,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current

    //val currentOnTimeout by rememberUpdatedState(onTimeout)
    //val paymentUiState = paymentViewModel.uiState.collectAsState()

    PaymentScreenContent(amount, onCancelClick)

    LaunchedEffect(Unit) {
        // Small delay to ensure the UI is rendered before starting payment processing
        delay(500)

        try {
            context.findActivity()?.let {
                // update paymentUiState
                onPaymentSuccess(paymentViewModel.processPayment(it, amount))
            }
        } catch (e: SwitcloudL2Exception) {
            //TODO log error
            // update paymentUiState
            onPaymentFailed()
        }
    }
}

@Composable
fun PaymentScreenContent(amount: String, onCancelClick: () -> Unit) {
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
                    color = Color(0xFF000080), // Navy Blue
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Shopping Cart",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$$amount",
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_contactless),
                contentDescription = "EMVCo contactless logo"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Tap here to pay")
        }

        Spacer(modifier = Modifier.weight(1f))

        Column {
            Action(buttonText = "Cancel", onClick = onCancelClick)
            Footer()
        }
    }
}

@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreenContent("1000") { }
}