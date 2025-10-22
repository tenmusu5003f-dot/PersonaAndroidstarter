package com.example.persona.core

import platform.Foundation.NSDate
import platform.Foundation.NSLog

class IOSOSBridge : PersonaOSBridge {
    override fun log(message: String) { NSLog(message) }
    override fun nowMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}
