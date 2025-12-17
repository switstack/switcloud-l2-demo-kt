package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.switstack.switcloud.switcloud_l2_demo.data.NfcAntennaDeviceEnum
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayedValuesConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayedValuesConfig.ImageConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.AmountUtils
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(paymentViewModel: PaymentViewModel,
                  amount: String,
                  isShoppingCart: Boolean = false,
                  onPinRequired: () -> Unit,
                  onPaymentVerdict: (Boolean, String?) -> Unit,
                  onBackToPreviousClick: () -> Unit,
                  onCancelClick: () -> Unit
) {
    val uiState by paymentViewModel.uiState.collectAsStateWithLifecycle()

    val iconPayment = if (isShoppingCart) Icons.Filled.ShoppingCart else Icons.Filled.Payment

    val amountFormatted = AmountUtils.toUsdTwoDecimalString(amount)

    PaymentScreenContent(amountFormatted,
        uiState.initialized,
        uiState.success,
        uiState.errorMessage,
        onBackToPreviousClick,
        onCancelClick)

   /* PaymentScreenContent(
        amountFormatted,
        iconPayment,
        uiState.initialized,
        uiState.success,
        uiState.errorMessage,
        onBackToPreviousClick,
        onCancelClick)*/

    LaunchedEffect(uiState.initialized) {
        if (uiState.initialized
            && !uiState.success
            && uiState.errorMessage == null
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
        delay(1500)
        uiState.tlvString?.let { tlvStream ->
            onPaymentVerdict(uiState.success, tlvStream)
        }
    }
}

@Composable
fun PaymentScreenContent(amount: String,
                            ready: Boolean,
                            success: Boolean,
                            errorMessage: String?,
                            onBackToPreviousClick: () -> Unit,
                            onCancelClick: () -> Unit
) {
    val config = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        PaymentDisplayConfig(
            R.drawable.bg_payment_land,
            0.27f,
            MaterialTheme.typography.displayLarge

        )
    } else {
        PaymentDisplayConfig(
            R.drawable.bg_payment_port,
            0.31f,
            MaterialTheme.typography.displaySmall
        )
    }

    val modelEnum = try {
        NfcAntennaDeviceEnum.valueOf("${Build.BRAND.uppercase()}_${Build.PRODUCT.uppercase()}")
        //NfcAntennaDeviceEnum.INEFI_ANDROID_G17
        //NfcAntennaDeviceEnum.PEPPERL_FUCHS_TABIND10AND
    } catch (e: Exception) {
        println(e)
        null
    }

    val contentValues = when {
        errorMessage != null -> {
            PaymentDisplayedValuesConfig(
                ImageConfig(R.drawable.ic_crossmark, 100.dp, "Error crossmark"),
                errorMessage,
                R.string.back_to_previous
            )
        }
        success -> {
            PaymentDisplayedValuesConfig(
                ImageConfig(R.drawable.ic_checkmark, 100.dp, "Success checkmark"),
                stringResource(R.string.success),
                null
            )
        }
        !ready -> {
            PaymentDisplayedValuesConfig(
                null,
                stringResource(R.string.loading),
                null
            )
        }
        else -> {
            PaymentDisplayedValuesConfig(
                ImageConfig(R.drawable.ic_contactless, 200.dp, "EMVCo contactless logo"),
                stringResource(R.string.present_card),
                R.string.cancel
            )
        }
    }

    Surface() {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(config.backgroundResource),
            contentDescription = "Payment background")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Column {
                Spacer(modifier = Modifier.fillMaxHeight(config.headerPercent))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(64.dp, 64.dp, 0.dp, 0.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                )
            }
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
                        style = config.headerTextStyle
                    )
                }
            }

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.weight(0.5f))
                Box(modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    contentValues.imageConfig?.let { imageConfig ->
                        Image(
                            modifier = Modifier.sizeIn(maxWidth = imageConfig.width),
                            painter = painterResource(id = imageConfig.drawable),
                            contentDescription = imageConfig.contentDescription,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                        )
                    } ?: run {
                        CircularProgressIndicator(modifier = Modifier.size(75.dp))
                    }
                }

                Column(modifier = Modifier.weight(0.5f),//.padding(top =  contentValues.imageConfig?.spacerHeight ?: 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier.padding(bottom = 16.dp),
                        text = contentValues.text,
                        textAlign = TextAlign.Center,
                        style = if(errorMessage != null)
                            MaterialTheme.typography.headlineSmall
                        else
                            MaterialTheme.typography.headlineLarge)

                    Box(modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center) {
                        contentValues.buttonText?.let { stringId ->
                            Action(
                                modifier = Modifier.padding(top = 16.dp),
                                buttonText = stringResource(stringId),
                                buttonType = ButtonType.Elevated,
                                onClick = when(stringId) {
                                    R.string.cancel -> onCancelClick
                                    else -> onBackToPreviousClick
                                })
                        }
                    }
                    Footer()
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
        PaymentScreenContent("1000",
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
@Preview(device = PHONE)
@Composable
fun PaymentScreenReadyPreview() {
    Switcloudl2demoktTheme {
        PaymentScreenContent("1000",
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
                             true,
                             false,
                             "Error",
                             { },
                             { })
    }
}

