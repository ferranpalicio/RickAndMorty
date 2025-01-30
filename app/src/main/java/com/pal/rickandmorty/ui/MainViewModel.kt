package com.pal.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getListOfCharactersUseCase: GetListOfCharactersUseCase
) : ViewModel() {

    val characters: Flow<PagingData<Character>> =
        getListOfCharactersUseCase.invoke().flow.cachedIn(viewModelScope)
}