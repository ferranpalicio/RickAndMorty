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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

                LaunchedEffect(Unit) {
                    viewModel.getCharacters(1)
                }

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
                                listScreenState = viewModel.listState.collectAsStateWithLifecycle().value
                            ) {
                                val route = NavigationItem.Detail.route.replace(
                                    "{$DETAIL_ID_PARAM}",
                                    it.toString()
                                )
                                navController.navigate(route)
                            }
                        }
                        composable(
                            NavigationItem.Detail.route,
                            arguments = listOf(navArgument(DETAIL_ID_PARAM) {
                                type = NavType.IntType
                            })
                        ) {
                            val id: Int = it.arguments?.getInt(DETAIL_ID_PARAM, 0) ?: 0
                            CharacterDetail(
                                characterResult = viewModel.getCharacterDetail(id)
                            )
                        }
                    }
                }
            }
        }
    }

}