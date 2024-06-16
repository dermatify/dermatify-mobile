package com.bangkit.android.dermatify.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticlesResponse(
    @field:SerializedName("articles")
    val articles: List<ArticlesItem>
)

@Parcelize
data class ArticlesItem(
    @field:SerializedName("urlToImage")
    val imgUrl: String?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("url")
    val newsUrl: String?
) : Parcelable
