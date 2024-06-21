package com.bangkit.android.dermatify.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(
    @field:SerializedName("data")
    val data: AnalyzeResult
)

data class AnalyzeResult(
    val id: String,
    val createdAt: String,
    val issue: String,
    val score: Double
)

data class AnalyzeError(
    val status: String,
    val message: String
)