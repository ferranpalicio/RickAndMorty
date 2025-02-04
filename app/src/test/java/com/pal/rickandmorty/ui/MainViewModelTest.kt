package com.pal.rickandmorty.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.pal.rickandmorty.data.network.CharacterPagingSourceTest.Companion.mockListOfCharactersDTO
import com.pal.rickandmorty.data.network.model.toDomain
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.http.Query
import com.pal.rickandmorty.domain.Character as Character

class MainViewModelTest {
    private val mockGetListOfCharactersUseCase: GetListOfCharactersUseCase = mockk()
    private lateinit var sut: MainViewModel

    private fun mockPageResponse(initialKey: Int, query: String = "") =
        coEvery { mockGetListOfCharactersUseCase.invoke(query) } returns Pager(
            PagingConfig(pageSize = 1),
            initialKey,
            pagingSourceFactory = mockListOfCharactersDTO().filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }.map { it.toDomain() }
                .asPagingSourceFactory()
        )


    @Test
    fun `SHOULD return characters WHEN query is empty`() = runTest {

        val initialKey = 1
        mockPageResponse(initialKey)
        sut = MainViewModel(mockGetListOfCharactersUseCase)

        val items: Flow<PagingData<Character>> = sut.characters

        val itemsSnapshot: List<Character> = items.asSnapshot {
            scrollTo(initialKey)
        }

        assertEquals(mockListOfCharactersDTO().map { it.toDomain() }, itemsSnapshot)
    }

    @Test
    fun `SHOULD return filtered characters WHEN query is not empty`() = runTest {

        val initialKey = 0
        val query = "Rick"
        mockPageResponse(initialKey, query)
        sut = MainViewModel(mockGetListOfCharactersUseCase)
        sut.queryFlow.value = query

        val items: Flow<PagingData<Character>> = sut.characters

        val itemsSnapshot: List<Character> = items.asSnapshot {
            scrollTo(initialKey)
        }

        val expected =
            mockListOfCharactersDTO().filter { it.name.contains(query, ignoreCase = true) }
                .map { it.toDomain() }

        assertEquals(expected, itemsSnapshot)
    }

    @Test
    fun `SHOULD update characterSelectedTitle`() = runTest {
        val expectedTitle = "Rick Sanchez"
        mockPageResponse(1)
        sut = MainViewModel(mockGetListOfCharactersUseCase)
        sut.characterSelectedTitle.value = expectedTitle

        assertEquals(expectedTitle, sut.characterSelectedTitle.value)
    }

}
