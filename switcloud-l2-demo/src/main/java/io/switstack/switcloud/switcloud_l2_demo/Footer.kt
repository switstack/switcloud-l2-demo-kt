package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorTargetEnum
import io.switstack.switcloud.switcloud_l2_demo.utils.FlavorUtils.getFlavorTarget

@Composable
fun Footer(modifier: Modifier = Modifier) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .heightIn(max = 56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            painter = painterResource(id = when(getFlavorTarget()) {
                FlavorTargetEnum.QUALCOMM -> R.drawable.ic_dragonwing
                FlavorTargetEnum.SUNMI    -> R.drawable.ic_sunmi
                FlavorTargetEnum.FLYTECH  -> R.drawable.ic_flytech
                FlavorTargetEnum.NEWLAND  -> R.drawable.ic_newland
            }),
            contentDescription = "Brand Logo",
            alignment = Alignment.Center,
            contentScale = if (isLandscape) ContentScale.FillHeight else ContentScale.FillWidth,
            colorFilter = if (isSystemInDarkTheme())
            // using contrasted color with background
                ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            else {
                // using default color on brand logos
                ColorFilter.tint(when(getFlavorTarget()) {
                    FlavorTargetEnum.QUALCOMM -> Color(0xFF31017D)
                    FlavorTargetEnum.SUNMI    -> Color(0xFFFF6801)
                    FlavorTargetEnum.FLYTECH  -> Color(0xFF0081C6)
                    FlavorTargetEnum.NEWLAND  -> Color(0xFF164A85)
                })
            }
        )

        Image(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            painter = painterResource(
                id = if (isSystemInDarkTheme())
                    R.drawable.ic_switstack_dark
                else
                    R.drawable.ic_switstack_light
            ),
            contentDescription = "Switstack Logo",
            contentScale = ContentScale.Fit
        )

        when(getFlavorTarget()) {
            FlavorTargetEnum.QUALCOMM -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_oona),
                        contentDescription = "Oona Logo",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            FlavorTargetEnum.SUNMI -> {
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    painter = painterResource(id = R.drawable.ic_dragonwing),
                    contentDescription = "Qualcomm Logo",
                    contentScale = if (isLandscape) ContentScale.FillHeight else ContentScale.FillWidth,
                    colorFilter = if (isSystemInDarkTheme())
                        // using contrasted color with background
                        ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    else {
                        ColorFilter.tint(Color(0xFF31017D))
                    }
                )
            }
             else -> {}
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