package com.example.switcloud_l2_demo

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import com.example.switcloud_l2_demo.utils.EmvConfig
import com.example.switcloud_l2_demo.utils.MokaConfig
import com.example.switcloud_l2_demo.utils.Utils
import io.switstack.switcloud.switcloudl2.SwitcloudL2
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2Exception
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2NotFoundException
import io.switstack.switcloud.switcloudl2.helpers.CardInterfaceType
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Helper function to convert an integer to a BCD byte array of a specific length
fun intToBcd(value: Int, length: Int): ByteArray {
    val bcd = ByteArray(length)
    var temp = value
    for (i in length - 1 downTo 0) {
        bcd[i] = ((temp % 10) or ((temp / 10 % 10) shl 4)).toByte()
        temp /= 100
    }
    return bcd
}

fun createTrd(amount: String): ByteArray {
    return buildList<Byte> {
        // 1. Transaction Type (tag: 9c, length: 01, value: 00)
        add(0x9C.toByte()) // Tag 9C
        add(0x01.toByte()) // Length 01
        add(0x00.toByte()) // Value 00

        // 2. Date (tag: 9a, length: 03, value: current date in BCD YY MM DD)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyMMdd", Locale.US)
        val dateString = dateFormat.format(calendar.time)
        val dateBcd = Utils.hexStringToByteArray(dateString)

        add(0x9A.toByte()) // Tag 9A
        add(0x03.toByte()) // Length 03
        addAll(dateBcd.toList())

        // 3. Amount (tag: 9f02, length: 06, value: convert amount string to BCD)
        val parsedAmount = amount.replace(".", "").toIntOrNull() ?: 0
        val amountBcd = intToBcd(parsedAmount, 6) // Amount in cents, 6 bytes BCD

        add(0x9F.toByte()) // Tag 9F02 (first byte)
        add(0x02.toByte()) // Tag 9F02 (second byte)
        add(0x06.toByte()) // Length 06
        addAll(amountBcd.toList())
    }.toByteArray()
}

suspend fun processPayment(activity: Activity, amount: String): String {

    // Construct the 'trd' byte array in TLV format
    val trd = createTrd(amount)

    // Pseudocode for the logic (replace with your actual classes and methods)
    val switcloudL2 = SwitcloudL2.getInstance()
    switcloudL2.setActivity(activity)
    switcloudL2.initializeServices()

    val glase = switcloudL2.glase()
    val reader = switcloudL2.reader()

    reader.configure(MokaConfig.readerParams)

    glase.configureEntryPoint(EmvConfig.entryPointConfiguration)
    glase.addCombination(EmvConfig.combinationMastercard)
    glase.addCombination(EmvConfig.combinationVisa)
    glase.addCAKey(EmvConfig.caKey)

    var result: Pair<ByteArray?, Boolean> = glase.preProcessing(trd)
    if (!result.second)
        return "Pre-processing failed"

    val card = glase.protocolActivation(null)
    if (card != CardInterfaceType.CARD_INTERFACE_TYPE_CONTACTLESS)
        return "Card detection error"

    result = glase.combinationSelection()
    if (!result.second)
        return "Combination selection failed"

    val outcome = glase.kernelActivation(null)

    switcloudL2.cleanupServices()

    // Items to show on ticket
    val ticketTags = listOf(
        "9C",    // TT
        "9A",    // Data
        "9F02",  // Amount
        "4F",    // AID
        "84",    // DF Name
        "50",    // Application label
        "5A",    // PAN
        "9F27",  // CID
        "95",    // TVR
        "DF8129" // OPS
    )

    var ticketData: ByteArray = byteArrayOf()
    for (tag in ticketTags) {
        try {
            glase.getTag(Utils.hexStringToByteArray(tag))?.let { ticketData += it }
        } catch (e: SwitcloudL2NotFoundException) {
            // Skip that tag
        }
    }

    return Utils.byteArrayToHexString(ticketData)
}

@Composable
fun PaymentScreen(navController: NavController, amount: String) {
    val context = LocalContext.current
    val activity = context as Activity
    var paymentResult by remember { mutableStateOf<String?>(null) }

    Switcloudl2demoktTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Add this line
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Amount: $$amount")
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_contactless),
                contentDescription = "EMVCo contactless logo"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Please present card")
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { navController.navigate("shopping_cart") }) {
                Text("Cancel")
            }
        }
    }

    LaunchedEffect(Unit) {
        // Small delay to ensure the UI is rendered before starting payment processing
        delay(500)

        paymentResult = processPayment(activity, amount)
        paymentResult?.let { result ->
            navController.navigate("payment_ticket/$result") {
                // Pop up to the shopping cart screen to prevent going back to payment
                popUpTo("shopping_cart") { inclusive = false }
            }
        }
    }
}
