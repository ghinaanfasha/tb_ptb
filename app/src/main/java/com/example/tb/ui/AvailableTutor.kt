package com.example.tb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tb.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tb.ui.theme.ungu1
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import kotlinx.coroutines.launch


@Composable
fun AvailableTutor(
    navController: NavHostController = rememberNavController()
) {
    var selectedTab by remember { mutableStateOf(0) }
    Scaffold (
        topBar = {
            TopLayoutTutor()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                TabBarTutor(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                when (selectedTab) {
                    0 -> TutorList(navController)
                    1 -> ClassHistory()
                }
            }
        },

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLayoutTutor(){
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

@Composable
fun TabBarTutor(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton(
            text = "Available Tutor",
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        TabButton(
            text = "Class History",
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = if (selected) ungu1 else Color.Black,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun MenuBarTutor(){
    Box (
        modifier = Modifier.background(color = Color.White)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Available Tutor",
                    color = Color(0xFF6D2B4F),
                    fontWeight = FontWeight(600)
                )
            }
            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Create Class",
                    color = Color.Black,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

  @Composable
  fun TutorList(navController: NavHostController) {
      val context = LocalContext.current
      val scope = rememberCoroutineScope()
      val sharedPrefsManager = SharedPrefsManager(context)
      val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
      val tutors = remember { mutableStateOf<List<Map<String, Any>>?>(null) }

      LaunchedEffect(Unit) {
          scope.launch {
              val token = sharedPrefsManager.getToken() ?: ""
              tutors.value = authRepository.getClassesByUser(token)
          }
      }

      LazyColumn {
          tutors.value?.let { tutorsList ->
              items(tutorsList.size) { index ->
                  val tutor = tutorsList[index]
                  val user = tutor["user"] as Map<String, Any>
                  val category = user["category"] as Map<String, Any>
                
                  Card(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(vertical = 5.dp, horizontal = 15.dp)
                          .clickable {
    navController.navigate("list_class_screen/${user["username"]}/${category["name"]}/${user["id"]}")
}
,
                      colors = CardDefaults.cardColors(
                          containerColor = Color(0xFFEBEAEE)
                      )
                  ) {
                      Row(
                          modifier = Modifier.padding(15.dp),
                          horizontalArrangement = Arrangement.Center
                      ) {
                          Column {
                              Image(
                                  painter = painterResource(id = R.drawable.jisoo),
                                  contentDescription = null,
                                  modifier = Modifier
                                      .size(50.dp)
                                      .clip(RoundedCornerShape(100))
                              )
                          }
                          Column(
                              modifier = Modifier.padding(start = 20.dp)
                          ) {
                              Column {
                                  Text(
                                      text = user["username"] as String,
                                      fontSize = 15.sp,
                                      fontWeight = FontWeight.Bold,
                                      color = Color(0xFF2F2C4F)
                                  )
                                  Text(
                                      text = category["name"] as String,
                                      color = Color(0xFF2F2C4F)
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
fun BottomLayoutTutor(){
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
fun AvailableTutorPreview(){
    AvailableTutor()
}

