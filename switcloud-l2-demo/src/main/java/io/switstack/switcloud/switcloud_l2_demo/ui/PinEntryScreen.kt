package io.switstack.switcloud.switcloud_l2_demo.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.switstack.switcloud.switcloud_l2_demo.R
import io.switstack.switcloud.switcloud_l2_demo.secondary_display.LocalSecondaryDisplayManager
import io.switstack.switcloud.switcloud_l2_demo.ui.enums.FlavorTargetEnum
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget
import io.switstack.switcloud.switcloud_l2_demo.utils.isSmallHeightScreen

@Composable
fun PinEntryScreen(
    paymentViewModel: PaymentViewModel,
    onPinVerified: () -> Unit
) {
    var pin by rememberSaveable { mutableStateOf("") }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val secondaryDisplayManager = LocalSecondaryDisplayManager.current

    fun addDigit() {
        if (pin.length < 12) {
            pin += "*"
        }
    }

    fun removeDigit() {
        if (pin.isNotEmpty()) {
            pin = pin.dropLast(1)
        }
    }
    if (getFlavorTarget() == FlavorTargetEnum.SUNMI) {
        LaunchedEffect(Unit) {
            secondaryDisplayManager?.show {
                PinEntryScreenContent(
                    isLandscape,
                    pin,
                    true,
                    { addDigit() },
                    { removeDigit() },
                    {
                        paymentViewModel.setPinVerdict(false)
                        onPinVerified()
                    },
                    {
                        paymentViewModel.setPinVerdict(true)
                        onPinVerified()
                    }
                )
            }
        }
    }

    PinEntryScreenContent(
        isLandscape,
        pin,
        getFlavorTarget() != FlavorTargetEnum.SUNMI && secondaryDisplayManager?.secondaryDisplayExists() != true,
        { addDigit() },
        { removeDigit() },
        {
            paymentViewModel.setPinVerdict(false)
            onPinVerified()
        },
        {
            paymentViewModel.setPinVerdict(true)
            onPinVerified()
        }
    )
}

@Composable
fun PinEntryScreenContent(
    isLandScape: Boolean,
    pin: String,
    shouldShowPinEntry: Boolean = true,
    onPinButtonClicked: (String) -> Unit,
    onBackSpaceClicked: () -> Unit,
    onCancelClick: () -> Unit,
    onPinValidationClick: () -> Unit
) {
    val isSmallHeightScreen = isSmallHeightScreen()

    val config = when {
        isSmallHeightScreen -> PaymentDisplayConfig(R.drawable.bg_payment_land, 0.15f, MaterialTheme.typography.displaySmall)
        isLandScape -> PaymentDisplayConfig(R.drawable.bg_payment_land, 0.27f, MaterialTheme.typography.displayLarge)
        else -> PaymentDisplayConfig(R.drawable.bg_payment_port, 0.32f, MaterialTheme.typography.displaySmall)
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
                    text = stringResource(R.string.enter_your_pin),
                    style = config.headerTextStyle,
                    autoSize = TextAutoSize.StepBased(maxFontSize = config.headerTextStyle.fontSize),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (shouldShowPinEntry) {
                    Spacer(modifier = Modifier.weight(0.5f))
                    Text(
                        modifier = Modifier.padding(if (isSmallHeightScreen) 0.dp else 16.dp),
                        style = config.headerTextStyle,
                        textAlign = TextAlign.Center,
                        text = pin
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .widthIn(max = 500.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(15) { index ->
                            when (index) {
                                in 0..8, 10 -> {
                                    val number = (index + 1) % 11 // modulo to get only unit part
                                    PinButton(
                                        onClick = { onPinButtonClicked("$number") },
                                        content = { Text("$number", style = MaterialTheme.typography.titleLarge) }
                                    )
                                }

                                12 -> OperationButton(
                                    onClick = onCancelClick,
                                    enableCondition = { true },
                                    enabledButtonColor = MaterialTheme.colorScheme.error,
                                    content = {
                                        Text(
                                            "Cancel",
                                            autoSize = TextAutoSize.StepBased(maxFontSize = 22.sp),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                )

                                13 -> OperationButton(
                                    onClick = onBackSpaceClicked,
                                    enableCondition = { pin.isNotEmpty() },
                                    enabledButtonColor = MaterialTheme.colorScheme.inversePrimary,
                                    content = {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Backspace",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                )

                                14 -> OperationButton(
                                    onClick = onPinValidationClick,
                                    enableCondition = { pin.length >= 4 },
                                    enabledButtonColor = MaterialTheme.colorScheme.tertiary,
                                    content = {
                                        Icon(
                                            modifier = Modifier.size(36.dp),
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Checkmark",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                )
                                /*else -> {
                                    PinButton(
                                        onClick = {},
                                        enabled = false) {

                                    }
                                }*/
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.5f))
                if (!isSmallHeightScreen) {
                    Footer()
                }
            }
        }
    }
}

@Composable
fun PinButton(onClick: () -> Unit, buttonColors: ButtonColors? = null, enabled: Boolean = true, content: @Composable (RowScope.() -> Unit)) =
    FilledTonalButton(
        modifier = Modifier.height(55.dp),
        shape = RoundedCornerShape(16.dp),
        colors = buttonColors ?: ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = enabled,
        onClick = onClick,
        content = content
    )

@Composable
fun OperationButton(onClick: () -> Unit, enabledButtonColor: Color, enableCondition: () -> Boolean, content: @Composable (RowScope.() -> Unit)) =
    Button(
        modifier = Modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonColors(
            enabledButtonColor,
            MaterialTheme.colorScheme.onPrimary,
            enabledButtonColor.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.onPrimary
        ),
        enabled = enableCondition(),
        onClick = onClick,
        content = content
    )

@TabletPhonePreviews()
@Composable
fun PinEntryScreenPreview() {
    Switcloudl2demoktTheme {
        PinEntryScreenContent(true, "****", true, {}, {}, {}, {})
    }
}
