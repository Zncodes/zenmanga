package com.uas.mobile.zenmanga.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uas.mobile.zenmanga.screens.LoginScreen
import com.uas.mobile.zenmanga.screens.MangaDetails
import com.uas.mobile.zenmanga.screens.MangaReader
import com.uas.mobile.zenmanga.screens.RegisterScreen
import com.uas.mobile.zenmanga.screens.SearchScreen
import com.uas.mobile.zenmanga.screens.SettingsScreen
import com.uas.mobile.zenmanga.viewmodel.ReaderViewModel
import com.uas.mobile.zenmanga.viewmodel.SearchViewModel
import com.uas.mobile.zenmanga.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers

@SuppressLint("SuspiciousIndentation", "StateFlowValueCalledInComposition")
@Composable
fun Router(userViewModel: UserViewModel = viewModel()) {

    val searchViewModel: SearchViewModel = remember { SearchViewModel() }
    val readerViewModel: ReaderViewModel = viewModel()
    val navController = rememberNavController()

    LaunchedEffect(userViewModel.isLoggedIn.collectAsStateWithLifecycle()) {
        if (userViewModel.isLoggedIn.value) {
            navController.navigate(Routes.HOMESCREEN) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.HOMESCREEN) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {},
    ) {
        NavHost(navController = navController, startDestination = Routes.LOGIN, modifier = Modifier.padding(it)) {
            composable(Routes.LOGIN) {
                LoginScreen(navController = navController,userViewModel = userViewModel) {
                    navController.navigate(Routes.HOMESCREEN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
            composable(Routes.REGISTER) {
                RegisterScreen(navController = navController,userViewModel = userViewModel) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(navController = navController, readerViewModel = readerViewModel, userViewModel = userViewModel)
            }
            composable(Routes.HOMESCREEN) {
                if (userViewModel.isLoggedIn.value) {
                    SearchScreen(
                        navController = navController,
                        searchViewModel = searchViewModel,
                        readerViewModel = readerViewModel
                    )
                } else {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOMESCREEN) { inclusive = true }
                    }
                }
            }
            composable(Routes.COLLECTION) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.COLLECTION) { inclusive = true }

                }
            }
            composable(Routes.MANGADETAILS) {
                MangaDetails(navController, searchViewModel)
            }

            composable("${Routes.READER}/{index}") {
                val index = it.arguments?.getString("index")?.toInt() ?: 0
                val currentManga = searchViewModel.manga.collectAsStateWithLifecycle().value!!
                readerViewModel.setManga(currentManga)
                readerViewModel.setChapterIndex(index)
                LaunchedEffect(Dispatchers.IO) {
                }
                MangaReader(navController = navController, readerViewModel = readerViewModel)
            }
        }
    }
}

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOMESCREEN = "homescreen"
    const val COLLECTION = "collection"
    const val MANGADETAILS = "mangadetails"
    const val COLLECTION_MANGADETAILS = "collection_mangadetails"
    const val READER = "reader"
    const val SETTINGS = "settings"
}
