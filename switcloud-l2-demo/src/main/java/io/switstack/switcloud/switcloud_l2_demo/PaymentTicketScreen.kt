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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_onSurface
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_surface
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getTagLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getValueLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.SharedPrefUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.TlvUtils
import kotlin.random.Random

@Composable
fun PaymentTicketScreen(success: Boolean,
                        tlvStream: String?,
                        onBackToPreviousClick: () -> Unit
) {
    val tlvEntries = TlvUtils.parseTlvString(tlvStream?.uppercase())
    val transactionCounter = SharedPrefUtils(LocalContext.current).getTransactionCounter()

    PaymentTicketScreenContent(tlvEntries,
                               success,
                               transactionCounter,
                               onBackToPreviousClick)
}

@Composable
fun PaymentTicketScreenContent(tlvEntries: List<TlvEntry>,
                               success: Boolean,
                               transactionCounter: Int,
                               onBackToPreviousClick: () -> Unit
) {
    val ticketTextStyle = TextStyle(
        fontSize = if (isCompactDevice()) 12.sp else 18.sp,
        fontFamily = FontFamily.Monospace,
        color = md_theme_light_onSurface
    )

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
                    .wrapContentWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(modifier = Modifier
                    .shadow(16.dp, clip = false)
                    .widthIn(max = 400.dp),
                        color = md_theme_light_surface
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                           horizontalAlignment = Alignment.Start
                    ) {
                        Text(modifier = Modifier.fillMaxWidth(),
                             text = "Payment Receipt",
                             textAlign = TextAlign.Center,
                             style = ticketTextStyle
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        tlvEntriesToMap(tlvEntries, transactionCounter).forEach { entry ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = entry.key,
                                     maxLines = 1,
                                     style = ticketTextStyle,
                                     overflow = TextOverflow.Ellipsis,
                                     modifier = Modifier
                                         .padding(end = 8.dp)
                                         .weight(1f)
                                )
                                Text(textAlign = TextAlign.End,
                                     text = entry.value,
                                     maxLines = 1,
                                     style = ticketTextStyle
                                )
                            }
                        }
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Action(
                       buttonText = stringResource(R.string.back_to_previous),
                       buttonType = ButtonType.Filled,
                       onClick = onBackToPreviousClick)
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
    TlvEntry("9A", "251210"),
    TlvEntry("9F02", "000000015000"),
    TlvEntry("4F", "0000000"),
    TlvEntry("84", "A0000000041010"),
    TlvEntry("50", "00000000000"),
    TlvEntry("5A", "0000000"),
    TlvEntry("9F27", "0000000"),
    TlvEntry("95", "0000000"),
    TlvEntry("DF8129", "0000000")
)

@Preview(device = PIXEL_7)
@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = TABLET,
         widthDp = 800,
         heightDp = 1280)
@Composable
fun PaymentTicketScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(sampleTlv, true, 123) { }
    }
}