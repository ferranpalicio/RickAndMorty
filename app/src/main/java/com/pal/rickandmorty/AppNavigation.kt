package com.pal.rickandmorty

const val DETAIL_ID_PARAM = "id"

sealed class NavigationItem(val route: String) {
    data object List : NavigationItem("list")
    data object Detail : NavigationItem("detail/{$DETAIL_ID_PARAM}")
}