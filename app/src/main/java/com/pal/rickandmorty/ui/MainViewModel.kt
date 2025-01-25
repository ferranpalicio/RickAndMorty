package com.pal.rickandmorty.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pal.rickandmorty.data.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : ViewModel() {

    fun getCharacters(page: Int) {
        viewModelScope.launch {
            val result = networkDataSource.getCharacters(page)
            Log.i("MainViewModel", "getCharacters: $result")
        }
    }
}