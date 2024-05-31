package com.peterfarlow.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage


@Composable
fun DetailScreen(
    viewModel: DetailViewModel = viewModel<DetailViewModel>(factory = DetailViewModel.Factory),
    onNavigation: () -> Unit = { }
) {
    BackHandler {
        onNavigation()
    }
    val viewState by viewModel.viewState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${viewState.cat.id} is a ${viewState.cat.breeds.first().name}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        AsyncImage(
            model = viewState.cat.url,
            contentDescription = "image of the cat",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = viewState.cat.breeds.first().description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
