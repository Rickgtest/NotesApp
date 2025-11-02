// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.Data

import java.io.File
import java.security.MessageDigest

object Security {
    private const val PIN_FILE = "pin.txt"

    fun savePin(filesDir: File, pin: String) {
        val file = File(filesDir, PIN_FILE)
        file.writeText(hashPin(pin))
    }

    fun verifyPin(filesDir: File, pin: String): Boolean {
        val file = File(filesDir, PIN_FILE)
        if (!file.exists()) return false
        val storedHash = file.readText()
        return storedHash == hashPin(pin)
    }

    fun isPinSet(filesDir: File): Boolean {
        return File(filesDir, PIN_FILE).exists()
    }

    private fun hashPin(pin: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(pin.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
