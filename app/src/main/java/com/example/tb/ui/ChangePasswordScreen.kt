package com.example.tb.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tb.ui.theme.putih
import com.example.tb.ui.theme.ungu2
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import com.example.tb.data.preferences.SharedPrefsManager
import com.example.tb.data.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff 

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavHostController = rememberNavController()) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    // Update button click handler
    val onUpdatePassword = {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (newPassword != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            scope.launch {
                val token = sharedPrefsManager.getToken() ?: ""
                val success = authRepository.updatePassword(token, newPassword)
                if (success) {
                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                } else {
                    Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Change Password",
                        color = putih,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 40.dp, 0.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        PasswordTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = "New Password",
                            showPassword = showNewPassword,
                            onToggleVisibility = { showNewPassword = !showNewPassword }
                        )

                        PasswordTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirm New Password",
                            showPassword = showConfirmPassword,
                            onToggleVisibility = { showConfirmPassword = !showConfirmPassword }
                        )
                          Button(
                              onClick = { 
                                  if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                      Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                  } else if (newPassword != confirmPassword) {
                                      Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                  } else {
                                      scope.launch {
                                          val token = sharedPrefsManager.getToken() ?: ""
                                          val success = authRepository.updatePassword(token, newPassword)
                                          if (success) {
                                              Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                              navController.navigateUp()
                                          } else {
                                              Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                                          }
                                      }
                                  }
                              },
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .padding(top = 24.dp),
                              colors = ButtonDefaults.buttonColors(
                                  containerColor = Color(0xFF2F2C4F)
                              ),
                              shape = RoundedCornerShape(12.dp)
                          ) {
                              Text(
                                  "Update Password",
                                  modifier = Modifier.padding(vertical = 8.dp),
                                  fontSize = 16.sp
                              )
                        }
                    }
                }
            }
        }
    }
}@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (showPassword) "Hide password" else "Show password"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2F2C4F),
            focusedLabelColor = Color(0xFF2F2C4F)
        )
    )
}


