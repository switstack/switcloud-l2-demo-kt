package io.switstack.switcloud.switcloud_l2_demo.ui

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.switstack.switcloud.switcloud_l2_demo.data.CapkMultiScheme
import io.switstack.switcloud.switcloud_l2_demo.data.EmvMultiScheme
import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum
import io.switstack.switcloud.switcloud_l2_demo.data.OPSVerdictEnum
import io.switstack.switcloud.switcloud_l2_demo.utils.ByteArrayHexStringUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvConfig
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils.Companion.getOPSVerdict
import io.switstack.switcloud.switcloud_l2_demo.utils.MokaConfig
import io.switstack.switcloud.switcloud_l2_demo.utils.SharedPrefUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.TlvUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.readJsonFromAssets
import io.switstack.switcloud.switcloudapi.model.CAPKCreateSchema
import io.switstack.switcloud.switcloudapi.model.EMVCreateSchema
import io.switstack.switcloud.switcloudl2.IGlase
import io.switstack.switcloud.switcloudl2.IReader
import io.switstack.switcloud.switcloudl2.SwitcloudL2
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2Exception
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2InterruptedException
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2NotFoundException
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2TimeoutException
import io.switstack.switcloud.switcloudl2.helpers.CardInterfaceType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PaymentViewModel() : ViewModel() {

    private val switcloudL2: SwitcloudL2 = SwitcloudL2.getInstance()
    private lateinit var glase: IGlase
    private lateinit var reader: IReader
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        try {
            // Setting reference to switcloud components
            glase = switcloudL2.glase()
            reader = switcloudL2.reader()

        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    initialized = false,
                    errorMessage = "Initialization failed: ${e.message}"
                )
            }
        }
    }

    fun setupSwitcloudL2(activity: Activity) {
        switcloudL2.setActivity(activity)

        // Update the UI state to signal that initialization is complete.
        _uiState.update { it.copy(initialized = true) }
        println("SwitcloudL2 initialized")
    }

    fun cleanupSwitcloudL2() {
        switcloudL2.cleanupServices()
    }

    fun initializeSwitcloudL2(context: Context) {
        switcloudL2.initializeServices()

        reader.configure(MokaConfig.readerParams)
        glase.configureEntryPoint(EmvConfig.entryPointConfiguration)

        loadCapkCreateSchema(context)?.let {
            for (capk in it) {
                val cakey = TlvUtils.makeGlaseCAKey(capk)
                glase.addCAKey(cakey)
            }
        }

        loadEMVCreateSchema(context)?.let {
            for (emv in it) {
                val combination = TlvUtils.makeGlaseCombination(emv)
                glase.addCombination(combination)
            }
        }

//        Hardcoded loading
//        glase.addCombination(EmvConfig.combinationMastercard)
//        glase.addCombination(EmvConfig.combinationVisa)
//        glase.addCAKey(EmvConfig.caKey)
    }

    fun loadEMVCreateSchema(context: Context): List<EMVCreateSchema>? {
        try {
            val jsonString = context.readJsonFromAssets("emv-config/conf-multiScheme.json")

            val emvMultiSchemeData = TlvUtils.parseJsonToScheme(jsonString, EmvMultiScheme::class.java)

            // You can now use the parsed data
            if (emvMultiSchemeData != null) {
                println("Successfully parsed emv config: ${emvMultiSchemeData.emvs.size} emvs found!")
            }

            return emvMultiSchemeData?.emvs?.values?.toList()

        } catch (e: Exception) {
            _uiState.update {
                it.copy(errorMessage = "Failed to load EMV Config")
            }
            return null
        }
    }

    fun loadCapkCreateSchema(context: Context): List<CAPKCreateSchema>? {
        try {
            val jsonString = context.readJsonFromAssets("emv-config/multiSchemeKeys.json")

            val capkMultiSchemeData = TlvUtils.parseJsonToScheme(jsonString, CapkMultiScheme::class.java)

            if (capkMultiSchemeData != null) {
                println("Successfully parsed capk: ${capkMultiSchemeData.capks.size} capks found!")
            }

            return capkMultiSchemeData?.capks?.values?.toList()

        } catch (e: Exception) {
            _uiState.update {
                it.copy(errorMessage = "Failed to load CAPK list")
            }
            return null
        }
    }


    fun processPayment(amount: String) {
        // Guard clause to prevent processing before initialization is complete.
        if (!uiState.value.initialized) {
            _uiState.update { it.copy(errorMessage = "SwitCloudL2 is not ready.") }
            return
        }

        val currentActivity = switcloudL2.getActivity()

        SharedPrefUtils(currentActivity!!).incrementAndPutTransactionCounter()

        // Reset cancellation flag
        switcloudL2.setPollingCancelled(false)

        viewModelScope.launch(IO) {
            // All SwitcloudL2 initialization happens here, off the main thread.
            initializeSwitcloudL2(currentActivity)

            // Construct the 'trd' byte array in TLV format
            val trd = createTrd(amount)

            try {
                val preProcessingResult = glase.preProcessing(trd)
                if (!preProcessingResult.second)
                    _uiState.update {
                        it.copy(errorMessage = "Pre-processing failed")
                    }

                val card = glase.protocolActivation(null)
                if (card != CardInterfaceType.CARD_INTERFACE_TYPE_CONTACTLESS)
                    _uiState.update {
                        it.copy(errorMessage = "Card detection error")
                    }

                val combinationSelectionResult = glase.combinationSelection()
                if (!combinationSelectionResult.second)
                    _uiState.update {
                        it.copy(errorMessage = "Combination selection failed")
                    }

                glase.kernelActivation(null)

                // Items to show on ticket
                val ticketTags = listOf(
                    EmvTagEnum.TAG_9C,      //TT
                    EmvTagEnum.TAG_9A,      //Data
                    EmvTagEnum.TAG_9F02,    //Amount
                    EmvTagEnum.TAG_DF8129,   //OPS MASTERCARD
                    EmvTagEnum.TAG_9F8210,   //OPS C8
                    EmvTagEnum.TAG_4F,      //AID
                    EmvTagEnum.TAG_5F20,      //Cardholder Name
                    EmvTagEnum.TAG_84,      //DF_Name
                    EmvTagEnum.TAG_50,      //Application_label
                    EmvTagEnum.TAG_5A,      //PAN
                    EmvTagEnum.TAG_9F27,    //CID
                    EmvTagEnum.TAG_95      //TVR
                )

                var ticketData: ByteArray = byteArrayOf()
                var success = false
                var pinEntryRequired = false
                for (tag in ticketTags) {
                    try {
                        glase.getTag(ByteArrayHexStringUtils.hexStringToByteArray(tag.hexTag))?.let { value ->
                            ticketData += value

                            if (tag == EmvTagEnum.TAG_DF8129 || tag == EmvTagEnum.TAG_9F8210) {
                                val opsHexString = ByteArrayHexStringUtils.byteArrayToHexString(value)
                                val OPSTlvEntry = TlvUtils.parseTlvString(opsHexString).single()
                                when (getOPSVerdict(OPSTlvEntry.value)) {
                                    OPSVerdictEnum.SUCCESS -> success = true
                                    OPSVerdictEnum.PIN_REQUIRED -> pinEntryRequired = true
                                    OPSVerdictEnum.FAILURE -> success = false
                                }
                            }
                        }
                    } catch (e: SwitcloudL2NotFoundException) {
                        // Skip that tag
                        println("Tag not found in ticket : ${tag.name}")
                    }
                }

                val tlvString = ByteArrayHexStringUtils.byteArrayToHexString(ticketData)

                _uiState.update {
                    if (pinEntryRequired)
                        it.copy(showPinEntry = true, tlvString = tlvString)
                    else
                        it.copy(success = success, tlvString = tlvString, errorMessage = "Failure".takeUnless { success })
                }

            } catch (e: SwitcloudL2TimeoutException) {
                _uiState.update {
                    it.copy(errorMessage = "Card detection timeout")
                }
            } catch (e: SwitcloudL2InterruptedException) {
                _uiState.update {
                    it.copy(errorMessage = "User cancelled")
                }
            } catch (e: SwitcloudL2Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            } finally {
                cleanupSwitcloudL2()
            }
        }
    }

    fun cancelPayment() {
        // Guard clause to prevent processing before initialization is complete.
        if (!uiState.value.initialized) {
            _uiState.update { it.copy(errorMessage = "SwitCloudL2 is not ready.") }
            return
        }

        switcloudL2.setPollingCancelled(true)
    }

    fun resetPaymentState() {
        cleanupSwitcloudL2()
        _uiState.update {
            it.copy(
                showPinEntry = false,
                tlvString = null,
                success = false,
                errorMessage = null
            )
        }
    }

    // Helper function to convert an integer to a BCD byte array of a specific length
    private fun intToBcd(value: Int, length: Int): ByteArray {
        val bcd = ByteArray(length)
        var temp = value
        for (i in length - 1 downTo 0) {
            bcd[i] = ((temp % 10) or ((temp / 10 % 10) shl 4)).toByte()
            temp /= 100
        }
        return bcd
    }

    private fun createTrd(amount: String): ByteArray {
        return buildList<Byte> {
            // 1. Transaction Type (tag: 9c, length: 01, value: 00)
            add(0x9C.toByte()) // Tag 9C
            add(0x01.toByte()) // Length 01
            add(0x00.toByte()) // Value 00

            // 2. Date (tag: 9a, length: 03, value: current date in BCD YY MM DD)
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyMMdd", Locale.US)
            val dateString = dateFormat.format(calendar.time)
            val dateBcd = ByteArrayHexStringUtils.hexStringToByteArray(dateString)

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

            // 4. Currency Code (tag: 5f2a, length: 02, value: Euro code)
            add(0x5F.toByte()) // Tag 5F2A (first byte)
            add(0x2A.toByte()) // Tag 5F2A (second byte)
            add(0x02.toByte()) // Length 02
            add(0x09.toByte()) // Value Euro code (first byte)
            add(0x78.toByte()) // Value Euro code (second byte)

            // 5. Transaction Currency Exponent (tag: 5f36, length: 01, value: 2)
            add(0x5F.toByte()) // Tag 5F36 (first byte)
            add(0x36.toByte()) // Tag 5F36 (second byte)
            add(0x01.toByte()) // Length 01
            add(0x02.toByte()) // Value exponent

        }.toByteArray()
    }
}