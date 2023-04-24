package com.example.pixabayseachimage.data.remote

import com.example.pixabayseachimage.data.remote.reponse.SearchAPIResponse
import retrofit2.http.*


interface MyApi {
    @GET("?image_type=photo")
    suspend fun searchImages(
        @Query("q") searchKey: String,
        @Query("page") pageNo: Int,
        @Query("per_page") pageSize: Int
    ): SearchAPIResponse
}