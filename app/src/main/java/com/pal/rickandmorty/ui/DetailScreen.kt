package com.pal.rickandmorty.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.pal.rickandmorty.R
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.getStringRepresentation
import com.pal.rickandmorty.ui.theme.spacing
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CharacterDetail(
    characterResult: Result<Character>,
    onGoBack: () -> Unit
) {

    BackHandler {
        onGoBack()
    }

    characterResult.fold(
        onSuccess = { character ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .size(256)
                        .build(),
                    modifier = Modifier
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    text = stringResource(R.string.species, character.species),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    text = stringResource(R.string.character_type, character.type),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    text = stringResource(R.string.character_location, character.location),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    text = stringResource(
                        R.string.gender,
                        character.gender.getStringRepresentation()
                    ),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        onFailure = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.generic_error))
            }
        }
    )
}

@Composable
fun CharacterToolbar(characterSelectedState: State<String?>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = characterSelectedState.value ?: "",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailPreview() {
    Column {
        CharacterToolbar(characterSelectedState = MutableStateFlow("Rick").collectAsState())
        CharacterDetail(
            characterResult = Result.success(
                Character(
                    id = 1,
                    name = "Rick",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                )
            ),
            onGoBack = {}
        )
    }
}