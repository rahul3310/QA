package com.project.qa

import android.app.Application
import com.googlecode.tesseract.android.TessBaseAPI

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val tessBaseApi = TessBaseAPI()

        val assetsPath = "file:///android_asset/"  // Base path for assets
        val tessdataPath = "$assetsPath/tessdata"  // Path to tessdata in assets

        tessBaseApi.init(assetsPath, "eng")  // Use "eng" or your language code
    }
}