package com.kapmacs.pokemon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaughtPokemonDao {
    @Query("SELECT * FROM caught_pokemon")
    fun getAllCaughtPokemon(): Flow<List<CaughtPokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun catchPokemon(pokemon: CaughtPokemonEntity)

    @Delete
    suspend fun releasePokemon(pokemon: CaughtPokemonEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM caught_pokemon WHERE name = :pokemonName LIMIT 1)")
    fun isPokemonCaught(pokemonName: String): Flow<Boolean>
}
