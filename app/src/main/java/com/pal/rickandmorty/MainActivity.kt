package com.pal.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.pal.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {

                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()

                val characters: LazyPagingItems<Character> =
                    viewModel.characters.collectAsLazyPagingItems()

                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Rick and Morty")
                        }
                    }
                ) { innerPadding ->
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
                            val characterResult = if (pos > 0) {
                                Result.success(characters.itemSnapshotList.items[pos])
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