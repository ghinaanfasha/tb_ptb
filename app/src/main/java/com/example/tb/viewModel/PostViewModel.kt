package com.example.tb.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tb.R
import com.example.tb.ui.theme.Post

class PostViewModel : ViewModel() {

    private val _posts = mutableStateOf<List<Post>>(emptyList())
    val posts: State<List<Post>> = _posts

    init {
        loadPosts()
    }

    private fun loadPosts() {
        _posts.value = listOf(
            Post(
                id = 1,
                name = "You",
                department = "Information System",
                time = "09:30 1/10/2024",
                content = "Coming Soon Open Recruitment LABGIS. Tungguin infonya guys!",
                image = null
            ),
            Post(
                id = 2,
                name = "Jennie",
                department = "Information System",
                time = "12:30 10/10/2024",
                content = "Bentar lagi ada lomba cerdas cermat nih gais jangan lupa daftar ya!",
                image = R.drawable.sample
            )
        )
    }
}
