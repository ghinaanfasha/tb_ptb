package com.example.tb.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R
import androidx.navigation.NavHostController
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun ViewClassScreen(
    navController: NavHostController,
    className: String,
    subject: String,
    start: String,
    end: String,
    location: String,
    id: String,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Class Details", color = Color.White) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = className,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F2C4F)
                    )
                    Text(
                        text = subject,
                        fontSize = 18.sp,
                        color = Color(0xFF6D2B4F)
                    )
                }
            }

            // Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailItem(
                        title = "Schedule",
                        value = start + " - " + end
                    )
                    DetailItem(
                        title = "Location",
                        value = location
                    )
                    
                }
            }

            Spacer(modifier = Modifier.weight(1f))
              val context = LocalContext.current
              val scope = rememberCoroutineScope()
              val sharedPrefsManager = SharedPrefsManager(context)
              val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

              Button(
               onClick = {
                  println("DEBUG: if - ${id}")
        scope.launch {
            if (id.isNotEmpty()) {
                val token = sharedPrefsManager.getToken() ?: ""
                val success = authRepository.createOrder(token, id)
                if (success) {
                    Toast.makeText(context, "Successfully registered for class", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                } else {
                    Toast.makeText(context, "Failed to register for class", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Invalid class ID", Toast.LENGTH_SHORT).show()
            }
        }
    },
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(56.dp),
                  colors = ButtonDefaults.buttonColors(
                      containerColor = Color(0xFF6D2B4F)
                  ),
                  shape = RoundedCornerShape(12.dp)
              ) {
                  Text(
                      text = "Register for this Class",
                      fontSize = 18.sp,
                      fontWeight = FontWeight.Bold
                  )
              }
          }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F2C4F)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF6D2B4F)
        )
    }
}
