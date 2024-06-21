package com.bangkit.android.dermatify.ui.examineresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.repository.JourneyRepository
import kotlinx.coroutines.launch


class ExamineResultViewModel(private val repo: JourneyRepository) : ViewModel() {

    fun saveExamineResult(picUri: String, diagnosis: String, createdAt: String, diagnosisDesc: String) {
        val scan = Scans(
            imageUri = picUri,
            diagnosis = diagnosis,
            timestamp = createdAt,
            description = diagnosisDesc
        )
        viewModelScope.launch {
            repo.addHistory(scan)
        }
    }
}