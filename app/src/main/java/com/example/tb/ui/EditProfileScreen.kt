package com.example.tb.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.tb.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu2
import com.example.tb.utils.FileUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    // Add dialog state
    var showDialog by remember { mutableStateOf(false) }

    // Add camera related states and launchers
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            selectedImageUri = tempCameraUri
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

    // Add userImage state
    var userImage by remember { mutableStateOf<String?>(null) }

    // Load user data including image
    LaunchedEffect(Unit) {
        val token = sharedPrefsManager.getToken() ?: ""
        val userData = authRepository.getUserSelf(token)
        userData?.let {
            username = it["username"] as? String ?: ""
            email = it["email"] as? String ?: ""
            nim = it["nim"] as? String ?: ""
            userImage = it["image"] as? String
            if (userImage != null) {
                selectedImageUri = Uri.parse("http://10.0.2.2:8081/uploads/$userImage")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        color = putih,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 40.dp, 0.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        ) {
                              if (selectedImageUri != null) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = "Profile Picture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            tint = Color(0xFF2F2C4F)
        )
    }
                            FloatingActionButton(
                                onClick = { showDialog = true },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd),
                                containerColor = Color(0xFF2F2C4F)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Change Picture",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Add AlertDialog for image selection
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Choose Image Source") },
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
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        CustomTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Username",
                            icon = Icons.Default.Person
                        )

                        CustomTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            icon = Icons.Default.Email
                        )

                        CustomTextField(
                            value = nim,
                            onValueChange = { nim = it },
                            label = "NIM",
                            icon = Icons.Default.Badge
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    val token = sharedPrefsManager.getToken() ?: ""
                                    val imageFile = selectedImageUri?.let { FileUtils.getImageFile(it, context) }
                                    
                                    val success = authRepository.updateUser(
                                        token = token,
                                        username = username,
                                        email = email,
                                        nim = nim,
                                        imageFile = imageFile
                                    )

                                    if (success) {
                                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                        navController.navigateUp()
                                    } else {
                                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2F2C4F)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Save Changes",
                                modifier = Modifier.padding(vertical = 8.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2F2C4F),
            focusedLabelColor = Color(0xFF2F2C4F)
        )
    )
}
