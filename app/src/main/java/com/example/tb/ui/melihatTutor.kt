package com.example.projectptb.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tb.R

@Composable
fun MelihatTutor(navController: NavController, modifier: Modifier = Modifier) {
    var activeTab by remember { mutableStateOf("Available Tutors") }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { NavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle Add Action */ },
                containerColor = Color(0xFF6D2B4F),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(65.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Tabs
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabButton("Available Tutors", activeTab) { activeTab = "Available Tutors" }
                    TabButton("Create Class", activeTab) { activeTab = "Create Class" }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Content
                if (activeTab == "Available Tutors") {
                    TutorList(navController)
                } else {
                    Text("Create Class Content Here", fontSize = 18.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TabButton(tabName: String, activeTab: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = tabName,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (tabName == activeTab) Color(0xFF6D2B4F) else Color.Gray
            )
        )
    }
}

@Composable
fun TutorList(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        TutorItem("Jisu", "Information System", R.drawable.tutor1, navController)
        Spacer(modifier = Modifier.height(10.dp))
        TutorItem("Rosa", "Computer Engineering", R.drawable.tutor2, navController)
        Spacer(modifier = Modifier.height(10.dp))
        TutorItem("Lusi", "Informatics", R.drawable.tutor3, navController)
        Spacer(modifier = Modifier.height(10.dp))
        TutorItem("Jena", "Information System", R.drawable.tutor4, navController)
    }
}

@Composable
fun TutorItem(name: String, major: String, imageRes: Int, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2F2C4F).copy(alpha = 0.1f))
            .clickable{ navController.navigate("melihatDetailTutor") } // Navigasi
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Tutor Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2F2C4F))
                Text(major, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun TopBar() {

}
