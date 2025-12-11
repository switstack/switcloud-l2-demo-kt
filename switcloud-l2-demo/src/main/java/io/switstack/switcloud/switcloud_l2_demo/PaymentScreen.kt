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
import androidx.compose.material.icons.filled.Payment
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(paymentViewModel: PaymentViewModel,
                  amount: String,
                  isShoppingCart: Boolean = false,
                  onPaymentVerdict: (Boolean, String?) -> Unit,
                  onBackToPreviousClick: () -> Unit,
                  onCancelClick: () -> Unit
) {
    val uiState by paymentViewModel.uiState.collectAsStateWithLifecycle()

    val iconPayment = if (isShoppingCart) Icons.Filled.ShoppingCart else Icons.Filled.Payment

    val amountFormatted = String.format("%.2f", amount.filter(Char::isDigit).toDouble())

    PaymentScreenContent(amountFormatted,
                         iconPayment,
                         uiState.initialized,
                         uiState.success,
                         uiState.errorMessage,
                         onBackToPreviousClick,
                         onCancelClick)

    LaunchedEffect(uiState.initialized) {
        if (uiState.initialized) {
            paymentViewModel.processPayment(amountFormatted)
        }
    }

    LaunchedEffect(uiState.tlvString) { //replace with success whn pin screen will be implemented
        delay(1000)
        uiState.tlvString?.let { tlvStream ->
            onPaymentVerdict(uiState.success, tlvStream)
        }
    }
}

@Composable
fun PaymentScreenContent(amount: String,
                         iconPayment: ImageVector,
                         ready: Boolean,
                         success: Boolean,
                         errorMessage: String?,
                         onBackToPreviousClick: () -> Unit,
                         onCancelClick: () -> Unit
) {
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
                    imageVector = iconPayment,
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
                errorMessage != null -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_crossmark),
                        contentDescription = "Error crossmark",
                        modifier = Modifier.width(100.dp)
                    )
                    Text(text = errorMessage,
                         color = MaterialTheme.colorScheme.onBackground,
                         style = MaterialTheme.typography.bodyLarge)
                }

                success -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_checkmark),
                        contentDescription = "Success checkmark",
                        modifier = Modifier.width(100.dp)
                    )
                }

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

        errorMessage?.let {
            Action(buttonText = stringResource(R.string.back_to_previous),
                   buttonType = ButtonType.Filled,
                   onClick = onBackToPreviousClick)
        } ?: run {
            if (ready && !success) {
                Action(buttonText = "Cancel",
                       buttonType = ButtonType.Tonal,
                       onClick = onCancelClick)
            }
        }
        Footer()
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenLoadingPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000",
                             Icons.Filled.Payment,
                             false,
                             false,
                             null,
                             { },
                             { })
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = TABLET,
         widthDp = 800,
         heightDp = 1280)
@Composable
fun PaymentScreenReadyPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000",
                             Icons.Filled.Payment,
                             true,
                             false,
                             null,
                             { },
                             { })
    }
}


@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000",
                             Icons.Filled.Payment,
                             true,
                             true,
                             null,
                             { },
                             { })
    }
}


@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenErrorPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000",
                             Icons.Filled.Payment,
                             true,
                             false,
                             "Error",
                             { },
                             { })
    }
}

