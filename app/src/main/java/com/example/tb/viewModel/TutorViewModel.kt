package com.example.tb.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tb.ui.theme.Tutor

class TutorViewModel : ViewModel() {
    private val _tutors = mutableStateOf<List<Tutor>>(emptyList())
    val tutors: State<List<Tutor>> = _tutors

    init {
        loadTutors()
    }

    private fun loadTutors() {
        _tutors.value = listOf(
            Tutor(
                id = 1,
                name = "Kim Jisoo",
                department = "Information System",
                imageUrl = null
            ),
            Tutor(
                id = 2,
                name = "Bjorka",
                department = "Computer Scinece",
                imageUrl = null
            ),
            Tutor(
                id = 3,
                name = "Bjorka",
                department = "Computer Scinece",
                imageUrl = null
            ),
            Tutor(
                id = 4,
                name = "Bjorka",
                department = "Computer Scinece",
                imageUrl = null
            )
        )
    }
}