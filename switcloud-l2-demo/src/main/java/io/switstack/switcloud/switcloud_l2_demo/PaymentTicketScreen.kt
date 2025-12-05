package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.Utils

@Composable
fun PaymentTicketScreen(paymentViewModel: PaymentViewModel = viewModel(),
                        success: Boolean,
                        tlvStream: String?,
                        onBackToCartClick: () -> Unit
) {
    val tlvEntries = paymentViewModel.parseTlvString(tlvStream)

    PaymentTicketScreenContent(tlvEntries,
                               success,
                               onBackToCartClick)
}

@Composable
fun PaymentTicketScreenContent(tlvEntries: List<TlvEntry>,
                               success: Boolean,
                               onBackToCartClick: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (success) {
                    Text("Payment Successful!")
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        tlvEntries.forEach { entry ->
                            val tagName =
                                EmvUtils.emvTagNames[entry.tag.uppercase()] ?: entry.tag
                            val displayValue = if (entry.tag.uppercase() == "9C") {
                                EmvUtils.transactionTypeToString(entry.value)
                            } else if (EmvUtils.isTagASCII(entry.tag)) {
                                Utils.hexStringToAsciiString(entry.value)
                            } else {
                                entry.value.uppercase()
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = tagName)
                                Text(text = displayValue)
                            }
                        }
                    }
                } else {
                    Text("Payment Failed")
                }
            }
            Column {
                Action(buttonText = "Back to Cart",
                       buttonType = ButtonType.Filled,
                       onClick = onBackToCartClick)
                Footer()
            }
        }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentTicketScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(emptyList(), true) { }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentTicketScreenErrorPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(emptyList(), false) { }
    }
}