package com.pal.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    getListOfCharactersUseCase: GetListOfCharactersUseCase
) : ViewModel() {

    val characterSelectedTitle = MutableStateFlow<String?>(null)

    val queryFlow = MutableStateFlow("")

    val characters: Flow<PagingData<Character>> =
        queryFlow.flatMapLatest {
            getListOfCharactersUseCase.invoke(it)
                .flow
                .cachedIn(viewModelScope)
        }
}