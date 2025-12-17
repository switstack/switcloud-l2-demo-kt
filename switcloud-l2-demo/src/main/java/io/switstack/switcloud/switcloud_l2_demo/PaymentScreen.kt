package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import io.switstack.switcloud.switcloud_l2_demo.data.NfcAntennaDeviceEnum
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

    PaymentScreenContent(
        amountFormatted,
        iconPayment,
        uiState.initialized,
        uiState.success,
        uiState.errorMessage,
        onBackToPreviousClick,
        onCancelClick)

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
                         iconPayment: ImageVector,
                         ready: Boolean,
                         success: Boolean,
                         errorMessage: String?,
                         onBackToPreviousClick: () -> Unit,
                         onCancelClick: () -> Unit
) {
    val modelEnum = try {
        NfcAntennaDeviceEnum.valueOf("${Build.BRAND.uppercase()}_${Build.PRODUCT.uppercase()}")
        //NfcAntennaDeviceEnum.INEFI_ANDROID_G17
        //NfcAntennaDeviceEnum.PEPPERL_FUCHS_TABIND10AND
    } catch (e: Exception) {
        println(e)
        null
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
        contentAlignment = Alignment.Center) {

        Box(modifier = Modifier
            .width(300.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
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

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                errorMessage != null -> {
                    Image(modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.ic_crossmark),
                        contentDescription = "Error crossmark",
                    )
                    Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge)
                }

                success -> {
                    Image(modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.ic_checkmark),
                        contentDescription = "Success checkmark"
                    )
                }

                !ready -> {
                    CircularProgressIndicator(modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.CenterHorizontally))
                    Text(modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.loading),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge)
                }

                ready -> {
                    modelEnum?.let { model ->
                        Surface(modifier = Modifier
                            .absoluteOffset(
                                model.land_x_offset,
                                model.land_y_offset)
                            .widthIn(max = 400.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 32.dp, vertical = 8.dp)
                                    .width(150.dp),
                                painter = painterResource(id = R.drawable.ic_contactless),
                                contentDescription = "EMVCo contactless logo",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    }
                    Text(modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.present_card),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally) {
            errorMessage?.let {
                Action(
                    buttonText = stringResource(R.string.back_to_previous),
                    buttonType = ButtonType.Filled,
                    onClick = onBackToPreviousClick)
            } ?: run {
                if (ready && !success) {
                    Action(
                        buttonText = stringResource(R.string.cancel),
                        buttonType = ButtonType.Tonal,
                        onClick = onCancelClick)
                }
            }
            Footer()
        }
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

