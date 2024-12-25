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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tb.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import com.example.tb.ui.theme.abu2
import com.example.tb.utils.FileUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClass(navController: NavHostController= rememberNavController()) {
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedKhsUri = it
            fileName = getFileName(context, it)
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

    Scaffold(
      
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
                            onValueChange = { 
                                // Only allow numeric input
                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                    level = it
                                }
                            },
                            label = "Semester",

                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // KHS Upload Section
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
                                
                                val success = authRepository.createClass(
                                    token = token,
                                    quota = quota,
                                    subject = subject,
                                    topic = topic,
                                    location = location,
                                    start = startTime,
                                    end = endTime,
                                    level = level,
                                    khsFile = khsFile
                                )

                                if (success) {
                                    Toast.makeText(context, "Class created successfully", Toast.LENGTH_SHORT).show()
                                    // Reset all input values
                                    subject = ""
                                    topic = ""
                                    startTime = ""
                                    endTime = ""
                                    location = ""
                                    quota = ""
                                    selectedKhsUri = null
                                    fileName = ""
                                    level = ""
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D2B4F))
                    ) {
                        Text("Create Class")
                    }
                }
            }
        }
        // Keep existing dialog
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLayout(){
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF381224)
        ),
        actions = {
            Image(
                painter = painterResource(id = R.drawable.jadwal),
                modifier = Modifier
                    .size(22.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    )
}


@ExperimentalMaterial3Api
@Composable
fun FormTutor(){
    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var datetutor by remember { mutableStateOf("") }
    var timetutor by remember { mutableStateOf("") }
    var locationtutor by remember { mutableStateOf("") }
    var uploadkhs by remember { mutableStateOf("") }
    var kuota by remember { mutableStateOf("") }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xFFEBEAEE))
    ){
        Column {
            Row {
                TextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = {
                        Text(
                            text = "Subject",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .padding(15.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White
                    )
                )
            }
            Row {
                TextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = {
                        Text(
                            text = "Topic",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .padding(15.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White
                    )
                )
            }
            Row {
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    TextField(
                        value = datetutor,
                        onValueChange = { datetutor = it },
                        placeholder = {
                            Text(
                                text = "Date ",
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.White
                        )
                    )
                }
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    TextField(
                        value = timetutor,
                        onValueChange = { timetutor = it },
                        placeholder = {
                            Text(
                                text = "Time",
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.White
                        )
                    )
                }
            }
            Row {
                TextField(
                    value = locationtutor,
                    onValueChange = { locationtutor = it },
                    placeholder = {
                        Text(
                            text = "Location",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .padding(15.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White
                    )
                )
            }
            Row {
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    TextField(
                        value = kuota,
                        onValueChange = { kuota = it },
                        placeholder = {
                            Text(
                                text = "Max kuota",
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.White
                        )
                    )
                }
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    TextField(
                        value = uploadkhs,
                        onValueChange = { uploadkhs = it },
                        placeholder = {
                            Text(
                                text = "Upload KHS",
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.White
                        )
                    )
                }
            }
            Row {
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(1.dp, color = Color(0xFF2F2C4F))
                    ) {
                        Text(
                            text = "Reset",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color(0xFF2F2C4F)
                        )
                    }
                }
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2F2C4F)
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Create Class",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BottomLayout(){
    Box(modifier = Modifier.fillMaxWidth()){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(6f),
            horizontalArrangement = Arrangement.Center
        ){
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(50.dp)
                    .zIndex(10f)
                    .offset(y = (-20).dp)
                    .align(Alignment.CenterVertically),
                containerColor = Color(0xFF6D2B4F),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        BottomAppBar (
            containerColor = Color(0xFF381224),
            modifier = Modifier
                .height(70.dp)
                .zIndex(5F)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .zIndex(8F),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ){
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "home",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ){
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.tutor),
                            contentDescription = "tutor",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.padding(horizontal = 20.dp))
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ){
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.list),
                            contentDescription = "list",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ){
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.profil),
                            contentDescription = "profil",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}




@Preview(showBackground =true, showSystemUi = true)
@Composable
fun CreateClassPreview(){
    CreateClass()
}

