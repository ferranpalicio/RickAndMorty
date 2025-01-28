package com.pal.rickandmorty

enum class Screen {
    LIST,
    DETAIL,
}

sealed class NavigationItem(val route: String) {
    data object List : NavigationItem(Screen.LIST.name)
    data object Detail : NavigationItem(Screen.DETAIL.name)
}