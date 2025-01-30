package com.pal.rickandmorty.ui

import com.pal.rickandmorty.domain.Character

data class ListScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val characters: List<Character> = emptyList()
)
