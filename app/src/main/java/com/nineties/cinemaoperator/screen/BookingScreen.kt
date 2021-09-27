package com.nineties.cinemaoperator.screen

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BookingScreen(inputURL: String, navController: NavHostController?) {
    Scaffold(topBar = {
        AppBar(
            title = "Booking Screen",
            icon = Icons.Default.ArrowBack
        ) {
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            WebViewScreen(inputURL = inputURL)
        }
    }
}

@Composable
fun WebViewScreen(inputURL: String) {
    val decodedURL = URLDecoder.decode(inputURL, StandardCharsets.UTF_8.toString())
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = WebViewClient()
            loadUrl(decodedURL)
        }
    }, update = {
        it.loadUrl(decodedURL)
    })
}