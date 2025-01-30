package com.pal.rickandmorty.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.pal.rickandmorty.R
import com.pal.rickandmorty.domain.Character
import java.net.UnknownHostException

@Composable
fun ListOfCharacters(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .build()
    when {
        //Initial load
        characters.loadState.refresh is LoadState.Loading && characters.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp), color = Color.White
                )
            }
        }

        //Empty list
        characters.loadState.refresh is LoadState.NotLoading && characters.itemCount == 0 -> {
            Text(text = "No data")
        }

        //error
        characters.loadState.append is LoadState.Error -> {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                val e = characters.loadState.refresh as LoadState.Error
                val message = if (e.error is UnknownHostException) {
                    "Connection error"
                } else {
                    e.error.localizedMessage ?: "Unknown error"
                }
                Text(text = message)
            }
        }

        else -> {
            LazyColumn {
                items(characters.itemCount) { index ->
                    characters[index]?.let { characterModel ->
                        CharacterItem(
                            character = characterModel,
                            modifier = Modifier.clickable {
                                onCharacterClick(index)
                            },
                            imageLoader = imageLoader
                        )
                    }
                }
            }

            if (characters.loadState.append is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp), color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader?
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            imageLoader = imageLoader ?: LocalContext.current.imageLoader,
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.item_placeholder),
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .background(Color.White)
        ) {
            Text(text = character.name)
            Text(text = "${character.type} - ${character.type} - ${character.location}")
        }
    }
}

@Preview
@Composable
fun PreviewCharacterItem() {
    CharacterItem(
        character = Character(
            id = 1,
            name = "Rick",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        ), imageLoader = null
    )
}
