package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.ui.unit.sp
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
    val buttonModifier = modifier
        .padding(vertical = 25.dp)
        .height(80.dp)
        .width(300.dp)

    val text: @Composable RowScope.() -> Unit = {
        Text(buttonText,
             maxLines = 1)
    }

    when (buttonType) {
        ButtonType.Elevated -> ElevatedButton(
            modifier = buttonModifier,
            onClick = onClick,
            content = text)

        ButtonType.Filled -> Button(
            modifier = buttonModifier,
            onClick = onClick,
            content = text)

        ButtonType.Outlined -> OutlinedButton(
            modifier = buttonModifier,
            onClick = onClick,
            content = text)

        ButtonType.Text -> TextButton(
            modifier = buttonModifier,
            onClick = onClick,
            content = text)

        ButtonType.Tonal -> FilledTonalButton(
            modifier = buttonModifier,
            onClick = onClick,
            content = text)
    }

}

@Composable
fun RoundAction(modifier: Modifier = Modifier,
                buttonText: String,
                buttonType: ButtonType,
                onClick: (String) -> Unit) {
    val buttonModifier = modifier.size(100.dp)
    val text: @Composable RowScope.() -> Unit = {
        Text(buttonText,
             maxLines = 1,
             autoSize = TextAutoSize.StepBased(maxFontSize = 15.sp))
    }

    when (buttonType) {
        ButtonType.Elevated -> ElevatedButton(
            modifier = buttonModifier,
            onClick = { onClick(buttonText) },
            content = text)

        ButtonType.Filled -> Button(
            modifier = buttonModifier,
            onClick = { onClick(buttonText) },
            content = text)

        ButtonType.Outlined -> OutlinedButton(
            modifier = buttonModifier,
            onClick = { onClick(buttonText) },
            content = text)

        ButtonType.Text -> TextButton(
            modifier = buttonModifier,
            onClick = { onClick(buttonText) },
            content = text)

        ButtonType.Tonal -> FilledTonalButton(
            modifier = buttonModifier,
            onClick = { onClick(buttonText) },
            content = text)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionFilledPreview() {
    Switcloudl2demoktTheme() {
        Action(buttonText = "Filled",
               buttonType = ButtonType.Filled) { }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoundActionFilledPreview() {
    Switcloudl2demoktTheme() {
        RoundAction(buttonText = "Filled",
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoundActionTonalPreview() {
    Switcloudl2demoktTheme() {
        RoundAction(buttonText = "Tonal",
                    buttonType = ButtonType.Tonal) { }
    }
}
