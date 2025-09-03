package com.kapmacs.pokemon.ui.features.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kapmacs.pokemon.domain.model.PokemonListItem
import com.kapmacs.pokemon.domain.model.PokemonType
import com.kapmacs.pokemon.ui.navigation.Screen
import com.kapmacs.pokemon.ui.theme.*

@Composable
fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = { Text("Search", color = DarkGrey.copy(alpha = 0.6f)) },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = DarkGrey.copy(alpha = 0.6f)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedBorderColor = LightGreyBorder,
            unfocusedBorderColor = LightGreyBorder,
            cursorColor = DarkGrey
        ),
        textStyle = LocalTextStyle.current.copy(color = DarkGrey)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTypeDropdown(
    pokemonTypes: List<PokemonType>,
    selectedType: PokemonType?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onTypeSelected: (PokemonType?) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedType?.name ?: "All Pokémon",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select...", color = DarkGrey.copy(alpha = 0.6f)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = LightGreyBorder,
                unfocusedBorderColor = LightGreyBorder
            ),
            textStyle = LocalTextStyle.current.copy(color = DarkGrey)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            DropdownMenuItem(
                text = { Text("All Pokémon", color = DarkGrey) },
                onClick = { onTypeSelected(null) },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            pokemonTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name, color = DarkGrey) },
                    onClick = { onTypeSelected(type) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun PokemonListHeader() {
    Row(
        modifier = Modifier
            .background(PaleBlue)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Name",
            modifier = Modifier
                .weight(1.2f)
                .padding(start = 8.dp, end = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DarkGrey
        )
        Text(
            "Type",
            modifier = Modifier
                .weight(0.8f)
                .padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = DarkGrey
        )
        Text(
            "Status",
            modifier = Modifier
                .weight(0.8f)
                .padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = DarkGrey
        )
        Spacer(
            modifier = Modifier
                .weight(1.2f)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun ErrorState(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TopBarColor)
        ) {
            Text("Retry")
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = DarkGrey,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PokemonListContent(
    isLoadingPokemons: Boolean,
    filteredPokemons: List<PokemonListItem>,
    error: String?,
    caughtPokemonNames: Set<String>,
    navController: NavController,
    onRetry: () -> Unit,
    onToggleCatchRelease: (String, Boolean) -> Unit
) {
    if (isLoadingPokemons) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            color = TopBarColor
        )
    }

    error?.let {
        ErrorState(error = it, onRetry = onRetry)
    }

    if (!isLoadingPokemons && filteredPokemons.isEmpty() && error == null) {
        EmptyState(message = "No Pokémon found.")
    } else if (error == null && !isLoadingPokemons) {
        LazyColumn(
            modifier = Modifier
                .background(PaleBlue)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(
                items = filteredPokemons, 
                key = { it.name },
                contentType = { "pokemon_row" } // Added contentType
            ) { pokemon ->
                val isCaught = caughtPokemonNames.contains(pokemon.name)
                PokemonRow(
                    pokemon = pokemon,
                    isCaught = isCaught,
                    typeColumnText = pokemon.type,
                    onCatchToggle = { onToggleCatchRelease(pokemon.name, isCaught) },
                    onItemClick = {
                        navController.navigate(
                            Screen.PokemonDetail.createRoute(
                                pokemon.name
                            )
                        )
                    }
                )
                HorizontalDivider(color = LightGreyBorder.copy(alpha = 0.5f))
            }
        }
    }
}


@Composable
fun PokemonRow(
    pokemon: PokemonListItem,
    typeColumnText: String,
    isCaught: Boolean,
    onCatchToggle: () -> Unit,
    onItemClick: () -> Unit
) {
    val buttonColor = if (isCaught) ReleaseButtonColor else CatchButtonColor
    val buttonTextColor = Color.White
    val buttonText = if (isCaught) "Release" else "Catch"

    val statusColumnText = if (isCaught) "Caught" else "-"

    val rowModifierBase = Modifier
        .fillMaxWidth()
        .clickable(onClick = onItemClick)

    val rowModifier =
        if (isCaught) {
            rowModifierBase.border(
                BorderStroke(1.dp, ReleaseButtonColor.copy(alpha = 0.7f)),
                RoundedCornerShape(4.dp)
            )
        } else {
            rowModifierBase
        }


    Row(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 4.dp
            )
            .height(IntrinsicSize.Min),

        ) {
        Row(
            modifier = rowModifier
                .background(Color.White)
                .weight(2.8f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pokemon.name,
                modifier = Modifier
                    .weight(1.2f)
                    .padding(start = 4.dp, end = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                color = DarkGrey
            )
            Text(
                text = typeColumnText,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCaught && statusColumnText == "Caught") FontWeight.Bold else FontWeight.Normal,
                color = DarkGrey
            )
            Text(
                text = statusColumnText,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGrey
            )
        }
        Box(
            modifier = Modifier
                .weight(1.2f)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onCatchToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonTextColor
                ),
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(buttonText, fontSize = 12.sp, maxLines = 1)
            }
        }
    }
}
