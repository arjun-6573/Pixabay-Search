package com.example.pixabayseachimage.ui.searchImages

import com.example.pixabayseachimage.ui.models.ImageItemUIModel
import com.example.pixabayseachimage.ui.models.PagingState

data class SearchImageState(
    val pagingState: PagingState = PagingState.IDLE,
    val canPaginate: Boolean = false,
    val isNoDataFound: Boolean = false,
    val images: List<ImageItemUIModel> = emptyList(),
    val isConfirmationDialogOpened: Boolean = false,
    val searchKey: String = "",
    val imageDetails: ImageItemUIModel? = null
)
