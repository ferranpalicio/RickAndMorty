package com.pal.rickandmorty.data.network.model

import com.pal.rickandmorty.domain.Gender
import com.pal.rickandmorty.domain.Status
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

const val ID: Int = 361
const val NAME: String = "Name"
const val IMAGE: String = "https://rickandmortyapi.com/api/character/avatar/361.jpeg"
const val LOCATION: String = "Earth"

const val GENDER_MALE: String = "Male"
const val GENDER_FEMALE: String = "Female"
const val GENDER_GENDERLESS: String = "Genderless"
const val GENDER_UNKNKOWN: String = "dunno"

const val STATUS_ALIVE: String = "Alive"
const val STATUS_DEAD: String = "Dead"
const val STATUS_UNKNOWN: String = "who knows"

@RunWith(Parameterized::class)
class CharacterDTOTest(
    private val status: String,
    private val gender: String,
    private val type: String,
    private val species: String,
    private val characterDTO: CharacterDTO,
    private val expectedStatus: Status,
    private val expectedGender: Gender,
    private val expectedType: String
) {

    @Test
    fun `SHOULD convert CharacterDTO to Character`() {
        with(characterDTO.toDomain()) {
            Assert.assertEquals(status, expectedStatus)
            Assert.assertEquals(gender, expectedGender)
            Assert.assertEquals(type, expectedType)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(
                    STATUS_DEAD,//status
                    GENDER_MALE,//gender
                    "Rick's Toxic Side",//type
                    "Humanoid",//species
                    CharacterDTO(
                        ID,
                        NAME,
                        STATUS_DEAD,
                        "Humanoid",
                        "Rick's Toxic Side",
                        IMAGE,
                        LocationDTO(LOCATION),
                        GENDER_MALE,
                    ),
                    Status.Dead,
                    Gender.Male,
                    "Rick's Toxic Side"
                ),
                arrayOf(
                    STATUS_ALIVE,//status
                    GENDER_MALE,//gender
                    "Rick's Toxic Side",//type
                    "Humanoid",//species
                    CharacterDTO(
                        ID,
                        NAME,
                        STATUS_ALIVE,
                        "Humanoid",
                        "Rick's Toxic Side",
                        IMAGE,
                        LocationDTO(LOCATION),
                        GENDER_FEMALE,
                    ),
                    Status.Alive,
                    Gender.Female,
                    "Rick's Toxic Side"
                ),
                arrayOf(
                    STATUS_UNKNOWN,//status
                    GENDER_GENDERLESS,//gender
                    "Rick's Toxic Side",//type
                    "Humanoid",//species
                    CharacterDTO(
                        ID,
                        NAME,
                        STATUS_UNKNOWN,
                        "Humanoid",
                        "Rick's Toxic Side",
                        IMAGE,
                        LocationDTO(LOCATION),
                        GENDER_GENDERLESS,
                    ),
                    Status.Unknown,
                    Gender.Genderless,
                    "Rick's Toxic Side"
                ),
                arrayOf(
                    STATUS_ALIVE,//status
                    GENDER_UNKNKOWN,//gender
                    "",//type
                    "Humanoid",//species
                    CharacterDTO(
                        ID,
                        NAME,
                        STATUS_ALIVE,
                        "Humanoid",
                        "",
                        IMAGE,
                        LocationDTO(LOCATION),
                        GENDER_UNKNKOWN,
                    ),
                    Status.Alive,
                    Gender.Unknown,
                    "Humanoid"
                ),
                // Add more test cases here if needed
            )
        }
    }
}