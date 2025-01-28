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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pal.rickandmorty.ui.ListOfCharacters
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
                        composable(NavigationItem.List.route) { ListOfCharacters(navController) }
                        composable(NavigationItem.Detail.route) { CharacterDetail(navController) }
                    }
                }
            }
        }
    }

    @Composable
    private fun CharacterDetail(navController: NavHostController) {
        TODO("Not yet implemented")
    }

}