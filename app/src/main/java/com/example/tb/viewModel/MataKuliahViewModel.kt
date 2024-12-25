package com.example.tb.viewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class MataKuliahViewModel : ViewModel() {

    private val _mataKuliahData = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val mataKuliahData: State<Map<String, List<String>>> = _mataKuliahData

    fun loadData(department: String) {
        _mataKuliahData.value = when (department) {
            "Information System" -> mapOf(
                "Semester 1" to listOf("Introduction to IS", "Programming 101"),
                "Semester 2" to listOf("Database Systems", "Web Development")
            )
            "Computer Science" -> mapOf(
                "Semester 1" to listOf("Algorithms", "Data Structures"),
                "Semester 2" to listOf("Operating Systems", "Networks")
            )
            "Informatics" -> mapOf(
                "Semester 1" to listOf("Discrete Math", "Introduction to AI"),
                "Semester 2" to listOf("Machine Learning", "Mobile Development")
            )
            else -> emptyMap()
        }
    }
}
