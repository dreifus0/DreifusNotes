package com.dreifus.app.data.notes

expect object PinHasher {
    fun hash(pin: String): String
    fun verify(pin: String, stored: String): Boolean
}
