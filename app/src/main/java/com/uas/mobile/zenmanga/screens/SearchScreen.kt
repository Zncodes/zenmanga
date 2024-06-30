package com.uas.mobile.zenmanga.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uas.mobile.zenmanga.components.BottomBar
import com.uas.mobile.zenmanga.components.LambdaManga
import com.uas.mobile.zenmanga.components.SearchBar
import com.uas.mobile.zenmanga.components.SearchList
import com.uas.mobile.zenmanga.dto.MangaDTO
import com.uas.mobile.zenmanga.nav.Routes
import com.uas.mobile.zenmanga.viewmodel.ReaderViewModel
import com.uas.mobile.zenmanga.viewmodel.SearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavController?,
    searchViewModel: SearchViewModel,
    readerViewModel: ReaderViewModel
) {
    val searchMangas = searchViewModel.searchMangas.collectAsState()
    val currentQuery = searchViewModel.currentQuery.collectAsState()

    val onMangaClick: LambdaManga = { manga: MangaDTO ->
        searchViewModel.setSelectedManga(manga)
        searchViewModel.resetStatus()
        navController?.navigate(Routes.MANGADETAILS)
    }

    LaunchedEffect(Unit) {
        if (searchMangas.value.isEmpty()) {
            searchViewModel.searchMangas(title = "")
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBar(onScreenChange = {
                readerViewModel.setChapterIndex(-1)
                navController?.popBackStack(route = it, inclusive = true, saveState = false)
                navController?.navigate(it)
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                initialQuery = currentQuery.value,
                onSearch = { query ->
                    searchViewModel.searchMangas(query)
                },
                label = "Search manga title..."
            )
            SearchList(mangaList = searchMangas.value, onClick = onMangaClick)
        }
    }
}
