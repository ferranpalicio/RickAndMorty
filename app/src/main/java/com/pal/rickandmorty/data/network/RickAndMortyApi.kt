package com.pal.rickandmorty.data.network

import com.pal.rickandmorty.data.network.model.CharacterDTO
import com.pal.rickandmorty.data.network.model.PageResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://rickandmortyapi.com/api/"

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): PageResponse<CharacterDTO>
}