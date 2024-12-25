package com.example.tb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tb.R
import com.example.tb.data.api.RetrofitClient
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Lock
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


    @Composable
    fun Register(
        navController: NavHostController = rememberNavController()
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var fullName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var nim by remember { mutableStateOf("") }
        var selectedDepartment by remember { mutableStateOf<Category?>(null) }
        var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
        var expanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            val token = "" 
            val response = RetrofitClient.apiService.getCategories(token)
            if (response.isSuccessful) {
                val data = response.body()?.get("data") as? List<Map<String, Any>>
                categories = data?.map { 
                    Category(
                        id = (it["id"] as Double).toInt(),
                        name = it["name"] as String
                    )
                } ?: emptyList()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(40.dp)) }
      
            item {
                Image(
                    painter = painterResource(id = R.drawable.byti),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            item {
                Text(
                    text = "Register",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F2C4F),
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        leadingIcon = {
                            Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF6D2B4F)
                        )
                        }
                    )

                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon ={Icon(
        imageVector = Icons.Default.Email,
        contentDescription = null,
        tint = Color(0xFF6D2B4F)
    )}
                    )

                    CustomTextField(
                        value = nim,
                        onValueChange = { nim = it },
                        label = "NIM",
                        leadingIcon ={Icon(
        imageVector = Icons.Default.Badge,
        contentDescription = null,
        tint = Color(0xFF6D2B4F)
    )}
                    )
                    @OptIn(ExperimentalMaterial3Api::class)
                  ExposedDropdownMenuBox(
      expanded = expanded,
      onExpandedChange = { expanded = !expanded },
      modifier = Modifier.fillMaxWidth()
    ) {
      OutlinedTextField(
          value = selectedDepartment?.name ?: "",
          onValueChange = {},
          readOnly = true,
          label = { Text("Department") },
          leadingIcon = {
              Icon(
                  imageVector = Icons.Default.School,
                  contentDescription = null,
                  tint = Color(0xFF6D2B4F)
              )
          },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
          modifier = Modifier
              .menuAnchor()
              .fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color(0xFFEBEAEE),
              focusedContainerColor = Color(0xFFEBEAEE),
              unfocusedBorderColor = Color.Transparent,
              focusedBorderColor = Color.Transparent
          ),
          shape = RoundedCornerShape(12.dp)
      )

      ExposedDropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false },
          modifier = Modifier
              .background(Color(0xFFEBEAEE))
              .fillMaxWidth()
      ) {
          categories.forEach { category ->
              DropdownMenuItem(
                  text = { 
                      Text(
                          text = category.name,
                          color = Color(0xFF2F2C4F),
                          modifier = Modifier.padding(vertical = 8.dp)
                      ) 
                  },
                  onClick = {
                      selectedDepartment = category
                      expanded = false
                  },
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(48.dp),
                  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
              )
          }
      }
    }

 
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon ={Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = null,
        tint = Color(0xFF6D2B4F)
    )},
                        isPassword = true
                    )

                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        leadingIcon = {
                            Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = null,
        tint = Color(0xFF6D2B4F))
                        },
                        isPassword = true
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        when {
                            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                            }
                            password.isEmpty() || password.length < 8 -> {
                                Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                            }
                            password != confirmPassword -> {
                                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                            }
                            fullName.isEmpty() -> {
                                Toast.makeText(context, "Username is required", Toast.LENGTH_SHORT).show()
                            }
                            nim.isEmpty() -> {
                                Toast.makeText(context, "NIM is required", Toast.LENGTH_SHORT).show()
                            }
                            selectedDepartment == null -> {
                                Toast.makeText(context, "Department is required", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                scope.launch {
                                    try {
                                        val result = RetrofitClient.apiService.register(
                                            mapOf(
                                                "email" to email,
                                                "password" to password,
                                                "username" to fullName,
                                                "nim" to nim,
                                                "category_id" to selectedDepartment!!.id.toString()
                                            )
                                        )

                                        if (result.isSuccessful) {
                                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        } else {
                                            val errorBody = result.errorBody()?.string()
                                            Toast.makeText(
                                                context,
                                                errorBody ?: "Registration failed",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381224)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Register",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?")
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text(
                            text = "Login",
                            color = Color(0xFF2F2C4F),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword && !passwordVisible) 
            PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFEBEAEE),
            focusedContainerColor = Color(0xFFEBEAEE),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) 
                            Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF6D2B4F)
                    )
                }
            }
        }
    )
}

data class Category(val id: Int, val name: String)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview(){
    Register()
}
