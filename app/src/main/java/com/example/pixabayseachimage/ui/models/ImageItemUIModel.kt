package com.example.pixabayseachimage.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItemUIModel(
    val id: String = "",
    val username: String = "",
    val userPic: String = "",
    val thumbnail: String ="",
    val image: String = "",
    val likesCount: String = "",
    val commentsCount: String ="",
    val viewsCount: String = "",
    val downloadsCount: String = "",
    val tags: List<String> = emptyList(),
) : Parcelable
