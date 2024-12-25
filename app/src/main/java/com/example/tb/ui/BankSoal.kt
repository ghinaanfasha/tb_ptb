package com.example.tb.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tb.R
import com.example.tb.ui.theme.Departemen
import com.example.tb.ui.theme.biru1
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu1
import com.example.tb.ui.theme.ungu2
import com.example.tb.viewModel.MataKuliahViewModel

@Composable
fun BankSoal (navController:NavController= rememberNavController()){
    var selectedDepartment by remember { mutableStateOf<Departemen?>(null) }
    var selectedSemester by remember { mutableStateOf<String?>(null) }

    if (selectedDepartment == null) {
        DepertemenList (
            departemenList = listOf(
                Departemen("Information System", R.drawable.sisfor),
                Departemen("Computer Science", R.drawable.tekkom),
                Departemen("Informatics", R.drawable.inform)
            ),
            onDepartmentClick = { department -> selectedDepartment = department },
            navController
        )
    } else if (selectedSemester == null) {
        SemesterList(
            departemen = selectedDepartment!!,
            onSemesterClick = { semester -> selectedSemester = semester },
            onBackClick = { selectedDepartment = null }
        )
    } else {
        val mataKuliahViewModel: MataKuliahViewModel = viewModel()
        mataKuliahViewModel.loadData(selectedDepartment!!.name)
        MataKuliahView(
            departemen = selectedDepartment!!,
            semester = selectedSemester!!,
            mataKuliahViewModel = mataKuliahViewModel,
            onBackClick = { selectedSemester = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepertemenList(departemenList: List<Departemen>, onDepartmentClick: (Departemen) -> Unit, navController:NavController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp, 0.dp, 0.dp, 0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Exam Documents",
                            color = putih,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = ungu2,
                    titleContentColor = putih,
                    navigationIconContentColor = putih,
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("add_bank_soal")  }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_adddoc),
                            contentDescription = null,
                            tint = putih
                        )
                    }
                },
            )
        }
    ){ innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(departemenList){ departemen ->
                val color = when (departemen.name) {
                    "Information System" -> ungu1
                    "Computer Science" -> ungu2
                    "Informatics" -> biru1
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 12.dp)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
                        .clickable { onDepartmentClick(departemen)  }
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color)
                            .padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ){
                        Image(
                            painter = painterResource(id = departemen.imageRes),
                            contentDescription = "${departemen.name} Image",
                            modifier = Modifier
                                .size(150.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 16.dp),
                            horizontalAlignment = Alignment.End,
                        ) {
                            Text(
                                text = departemen.name,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterList(
    departemen: Departemen,
    onSemesterClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val semesterList = (1..7).map { "Semester $it" }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${departemen.name}",
                        color = putih,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 40.dp, 0.dp)
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
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
    ){ innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(semesterList) { semester ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 12.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                        .clickable { onSemesterClick(semester) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_doc), // Ganti dengan resource ikon Anda
                            contentDescription = null,
                            tint = ungu1,
                            modifier = Modifier.size(55.dp)
                        )
                        Text(
                            text = semester,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = ungu1,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MataKuliahView(
    departemen: Departemen,
    semester: String,
    mataKuliahViewModel: MataKuliahViewModel,
    onBackClick: () -> Unit
) {
    val mataKuliahList = mataKuliahViewModel.mataKuliahData.value[semester] ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${departemen.name} | $semester",
                        color = putih,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 40.dp, 0.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(mataKuliahList) { mataKuliah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 12.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_file),
                            contentDescription = null,
                            tint = ungu1,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = mataKuliah,
                            color = ungu1,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}






