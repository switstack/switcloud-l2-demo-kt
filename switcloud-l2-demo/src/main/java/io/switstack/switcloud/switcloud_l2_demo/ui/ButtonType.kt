package io.switstack.switcloud.switcloud_l2_demo.ui

sealed interface ButtonType {
    object Filled : ButtonType
    object Tonal : ButtonType
    object Outlined : ButtonType
    object Elevated : ButtonType
    object Text : ButtonType
}