package com.pal.rickandmorty.ui

import com.pal.rickandmorty.domain.Character

data class ListScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val characters: List<CharacterItem> = emptyList()
)

data class CharacterItem(
    val name: String,
    val image: String,
    val subtitle: String
)

fun Character.toItemState() = CharacterItem(
    name = name,
    image = image,
    subtitle = "$status - $species - $type"
)