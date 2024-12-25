package com.example.tb.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tb.R
import com.example.tb.ui.theme.Routes
import com.example.tb.ui.theme.biru1
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu1
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.tb.data.preferences.SharedPrefsManager


@Composable
fun ButtonColumn(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier
                .width(250.dp)
                .height(55.dp)
            ,
            onClick = {navController.navigate("register")},
            colors = ButtonDefaults.buttonColors(
                containerColor  = ungu1
            )
        ) {
            Text(
                "Register",
                color = putih,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)
            ,
            onClick = {navController.navigate("login")},
            colors = ButtonDefaults.buttonColors(
                containerColor  = biru1
            )
        ) {
            Text(
                "Login",
                color = putih,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )
        }
    }
}

@Composable
fun LandingPage(navController: NavHostController = rememberNavController()) {
      val context = LocalContext.current
    val sharedPrefsManager = SharedPrefsManager(context)

    LaunchedEffect(Unit) {
        val token = sharedPrefsManager.getToken()
        if (token != null) {
            navController.navigate("main") {
                popUpTo("landing") { inclusive = true }
            }
        }
    }
    val image = painterResource(id = R.drawable.landing)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        ) {
            ButtonColumn(navController)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingPagePreview() {
    LandingPage()
}
