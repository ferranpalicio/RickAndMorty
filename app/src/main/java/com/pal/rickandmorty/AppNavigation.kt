package com.pal.rickandmorty

const val DETAIL_POS_PARAM = "pos"

sealed class NavigationItem(val route: String) {
    data object List : NavigationItem("list")
    data object Detail : NavigationItem("detail/{$DETAIL_POS_PARAM}")
}