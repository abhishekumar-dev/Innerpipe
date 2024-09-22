package com.example.innerpipe.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.innertube.model.Comment

@Composable
fun Comments(
    uiState: PlayerUiState,
    playerViewModel: PlayerViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
        )
        HorizontalDivider()
        LazyColumn {
            items(uiState.comments) { comment ->
                Comment(comment)
                HorizontalDivider()
            }
            item {
                if (uiState.token != null) {
                    playerViewModel.comments(uiState.token)
                }
            }
        }
    }
}

@Composable
private fun Comment(comment: Comment, onRepliesClick: (String) -> Unit = {}) {
    Row {
        AsyncImage(
            model = comment.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(40.dp)
                .clip(CircleShape)
        )
        Column {
            Text(text = "${comment.author} ${comment.publishedTime}")
            Text(text = comment.comment)
            Text(text = "${comment.like} ${comment.reply} ${if (comment.isHearted) "❤️" else ""}")
            if (comment.tokenForReply != null) {
                Button({ onRepliesClick.invoke(comment.tokenForReply!!) }) {
                    Text("Show replies")
                }
            }
        }
    }
}