package com.example.pixabayseachimage.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pixabayseachimage.R
import com.example.pixabayseachimage.ui.theme.PixabaySeachImageTheme

@Composable
fun HashTagWidget(tag: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun UserInfoWidget(
    modifier: Modifier = Modifier,
    userName: String,
    picture: String,
    textColor: Color = Color.White,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        AsyncImage(
            model = picture,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = userName, color = textColor)
    }
}

@Composable
fun LoadingWidget(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message)
    }
}

@Composable
fun ImageStatusWidget(
    modifier: Modifier = Modifier,
    image: ImageVector,
    text: String,
    tintColor: Color = LocalContentColor.current,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = image, contentDescription = null, tint = tintColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImageStatusWidget() {
    PixabaySeachImageTheme {
        ImageStatusWidget(
            text = stringResource(id = R.string.retry),
            image = Icons.Default.Refresh,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingWidget() {
    PixabaySeachImageTheme {
        LoadingWidget(
            message = stringResource(id = R.string.please_wait),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserInfoWidget() {
    PixabaySeachImageTheme {
        UserInfoWidget(
            userName = "User Name",
            picture = "",
            textColor = Color.Black
        )
    }
}

@Preview
@Composable
fun PreviewHashTagWidget() {
    PixabaySeachImageTheme {
        HashTagWidget(
            tag = "#tag"
        )
    }
}