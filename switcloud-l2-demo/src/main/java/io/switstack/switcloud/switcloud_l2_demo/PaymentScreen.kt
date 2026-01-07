package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.switstack.switcloud.switcloud_l2_demo.secondary_display.LocalSecondaryDisplayManager
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayedStateValues
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayedStateValues.ImageConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.TabletPhonePreviews
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.AmountUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorTargetEnum
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget
import io.switstack.switcloud.switcloud_l2_demo.utils.isSmallSquareScreen
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(paymentViewModel: PaymentViewModel,
                  amount: String,
                  onPinRequired: () -> Unit,
                  onPaymentVerdict: (Boolean, String?) -> Unit,
                  onBackToPreviousClick: () -> Unit,
                  onCancelClick: () -> Unit
) {
    val uiState by paymentViewModel.uiState.collectAsStateWithLifecycle()

    val amountFormatted = AmountUtils.toUsdTwoDecimalString(amount)

    val secondaryDisplayManager = LocalSecondaryDisplayManager.current

    val shouldDisplayNfcLogo = getFlavorTarget() != null

    val shouldDisplayNfcOnPrimaryScreen = shouldDisplayNfcLogo && secondaryDisplayManager?.secondaryDisplayExists() == false

    val isSplitScreen = getFlavorTarget() == FlavorTargetEnum.FLYTECH

    val isSmallSquareScreen = isSmallSquareScreen()

    PaymentScreenContent(amountFormatted,
        shouldDisplayNfcLogo = shouldDisplayNfcOnPrimaryScreen,
        shouldDisplayBackAction = true,
        shouldDisplayFooter = !isSmallSquareScreen,
        isSplitScreen = isSplitScreen,
        isSmallSquareScreen = isSmallSquareScreen,
        ready = uiState.initialized,
        success = uiState.success,
        errorMessage = uiState.declinedOpsStatusAndErrorIndicationMessage ?: uiState.errorMessageResource?.let { stringResource(it) },
        onBackToPreviousClick = onBackToPreviousClick,
        onCancelClick = onCancelClick)

    if (getFlavorTarget() == FlavorTargetEnum.SUNMI) {
        LaunchedEffect(Unit) {
            secondaryDisplayManager?.show {
                PaymentScreenContent(
                    amountFormatted,
                    shouldDisplayNfcLogo = !shouldDisplayNfcOnPrimaryScreen,
                    shouldDisplayBackAction = false,
                    shouldDisplayFooter = false,
                    isSplitScreen = false,
                    isSmallSquareScreen = true,
                    ready = uiState.initialized,
                    success = uiState.success,
                    errorMessage = uiState.declinedOpsStatusAndErrorIndicationMessage ?: uiState.errorMessageResource?.let { stringResource(it) },
                    onBackToPreviousClick = onBackToPreviousClick,
                    onCancelClick = onCancelClick
                )
            }
        }
    }

    LaunchedEffect(uiState.initialized) {
        if (uiState.initialized
            && uiState.success == null
            && uiState.errorMessageResource == null
            && uiState.tlvString == null)
        {
            paymentViewModel.processPayment(amountFormatted)
        }
    }

    LaunchedEffect(uiState.showPinEntry) {
        if (uiState.showPinEntry) {
            onPinRequired()
        }
    }

    LaunchedEffect(uiState.success) {
        delay(3000)
        if(uiState.success != null) {
            uiState.tlvString?.let { tlvStream ->
                onPaymentVerdict(uiState.success == true, tlvStream)
            }
        }
    }
}

@Composable
fun PaymentScreenContent(amount: String,
                         shouldDisplayNfcLogo: Boolean,
                         shouldDisplayBackAction: Boolean,
                         shouldDisplayFooter: Boolean,
                         isSplitScreen: Boolean,
                         isSmallSquareScreen: Boolean,
                         ready: Boolean,
                         success: Boolean?,
                         errorMessage: String?,
                         onBackToPreviousClick: () -> Unit,
                         onCancelClick: () -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val config = when {
        isSmallSquareScreen -> PaymentDisplayConfig(
            when {
                errorMessage != null -> R.drawable.bg_error_land
                success == true      -> R.drawable.bg_success_land
                else                 -> R.drawable.bg_payment_land
            },
            0.15f,
            MaterialTheme.typography.displayLarge
        )
        isLandscape         -> PaymentDisplayConfig(
            when {
                errorMessage != null -> R.drawable.bg_error_land
                success == true      -> R.drawable.bg_success_land
                else                 -> R.drawable.bg_payment_land
            },
            0.27f,
            MaterialTheme.typography.displayLarge
        )
        else                -> PaymentDisplayConfig(
            when {
                errorMessage != null -> R.drawable.bg_error_port
                success == true      -> R.drawable.bg_success_port
                else                 -> R.drawable.bg_payment_port
            },
            0.32f,
            MaterialTheme.typography.displaySmall
        )
    }
    val contentValues = when {
        errorMessage != null || success == false -> {
            val displayedMessage = errorMessage ?: stringResource(R.string.declined)
            PaymentDisplayedStateValues(
                ImageConfig(R.drawable.ic_crossmark, 100.dp, "Error crossmark"),
                displayedMessage,
                when {
                    success == false && displayedMessage !in listOf(
                        stringResource(R.string.error_loading_emv_config),
                        stringResource(R.string.error_loading_capks),
                        stringResource(R.string.error_timeout),
                        stringResource(R.string.error_failure),
                        stringResource(R.string.error_pre_processing),
                        stringResource(R.string.error_card_detection),
                        stringResource(R.string.error_combination_selection),
                        stringResource(R.string.error_not_ready),
                        stringResource(R.string.error_init_failed)
                    )  -> null
                    else -> R.string.back_to_previous
                },
                MaterialTheme.colorScheme.error
            )
        }
        success == true -> {
            PaymentDisplayedStateValues(
                ImageConfig(R.drawable.ic_checkmark, 100.dp, "Success checkmark"),
                stringResource(R.string.success),
                null,
                MaterialTheme.colorScheme.tertiary
            )
        }
        !ready -> {
            PaymentDisplayedStateValues(
                null,
                stringResource(R.string.loading),
                null,
                null
            )
        }
        else -> {
            PaymentDisplayedStateValues(
                when {
                    shouldDisplayNfcLogo -> ImageConfig(R.drawable.ic_contactless, 200.dp, "EMVCo contactless logo")
                    else -> ImageConfig(R.drawable.ic_payment_placeholder, 200.dp, "Tap placeholder")
                },
                stringResource(R.string.present_card),
                R.string.cancel,
                MaterialTheme.colorScheme.onBackground.takeIf { shouldDisplayNfcLogo }
            )
        }
    }

    Surface {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(config.backgroundResource),
            contentDescription = "Payment background")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(config.headerPercent)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center),
                        text = "$$amount",
                        color = MaterialTheme.colorScheme.onPrimary,
                        autoSize = TextAutoSize.StepBased(maxFontSize = config.headerTextStyle.fontSize),
                        style = config.headerTextStyle
                    )
                }
            }
            Row {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE && isSplitScreen) {
                    Box(Modifier.weight(1f)) { }
                }
                Box(Modifier.weight(1f)) {

                    Column {
                        Spacer(modifier = Modifier.fillMaxHeight(config.headerPercent))
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        Box(
                            modifier = Modifier.size(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            contentValues.imageConfig?.let { imageConfig ->
                                Image(
                                    modifier = Modifier.sizeIn(maxWidth = imageConfig.width),
                                    painter = painterResource(id = imageConfig.drawable),
                                    contentDescription = imageConfig.contentDescription,
                                    colorFilter = contentValues.colorTint?.let { ColorFilter.tint(it) }
                                )
                            } ?: run {
                                CircularProgressIndicator(modifier = Modifier.size(75.dp))
                            }
                        }

                        Column(
                            modifier = Modifier.weight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 16.dp),
                                text = contentValues.text,
                                color =  contentValues.colorTint ?: MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall)


                            contentValues.buttonText?.let { stringId ->
                                if(shouldDisplayBackAction || stringId == R.string.cancel) {
                                    Action(
                                        buttonText = stringResource(stringId),
                                        buttonType = ButtonType.Elevated,
                                        onClick = when (stringId) {
                                            R.string.cancel -> onCancelClick
                                            else            -> onBackToPreviousClick
                                        })
                                }
                            }
                            if(shouldDisplayFooter) {
                                Footer()
                            }
                        }
                    }
                }
            }
        }
    }

    // Used for development only, when using previews to check that nfc logo is well centered
    if (LocalInspectionMode.current) {
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
            onDraw = {

                val canvasWidth = size.width
                val canvasHeight = size.height
                drawLine(
                    start = Offset(x = 0.dp.toPx(), y = 0f),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = Color.Red,
                    strokeWidth = 5.dp.toPx()
                )

                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = 0f, y = canvasHeight),
                    color = Color.Red,
                    strokeWidth = 5.dp.toPx()
                )
            }
        )
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenLoadingPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = true,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = false,
            isSmallSquareScreen = false,
            ready = false,
            success = null,
            errorMessage = null,
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenLoadingFlytechPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = true,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = true,
            isSmallSquareScreen = false,
            ready = false,
            success = null,
            errorMessage = null,
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}

@TabletPhonePreviews
@Composable
fun PaymentScreenReadyUnknownDevicePreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = false,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = false,
            isSmallSquareScreen = false,
            ready = true,
            success = null,
            errorMessage = null,
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}

@TabletPhonePreviews
@Composable
fun PaymentScreenReadyPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = true,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = true,
            isSmallSquareScreen = false,
            ready = true,
            success = null,
            errorMessage = null,
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}

@TabletPhonePreviews
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenSuccessPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = true,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = false,
            isSmallSquareScreen = false,
            ready = true,
            success = true,
            errorMessage = null,
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}

@TabletPhonePreviews
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentScreenErrorPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent(
            "1000",
            shouldDisplayNfcLogo = true,
            shouldDisplayBackAction = true,
            shouldDisplayFooter = true,
            isSplitScreen = false,
            isSmallSquareScreen = false,
            ready = true,
            success = false,
            errorMessage = "Error",
            onBackToPreviousClick = { },
            onCancelClick = { })
    }
}