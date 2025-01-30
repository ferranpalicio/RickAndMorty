package com.pal.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pal.rickandmorty.domain.AppFailure
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getListOfCharactersUseCase: GetListOfCharactersUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow(ListScreenState())
    val listState: StateFlow<ListScreenState> get() = _listState

    fun getCharacters(page: Int) {
        viewModelScope.launch {
            _listState.update {
                it.copy(isLoading = true, isError = false)
            }
            delay(2000)
            getListOfCharactersUseCase(page).fold(onSuccess = { characters ->
                _listState.update { state ->
                    state.copy(
                        characters = characters, isLoading = false, isError = false
                    )
                }
            }, onFailure = { throwable ->
                _listState.update {
                    it.copy(
                        isLoading = false, isError = true
                    )
                }
            })
        }
    }

    fun getCharacterDetail(id: Int): Result<Character> {
        return _listState.value.characters.find { it.id == id }?.let {
            Result.success(it)
        } ?: Result.failure(AppFailure.LocalAppFailure.NoDataError())
    }
}