package com.pal.rickandmorty.data.network.model

import com.pal.rickandmorty.domain.Character
import com.pal.rickandmorty.domain.Gender
import com.pal.rickandmorty.domain.Status

class CharacterDTO(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val image: String,
    val location: LocationDTO,
    val gender: String
)

class LocationDTO(val name: String)

fun CharacterDTO.toDomain() = Character(
    id = id,
    name = name,
    status = toStatus(status),
    species = species,
    type = type.ifEmpty { species },
    image = image,
    gender = toGender(gender),
    location = location.name
)

private fun toGender(gender: String) = when (gender) {
    "Female" -> Gender.Female
    "Male" -> Gender.Male
    "Genderless" -> Gender.Genderless
    else -> Gender.Unknown
}

private fun toStatus(status: String) = when (status) {
    "Alive" -> Status.Alive
    "Dead" -> Status.Dead
    else -> Status.Unknown
}

/**
 *  {
 *       "id": 361,
 *       "name": "Toxic Rick",
 *       "status": "Dead", //'Alive', 'Dead' or 'unknown'
 *       "species": "Humanoid",
 *       "type": "Rick's Toxic Side",
 *       "gender": "Male", //'Female', 'Male', 'Genderless' or 'unknown'
 *       "origin": {
 *         "name": "Alien Spa",
 *         "url": "https://rickandmortyapi.com/api/location/64"
 *       },
 *       "location": {
 *         "name": "Earth",
 *         "url": "https://rickandmortyapi.com/api/location/20"
 *       },
 *       "image": "https://rickandmortyapi.com/api/character/avatar/361.jpeg",
 *       "episode": [
 *         "https://rickandmortyapi.com/api/episode/27"
 *       ],
 *       "url": "https://rickandmortyapi.com/api/character/361",
 *       "created": "2018-01-10T18:20:41.703Z"
 *     }
 * */