package com.bangkit.android.dermatify.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(
    @field:SerializedName("data")
    val diagnosis: String,
    val description: String,
    val date: String
)