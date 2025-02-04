package com.pal.rickandmorty.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pal.rickandmorty.data.network.model.toDomain
import com.pal.rickandmorty.domain.Character
import okio.IOException
import javax.inject.Inject


class CharacterPagingSource @Inject constructor(
    private val api: RickAndMortyApi
) : PagingSource<Int, Character>() {

    private var query = ""
    fun withQuery(query: String) {
        this.query = query
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = params.key ?: 1
            val response = api.getCharacters(query, page)
            val characters = response.results

            val prevKey = if (page > 0) page - 1 else null
            val nextKey = if (response.info.next != null) page + 1 else null

            LoadResult.Page(
                data = characters.map { it.toDomain() },
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            //todo map to app domain errors
            LoadResult.Error(exception)
        }
    }
}
