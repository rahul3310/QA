 package com.project.qa

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.project.qa.ui.theme.QATheme
import com.project.qa.ui_components.ChooseImageDialogue
import com.project.qa.utils.uriToBitmap
import com.project.qa.viewmodels.GetYourAnswerViewModel

 class MainActivity : ComponentActivity() {
     private val getYourAnswerViewModel : GetYourAnswerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetYourAnswer(getYourAnswerViewModel)
                }
            }
        }
    }
}

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun GetYourAnswer(
     getYourAnswerViewModel: GetYourAnswerViewModel
 ){
     val context = LocalContext.current
     val openDialogue = remember { mutableStateOf(false) }
     val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {}

     val galleryLauncher =
         rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
             if (it != null) {
                 getYourAnswerViewModel.questionPaperUri.value = it
             }
         }
     val capturedBitmap by remember { mutableStateOf<Bitmap?>(uriToBitmap(contentResolver = context.contentResolver, selectedFileUri = getYourAnswerViewModel.questionPaperUri.value)) }
     var recognizedText by remember { mutableStateOf("") }

     LaunchedEffect(capturedBitmap) {
         if (capturedBitmap != null) {
             recognizedText = getYourAnswerViewModel.performOCR(capturedBitmap!!)
         }
     }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            if (getYourAnswerViewModel.questionPaperUri.value != Uri.EMPTY) {
                cameraLauncher.launch(getYourAnswerViewModel.questionPaperUri.value)
            }
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

     Box(modifier = Modifier
         .fillMaxSize()
         .background(Color.LightGray)
     ){
         TopAppBar(
             title = {
                 Text(
                     text = "Get your answer",
                     fontSize = 16.sp,
                     color = Color.Black,
                     fontWeight = FontWeight.SemiBold,
                 )
             },
             modifier = Modifier.align(Alignment.TopCenter),
             colors = TopAppBarDefaults.smallTopAppBarColors(Color.White)
         )

         if (capturedBitmap == null) {
             Image(
                 modifier = Modifier
                     .align(Alignment.TopCenter)
                     .padding(top = 74.dp)
                     .fillMaxWidth()
                     .height(320.dp),
                 painter = rememberAsyncImagePainter(getYourAnswerViewModel.questionPaperUri.value),
                 contentDescription = "set added photo",
             )
         } else {
             Text(text = recognizedText, modifier = Modifier.padding(16.dp))
         }

         FloatingActionButton(
             onClick = { openDialogue.value = true },
             content ={
                 Image(painter = painterResource(id = R.drawable.outline_camera_alt_24),
                     contentDescription ="add",
                     modifier = Modifier
                         .size(36.dp)
                         .padding(6.dp))
             },
             shape = RoundedCornerShape(8.dp),
             containerColor = Color.White,
             modifier = Modifier
                 .align(Alignment.BottomEnd)
                 .padding(end = 16.dp, bottom = 16.dp)
         )
         if (openDialogue.value){
             ChooseImageDialogue(
                 openChooseDialogue = openDialogue,
                 resultLauncherGallery = galleryLauncher,
                 cameraLauncher = cameraLauncher,
                 context = context,
                 uri = getYourAnswerViewModel.questionPaperUri,
                 permissionLauncher = permissionLauncher
             )
         }
     }


 }


