package com.bangkit.android.dermatify.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticlesResponse(
    @field:SerializedName("data")
    val articles: List<ArticlesItem>
)

@Parcelize
data class ArticlesItem(
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("subtitle")
    val subtitle: String?,
    @field:SerializedName("original_link")
    val newsUrl: String?,
    @field:SerializedName("thumbnail")
    val thumbnail: String?
) : Parcelable
