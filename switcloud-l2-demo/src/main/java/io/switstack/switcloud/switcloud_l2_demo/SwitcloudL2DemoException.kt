package io.switstack.switcloud.switcloud_l2_demo

class SwitcloudL2DemoException : RuntimeException {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

    fun makeMessage(message: String, cause: Throwable): String {
        return "$message: ${cause.message}"
    }
}