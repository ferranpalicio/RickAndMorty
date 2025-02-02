package com.pal.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getListOfCharactersUseCase: GetListOfCharactersUseCase
) : ViewModel() {

    val characterSelectedTitle = MutableStateFlow("")

    val queryFlow = MutableStateFlow("")

    val characters: Flow<PagingData<Character>> =
        getListOfCharactersUseCase.invoke()
            .flow
            .cachedIn(viewModelScope)
            .combine(queryFlow) { pagingData, query ->
                if (query.isEmpty()) {
                    return@combine pagingData
                }
                pagingData.filter { it.name.contains(query, ignoreCase = true) }
            }
}