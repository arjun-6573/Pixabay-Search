package com.example.pixabayseachimage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.pixabayseachimage.R
import com.example.pixabayseachimage.ui.theme.PixabaySeachImageTheme

@Composable
fun MyDialog(
    title: String,
    message: String,
    positiveText: String,
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit,
    negativeText: String? = null,
    onNegativeClick: (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = { }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight =
                FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                negativeText?.let {
                    TextButton(onClick = {
                        onNegativeClick?.invoke()
                        onDismiss()
                    }) {
                        Text(text = it)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    onPositiveClick()
                    onDismiss()
                }) {
                    Text(text = positiveText)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMyDialog(){
    PixabaySeachImageTheme {
        MyDialog(
            title = stringResource(id = R.string.confirmation_dialog_title),
            message = stringResource(id = R.string.confirmation_dialog_message),
            positiveText = stringResource(id = R.string.open),
            onPositiveClick = { /*TODO*/ },
            negativeText = stringResource(id = android.R.string.cancel),
            onDismiss = { /*TODO*/ })
    }
}