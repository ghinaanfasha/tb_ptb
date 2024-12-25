package com.example.tb.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun UpcomingClass(
    navController: NavHostController = rememberNavController()
) {
    Scaffold (
        topBar = {
            TopLayoutClass()
        },
        content = { paddingValues ->
            Box (
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(color = Color.White)
            ){
                Column {
                    MenuBarUpcoming()
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    LazyColumn {
                        item{
                            UpcomingList(navController)
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomLayoutUpcoming()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLayoutClass(){
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
fun MenuBarUpcoming(){
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
                    text = "Upcoming Class",
                    color = Color(0xFF6D2B4F),
                    fontWeight = FontWeight(600)
                )
            }
            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Class History",
                    color = Color.Black,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}
  @Composable
  fun UpcomingList(navController: NavHostController) {
      val context = LocalContext.current
      val scope = rememberCoroutineScope()
      val sharedPrefsManager = SharedPrefsManager(context)
      val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
      val classes = remember { mutableStateOf<List<Map<String, Any>>?>(null) }

      fun refreshClasses() {
          scope.launch {
              val token = sharedPrefsManager.getToken() ?: ""
              classes.value = authRepository.getClasses(token)
          }
      }

      LaunchedEffect(Unit) {
          refreshClasses()
      }

      classes.value?.let { classesList ->
          LazyColumn(
              modifier = Modifier.fillMaxSize()
          ) {
              items(classesList.size) { index ->
                  val classItem = classesList[index]
                  Card(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(vertical = 5.dp, horizontal = 15.dp),
                      colors = CardDefaults.cardColors(
                          containerColor = Color(0xFFEBEAEE)
                      )
                  ) {
                      Column(
                          modifier = Modifier.padding(20.dp)
                      ) {
                          Row(
                              modifier = Modifier.fillMaxWidth()
                          ) {
                              Column(
                                  modifier = Modifier.weight(1f)
                              ) {
                                  Text(
                                      text = classItem["topic"] as? String ?: "",
                                      fontSize = 13.sp,
                                      fontWeight = FontWeight.Bold,
                                      color = Color(0xFF2F2C4F)
                                  )
                                  Text(
                                      text = classItem["subject"] as? String ?: "",
                                      fontSize = 10.sp,
                                      color = Color(0xFF2F2C4F),
                                      modifier = Modifier.padding(vertical = 2.dp)
                                  )
                                  Text(
                                      text = "${classItem["start"] as? String} | ${classItem["end"] as? String}",
                                      fontSize = 10.sp,
                                      fontWeight = FontWeight.SemiBold,
                                      color = Color(0xFF6D2B4F)
                                  )
                              }
                              Column {
                            IconButton(onClick = { 
                            val classId = (classItem["id"] as Double).toInt()
                            navController.navigate("edit_class_screen/$classId") 
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.edit), 
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                alignment = Alignment.TopEnd
                            )
                        }

                              }
                          }
                          Spacer(modifier = Modifier.padding(vertical = 5.dp))
                          Row (
                              modifier = Modifier
                                  .fillMaxWidth()
                          ){
                              Button(
                                  onClick = { 
                                      Toast.makeText(context, "Class cancel successfully", Toast.LENGTH_SHORT).show()
                                   },
                                  modifier = Modifier
                                      .weight(1f)
                                      .padding(end = 2.dp),
                                  colors = ButtonDefaults.buttonColors(
                                      containerColor = Color(0xFF6D2B4F)
                                  )
                              ) {
                                  Text(
                                      text = "Cancel Class",
                                      fontWeight = FontWeight.SemiBold
                                  )
                              }
                              Button(
                                  onClick = {
                                      scope.launch {
                                          val token = sharedPrefsManager.getToken() ?: ""
                                          val classId = (classItem["id"] as Double).toInt()
                                          val success = authRepository.deleteClass(token, classId)
                                          if (success) {
                                              Toast.makeText(context, "Class deleted successfully", Toast.LENGTH_SHORT).show()
                                              refreshClasses()
                                          } else {
                                              Toast.makeText(context, "Failed to delete class", Toast.LENGTH_SHORT).show()
                                          }
                                      }
                                  },
                                  modifier = Modifier
                                      .weight(1f)
                                      .padding(start = 2.dp),
                                  colors = ButtonDefaults.buttonColors(
                                      containerColor = Color(0xFF2F2C4F)
                                  )
                              ) {
                                  Text(
                                      text = "Delete Class",
                                      fontWeight = FontWeight.SemiBold
                                  )
                              }
                          }
                      }
                  }
              }
          }
      }
  }@Composable
fun BottomLayoutUpcoming(){
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


@Preview(showBackground =true, showSystemUi =true)
@Composable
fun UpcomingClassPreview(){
    UpcomingClass()
}
