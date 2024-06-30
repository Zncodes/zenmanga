package com.uas.mobile.zenmanga.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uas.mobile.zenmanga.components.BottomBar
import com.uas.mobile.zenmanga.nav.Routes
import com.uas.mobile.zenmanga.utils.SETTINGS
import com.uas.mobile.zenmanga.viewmodel.ReaderViewModel
import com.uas.mobile.zenmanga.viewmodel.UserViewModel
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController?, userViewModel: UserViewModel, readerViewModel: ReaderViewModel) {
    val context = LocalContext.current

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
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            val currentUser by userViewModel.currentUser.collectAsState()
            currentUser?.let { user ->
                Text(
                    text = "Hello, ${user.username}",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            MangaLanguageSelector()

            ReadingModeSelector()

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "", // Label here if needed
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Button(onClick = {
                        userViewModel.logout()
                        navController?.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOMESCREEN) { inclusive = true }
                        }
                    }) {
                        Text("Logout")
                    }

                    VisitMangaDexButton(context)
                }
            }
        }
    }
}

@Composable
fun VisitMangaDexButton(context: Context) {
    Button(
        onClick = { openMangaDex(context) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Visit MangaDex")
    }
}

private fun openMangaDex(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mangadex.org/"))
    context.startActivity(intent)
}

@Composable
fun MangaLanguageSelector() {
    var mangaLang by remember { mutableStateOf(
        Languages.findByValue(SETTINGS.getMangaLang()) ?: Languages.ENGLISH
    ) }
    var showLangs by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Manga Language", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.width(10.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                if (showLangs) {
                    items(Languages.entries.toTypedArray()) { lang ->
                        LanguageItem(lang = lang) {
                            setLanguage(lang)
                            mangaLang = lang
                            showLangs = false
                        }
                    }
                } else {
                    item {
                        LanguageItem(lang = mangaLang) { showLangs = true }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingModeSelector() {
    var readingMode by remember { mutableStateOf(ReadingMode.valueOf(SETTINGS.getReadingMode().uppercase
        (
            Locale.ROOT
        )
    )) }
    var showModes by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Page Mode", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.width(10.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                if (showModes) {
                    items(ReadingMode.entries.toTypedArray()) { mode ->
                        ReadingItem(readingMode = mode) {
                            setReadingMode(mode)
                            readingMode = mode
                            showModes = false
                        }
                    }
                } else {
                    item {
                        ReadingItem(readingMode = readingMode) { showModes = true }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageItem(lang: Languages, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lang.name.replace('_', ' '),
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
private fun ReadingItem(readingMode: ReadingMode, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = readingMode.name.replace('_', ' '),
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

private fun setReadingMode(mode: ReadingMode) {
    SETTINGS.setReadingMode(mode.name.lowercase(Locale.ROOT))
}

private fun setLanguage(lang: Languages) {
    SETTINGS.setMangaLang(lang.value)
}

private enum class ReadingMode {
    PAGE,
    CASCADE
}

private enum class Languages(val value: String) {
    ENGLISH("en"),
    INDONESIA("id");

    companion object {
        fun findByValue(value: String): Languages? =
            entries.find { it.value == value }
    }
}
