package com.project.qa.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.googlecode.tesseract.android.TessBaseAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetYourAnswerViewModel : ViewModel() {
    val questionPaperUri = mutableStateOf(Uri.EMPTY)

    private val tessBaseApi = TessBaseAPI()

    suspend fun performOCR(bitmap: Bitmap): String {
        return withContext(Dispatchers.Default) {
            tessBaseApi.setImage(bitmap)
            val recognizedText = tessBaseApi.utF8Text
            recognizedText
        }
    }

    override fun onCleared() {
        tessBaseApi.end()
        super.onCleared()
    }
}