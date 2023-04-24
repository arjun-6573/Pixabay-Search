package com.example.pixabayseachimage.domain.repositories

import com.example.pixabayseachimage.domain.entity.ImageInfoEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchImage(
        searchKey: String,
        pageNo: Int
    ): Flow<List<ImageInfoEntity>>
}