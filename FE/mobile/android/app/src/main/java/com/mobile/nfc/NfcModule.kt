package com.mobile.nfc

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class NfcModule(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext) {
    //js에서 접근할 이름
    override fun getName(): String {
        return "DdopayNFC"
    }

    // 접근 후에 부를 메소드
    // @ReactMethod 어노테이션 필수
    @ReactMethod
    fun startNfcService(inputedData : String) {
        val intent = Intent(
            reactApplicationContext,
            CardService::class.java
        )
        CardService.savedData = inputedData
        reactApplicationContext.startService(intent)
    }
}