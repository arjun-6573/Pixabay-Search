package com.example.pixabayseachimage.ui.searchImages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixabayseachimage.domain.usecases.SearchImageUseCase
import com.example.pixabayseachimage.ui.mappers.UIDataMapper
import com.example.pixabayseachimage.utils.AppConstants
import com.example.pixabayseachimage.utils.dispatcher.MyDispatchers
import com.example.pixabayseachimage.ui.models.ImageItemUIModel
import com.example.pixabayseachimage.ui.models.PagingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val myDispatchers: MyDispatchers,
    private val uiDataMapper: UIDataMapper,
    private val searchImageUseCase: SearchImageUseCase,
) :
    ViewModel() {

    private val _state = MutableStateFlow(SearchImageState())
    val state: StateFlow<SearchImageState> = _state

    private val searchStream = MutableStateFlow<String?>(null)
    private var pageNo: Int = INITIAL_PAGE

    private var selectedItem: ImageItemUIModel? = null
    private var retry: (() -> Unit)? = null

    init {
        viewModelScope.launch {
            searchStream
                .debounce(AppConstants.SEARCH_DELAY)
                .distinctUntilChanged()
                .onStart {
                    onSearch("fruits")
                }
                .collectLatest { searchKey ->
                    searchKey?.let {
                        resetPage()
                        searchImages(it)
                    }
                }
        }
    }

    private fun searchImages(key: String) {
        retry = { searchImages(key) }
        viewModelScope.launch {
            val stateValue = _state.value
            val pagingState = if (pageNo == INITIAL_PAGE) {
                PagingState.LOADING
            } else {
                PagingState.APPENDING
            }

            if (stateValue.pagingState in arrayOf(
                    PagingState.IDLE,
                    PagingState.ERROR
                )
                && pageNo != INITIAL_PAGE
                || pageNo == INITIAL_PAGE
            ) {
                setState {
                    copy(pagingState = pagingState)
                }
                searchImageUseCase.invoke(
                    key,
                    pageNo
                )
                    .catch {
                        Timber.e(it)
                        setState { copy(pagingState = PagingState.ERROR) }
                    }
                    .map {
                        val state = if (it.size < AppConstants.PAGE_SIZE) {
                            PagingState.PAGINATION_EXHAUST
                        } else {
                            PagingState.IDLE
                        }
                        setState {
                            copy(
                                pagingState = state,
                                canPaginate = state != PagingState.PAGINATION_EXHAUST
                            )
                        }
                        uiDataMapper.toImageItemUIModel(it)
                    }
                    .flowOn(myDispatchers.computation)
                    .collect {
                        pageNo++
                        setState {
                            val images = images.toMutableList().apply {
                                addAll(it)
                            }
                            copy(
                                images = images,
                                isNoDataFound = images.isEmpty()
                            )
                        }
                    }
            }
        }
    }

    fun fetchNextImages() {
        searchImages(_state.value.searchKey)
    }

    private fun resetPage() {
        setState {
            copy(
                canPaginate = false,
                images = emptyList()
            )
        }
        pageNo = INITIAL_PAGE
        retry = null
    }

    fun onImageItemClick(item: ImageItemUIModel) {
        selectedItem = item
        setState {
            copy(isConfirmationDialogOpened = true)
        }
    }

    fun onDialogClick(confirm: Boolean) {
        if (confirm) {
            setState {
                copy(
                    isConfirmationDialogOpened = false,
                    imageDetails = selectedItem,
                )
            }
        } else {
            setState {
                copy(
                    isConfirmationDialogOpened = false,
                    imageDetails = null
                )
            }
        }
        selectedItem = null
    }

    fun onSearch(key: String) {
        searchStream.tryEmit(key)
        setState { copy(searchKey = key) }
    }

    fun onRetry() {
        retry?.invoke()
    }

    fun onImageDetailOpen() {
        selectedItem = null
        setState {
            copy(
                imageDetails = null,
            )
        }
    }

    private fun setState(reducer: SearchImageState.() -> SearchImageState) =
        viewModelScope
            .launch {
                _state.value = reducer(_state.value)
            }
}

const val INITIAL_PAGE = 1