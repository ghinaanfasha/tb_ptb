package com.example.tb.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import com.example.tb.utils.FileUtils
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClass(
    navController: NavHostController,
    classId: Int
) {
    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("") }
    var selectedKhsUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var level by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    LaunchedEffect(classId) {
        scope.launch {
            val token = sharedPrefsManager.getToken() ?: ""
            val response = authRepository.getClassById(token, classId)
            response?.let { classData ->
                subject = classData["subject"] as? String ?: ""
                topic = classData["topic"] as? String ?: ""
                startTime = classData["start"] as? String ?: ""
                endTime = classData["end"] as? String ?: ""
                location = classData["location"] as? String ?: ""
                quota = (classData["quota"] as? Double)?.toInt()?.toString() ?: ""
                level = (classData["level"] as? Double)?.toInt()?.toString() ?: ""
            }
        }
    }

val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            selectedKhsUri = tempCameraUri
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
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedKhsUri = it
            fileName = getFileName(context, it)
        }
    }

    // Rest of your file handling code from CreateClass.kt

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Class", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF381224)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEAEE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Input fields matching CreateClass.kt
                        CustomTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = "Subject"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            label = "Topic"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomTextField(
                                value = startTime,
                                onValueChange = { startTime = it },
                                label = "Start Time",
                                modifier = Modifier.weight(1f)
                            )
                            CustomTextField(
                                value = endTime,
                                onValueChange = { endTime = it },
                                label = "End Time",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = "Location"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            value = quota,
                            onValueChange = { quota = it },
                            label = "Max Participants"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            value = level,
                            onValueChange = { level = it },
                            label = "Semester"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // KHS Upload Section from CreateClass.kt
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .clickable { showDialog = true }
                        ) {
                            if (selectedKhsUri != null) {
                                AsyncImage(
                                    model = selectedKhsUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_upload),
                                        contentDescription = null,
                                        tint = Color(0xFF6D2B4F),
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text(
                                        "Upload KHS",
                                        color = Color(0xFF6D2B4F),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Save and Cancel buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color(0xFF2F2C4F))
                    ) {
                        Text("Cancel", color = Color(0xFF2F2C4F))
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val token = sharedPrefsManager.getToken() ?: ""
                                val khsFile = selectedKhsUri?.let { FileUtils.getImageFile(it, context) }
                                
                                val success = authRepository.updateClass(
                                    token = token,
                                    classId = classId,
                                    quota = quota,
                                    subject = subject,
                                    topic = topic,
                                    location = location,
                                    khsFile = khsFile
                                )

                                if (success) {
                                    Toast.makeText(context, "Class updated successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D2B4F))
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Choose KHS Source") },
                text = {
                    Column {
                        TextButton(onClick = {
                            launcher.launch("*/*")
                            showDialog = false
                        }) {
                            Text("Gallery")
                        }
                        TextButton(onClick = {
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            showDialog = false
                        }) {
                            Text("Camera")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    )
}
private fun getFileName(context: Context, uri: Uri): String {
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