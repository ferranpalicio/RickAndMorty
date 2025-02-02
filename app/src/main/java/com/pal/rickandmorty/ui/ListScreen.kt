package com.pal.rickandmorty.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
import com.pal.rickandmorty.domain.Status
import com.pal.rickandmorty.useDebounce
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search a character",
    onSearch: (String) -> Unit = {}
) {
    // 1. text field state
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    // 2. debouncing
    textFieldValue.useDebounce {
        onSearch(it.text)
    }

    var isHintDisplayed by remember {
        mutableStateOf(true)
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    leadingIcon = {
                        if (isHintDisplayed) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = textFieldValue.text,
                    innerTextField = {
                        if (isHintDisplayed) {
                            Text(hint)
                        }
                        innerTextField()
                    },
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    trailingIcon = {
                        Icon(imageVector = Icons.Filled.Close,
                            contentDescription = "search",
                            modifier = Modifier.clickable {
                                textFieldValue = TextFieldValue("")
                                focusManager.clearFocus()
                            })
                    },
                    contentPadding = PaddingValues(0.dp),
                )
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                //.shadow(5.dp, CircleShape)
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                }
        )
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
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val borderColor = when (character.status) {
            Status.Alive -> Color.Green
            Status.Dead -> Color.Red
            else -> Color.Gray
        }
        AsyncImage(
            imageLoader = imageLoader ?: LocalContext.current.imageLoader,
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.item_placeholder),
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, borderColor, CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)

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
