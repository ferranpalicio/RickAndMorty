package com.pal.rickandmorty.data.network.model

import com.pal.rickandmorty.domain.Character

class CharacterDTO(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val image: String,
    val location: LocationDTO
)

class LocationDTO(val name: String)

fun CharacterDTO.toDomain() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    image = image,
    location = location.name
)

/**
 *  {
 *       "id": 361,
 *       "name": "Toxic Rick",
 *       "status": "Dead",
 *       "species": "Humanoid",
 *       "type": "Rick's Toxic Side",
 *       "gender": "Male",
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