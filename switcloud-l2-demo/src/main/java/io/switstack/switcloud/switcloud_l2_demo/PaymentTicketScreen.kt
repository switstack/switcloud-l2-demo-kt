package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum
import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum.Companion.fromTag
import io.switstack.switcloud.switcloud_l2_demo.data.OPSVerdictEnum
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloud_l2_demo.secondary_display.LocalSecondaryDisplayManager
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.TabletPhonePreviews
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_onSurface
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_surface
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getOPSVerdict
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getOPSVerdictLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getTagLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getValueLabel
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorTargetEnum
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget
import io.switstack.switcloud.switcloud_l2_demo.utils.SharedPrefUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.TlvUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.isCompactDevice
import io.switstack.switcloud.switcloud_l2_demo.utils.isSmallSquareScreen
import kotlin.random.Random

@Composable
fun PaymentTicketScreen(success: Boolean,
                        tlvStream: String?,
                        onBackToPreviousClick: () -> Unit
) {
    val secondaryDisplayManager = LocalSecondaryDisplayManager.current
    val tlvEntries = TlvUtils.parseTlvString(tlvStream?.uppercase())
    val transactionCounter = SharedPrefUtils(LocalContext.current).getTransactionCounter()

    PaymentTicketScreenContent(
        tlvEntries,
        shouldDisplayBackAction = true,
        shouldDisplayFooter = true,
        success = success,
        transactionCounter = transactionCounter,
        onBackToPreviousClick = onBackToPreviousClick)


    if (getFlavorTarget() == FlavorTargetEnum.SUNMI) {
        LaunchedEffect(Unit) {
            secondaryDisplayManager?.show {
                PaymentTicketScreenContent(
                    tlvEntries,
                    shouldDisplayBackAction = false,
                    shouldDisplayFooter = false,
                    success = success,
                    transactionCounter = transactionCounter,
                    onBackToPreviousClick = onBackToPreviousClick)
            }
        }
    }
}

@Composable
fun PaymentTicketScreenContent(tlvEntries: List<TlvEntry>,
                               shouldDisplayBackAction: Boolean,
                               shouldDisplayFooter: Boolean,
                               success: Boolean,
                               transactionCounter: Int,
                               onBackToPreviousClick: () -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isSmallSquareScreen = isSmallSquareScreen()
    val config = when {
        isSmallSquareScreen -> PaymentDisplayConfig(R.drawable.bg_payment_land, 0.05f, MaterialTheme.typography.displayLarge)
        isLandscape         -> PaymentDisplayConfig(R.drawable.bg_payment_land, 0.10f, MaterialTheme.typography.displayLarge)
        else                -> PaymentDisplayConfig(R.drawable.bg_payment_port, 0.12f, MaterialTheme.typography.displaySmall)
    }

    val ticketTextStyle = TextStyle(
        fontSize = if (isCompactDevice()) 12.sp else 18.sp,
        fontFamily = FontFamily.Monospace,
        color = md_theme_light_onSurface
    )

    Surface {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(config.backgroundResource),
            contentDescription = "Payment background")

        Column {
            Box(
                modifier = Modifier
                    .fillMaxHeight(config.headerPercent)
                    .fillMaxWidth())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.heightIn(max = 60.dp))
                Column(modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .shadow(16.dp, clip = false)
                            .widthIn(max = 500.dp),
                        color = md_theme_light_surface
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.payment_receipt),
                                textAlign = TextAlign.Center,
                                style = ticketTextStyle
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            tlvEntriesToMap(tlvEntries, transactionCounter, success).forEach { entry ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = entry.key,
                                        maxLines = 1,
                                        style = ticketTextStyle,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .weight(1f)
                                    )
                                    Text(
                                        textAlign = TextAlign.End,
                                        text = entry.value,
                                        maxLines = 1,
                                        style = ticketTextStyle
                                    )
                                }
                            }
                        }
                    }
                }
                if(shouldDisplayBackAction) {
                    Action(
                        buttonText = stringResource(R.string.back_to_previous),
                        buttonType = ButtonType.Elevated,
                        buttonColors = ButtonDefaults.elevatedButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = onBackToPreviousClick)
                }
                if(shouldDisplayFooter) {
                    Footer()
                }
            }
        }
    }
}

private fun tlvEntriesToMap(tlvEntries: List<TlvEntry>, transactionCounter: Int, success: Boolean): Map<String, String> =
    tlvEntries.map { entry ->
        when(fromTag(entry.tag.uppercase())) {
            EmvTagEnum.TAG_DF8129, EmvTagEnum.TAG_9F8210 -> {
                if(getOPSVerdict(entry.value) == OPSVerdictEnum.PIN_REQUIRED) {
                    getTagLabel(entry.tag) to getOPSVerdictLabel(success)
                } else {
                    getTagLabel(entry.tag) to getValueLabel(entry.tag, entry.value)
                }
            }
            else -> getTagLabel(entry.tag) to getValueLabel(entry.tag, entry.value)
        }
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

@TabletPhonePreviews
@Composable
fun PaymentTicketScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentTicketScreenContent(sampleTlv, true, true, true, 123) { }
    }
}