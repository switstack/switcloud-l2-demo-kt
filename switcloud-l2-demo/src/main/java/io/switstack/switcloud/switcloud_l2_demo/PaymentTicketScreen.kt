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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getTagLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getValueLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.SharedPrefUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.TlvUtils
import kotlin.random.Random

@Composable
fun PaymentTicketScreen(success: Boolean,
                        tlvStream: String?,
                        onBackToCartClick: () -> Unit
) {
    val tlvEntries = TlvUtils.parseTlvString(tlvStream?.uppercase())
    val transactionCounter = SharedPrefUtils(LocalContext.current).getTransactionCounter()

    PaymentTicketScreenContent(tlvEntries,
                               success,
                               transactionCounter,
                               onBackToCartClick)
}

@Composable
fun PaymentTicketScreenContent(tlvEntries: List<TlvEntry>,
                               success: Boolean,
                               transactionCounter: Int,
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
                } else {
                    Text("Payment Failed")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    tlvEntriesToMap(tlvEntries, transactionCounter).forEach { entry ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = entry.key)
                            Text(text = entry.value)
                        }
                    }
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

private fun tlvEntriesToMap(tlvEntries: List<TlvEntry>, transactionCounter: Int): Map<String, String> =
    tlvEntries.map { entry ->
        getTagLabel(entry.tag) to getValueLabel(entry.tag, entry.value)
    }.toMutableList().apply {
        add(4, "Entry Method" to "Contactless")
        add("Approval number" to Random.nextInt(999999999).toString().padStart(10, '0'))
        add("Transaction #" to transactionCounter.toString().padStart(6, '0'))
    }.toMap()

private val sampleTlv = listOf(
    TlvEntry("9C", "0000000"),
    TlvEntry("9A", "0000000"),
    TlvEntry("9F02", "0000000"),
    TlvEntry("4F", "0000000"),
    TlvEntry("84", "0000000"),
    TlvEntry("50", "0000000"),
    TlvEntry("5A", "0000000"),
    TlvEntry("9F27", "0000000"),
    TlvEntry("95", "0000000"),
    TlvEntry("DF8129", "0000000")
)

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentTicketScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(sampleTlv, true, 123) { }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentTicketScreenErrorPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(sampleTlv, false, 0) { }
    }
}