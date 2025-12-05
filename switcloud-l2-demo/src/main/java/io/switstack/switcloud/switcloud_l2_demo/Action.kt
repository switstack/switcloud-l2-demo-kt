package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

sealed interface ButtonType {
    object Filled : ButtonType
    object Tonal : ButtonType
    object Outlined : ButtonType
    object Elevated : ButtonType
    object Text : ButtonType
}

@Composable
fun Action(modifier: Modifier = Modifier, buttonText: String, buttonType: ButtonType, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val buttonModifier = modifier
            .padding(vertical = 25.dp)
            .height(80.dp)
            .width(300.dp)
        when (buttonType) {
            ButtonType.Elevated -> ElevatedButton(
                modifier = buttonModifier,
                onClick = onClick
            ) {
                Text(buttonText)
            }

            ButtonType.Filled -> Button(
                modifier = buttonModifier,
                onClick = onClick
            ) {
                Text(buttonText)
            }

            ButtonType.Outlined -> OutlinedButton(
                modifier = buttonModifier,
                onClick = onClick
            ) {
                Text(buttonText)
            }

            ButtonType.Text -> TextButton(
                modifier = buttonModifier,
                onClick = onClick
            ) {
                Text(buttonText)
            }

            ButtonType.Tonal -> FilledTonalButton(
                modifier = buttonModifier,
                onClick = onClick
            ) {
                Text(buttonText)
            }
        }

    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionFullPreview() {
    Switcloudl2demoktTheme() {
        Action(buttonText = "Filled",
               buttonType = ButtonType.Filled) { }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionElevatedPreview() {
    Switcloudl2demoktTheme() {
        Action(buttonText = "Elevated",
               buttonType = ButtonType.Elevated) { }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionTonalPreview() {
    Switcloudl2demoktTheme() {
        Action(buttonText = "Tonal",
               buttonType = ButtonType.Tonal) { }
    }
}
