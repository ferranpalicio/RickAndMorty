package com.pal.rickandmorty

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSourceFactory
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.testing.asPagingSourceFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pal.rickandmorty.data.network.model.CharacterDTO
import com.pal.rickandmorty.data.network.model.LocationDTO
import com.pal.rickandmorty.data.network.model.toDomain
import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.GetListOfCharactersUseCase
import com.pal.rickandmorty.ui.LIST_TEST_TAG
import com.pal.rickandmorty.ui.MainViewModel
import com.pal.rickandmorty.ui.theme.RickAndMortyTheme
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @SuppressWarnings("LongMethod")
    private fun mockListOfCharactersDTO(): List<CharacterDTO> = listOf(
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
        ), CharacterDTO(
            id = 5,
            name = "Jerry Smith",
            status = "Alive",
            species = "Human",
            type = "",
            image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
            location = LocationDTO("Earth"),
            gender = "Male"
        ), CharacterDTO(
            id = 6,
            name = "Birdperson",
            status = "Dead",
            species = "Bird-Person",
            type = "",
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
            location = LocationDTO("Bird World"),
            gender = "Male"
        ), CharacterDTO(
            id = 7,
            name = "Squanchy",
            status = "Alive",
            species = "Cat-Person",
            type = "",
            image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
            location = LocationDTO("Squanch"),
            gender = "Male"
        ), CharacterDTO(
            id = 8,
            name = "Mr. Meeseeks",
            status = "Unknown",
            species = "Meeseeks",
            type = "",
            image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg",
            location = LocationDTO("Unknown"),
            gender = "Male"
        ), CharacterDTO(
            id = 9,
            name = "Mr.Poopybutthole",
            status = "Alive",
            species = "Poopybutthole",
            type = "",
            image = "https://rickandmortyapi.com/api/character/avatar/9.jpeg",
            location = LocationDTO("Unknown"),
            gender = "Male"
        )
    )

    @Test
    fun testListOfItems() {
        val (navController, viewModel) =
            mockData(mockListOfCharactersDTO().map { it.toDomain() }
                .asPagingSourceFactory())

        composeTestRule.activity.setContent {
            RickAndMortyTheme {
                MainContent(
                    characterSelectedState = viewModel.characterSelectedTitle.collectAsStateWithLifecycle(),
                    viewModel = viewModel,
                    navController = navController,
                    characters = viewModel.characters.collectAsLazyPagingItems()
                )
            }
        }

        composeTestRule.onNodeWithTag(SEARCH_BAR_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LIST_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText("No data found").isNotDisplayed()
    }

    @Test
    fun emptyScreen() = runTest {
        val (navController, viewModel) =
            mockData(listOf<Character>().asPagingSourceFactory())

        composeTestRule.activity.setContent {
            RickAndMortyTheme {
                MainContent(
                    characterSelectedState = viewModel.characterSelectedTitle.collectAsStateWithLifecycle(),
                    viewModel = viewModel,
                    navController = navController,
                    characters = viewModel.characters.collectAsLazyPagingItems()
                )
            }
        }

        composeTestRule.onNodeWithTag(SEARCH_BAR_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LIST_TEST_TAG).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("No data found").isDisplayed()
    }

    private fun mockData(source: PagingSourceFactory<Int, Character>): Pair<TestNavHostController, MainViewModel> {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        val mockGetListOfCharactersUseCase: GetListOfCharactersUseCase = mockk()

        coEvery { mockGetListOfCharactersUseCase.invoke("") } returns Pager(
            PagingConfig(pageSize = 1), 1, pagingSourceFactory = source
        )

        val viewModel = MainViewModel(mockGetListOfCharactersUseCase)
        return Pair(navController, viewModel)
    }
}
