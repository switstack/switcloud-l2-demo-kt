package com.example.switcloud_l2_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

@Composable
fun ShoppingCartScreen(navController: NavController) {
    val items = (1..5).map { it to it * 10.0 }
    val total = items.sumOf { it.second }

    Switcloudl2demoktTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Shopping Cart")
                Spacer(modifier = Modifier.height(16.dp))

                // Hardcoded shopping cart items
                items.forEach { (item, price) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Item $item")
                        Text("$${price}")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total")
                    Text("$$total")
                }
            }
            Button(onClick = {
                navController.navigate("payment/$total") {
                    popUpTo("shopping_cart") { inclusive = true }
                }
            }) {
                Text("Proceed with payment")
            }
        }
    }
}
