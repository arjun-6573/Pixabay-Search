package com.example.pixabayseachimage.ui.searchImages

import com.example.pixabayseachimage.domain.entity.ImageInfoEntity
import com.example.pixabayseachimage.domain.entity.UserEntity
import com.example.pixabayseachimage.domain.usecases.SearchImageUseCase
import com.example.pixabayseachimage.ui.mappers.UIDataMapper
import com.example.pixabayseachimage.ui.models.ImageItemUIModel
import com.example.pixabayseachimage.ui.models.PagingState
import com.example.pixabayseachimage.utils.dispatcher.MyTestDispatchersImpl
import com.example.pixabayseachimage.utils.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchImagesViewModelTest {
    var testCoroutineRule = TestCoroutineRule()
        @Rule get

    @InjectMockKs
    private lateinit var dispatcher: MyTestDispatchersImpl

    @InjectMockKs
    private lateinit var uiDataMapper: UIDataMapper

    @RelaxedMockK
    private lateinit var useCase: SearchImageUseCase
    private lateinit var searchViewModel: SearchImagesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchViewModel =
            SearchImagesViewModel(dispatcher, uiDataMapper, useCase)
    }

    @Test
    fun `test default search gives success result`() {
        testCoroutineRule.runBlockingTest {
            mockSuccess(10)
            val states = mutableListOf<SearchImageState>()
            searchViewModel.state.take(4).toList(states)
            advanceUntilIdle()
            states[0].run {
                assertEquals(PagingState.IDLE, pagingState)
                assertFalse(canPaginate)
                assert(images.isEmpty())
            }
            states[1].run {
                assertEquals(PagingState.LOADING, pagingState)
            }
            states[2].run {
                assertEquals(PagingState.IDLE, pagingState)
            }
            states[3].run {
                assertEquals(PagingState.IDLE, pagingState)
                assertEquals("1,1 k", images.first().likesCount)
                assertEquals("314,6 k", images.first().downloadsCount)
                assertEquals("268", images.first().commentsCount)
                assertEquals("#Vegetables", images.first().tags.first())
                assertEquals(10, images.size)
            }
        }
    }

    @Test
    fun `test searchImages gives success result`() {
        testCoroutineRule.runBlockingTest {
            mockSuccess(10)
            advanceUntilIdle()
            mockSuccess(2)
            searchViewModel.onSearch("test")
            searchViewModel.fetchNextImages()
            searchViewModel.state.value.run {
                assertEquals(PagingState.PAGINATION_EXHAUST, pagingState)
                assertEquals(12, images.size)
            }
        }
    }

    @Test
    fun `test searchImages gives error`() {
        testCoroutineRule.runBlockingTest {
            mockError()
            advanceUntilIdle()
            searchViewModel.onSearch("error")
            searchViewModel.state.value.run {
                assertEquals(PagingState.ERROR, pagingState)
                assertEquals(0, images.size)
            }
        }
    }

    @Test
    fun `test no data found state change`() {
        testCoroutineRule.runBlockingTest {
            mockError()
            searchViewModel.onSearch("error")
            advanceUntilIdle()
            searchViewModel.state.value.run {
                assertEquals(PagingState.ERROR, pagingState)
                assertEquals(0, images.size)
            }
        }
    }

    @Test
    fun `test page reset successfully`() {
        testCoroutineRule.runBlockingTest {
            mockSuccess(10)
            val states = mutableListOf<SearchImageState>()
            searchViewModel.onSearch("fruits")
            advanceUntilIdle()
            mockSuccess(12)
            searchViewModel.onSearch("test")
            searchViewModel.state.take(5).toList(states)
            states.last().run {
                assertEquals("test", searchKey)
                assertEquals(PagingState.IDLE, pagingState)
                assertEquals(12, images.size)
            }
        }
    }

    @Test
    fun `test next page loads successfully`() {
        testCoroutineRule.runBlockingTest {
            mockSuccess(10)
            advanceUntilIdle()
            searchViewModel.fetchNextImages()
            searchViewModel.state.value.run {
                assertEquals(PagingState.IDLE, pagingState)
                assertEquals(20, images.size)
            }
        }
    }

    @Test
    fun `test no api call once pagination ends`() {
        testCoroutineRule.runBlockingTest {
            mockSuccess(10)
            advanceUntilIdle()
            mockSuccess(2)
            searchViewModel.fetchNextImages()
            searchViewModel.fetchNextImages()
            searchViewModel.fetchNextImages()
            searchViewModel.state.value.run {
                assertEquals(PagingState.PAGINATION_EXHAUST, pagingState)
                assertEquals(12, images.size)
            }
            verify(exactly = 2) {
                useCase.invoke(any(), any())
            }
        }
    }

    @Test
    fun `test dialog shown on ImageItemClick`() {
        testCoroutineRule.runBlockingTest {
            val item = mockk<ImageItemUIModel>(relaxed = true)
            advanceUntilIdle()
            searchViewModel.onImageItemClick(item)
            searchViewModel.state.value.run {
                assert(isConfirmationDialogOpened)
            }
        }
    }

    @Test
    fun `test details page opens on confirming dialog`() {
        testCoroutineRule.runBlockingTest {
            val item = mockk<ImageItemUIModel>(relaxed = true)
            searchViewModel.onImageItemClick(item)
            advanceUntilIdle()
            searchViewModel.onDialogClick(true)
            searchViewModel.state.value.run {
                assertFalse(isConfirmationDialogOpened)
                assertEquals(item, imageDetails)
            }
        }
    }

    @Test
    fun `test details page not opens on canceling dialog`() {
        testCoroutineRule.runBlockingTest {
            val item = mockk<ImageItemUIModel>(relaxed = true)
            searchViewModel.onImageItemClick(item)
            advanceUntilIdle()
            searchViewModel.onDialogClick(false)
            searchViewModel.state.value.run {
                assertFalse(isConfirmationDialogOpened)
                assertEquals(null, imageDetails)
            }
        }
    }

    @Test
    fun `test state changes once image details open`() {
        testCoroutineRule.runBlockingTest {
            val item = mockk<ImageItemUIModel>(relaxed = true)
            searchViewModel.onImageItemClick(item)
            searchViewModel.onDialogClick(true)
            advanceUntilIdle()
            searchViewModel.onImageDetailOpen()
            searchViewModel.state.value.run {
                assertNull(imageDetails)
            }
        }
    }

    private fun mockSuccess(size: Int){
        val model = ImageInfoEntity(
            id = "",
            tags = listOf("vegetables","fruits", "food"),
            downloads = 314598,
            likes = 1075,
            comments = 268,
            user = UserEntity("","",""),
            thumbnail = "",
            image = "",
            views = 1000
        )
        val list: List<ImageInfoEntity> = mutableListOf<ImageInfoEntity>().apply {
            for(i in 0 until size){
                add(model)
            }
        }
        every {
            useCase.invoke(any(), any())
        } returns flowOf(list)
    }

    private fun mockError(){
        every {
            useCase.invoke(any(), any())
        } returns flow { throw Exception("Error") }
    }
}