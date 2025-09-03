package com.kapmacs.pokemon.ui.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapmacs.pokemon.domain.model.PokemonListItem
import com.kapmacs.pokemon.domain.model.PokemonType
import com.kapmacs.pokemon.domain.usecase.CatchPokemonUseCase
import com.kapmacs.pokemon.domain.usecase.GetAllPokemonsUseCase
import com.kapmacs.pokemon.domain.usecase.GetCaughtPokemonsUseCase
import com.kapmacs.pokemon.domain.usecase.GetPokemonTypesUseCase
import com.kapmacs.pokemon.domain.usecase.GetPokemonsByTypeUseCase
import com.kapmacs.pokemon.domain.usecase.ReleasePokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonTypesUseCase: GetPokemonTypesUseCase,
    private val getPokemonsByTypeUseCase: GetPokemonsByTypeUseCase,
    private val getAllPokemonsUseCase: GetAllPokemonsUseCase,
    getCaughtPokemonsUseCase: GetCaughtPokemonsUseCase,
    private val catchPokemonUseCase: CatchPokemonUseCase,
    private val releasePokemonUseCase: ReleasePokemonUseCase
) : ViewModel() {

    private val _pokemonTypes = MutableStateFlow<List<PokemonType>>(emptyList())
    val pokemonTypes: StateFlow<List<PokemonType>> = _pokemonTypes.asStateFlow()

    private val _selectedPokemonType = MutableStateFlow<PokemonType?>(null)
    val selectedPokemonType: StateFlow<PokemonType?> = _selectedPokemonType.asStateFlow()

    private val _allPokemonsSource = MutableStateFlow<List<PokemonListItem>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showOnlyCaught = MutableStateFlow(false)
    val showOnlyCaught: StateFlow<Boolean> = _showOnlyCaught.asStateFlow()

    private val _isLoadingTypes = MutableStateFlow(false)
    val isLoadingTypes: StateFlow<Boolean> = _isLoadingTypes.asStateFlow()

    private val _isLoadingPokemons = MutableStateFlow(false)
    val isLoadingPokemons: StateFlow<Boolean> = _isLoadingPokemons.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val caughtPokemonNames: StateFlow<Set<String>> = getCaughtPokemonsUseCase()
        .map { namesList -> namesList.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptySet())

    val filteredPokemons: StateFlow<List<PokemonListItem>> = combine(
        _allPokemonsSource, _searchQuery, _showOnlyCaught, caughtPokemonNames
    ) { pokemons, query, showCaught, caughtSet ->
        val searched = if (query.isBlank()) {
            pokemons
        } else {
            pokemons.filter { it.name.contains(query, ignoreCase = true) }
        }
        if (showCaught) {
            searched.filter { caughtSet.contains(it.name) }
        } else {
            searched
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        loadPokemonTypes()
        selectPokemonType(null)
    }

    fun loadPokemonTypes() {
        viewModelScope.launch {
            _isLoadingTypes.value = true
            getPokemonTypesUseCase().fold(
                onSuccess = { types ->
                    _pokemonTypes.value = types
                },
                onFailure = { e ->
                    _error.value = e.localizedMessage ?: "Error fetching types"
                }
            )
            _isLoadingTypes.value = false
        }
    }

    fun selectPokemonType(type: PokemonType?) {
        _selectedPokemonType.value = type
        if (type == null) {
            loadAllPokemons()
        } else {
            loadPokemonsByType(type.name)
        }
    }

    private fun loadAllPokemons() {
        viewModelScope.launch {
            _isLoadingPokemons.value = true
            _error.value = null
            getAllPokemonsUseCase().fold(
                onSuccess = { pokemons -> _allPokemonsSource.value = pokemons },
                onFailure = { e -> _error.value = e.localizedMessage ?: "Error fetching all Pokémon" }
            )
            _isLoadingPokemons.value = false
        }
    }

    private fun loadPokemonsByType(typeName: String) {
        viewModelScope.launch {
            _isLoadingPokemons.value = true
            _error.value = null
            getPokemonsByTypeUseCase(typeName).fold(
                onSuccess = { pokemons -> _allPokemonsSource.value = pokemons },
                onFailure = { e -> _error.value = e.localizedMessage ?: "Error fetching Pokémon for type $typeName" }
            )
            _isLoadingPokemons.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onShowOnlyCaughtChanged(show: Boolean) {
        _showOnlyCaught.value = show
    }

    fun clearError() {
        _error.value = null
    }

    fun toggleCatchRelease(pokemonName: String, isCurrentlyCaught: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyCaught) {
                releasePokemonUseCase(pokemonName)
            } else {
                catchPokemonUseCase(pokemonName)
            }
            result.onFailure { e ->
                 _error.value = e.localizedMessage ?: if(isCurrentlyCaught) "Error releasing Pokémon" else "Error catching Pokémon"
            }
        }
    }
}
