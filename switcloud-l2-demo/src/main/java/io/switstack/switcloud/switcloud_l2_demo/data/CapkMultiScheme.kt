package io.switstack.switcloud.switcloud_l2_demo.data

import io.switstack.switcloud.switcloudapi.model.CAPKCreateSchema
import kotlinx.serialization.Serializable

@Serializable
data class CapkMultiScheme(
    val capks: Map<String, CAPKCreateSchema>
)
