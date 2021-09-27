package com.nineties.cinemaoperator.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nineties.cinemaoperator.R

@Composable
fun ErrorDetailScreen(message: String, onClickAction: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(onClick = { onClickAction.invoke() }) {
            Text(text = stringResource(id = R.string.reload_page))
        }
    }
}