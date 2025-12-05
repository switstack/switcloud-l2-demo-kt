package io.switstack.switcloud.switcloud_l2_demo.data

import com.squareup.moshi.Json
import io.switstack.switcloud.switcloudapi.model.EMVCreateSchema

data class EmvMultiScheme(

    @Json(name = "emvs")
    val emvs: Map<String, EMVCreateSchema>

)
