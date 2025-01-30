package com.pal.rickandmorty.ui

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import com.pal.rickandmorty.domain.Character as Character

@Composable
fun CharacterDetail(
    characterResult: Result<Character>,
) {
    Log.i("TAG", "CharacterDetail")
    characterResult.fold(
        onSuccess = { character ->
            Text(text = character.name)
        },
        onFailure = {
            Text(text = "Error")
        }
    )

}