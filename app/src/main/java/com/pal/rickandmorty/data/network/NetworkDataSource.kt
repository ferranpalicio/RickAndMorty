package com.pal.rickandmorty.data.network

import com.pal.rickandmorty.data.network.model.CharacterDTO
import javax.inject.Inject

interface NetworkDataSource {
    suspend fun getCharacters(page: Int): Result<List<CharacterDTO>>
}

class NetworkDataSourceImpl @Inject constructor(
    private val api: RickAndMortyApi
) : NetworkDataSource {
    override suspend fun getCharacters(page: Int): Result<List<CharacterDTO>> {
        val response = api.getCharacters(page)
        return response.mapCatching { it.results }
    }
}