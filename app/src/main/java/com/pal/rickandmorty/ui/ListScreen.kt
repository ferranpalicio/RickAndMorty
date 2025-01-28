package com.pal.rickandmorty.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.pal.rickandmorty.R

@Composable
fun ListOfCharacters(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val listScreenStateState = viewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCharacters(1)
    }

    if (listScreenStateState.value.isLoading) {
        Text(text = "Loading...")
    } else if (listScreenStateState.value.isError) {
        Text(text = "Error")
    } else {
        val imageLoader = LocalContext.current.imageLoader.newBuilder()
            .logger(DebugLogger())
            .build()

        val items = listScreenStateState.value.characters
        LazyColumn {
            items(
                count = items.size,
                key = { items[it].id }) { index ->
                CharacterItem(items[index], imageLoader)
            }
        }
    }
}

@Composable
fun CharacterItem(character: CharacterItem, imageLoader: ImageLoader?) {
    Row(
        modifier = Modifier
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
            Text(text = character.subtitle)
        }
    }
}

@Preview
@Composable
fun PreviewCharacterItem() {
    CharacterItem(
        character = CharacterItem(
            id = 1,
            name = "Rick",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            subtitle = "Alive - Human - Scientist"
        ), null
    )
}
