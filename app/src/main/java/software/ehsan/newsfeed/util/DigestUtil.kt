package software.ehsan.newsfeed.util

import java.security.MessageDigest

class DigestUtil {
    companion object {
        fun sha256(value: String): String {
            return try {
                val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
                val hash: ByteArray = digest.digest(value.toByteArray(charset("UTF-8")))
                val hexString = StringBuilder()
                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
    }

}