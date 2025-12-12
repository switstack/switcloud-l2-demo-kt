package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@Composable
fun Footer(modifier: Modifier = Modifier) {
    BoxWithConstraints() {
        val narrowWidth = maxWidth < 400.dp
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(110.dp),
            horizontalArrangement = Arrangement.spacedBy(if (narrowWidth) 20.dp else 80.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_dragonwing),
                contentDescription = "Dragonwing Logo",
                modifier = Modifier.weight(1f),
                contentScale = ContentScale.Crop,
                colorFilter = if (isSystemInDarkTheme())
                // using contrasted color with background
                    ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                else
                // using default color of dragonwing logo
                    ColorFilter.tint(Color(0xFF31017D))
            )
            Box(modifier = Modifier.weight(0.4f),
                contentAlignment = Alignment.Center)
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_oona),
                    contentDescription = "Oona Logo",
                    contentScale = ContentScale.Fit
                )
            }
            Image(
                painter = painterResource(id = if (isSystemInDarkTheme())
                    R.drawable.ic_switstack_dark
                else
                    R.drawable.ic_switstack_light
                ),
                contentDescription = "Switstack Logo",
                modifier = Modifier.weight(1f),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FooterPreview() {
    Switcloudl2demoktTheme {
        Footer()
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FooterPhonePreview() {
    Switcloudl2demoktTheme {
        Footer()
    }
}