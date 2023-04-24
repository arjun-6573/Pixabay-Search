package com.example.pixabayseachimage.data.datasources

import com.example.pixabayseachimage.data.remote.MyApi
import com.example.pixabayseachimage.utils.AppConstants
import com.example.pixabayseachimage.utils.dispatcher.MyDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: MyApi,
    private val dispatchers: MyDispatchers,
) : RemoteDataSource {

    override suspend fun searchImage(searchKey: String, pageNo: Int) =
        withContext(dispatchers.io) {
            api.searchImages(
                searchKey,
                pageNo,
                AppConstants.PAGE_SIZE
            )
        }
}
