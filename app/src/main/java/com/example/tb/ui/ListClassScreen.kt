package com.example.tb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun ListClassScreen(
    navController: NavHostController,
    tutorName: String,
    department: String,
    userId: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
    val classes = remember { mutableStateOf<List<Map<String, Any>>?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = sharedPrefsManager.getToken() ?: ""
            classes.value = authRepository.getClasses(token, userId)
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Tutor Classes", color = Color.White) },
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Card Header with Tutor Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jisoo),
                        contentDescription = "Tutor Image",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = tutorName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F2C4F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = department,
                            fontSize = 16.sp,
                            color = Color(0xFF6D2B4F)
                        )
                    }
                }
            }

            // Card Body with Class List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Available Classes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F2C4F),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        classes.value?.let { classList ->
                            items(classList.size) { index ->
                                val classItem = classList[index]
                                ClassItem(
                                    className = classItem["topic"] as? String ?: "",
                                    subject = classItem["subject"] as? String ?: "",
                                    start = classItem["start"] as? String ?: "",
                                    end = classItem["end"] as? String ?: "",
                                    location = classItem["location"] as? String ?: "",
                                  id = (classItem["id"] as Double).toInt().toString(),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun ClassItem(
    className: String,
    subject: String,
    start: String,
    end: String,
    location: String,
    id: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
             navController.navigate("view_class_screen/$className/$subject/$start/$end/$location/$id")
          
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEAEE)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6D2B4F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = className.first().toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = className,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F2C4F)
                )
                Text(
                    text = subject,
                    fontSize = 14.sp,
                    color = Color(0xFF6D2B4F)
                )
            }
        }
    }
}