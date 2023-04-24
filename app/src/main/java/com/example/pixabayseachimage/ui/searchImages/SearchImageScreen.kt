package com.example.pixabayseachimage.ui.searchImages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pixabayseachimage.R
import com.example.pixabayseachimage.ui.MyDestinations
import com.example.pixabayseachimage.ui.component.HashTagWidget
import com.example.pixabayseachimage.ui.component.ImageStatusWidget
import com.example.pixabayseachimage.ui.component.LoadingWidget
import com.example.pixabayseachimage.ui.component.MyDialog
import com.example.pixabayseachimage.ui.component.MyToolbar
import com.example.pixabayseachimage.ui.component.SearchWidget
import com.example.pixabayseachimage.ui.component.UserInfoWidget
import com.example.pixabayseachimage.ui.imageDetails.DETAILS_DATA
import com.example.pixabayseachimage.ui.theme.PixabaySeachImageTheme
import com.example.pixabayseachimage.ui.models.ImageItemUIModel
import com.example.pixabayseachimage.ui.models.PagingState

@Composable
fun SearchImageScreen(
    isLandscapeMode: Boolean = false,
    viewModel: SearchImagesViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val state by viewModel.state.collectAsState()
    val lazyColumnListState = rememberLazyListState()

    val shouldStartPaginate by remember {
        derivedStateOf {
            state.canPaginate && state.pagingState == PagingState.IDLE
                    && (lazyColumnListState.layoutInfo.visibleItemsInfo
                .lastOrNull()?.index
                ?: (-VISIBLE_ITEM_COUNT - 1)) >= (lazyColumnListState
                .layoutInfo.totalItemsCount - VISIBLE_ITEM_COUNT)
        }
    }
    LaunchedEffect(key1 = shouldStartPaginate) {
        if (shouldStartPaginate)
            viewModel.fetchNextImages()
    }

    LaunchedEffect(key1 = state.imageDetails) {
        state.imageDetails?.let {
            viewModel.onImageDetailOpen()
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                set(DETAILS_DATA, state.imageDetails)
            }
            navController.navigate(MyDestinations.IMAGE_DETAIL_ROUTE)
        }
    }
    SearchImageScaffold(
        state = state,
        isLandscapeMode = isLandscapeMode,
        listState = lazyColumnListState,
        onItemClick = { viewModel.onImageItemClick(it) },
        onDialogClick = { viewModel.onDialogClick(it) },
        onSearch = { viewModel.onSearch(it) },
        onRetry = { viewModel.onRetry() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchImageScaffold(
    state: SearchImageState,
    isLandscapeMode: Boolean = false,
    listState: LazyListState = rememberLazyListState(),
    onItemClick: (item: ImageItemUIModel) -> Unit = {},
    onDialogClick: (positive: Boolean) -> Unit = {},
    onSearch: (key: String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyToolbar(
                backEnable = false,
                title = stringResource(id = R.string.app_name)
            )
        }
    ) { padding ->
        if (state.isConfirmationDialogOpened) {
            MyDialog(
                title = stringResource(id = R.string.confirmation_dialog_title),
                message = stringResource(id = R.string.confirmation_dialog_message),
                positiveText = stringResource(id = R.string.open),
                onPositiveClick = {
                    onDialogClick(true)
                },
                negativeText = stringResource(id = android.R.string.cancel),
                onNegativeClick = {
                    onDialogClick(false)
                },
                onDismiss = { })
        }

        Box(modifier = Modifier.fillMaxSize(1f)) {
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme
                                .colorScheme.primary
                        )
                        .padding(
                            start = 16.dp, end = 16.dp,
                            bottom = 16.dp
                        )
                ) {
                    SearchWidget(state.searchKey) {
                        onSearch(it)
                    }
                }
                SearchItemList(
                    listState,
                    state,
                    onItemClick,
                    onRetry,
                    isLandscapeMode,
                    Modifier.weight(1f)
                )
            }
            if (state.pagingState == PagingState.LOADING) {
                LoadingWidget(
                    message = stringResource(id = R.string.please_wait),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else
                if (state.isNoDataFound) {
                    ImageStatusWidget(
                        image = Icons.Default.Face,
                        text = stringResource(id = R.string.no_data_found),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
        }
    }
}

@Composable
private fun SearchItemList(
    listState: LazyListState,
    state: SearchImageState,
    onItemClick: (item: ImageItemUIModel) -> Unit,
    onRetry: () -> Unit,
    isLandscapeMode: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLandscapeMode) {
        LazyRow(modifier = modifier, state = listState) {
            items(state.images) {
                SearchItemWidget(
                    data = it,
                    modifier = Modifier
                        .width(250.dp)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ), onClick = { onItemClick(it) })
            }
            if (state.pagingState == PagingState.APPENDING) {
                item {
                    LoadingWidget(
                        message = stringResource(id = R.string.loading),
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                    )
                }
            }

            if (state.pagingState == PagingState.ERROR) {
                item {
                    ImageStatusWidget(
                        text = stringResource(id = R.string.retry),
                        image = Icons.Default.Refresh,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .clickable { onRetry() },
                    )
                }
            }
        }
    } else {
        LazyColumn(modifier = modifier, state = listState) {
            items(state.images) {
                SearchItemWidget(
                    data = it,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ), onClick = { onItemClick(it) })
            }
            if (state.pagingState == PagingState.APPENDING) {
                item {
                    LoadingWidget(
                        message = stringResource(id = R.string.loading),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            }

            if (state.pagingState == PagingState.ERROR) {
                item {
                    ImageStatusWidget(
                        text = stringResource(id = R.string.retry),
                        image = Icons.Default.Refresh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable { onRetry() },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItemWidget(
    data: ImageItemUIModel, modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(modifier = modifier.height(200.dp), onClick = onClick) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )

            AsyncImage(
                model = data.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Transparent)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black
                            )
                        ),
                    )
            )

            Column(
                Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                UserInfoWidget(userName = data.username, picture = data.userPic)
                Spacer(modifier = Modifier.height(8.dp))
                HashTagWidgetList(tags = data.tags)
            }
        }
    }
}

@Composable
fun HashTagWidgetList(tags: List<String>, modifier: Modifier = Modifier) {
    LazyRow(modifier) {
        items(tags) {
            HashTagWidget(
                tag = it,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchImageScaffold() {
    PixabaySeachImageTheme {
        SearchImageScaffold(
            SearchImageState(images = listOf(ImageItemUIModel())),
        )
    }
}

private const val VISIBLE_ITEM_COUNT = 5

