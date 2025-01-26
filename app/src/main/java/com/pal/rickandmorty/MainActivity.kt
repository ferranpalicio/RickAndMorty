package com.pal.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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

                val viewModel: MainViewModel by viewModels()
                val listScreenStateState = viewModel.listState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.getCharacters(1)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (listScreenStateState.value.isLoading) {
                            CircularProgressIndicator()
                        } else if (listScreenStateState.value.isError) {
                            Text(
                                text = "Error",
                                modifier = Modifier.padding(innerPadding)
                            )
                        } else {
                            Text(text = "Success ${listScreenStateState.value.characters}")
                        }
                    }
                }
            }
        }
    }
}