package io.switstack.switcloud.switcloud_l2_demo.secondary_display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.switstack.switcloud.switcloud_l2_demo.R
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@Composable
fun SunmiBrandsScreen () {

    val spacersWeight = 0.5f

    Image(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillHeight,
        painter = painterResource(R.drawable.bg_payment_land),
        contentDescription = "Payment background")
    Column {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.27f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center) { }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(48.dp, 48.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(spacersWeight))
            Image(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp, end = 10.dp),
                painter = painterResource(id = R.drawable.ic_sunmi),
                contentDescription = "Sunmi brand Logo",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight,
                colorFilter = if (isSystemInDarkTheme())
                    ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                else {
                    ColorFilter.tint(Color(0xFFFF6801))
                }
            )
            Spacer(Modifier.weight(spacersWeight))
            Image(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                painter = painterResource(
                    id = if (isSystemInDarkTheme())
                        R.drawable.ic_switstack_dark
                    else
                        R.drawable.ic_switstack_light
                ),
                contentDescription = "Switstack Logo",
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.weight(spacersWeight))
            Image(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 5.dp),
                painter = painterResource(id = R.drawable.ic_dragonwing),
                contentDescription = "Qualcomm Logo",
                contentScale = ContentScale.FillHeight,
                colorFilter = if (isSystemInDarkTheme())
                // using contrasted color with background
                    ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                else {
                    ColorFilter.tint(Color(0xFF31017D))
                }
            )
            Spacer(Modifier.weight(spacersWeight))
        }
    }
}

@Preview(
    name = "Sunmi terminal small screen",
    group = "Phone",
    device = PHONE,
    widthDp = 800,
    heightDp = 480
)
@Composable
fun SunmiBrandsScreenPreview() {
    Switcloudl2demoktTheme() {
        SunmiBrandsScreen()
    }
}