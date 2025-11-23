package io.switstack.switcloud.switcloud_l2_demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Footer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_dragonwing),
            contentDescription = "Dragonwing Logo",
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.ic_oona),
            contentDescription = "Oona Logo",
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.ic_switstack),
            contentDescription = "Switstack Logo",
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Fit
        )
    }
}
