package com.kapmacs.pokemon.ui.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapmacs.pokemon.domain.model.PokemonDetails
import com.kapmacs.pokemon.domain.usecase.CatchPokemonUseCase
import com.kapmacs.pokemon.domain.usecase.GetPokemonDetailsUseCase
import com.kapmacs.pokemon.domain.usecase.IsPokemonCaughtUseCase
import com.kapmacs.pokemon.domain.usecase.ReleasePokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    private val isPokemonCaughtUseCase: IsPokemonCaughtUseCase,
    private val catchPokemonUseCase: CatchPokemonUseCase,
    private val releasePokemonUseCase: ReleasePokemonUseCase
) : ViewModel() {

    private val pokemonName: String = savedStateHandle.get<String>("pokemonName") ?: ""

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> = _pokemonDetails.asStateFlow()

    private val _isCaught = MutableStateFlow(false)
    val isCaught: StateFlow<Boolean> = _isCaught.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        if (pokemonName.isNotBlank()) {
            loadPokemonDetails()
            observeCaughtStatus()
        }
    }

    private fun loadPokemonDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            getPokemonDetailsUseCase(pokemonName).fold(
                onSuccess = { details -> _pokemonDetails.value = details },
                onFailure = { e -> _error.value = e.localizedMessage ?: "Error fetching details" }
            )
            _isLoading.value = false
        }
    }

    private fun observeCaughtStatus() {
        viewModelScope.launch {
            isPokemonCaughtUseCase(pokemonName).collect { caughtStatus ->
                _isCaught.value = caughtStatus
            }
        }
    }

    fun toggleCatchRelease() {
        viewModelScope.launch {
            val currentDetails = _pokemonDetails.value ?: return@launch
            _isLoading.value = true
            val currentIsCaught = _isCaught.first()
            val result = if (currentIsCaught) {
                releasePokemonUseCase(currentDetails.name)
            } else {
                catchPokemonUseCase(currentDetails.name)
            }
            result.onFailure { e ->
                 _error.value = e.localizedMessage ?: if(currentIsCaught) "Error releasing Pokémon" else "Error catching Pokémon"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun retryLoadDetails() {
        if (pokemonName.isNotBlank()) {
            loadPokemonDetails()
        }
    }
}
