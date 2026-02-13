package io.switstack.switcloud.switcloud_l2_demo.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.switstack.switcloud.switcloud_l2_demo.R
import io.switstack.switcloud.switcloud_l2_demo.secondary_display.LocalSecondaryDisplayManager
import io.switstack.switcloud.switcloud_l2_demo.secondary_display.SunmiBrandsScreen
import io.switstack.switcloud.switcloud_l2_demo.ui.enums.FlavorTargetEnum
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget
import io.switstack.switcloud.switcloud_l2_demo.utils.isCompactDevice
import io.switstack.switcloud.switcloud_l2_demo.utils.isSmallSquareScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmountEntryScreen(onProceedPaymentClick: (total: String) -> Unit) {
    val customAmount = rememberTextFieldState()
    val keyboardVisible = WindowInsets.isImeVisible
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isSmallSquareScreen = isSmallSquareScreen()
    val secondaryDisplayManager = LocalSecondaryDisplayManager.current

    val config = when {
        isSmallSquareScreen -> PaymentDisplayConfig(
            R.drawable.bg_payment_land,
            0.15f,
            MaterialTheme.typography.displayLarge
        )

        isLandscape -> PaymentDisplayConfig(
            R.drawable.bg_payment_land,
            0.27f,
            MaterialTheme.typography.displayLarge
        )

        else -> PaymentDisplayConfig(
            R.drawable.bg_payment_port,
            0.32f,
            MaterialTheme.typography.displaySmall
        )
    }

    if (getFlavorTarget() == FlavorTargetEnum.SUNMI) {
        LaunchedEffect(Unit) {
            secondaryDisplayManager?.show {
                SunmiBrandsScreen()
            }
        }
    }

    Surface {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(config.backgroundResource),
            contentDescription = "Payment background"
        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxHeight(config.headerPercent)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    text = stringResource(if (isSmallSquareScreen) R.string.select_amount else R.string.enter_amount),
                    color = MaterialTheme.colorScheme.onPrimary,
                    autoSize = TextAutoSize.StepBased(maxFontSize = config.headerTextStyle.fontSize),
                    style = config.headerTextStyle
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    ResponsiveLayout(
                        item1 = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                                    RoundAction(
                                        buttonText = "$5",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text -> onProceedPaymentClick(text) }
                                    )
                                    RoundAction(
                                        buttonText = "$25",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text -> onProceedPaymentClick(text) }
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                                    RoundAction(
                                        buttonText = "$50",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text -> onProceedPaymentClick(text) }
                                    )
                                    RoundAction(
                                        buttonText = "$100",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text -> onProceedPaymentClick(text) }
                                    )
                                }
                            }
                        },
                        item2 = {
                            if (!isSmallSquareScreen) {
                                TextField(
                                    modifier = Modifier.widthIn(max = 200.dp),
                                    state = customAmount,
                                    shape = RoundedCornerShape(8.dp),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.AttachMoney,
                                            contentDescription = "Currency Symbol"
                                        )
                                    },
                                    lineLimits = TextFieldLineLimits.SingleLine,
                                    placeholder = {
                                        Text(
                                            "Custom amount",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    inputTransformation = DigitOnlyInputTransformation(),
                                    onKeyboardAction = {
                                        customAmount.text.takeIf { it.isNotBlank() }?.let {
                                            onProceedPaymentClick(it.toString())
                                        }
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            }
                        },
                        isLandscape
                    )
                }
                if (!keyboardVisible) {
                    Footer()
                }
            }
        }
    }
}

@Composable
private fun ResponsiveLayout(
    item1: @Composable () -> Unit,
    item2: @Composable () -> Unit,
    isLandscape: Boolean
) {
    if (!isCompactDevice() && isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                item1()
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                item2()
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item1()
            item2()
        }
    }
}

class DigitOnlyInputTransformation : InputTransformation {

    override fun TextFieldBuffer.transformInput() {
        if (!asCharSequence().isDigitsOnly() || asCharSequence().length > 10) {
            revertAllChanges()
        }
    }
}

@TabletPhonePreviews
@Composable
fun AmountEntryScreenPreview() {
    Switcloudl2demoktTheme {
        AmountEntryScreen { }
    }
}