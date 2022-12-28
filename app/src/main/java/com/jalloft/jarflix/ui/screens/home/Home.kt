package com.jalloft.jarflix.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.jalloft.jarflix.ui.components.*
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel(), action: (HorizontalPanelAction) -> Unit) {
    val searchQuery by viewModel.searchQuery.observeAsState()

    Scaffold(
        topBar = { HomeTopAppBar() { viewModel.onSearchQueryChange(it) } },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (searchQuery.isNullOrEmpty()) {
            HomeScreen(innerPadding, viewModel) { action(it) }
        } else {
            SearchScreen(innerPadding, viewModel) { action(it) }
        }
    }
}



