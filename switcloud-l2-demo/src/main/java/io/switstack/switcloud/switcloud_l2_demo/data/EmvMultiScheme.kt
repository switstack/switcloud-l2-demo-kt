package io.switstack.switcloud.switcloud_l2_demo.data

import io.switstack.switcloud.switcloudapi.model.EMVCreateSchema
import kotlinx.serialization.Serializable

@Serializable
data class EmvMultiScheme(
    val emvs: Map<String, EMVCreateSchema>
)
