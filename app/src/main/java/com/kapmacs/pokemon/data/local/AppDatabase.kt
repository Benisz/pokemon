package com.kapmacs.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CaughtPokemonEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun caughtPokemonDao(): CaughtPokemonDao
}
