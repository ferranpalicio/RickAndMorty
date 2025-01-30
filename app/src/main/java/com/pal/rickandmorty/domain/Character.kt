package com.pal.rickandmorty.domain

//todo map some fields from string to classes
data class Character(
    val id: Int,
    val name: String,
    val status: String = "Alive",
    val species: String = "Humanoid",
    val type: String = "Rick's Toxic",
    val image: String,
    val location: String = "Earth",
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
