package com.example.pixabayseachimage.data.repositories

import com.example.pixabayseachimage.data.datasources.RemoteDataSource
import com.example.pixabayseachimage.data.mappers.DataMapper
import com.example.pixabayseachimage.domain.entity.ImageInfoEntity
import com.example.pixabayseachimage.domain.repositories.SearchRepository
import com.example.pixabayseachimage.utils.dispatcher.MyDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatchers: MyDispatchers,
    private val mapper: DataMapper
) : SearchRepository {

    override fun searchImage(
        searchKey: String,
        pageNo: Int
    ): Flow<List<ImageInfoEntity>> = flow {
        emit(remoteDataSource.searchImage(searchKey, pageNo))
    }.map {
        mapper.toSearchImagePageData(it)
    }.flowOn(dispatchers.computation)
}