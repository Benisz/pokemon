package com.kapmacs.pokemon.ui.features.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kapmacs.pokemon.domain.model.PokemonDetails

private val ImageBorderColor = Color(0xFFFFC107)
private val DetailRowBlueGray = Color(0xFFE0E0E0)
private val DetailRowYellow = Color(0xFFFFFDE7)
private val DetailTextColor = Color(0xFF424242)
private val ReleaseButtonColor = Color(0xFFFFC107)
private val CatchButtonColor = Color(0xFF448AFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()
    val isCaught by viewModel.isCaught.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            isLoading && pokemonDetails == null -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error ?: "An unknown error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = {
                        viewModel.clearError()
                        viewModel.retryLoadDetails()
                    }) {
                        Text("Retry")
                    }
                }
            }

            pokemonDetails != null -> {
                PokemonProfileContent(pokemonDetails!!, isCaught, viewModel::toggleCatchRelease)
            }

            else -> {
                Text(
                    "No details available.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun PokemonProfileContent(
    details: PokemonDetails,
    isCaught: Boolean,
    onCatchToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(4.dp, ImageBorderColor, RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(details.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${details.name} image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            DetailInfoRow(
                label = "Name",
                value = details.name.replaceFirstChar { it.titlecase() },
                backgroundColor = DetailRowBlueGray
            )
            DetailInfoRow(
                label = "Weight",
                value = "${details.weight / 10.0} kg",
                backgroundColor = DetailRowYellow
            )
            DetailInfoRow(
                label = "Height",
                value = "${details.height / 10.0} m",
                backgroundColor = DetailRowBlueGray
            )

            DetailInfoRow(label = "Abilities", backgroundColor = DetailRowYellow) {
                if (details.abilities.isEmpty()) {
                    Text("-", color = DetailTextColor, fontSize = 16.sp)
                } else {
                    Column {
                        details.abilities.forEach {
                            Text(
                                it.name.replaceFirstChar { tc -> tc.titlecase() },
                                color = DetailTextColor,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
            }
            DetailInfoRow(
                label = "Status",
                value = if (isCaught) "Caught" else "Not Caught",
                backgroundColor = DetailRowBlueGray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onCatchToggle,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCaught) ReleaseButtonColor else CatchButtonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                if (isCaught) "Release" else "Catch",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailInfoRow(label: String, value: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.4f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DetailTextColor
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.6f),
            fontSize = 16.sp,
            color = DetailTextColor
        )
    }
}

@Composable
fun DetailInfoRow(label: String, backgroundColor: Color, valueContent: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(0.4f)
                .padding(top = if (label == "Abilities") 0.dp else 0.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DetailTextColor
        )
        Box(modifier = Modifier.weight(0.6f)) {
            valueContent()
        }
    }
}
