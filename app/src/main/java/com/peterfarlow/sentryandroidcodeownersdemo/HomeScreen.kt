@file:OptIn(ExperimentalMaterial3Api::class)

package com.peterfarlow.sentryandroidcodeownersdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.peterfarlow.core.models.Cat
import com.peterfarlow.sentryandroidcodeownersdemo.domain.MainViewModel
import com.peterfarlow.sentryandroidcodeownersdemo.ui.theme.SentryAndroidCodeownersDemoTheme

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel<MainViewModel>(factory = MainViewModel),
    onNavigation: (cat: Cat) -> Unit = { }
) {
    val viewState by viewModel.viewState.collectAsState()
    TestView(viewState = viewState) {
        viewModel.onCatClicked(it.id)
    }
    /*LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 64.dp, horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Cats",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        items(viewState.cats) { cat ->
            Card(
                onClick = { onNavigation(cat) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val title = "${cat.breeds.first().name} cat"
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    AsyncImage(
                        model = cat.url,
                        contentDescription = null,
                        modifier = Modifier.height(128.dp)
                    )
                }
            }
        }
        item {
            Button(onClick = { HomeCrasher().crash34915845() }) {
                Text(
                    text = "crash app",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }*/
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SentryAndroidCodeownersDemoTheme {
        HomeScreen()
    }
}
