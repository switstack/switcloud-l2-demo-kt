package io.switstack.switcloud.switcloud_l2_demo.data

import com.squareup.moshi.Json
import io.switstack.switcloud.switcloudapi.model.CAPKCreateSchema

data class CapkMultiScheme(

    @Json(name = "capks")
    val capks: Map<String, CAPKCreateSchema>
)
