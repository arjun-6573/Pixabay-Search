package com.example.pixabayseachimage.data.repositories

import com.example.pixabayseachimage.data.datasources.FakeRemoteDataSourceImpl
import com.example.pixabayseachimage.data.mappers.DataMapper
import com.example.pixabayseachimage.utils.dispatcher.MyTestDispatchersImpl
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

internal class SearchRepositoryImplTest {
    @InjectMockKs
    private lateinit var dispatcher: MyTestDispatchersImpl

    @InjectMockKs
    private lateinit var remoteDataSource: FakeRemoteDataSourceImpl

    @InjectMockKs
    private lateinit var dataMapper: DataMapper

    @InjectMockKs
    private lateinit var repositoryImpl: SearchRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testSearchImage() = runBlocking {
        repositoryImpl
            .searchImage("", 1)
            .lastOrNull()
            .let {
                assertEquals(10, it?.size)
                assertEquals(3, it?.firstOrNull()?.tags?.size)
                assertEquals("dbreen", it?.firstOrNull()?.user?.name)
            }
    }

    @Test
    fun testSearchImageWithError() = runBlocking {
        repositoryImpl
            .searchImage("", 3)
            .catch {
                assertNotNull(it)
            }
            .collect {
                assert(false)
            }
    }
}