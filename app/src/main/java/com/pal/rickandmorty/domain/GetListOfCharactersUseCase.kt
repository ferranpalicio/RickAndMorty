package com.pal.rickandmorty.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.pal.rickandmorty.data.network.CharacterPagingSource
import javax.inject.Inject

class GetListOfCharactersUseCase @Inject constructor(
    private val characterPagingSource: CharacterPagingSource
) {
    companion object {
        const val MAX_ITEMS = 10
        const val PREFETCH_ITEMS = 5
    }

    operator fun invoke(): Pager<Int, Character> {
        return Pager(config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = { characterPagingSource })
    }
}

