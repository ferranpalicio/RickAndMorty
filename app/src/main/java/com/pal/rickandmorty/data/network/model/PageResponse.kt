package com.pal.rickandmorty.data.network.model

class PageResponse<T>(
    val info: Info,
    val results: List<T>
)

class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
