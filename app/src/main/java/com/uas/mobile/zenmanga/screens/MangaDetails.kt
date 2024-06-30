package com.uas.mobile.zenmanga.screens

//import com.uas.mobile.zenmanga.utils.Status
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uas.mobile.zenmanga.components.DisplayButton
import com.uas.mobile.zenmanga.dto.chapter.ChapterDTO
import com.uas.mobile.zenmanga.nav.Routes
import com.uas.mobile.zenmanga.viewmodel.MangaViewModel
import com.uas.mobile.zenmanga.viewmodel.SearchViewModel

typealias LambdaChapter = (Int) -> Unit

@SuppressLint("AutoboxingStateCreation")
@Composable
fun MangaDetails(navController: NavController?, viewModel: MangaViewModel) {
    val manga = (viewModel as? SearchViewModel)?.manga?.collectAsState() ?: return
    val scrollState = rememberScrollState()

    Log.d("idManga", "manga con id ${manga.value?.id}")

    manga.value?.let { mangaValue ->
        LaunchedEffect(Unit) {
            viewModel.getChapters(mangaValue.id)
        }

        val chapters = viewModel.chapters.collectAsState()
        val selectedChapter = remember { mutableStateOf(-1) }

        if (selectedChapter.value != -1) {
            Log.d("toReader", "go to the chapter ${selectedChapter.value}")
            navController?.navigate("${Routes.READER}/${selectedChapter.value}")
        }

        val titleSize = when {
            mangaValue.title.length > 60 -> 14.sp
            mangaValue.title.length > 25 -> 20.sp
            else -> 35.sp
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .scrollable(state = scrollState, orientation = Orientation.Vertical),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = mangaValue.cover ?: "",
                contentDescription = "Manga cover",
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(width = 1.dp, color = Color.Blue)
                    .padding(8.dp)
                    .shadow(elevation = 4.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = mangaValue.title,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                DescriptionToggle(description = mangaValue.description["en"] ?: mangaValue.description["id"] ?: "<empty description>")
                    ChaptersToggle(chapters = chapters.value) {
                        selectedChapter.value = it
                    }
            }
        }
    } ?: Text("There is no manga selected")
}

@Composable
fun ChaptersToggle(chapters: List<ChapterDTO>, onSelect: LambdaChapter) {
    val chaptersComponent = @Composable {
        if (chapters.isEmpty()) Text(text = "Loading Chapter Info, if it takes too long probably wasn't updated yet")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(chapters) { index, chapter ->
                ChapterItem(chapter = chapter, index = index, onSelect)
            }
        }
    }
    DisplayButton(
        onDisplay = chaptersComponent,
        title = "Chapters"
    )
}

@Composable
fun ChapterItem(chapter: ChapterDTO, index: Int, onSelect: LambdaChapter) {
    val title = (chapter.attributes?.title ?: "").ifEmpty { "" }
    Log.d("chapterItem", "index=$index")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                Log.d("chapterItem", "chapter $index")
                onSelect(index)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${index + 1}")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = title, modifier = Modifier.weight(1f), overflow = TextOverflow.Clip)
    }
}

@Composable
fun DescriptionToggle(description: String) {
    val scrollState = rememberScrollState()
    val summaryComponent = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = description,
                textAlign = TextAlign.Justify,
            )
        }
    }
    DisplayButton(
        onDisplay = summaryComponent,
        title = "Synopsis"
    )
}
