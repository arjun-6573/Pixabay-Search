package com.example.pixabayseachimage.data.datasources

import com.example.pixabayseachimage.data.remote.reponse.SearchAPIResponse

interface RemoteDataSource {
    suspend fun searchImage(searchKey: String, pageNo: Int): SearchAPIResponse
}