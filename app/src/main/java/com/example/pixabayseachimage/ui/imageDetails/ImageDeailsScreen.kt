package com.example.pixabayseachimage.ui.imageDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pixabayseachimage.R
import com.example.pixabayseachimage.ui.component.ImageStatusWidget
import com.example.pixabayseachimage.ui.component.MyToolbar
import com.example.pixabayseachimage.ui.component.UserInfoWidget
import com.example.pixabayseachimage.ui.theme.PixabaySeachImageTheme
import com.example.pixabayseachimage.ui.models.ImageItemUIModel
import com.example.pixabayseachimage.ui.searchImages.HashTagWidgetList

@Composable
fun ImageDetailsScreen(
    isLandscapeMode: Boolean = false,
    navController: NavHostController = rememberNavController(),
) {
    navController.previousBackStackEntry
        ?.savedStateHandle?.get<ImageItemUIModel>(
            DETAILS_DATA
        )?.let {
        ImageDetailsScaffold(
            isLandscapeMode = isLandscapeMode,
            state = it,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScaffold(
    isLandscapeMode: Boolean = false,
    state: ImageItemUIModel,
    navController: NavHostController = rememberNavController(),
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            MyToolbar(
                backEnable = true,
                title = stringResource(id = R.string.details),
                navController = navController
            )
        },
    ) { padding ->
        if (isLandscapeMode) {
            HorizontalLayout(
                state = state, modifier = Modifier
                    .padding(padding)
                    .padding(
                        horizontal = 8.dp
                    )
                    .verticalScroll(scrollState)
            )
        } else {
            VerticalLayout(
                state = state, modifier = Modifier
                    .padding(padding)
                    .padding(
                        horizontal = 8.dp
                    )
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Composable
private fun VerticalLayout(
    state: ImageItemUIModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        UserInfoWidget(
            userName = state.username,
            picture = state.userPic,
            textColor = LocalContentColor.current,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box(Modifier.wrapContentSize()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
            AsyncImage(
                model = state.thumbnail,
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
            AsyncImage(
                model = state.image,
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HashTagWidgetList(tags = state.tags)

        Spacer(modifier = Modifier.height(8.dp))
        ImageStatusSection(
            uiModel = state,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun HorizontalLayout(
    state: ImageItemUIModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Box(
            Modifier
                .weight(2f)
                .padding(8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
            AsyncImage(
                model = state.image,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        }
        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)) {
            UserInfoWidget(
                userName = state.username,
                picture = state.userPic,
                textColor = Color.Black,
            )
            Spacer(modifier = Modifier.height(8.dp))
            HashTagWidgetList(tags = state.tags)

            Spacer(modifier = Modifier.height(8.dp))
            ImageStatusSection(
                uiModel = state,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ImageStatusSection(
    uiModel: ImageItemUIModel,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        ImageStatusWidget(
            image = Icons.Default.Favorite,
            text = uiModel.likesCount,
            tintColor = Color.Red,
            modifier = Modifier.weight(1f)
        )
        ImageStatusWidget(
            image = Icons.Default.Download,
            text = uiModel.downloadsCount,
            modifier = Modifier.weight(1f)
        )
        ImageStatusWidget(
            image = Icons.Default.Comment,
            text = uiModel.commentsCount,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewSearchImageScaffold() {
    PixabaySeachImageTheme {
        ImageDetailsScaffold(
            isLandscapeMode = false,
            ImageItemUIModel(),
        )
    }
}


const val DETAILS_DATA = "DETAILS_DATA"