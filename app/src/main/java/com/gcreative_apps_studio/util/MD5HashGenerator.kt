package com.gcreative_apps_studio.util

import java.security.MessageDigest
import java.math.BigInteger


class MD5HashGenerator {
    companion object {
        private const val algorithm: String = "MD5"
        private const val positive: Int = 1
        private const val hex: Int = 16

        // GENERATE MD5 HASH
        fun generate(pass: String): String {
            var buffer = pass.toByteArray(Charsets.UTF_8)

            val md = MessageDigest.getInstance(algorithm)
            md.update(buffer, 0, buffer.size)

            return BigInteger(positive, md.digest()).toString(hex)
        }
    }
}