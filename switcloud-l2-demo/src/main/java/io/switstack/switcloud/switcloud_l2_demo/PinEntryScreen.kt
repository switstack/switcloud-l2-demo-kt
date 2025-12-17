package io.switstack.switcloud.switcloud_l2_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentViewModel
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@Composable
fun PinEntryScreen(paymentViewModel: PaymentViewModel,
                   onPinValidationClick: () -> Unit) {

    PinEntryScreenContent { success ->
        paymentViewModel.setPinVerdict(success)
        onPinValidationClick()
    }
}

@Composable
fun PinEntryScreenContent(onPinValidationClick: (Boolean) -> Unit) {
    var pin by remember { mutableStateOf("") }

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

    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = stringResource(R.string.enter_your_pin))
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = pin)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(12) { index ->
                        when (index) {
                            9    -> PinButton(
                                onClick = { removeDigit() },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Backspace",
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            )
                            10   -> PinButton(
                                onClick = { addDigit() },
                                content = { Text("0", style = MaterialTheme.typography.titleLarge) })
                            11   -> Button(
                                modifier = Modifier.height(60.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonColors(
                                    MaterialTheme.colorScheme.surfaceTint,
                                    MaterialTheme.colorScheme.onPrimary,
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.onPrimary),
                                enabled = pin.length >= 4,
                                onClick = { onPinValidationClick(true) },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(36.dp),
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Checkmark",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            )
                            else -> PinButton(
                                onClick = { addDigit() },
                                content = { Text("${index + 1}", style = MaterialTheme.typography.titleLarge) })
                        }
                    }
                }
                Action(
                    buttonText = stringResource(R.string.cancel),
                    buttonType = ButtonType.Tonal,
                    onClick = { onPinValidationClick(false) },
                )
            }
            Footer(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun PinButton(onClick: () -> Unit, content: @Composable (RowScope.() -> Unit)) =
    FilledTonalButton(
        modifier = Modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        content = content
    )

@Preview(device = TABLET)
@Composable
fun PinEntryScreenPreview() {
    Switcloudl2demoktTheme {
        PinEntryScreenContent { }
    }
}
