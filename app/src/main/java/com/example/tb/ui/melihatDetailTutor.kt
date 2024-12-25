package com.example.projectptb.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tb.R
import com.example.tb.ui.Register

@Composable
fun MelihatDetailTutor(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { DetailTopBar() },
        //bottomBar = { com.example.projectptb.ui.theme.components.NavigationBar() },
        floatingActionButton = { FloatingActionButtonCentered() },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header Profil Tutor
            TutorProfileSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Upcoming Class Section
            Text(
                text = "Upcoming Class",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.Gray.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(8.dp))

            // Class List
            ClassCard(title = "Membuat UI dengan kotlin", subtitle = "Pemrograman Teknologi Bergerak")
            Spacer(modifier = Modifier.height(8.dp))
            ClassCard(title = "HTML, CSS, node js", subtitle = "Pemrograman Web")
            Spacer(modifier = Modifier.height(8.dp))
            ClassCard(title = "Back-end dengan bahasa PHP", subtitle = "Basis Data")
            Spacer(modifier = Modifier.height(8.dp))
            ClassCard(title = "Borland C++", subtitle = "Dasar-Dasar Pemrogramman")
        }
    }
}

@Composable
fun TutorProfileSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFFF2F2F2), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tutor1),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Kim Jisoo",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2F2C4F))
            )
            Text(
                text = "Information System",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }
    }
}

@Composable
fun ClassCard(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2F2C4F))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle calendar */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.jadwal),
                    contentDescription = "Calendar",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6D2B4F))
    )
}

@Composable
fun FloatingActionButtonCentered() {
    FloatingActionButton(
        onClick = { /* Handle action */ },
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
}

@Composable
fun NavigationBar() {
    BottomAppBar(
        containerColor = Color(0xFF381124),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val icons = listOf(
                Pair(R.drawable.home, "Home"),
                Pair(R.drawable.tutor, "Tutor"),
                Pair(R.drawable.soal, "Soal"),
                Pair(R.drawable.profil, "Profile")
            )
            icons.forEach { (icon, description) ->
                IconButton(onClick = { /* Handle navigation */ }) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description,
                        modifier = Modifier.size(35.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailTutorPreview(){
    MelihatDetailTutor()
}