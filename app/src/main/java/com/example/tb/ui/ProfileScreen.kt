package com.example.tb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository

@Composable
fun ProfileScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val sharedPrefsManager = SharedPrefsManager(context)
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var userImage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    LaunchedEffect(Unit) {
        val token = sharedPrefsManager.getToken() ?: ""
        userData = authRepository.getUserSelf(token)
        userData?.let {
            userImage = it["image"] as? String
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top Gradient Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF2F2C4F))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (You can replace this with a custom icon)
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )

                // Title
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Placeholder for right side icon (e.g., settings)
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            // Profile Card
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
                    // Replace Icon with AsyncImage
                    if (userImage != null) {
                        AsyncImage(
                            model = "http://10.0.2.2:8081/uploads/$userImage",
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Update Text components
                    Text(
                        text = userData?.get("username") as? String ?: "Username",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F2C4F)
                    )

                    Text(
                        text = userData?.get("email") as? String ?: "Email",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // Menu Items

            // Existing profile content...

                MenuCard(
                    icon = Icons.Default.School, // Or use custom icon
                    title = "My Class",
                    subtitle = "View your class history and create new class",
                    onClick = { navController.navigate("myClass") }
                )


            MenuCard(
                icon = Icons.Default.Edit,
                title = "Edit Profile",
                subtitle = "Change your personal information",
                onClick = { navController.navigate("editProfile") }
            )

            MenuCard(
                icon = Icons.Default.Lock,
                title = "Change Password",
                subtitle = "Update your security settings",
                onClick = { navController.navigate("changePassword") }
            )

            MenuCard(
                icon = Icons.Default.ExitToApp,
                title = "Logout",
                subtitle = "Sign out from your account",
                onClick = {   sharedPrefsManager.clear() // Clear all saved data
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true } // Clear back stack
                    } }
            )
        }
    }
}

@Composable
fun MenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2F2C4F),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}