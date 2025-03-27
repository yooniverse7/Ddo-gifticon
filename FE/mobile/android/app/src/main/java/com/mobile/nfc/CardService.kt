package com.mobile.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.widget.Toast
import java.util.Arrays


private const val TAG = "CardService_싸피"
class CardService : HostApduService() {

    override fun onDeactivated(reason: Int) {}


    // BEGIN_INCLUDE(processCommandApdu)
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        // If the APDU matches the SELECT AID command for this service,
        val EXTRA_DATA_STRING = savedData

        return if (Arrays.equals(SELECT_APDU, commandApdu)) {
            val account = EXTRA_DATA_STRING
            val accountBytes = account!!.toByteArray()

            concatArrays(accountBytes, SELECT_OK_SW)
        } else {
            UNKNOWN_CMD_SW
        }
    }


    companion object {
        // AID for our loyalty card service.
        private const val SAMPLE_LOYALTY_CARD_AID = "F222222233"

        // ISO-DEP command HEADER for selecting an AID.
        // Format: [Class | Instruction | Parameter 1 | Parameter 2]
        private const val SELECT_APDU_HEADER = "00A40400"

        // "OK" status word sent in response to SELECT AID command (0x9000)
        private val SELECT_OK_SW = hexStringToByteArray("9000")

        // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
        private val UNKNOWN_CMD_SW = hexStringToByteArray("0000")
        private val SELECT_APDU = buildSelectApdu(SAMPLE_LOYALTY_CARD_AID)
        // END_INCLUDE(processCommandApdu)

        // 데이터 저장 정적 변수
        var savedData: String? = null

        fun buildSelectApdu(aid: String): ByteArray {
            // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
            return hexStringToByteArray(
                SELECT_APDU_HEADER + String.format(
                    "%02X",
                    aid.length / 2
                ) + aid
            )
        }

        fun byteArrayToHexString(bytes: ByteArray): String {
            val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
            val hexChars = CharArray(bytes.size * 2) // Each byte has two hex characters (nibbles)
            var v: Int
            for (j in bytes.indices) {
                v = bytes[j].toInt() and 0xFF // Cast bytes[j] to int, treating as unsigned value
                hexChars[j * 2] = hexArray[v ushr 4] // Select hex character from upper nibble
                hexChars[j * 2 + 1] = hexArray[v and 0x0F] // Select hex character from lower nibble
            }
            return String(hexChars)
        }

        @Throws(IllegalArgumentException::class)
        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            require(len % 2 != 1) { "Hex string must have even number of characters" }
            val data = ByteArray(len / 2) // Allocate 1 byte per 2 hex characters
            var i = 0
            while (i < len) {
                // Convert each character into a integer (base-16), then bit-shift into place
                data[i / 2] = ( (s[i].digitToIntOrNull(16)!!.shl(4))
                        + s[i + 1].digitToIntOrNull(16)!!).toByte()
                i += 2
            }
            return data
        }

        fun concatArrays(first: ByteArray, vararg rest: ByteArray): ByteArray {
            var totalLength = first.size
            for (array in rest) {
                totalLength += array.size
            }
            val result = Arrays.copyOf(first, totalLength)
            var offset = first.size
            for (array in rest) {
                System.arraycopy(array, 0, result, offset, array.size)
                offset += array.size
            }
            return result
        }
    }
}