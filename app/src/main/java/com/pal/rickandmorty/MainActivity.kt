package com.pal.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
import com.pal.rickandmorty.ui.CharacterToolbar
import com.pal.rickandmorty.ui.ListOfCharacters
import com.pal.rickandmorty.ui.MainViewModel
import com.pal.rickandmorty.ui.SearchBar
import com.pal.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

const val SEARCH_BAR_TEST_TAG = "searchBar"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {

                val viewModel: MainViewModel = hiltViewModel()
                val navController = rememberNavController()
                val characterSelectedState =
                    viewModel.characterSelectedTitle.collectAsStateWithLifecycle()

                MainContent(
                    characterSelectedState,
                    viewModel,
                    navController,
                    viewModel.characters.collectAsLazyPagingItems()
                )
            }
        }
    }
}

@Composable
fun MainContent(
    characterSelectedState: State<String?>,
    viewModel: MainViewModel,
    navController: NavHostController,
    characters: LazyPagingItems<Character>
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Surface(shadowElevation = 3.dp) {
                if (characterSelectedState.value.isNullOrBlank()) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(SEARCH_BAR_TEST_TAG),
                        hint = stringResource(R.string.search_hint)
                    ) {
                        viewModel.queryFlow.tryEmit(it.trim())
                    }
                } else {
                    CharacterToolbar(characterSelectedState)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = NavigationItem.List.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.List.route) {
                ListOfCharacters(characters = characters) { index ->
                    val route = NavigationItem.Detail.route.replace(
                        "{$DETAIL_POS_PARAM}",
                        index.toString()
                    )
                    viewModel.characterSelectedTitle.tryEmit(characters[index]?.name)
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
                    Result.success(characters.itemSnapshotList.items[pos])
                } else {
                    Result.failure(AppFailure.LocalAppFailure.NoDataError())
                }

                CharacterDetail(characterResult = characterResult,
                    onGoBack = {
                        viewModel.characterSelectedTitle.tryEmit(null)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
