package com.pal.rickandmorty.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import com.pal.rickandmorty.data.network.model.CharacterDTO
import com.pal.rickandmorty.data.network.model.Info
import com.pal.rickandmorty.data.network.model.LocationDTO
import com.pal.rickandmorty.data.network.model.PageResponse
import com.pal.rickandmorty.data.network.model.toDomain
import com.pal.rickandmorty.domain.Character
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterPagingSourceTest {

    private val mockApi: RickAndMortyApi = mockk()

    private lateinit var sut: CharacterPagingSource

    companion object {
        fun mockListOfCharactersDTO(): List<CharacterDTO> = listOf(
            CharacterDTO(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                location = LocationDTO("Earth"),
                gender = "Male"
            ), CharacterDTO(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                location = LocationDTO("Earth"),
                gender = "Male"
            ), CharacterDTO(
                id = 3,
                name = "Summer Smith",
                status = "Alive",
                species = "Human",
                type = "",
                image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
                location = LocationDTO("Earth"),
                gender = "Female"
            ), CharacterDTO(
                id = 4,
                name = "Beth Smith",
                status = "Alive",
                species = "Human",
                type = "",
                image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
                location = LocationDTO("Earth"),
                gender = "Female"
            )
        )

        val mockedList = mockListOfCharactersDTO()
        val nextResponse = PageResponse(
            info = Info(mockedList.size, 10, "next", "prev"), results = mockedList
        )
    }

    @Before
    fun setup() {
        sut = CharacterPagingSource(mockApi)
    }

    @Test
    fun `SHOULD return correct page WHEN api correct response`() = runTest {
        val key = 1
        coEvery { mockApi.getCharacters(key) } returns nextResponse

        val expectedResult = LoadResult.Page(
            data = nextResponse.results.map { it.toDomain() },
            prevKey = key - 1,
            nextKey = key + 1
        )

        assertEquals(
            expectedResult,
            sut.load(PagingSource.LoadParams.Append(key, 1, false))
        )
    }

    @Test
    fun `SHOULD return LoadResult error WHEN api throws an error`() = runTest {
        val error = IOException("404", Throwable())
        coEvery { mockApi.getCharacters(1) } throws error

        val expectedResult = LoadResult.Error<Int, Character>(error)

        assertEquals(
            expectedResult,
            sut.load(PagingSource.LoadParams.Append(1, 1, false))
        )
    }

}