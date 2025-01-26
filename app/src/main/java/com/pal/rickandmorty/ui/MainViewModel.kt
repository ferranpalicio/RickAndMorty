package com.pal.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    /*
    val listState: StateFlow<ListScreenState>
        field = MutableStateFlow<ListScreenState>(ListScreenState())
        still in beta
     */
    private val _listState = MutableStateFlow(ListScreenState())
    val listState: StateFlow<ListScreenState> get() = _listState

    fun getCharacters(page: Int) {
        viewModelScope.launch {
            _listState.update {
                it.copy(isLoading = true, isError = false)
            }
            delay(2000)
            getListOfCharactersUseCase(page).fold(
                onSuccess = { characters ->
                    _listState.update {
                        it.copy(
                            characters = characters.map { it.toItemState() },
                            isLoading = false,
                            isError = false
                        )
                    }
                },
                onFailure = { throwable ->
                    _listState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            )
        }
    }
}