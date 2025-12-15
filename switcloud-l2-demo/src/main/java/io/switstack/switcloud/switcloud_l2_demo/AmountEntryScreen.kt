package io.switstack.switcloud.switcloud_l2_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentEntryScreen(onProceedPaymentClick: (total: String) -> Unit) {
    val customAmount = rememberTextFieldState()
    val keyboardVisible = WindowInsets.isImeVisible

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
            ) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                     text = "Please enter amount",
                     textAlign = TextAlign.Center,
                     style = MaterialTheme.typography.titleLarge)
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)) {
                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .align(Alignment.Center),
                           verticalArrangement = Arrangement.spacedBy(32.dp) // Adds space between the columns
                    ) {
                        Row(modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            RoundAction(buttonText = "$5.00",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text ->
                                            onProceedPaymentClick(text)
                                        })
                            RoundAction(buttonText = "$25.00",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text ->
                                            onProceedPaymentClick(text)
                                        })
                        }
                        Row(modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            RoundAction(buttonText = "$50.00",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text ->
                                            onProceedPaymentClick(text)
                                        })
                            RoundAction(buttonText = "$100.00",
                                        buttonType = ButtonType.Elevated,
                                        onClick = { text ->
                                            onProceedPaymentClick(text)
                                        })
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 32.dp),
                            state = customAmount,
                            leadingIcon = {
                                Icon(imageVector = Icons.Filled.AttachMoney,
                                     contentDescription = "Currency Symbol")
                            },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            label = { Text("Or enter a custom amount") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            inputTransformation = DigitOnlyInputTransformation(),
                            onKeyboardAction = {
                                customAmount.text.takeIf { it.isNotBlank() }?.let {
                                    onProceedPaymentClick("$it.00")
                                }
                            }
                        )
                    }
                }
            }
            if (!keyboardVisible) {
                Footer()
            }
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


@Preview(device = TABLET)
@Composable
fun PaymentEntryScreenPreview() {
    Switcloudl2demoktTheme() {
        PaymentEntryScreen() { }
    }
}