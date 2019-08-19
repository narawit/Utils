package com.kuuga.updater

import java.io.IOException

class VersionFormatException : Exception() {
    override val message: String
        get() = "Version format not match on google play "
}