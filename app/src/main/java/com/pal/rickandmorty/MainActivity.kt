package com.pal.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pal.rickandmorty.domain.AppFailure
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.ui.CharacterDetail
import com.pal.rickandmorty.ui.ListOfCharacters
import com.pal.rickandmorty.ui.MainViewModel
import com.pal.rickandmorty.ui.SearchBar
import com.pal.rickandmorty.ui.theme.RickAndMortyTheme
import com.pal.rickandmorty.ui.theme.spacing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {

                val viewModel: MainViewModel = hiltViewModel()

                val characters: LazyPagingItems<Character> =
                    viewModel.characters.collectAsLazyPagingItems()

                val characterTitleSelected = viewModel.characterSelectedTitle.collectAsState()

                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
                    topBar = {
                        Surface(shadowElevation = 3.dp) {
                            if (characterTitleSelected.value.isEmpty()) {
                                SearchBar(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    hint = "Search a character"
                                ) {
                                    viewModel.queryFlow.tryEmit(it.trim())
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(MaterialTheme.spacing.medium)
                                ) {
                                    Text(
                                        text = characterTitleSelected.value,
                                        fontSize = 20.sp,
                                        color = Color.Black,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = NavigationItem.List.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavigationItem.List.route) {
                            ListOfCharacters(
                                characters = characters
                            ) { index ->
                                val route = NavigationItem.Detail.route.replace(
                                    "{$DETAIL_POS_PARAM}",
                                    index.toString()
                                )
                                navController.navigate(route)
                            }
                        }
                        composable(
                            NavigationItem.Detail.route,
                            arguments = listOf(navArgument(DETAIL_POS_PARAM) {
                                type = NavType.IntType
                            })
                        ) {
                            val pos = it.arguments?.getInt(DETAIL_POS_PARAM, -1) ?: -1
                            val characterResult = if (pos >= 0) {
                                val character = characters.itemSnapshotList.items[pos]
                                viewModel.characterSelectedTitle.tryEmit(character.name)
                                Result.success(character)
                            } else {
                                Result.failure(AppFailure.LocalAppFailure.NoDataError())
                            }

                            CharacterDetail(characterResult = characterResult)
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewSearchBar() {
    RickAndMortyTheme {
        SearchBar()
    }
}

@Composable
fun <T> T.useDebounce(
    delayMillis: Long = 300L,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onChange: (T) -> Unit
): T {
    val state by rememberUpdatedState(this)

    DisposableEffect(state) {
        val job = coroutineScope.launch {
            delay(delayMillis)
            onChange(state)
        }
        onDispose {
            job.cancel()
        }
    }
    return state
}