package io.switstack.switcloud.switcloud_l2_demo

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.switstack.switcloud.switcloud_l2_demo.ui.ButtonType
import io.switstack.switcloud.switcloud_l2_demo.ui.PaymentDisplayConfig
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_onSurface
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.md_theme_light_surface
import io.switstack.switcloud.switcloud_l2_demo.utils.isCompactDevice
import java.util.Locale

@Composable
fun ShoppingCartScreen(
    onProceedPaymentClick: (total: String) -> Unit
) {
    val items = (1..5).map { it to it * 10.0 }
    val total = String.format(Locale.ROOT, "%.2f", items.sumOf { it.second })

    val config =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            PaymentDisplayConfig(
                R.drawable.bg_payment_land,
                0.27f,
                MaterialTheme.typography.displayLarge
            )
        } else {
            PaymentDisplayConfig(
                R.drawable.bg_payment_port,
                0.32f,
                MaterialTheme.typography.displaySmall
            )
        }

    val ticketTextStyle = TextStyle(
        fontSize = if (isCompactDevice()) 12.sp else 18.sp,
        fontFamily = FontFamily.Monospace,
        color = md_theme_light_onSurface
    )

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
                    text = "Shopping cart",
                    color = MaterialTheme.colorScheme.onPrimary,
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
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .widthIn(max = 500.dp),
                        color = md_theme_light_surface,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Hardcoded shopping cart items
                            items.forEach { (item, price) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Item $item", style = ticketTextStyle)
                                    Text("$$price", style = ticketTextStyle)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total", style = ticketTextStyle)
                                Text("$$total", style = ticketTextStyle)
                            }
                        }
                    }
                }
                Action(
                    buttonText = stringResource(R.string.proceed_payment),
                    buttonType = ButtonType.Filled,
                    onClick = {
                        onProceedPaymentClick(total)
                    }
                )
                Footer()
            }
        }
    }
}

@Preview(device = TABLET)
@Preview(device = TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShoppingCartPreview() {
    Switcloudl2demoktTheme {
        ShoppingCartScreen(onProceedPaymentClick = {})
    }
}