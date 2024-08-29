package com.peterfarlow.sentryandroidcodeownersdemo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.peterfarlow.core.models.Cat
import com.peterfarlow.sentryandroidcodeownersdemo.domain.ViewState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TestView(viewState: ViewState, onClick: (Cat) -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val modalState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 500),
        skipHalfExpanded = true
    )
    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "add focus metrics",
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
            ) {
                items(viewState.cats) { cat ->
                    Card(
                        onClick = { onClick(cat) }
                    ) {
                        val background = if (cat.selected) { Color.Blue } else { Color.White }
                        val textColor = if (cat.selected) { Color.White } else { Color.Black }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = background)
                                .height(56.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val title = "${cat.breeds.first().name} cat"
                            androidx.compose.material3.Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor
                            )
                            AsyncImage(
                                model = cat.url,
                                contentDescription = null,
                                modifier = Modifier.height(128.dp)
                            )
                        }
                    }
                }
            }
            Button(onClick = { }) {
                Text(text = "SAVE")
            }
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            modalState.show()
                        }
                    },
                    title = { Text(text = "Customize") }
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                ) {
                    items(viewState.cats) { cat ->
                        Card(
                            onClick = { }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val title = "${cat.breeds.first().name} cat"
                                androidx.compose.material3.Text(
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
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { }) {
                        Text(text = "SAVE")
                    }
                    Button(onClick = {
                        coroutineScope.launch {
                            modalState.show()
                        }
                    }) {
                        Text(text = "Open Sheet")
                    }
                }

            }
        }
    )
}
