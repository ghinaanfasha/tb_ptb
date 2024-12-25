package com.example.tb.pages

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import com.example.tb.ui.theme.abu2
import com.example.tb.ui.theme.biru1
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu2
import kotlinx.coroutines.launch
import java.io.File
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.tb.utils.FileUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddPost(
     itemId: Int = -1,
   initialDescription: String = "",
    initialImage: String = "",
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf(initialDescription) }
       var post by remember { mutableStateOf(initialDescription) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
  

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            fileName = getFileName(context, it)
        }
    }


    // Function to handle item creation
    fun createItem(description: String, imageUri: Uri?) {
        scope.launch {
            isLoading = true
            try {
                // Convert Uri to File using FileUtils
                val imageFile = imageUri?.let { uri ->
                    FileUtils.getImageFile(uri, context)
                }

                // Call repository to create item
                val token = sharedPrefsManager.getToken() ?: ""
                val success = authRepository.createItem(
                    token = token,
                    description = description,
                    imageFile = imageFile
                )

                if (success) {
                    Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()
                    onBack()
                } else {
                    Toast.makeText(context, "Failed to create post", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            selectedFileUri = tempCameraUri
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = FileUtils.createImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }



    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(
                    "Add Post",
                    color = putih
                )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = putih
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = ungu2,
                    titleContentColor = putih,
                    navigationIconContentColor = putih,
                )
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(
                    text = "Add text here",
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )},
                value = post,
                onValueChange = {post = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = abu2,
                    focusedBorderColor = ungu2
                ),
                maxLines = 10,
                singleLine = false
            )
            Spacer(modifier = Modifier.height(40.dp))


            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Pilih Sumber") },
                    text = {
                        Column {
                            TextButton(
                                onClick = {
                                    launcher.launch("*/*")
                                    showDialog = false
                                }
                            ) {
                                Text("Gallery")
                            }
                            TextButton(
                                onClick = {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                    showDialog = false
                                }
                            ) {
                                Text("Camera")
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        color = abu2,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { showDialog = true },
            ) {
                if (selectedFileUri != null) {
                    AsyncImage(
                        model = selectedFileUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                modifier = Modifier
                    .width(250.dp)
                    .height(55.dp),
                onClick = {
                    if (post.isNotBlank()) {
                        scope.launch {
                            val token = sharedPrefsManager.getToken() ?: ""
                            val imageFile = selectedFileUri?.let { uri ->
                                FileUtils.getImageFile(uri, context)
                            }

                            val success = if (initialDescription.isEmpty()) {
                                // Create new item
                                authRepository.createItem(token, post, imageFile)
                            } else {
                                // Update existing item
                                authRepository.updateItem(token, itemId, post, imageFile)
                            }

                            if (success) {
                                Toast.makeText(context, 
                                    if (initialDescription.isEmpty()) "Post created successfully" 
                                    else "Post updated successfully", 
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBack()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please add some text", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = biru1
                )
            ) {
                Text(
                    if (isLoading) "Posting..." else "Post",
                    color = putih,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            

        }
    }
}private fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                result = cursor.getString(nameIndex)
            }
        }
    }
    return result ?: "Unknown file"
}


private fun createImageUri(context: Context): Uri {
    val timeStamp = System.currentTimeMillis()
    val imageFileName = "JPEG_${timeStamp}_"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: throw IllegalStateException("Failed to create image URI")
}
