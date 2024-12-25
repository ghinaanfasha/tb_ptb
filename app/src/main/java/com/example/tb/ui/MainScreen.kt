package com.example.tb.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tb.R
import com.example.tb.ui.AvailableTutor
import com.example.tb.ui.ProfileScreen
import com.example.tb.ui.theme.NavItem
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu1
import com.example.tb.ui.theme.ungu2

@Composable
fun MainScreen(navController: NavHostController) {
    val navItemList = listOf(
        NavItem(icon = Icons.Default.Home),
        NavItem(icon = painterResource(id = R.drawable.ic_tutor)),
        NavItem(icon = painterResource(id = R.drawable.ic_soal)),
        NavItem(icon = painterResource(id = R.drawable.ic_profile)),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_post") },
                shape = RoundedCornerShape(50),
                containerColor = ungu1,
                contentColor = putih,
                modifier = Modifier.offset(y = 45.dp)
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(containerColor = ungu2) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            when (val icon = navItem.icon) {
                                is ImageVector -> Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (selectedIndex == index) ungu2 else putih,
                                    modifier = Modifier.size(35.dp)
                                )
                                is Painter -> Icon(
                                    painter = icon,
                                    contentDescription = null,
                                    tint = if (selectedIndex == index) ungu2 else putih,
                                    modifier = Modifier.size(35.dp)
                                )
                                else -> throw IllegalArgumentException("Unsupported icon type")
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            ContentScreen(
                selectedIndex = selectedIndex,
                onBack = { selectedIndex = 0 },
                navController = navController
            )
        }
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onBack: () -> Unit,
    navController: NavHostController
) {
    when(selectedIndex) {
        0 -> HomePage(navController)
        1 -> AvailableTutor(navController)
        2 -> BankSoal(navController)
        3 -> ProfileScreen(navController)
    }
}
