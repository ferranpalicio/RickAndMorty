package com.pal.rickandmorty.domain

import com.pal.rickandmorty.data.network.NetworkDataSource
import com.pal.rickandmorty.data.network.model.ApiFailure
import com.pal.rickandmorty.data.network.model.CharacterDTO
import com.pal.rickandmorty.data.network.model.toDomain
import com.pal.rickandmorty.data.network.model.toDomainFailure
import javax.inject.Inject

class GetListOfCharactersUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {

    suspend operator fun invoke(page: Int): Result<List<Character>> {
        return networkDataSource.getCharacters(page).fold(
            onSuccess = { characters ->
                Result.success(characters.map(CharacterDTO::toDomain))
            },
            onFailure = { throwable ->
                Result.failure(
                    (throwable as? ApiFailure)?.toDomainFailure() ?: AppFailure.UnknownError()
                )
            }
        )
    }
}

