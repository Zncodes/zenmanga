package com.uas.mobile.zenmanga.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import com.uas.mobile.zenmanga.R
import com.uas.mobile.zenmanga.nav.Routes
import com.uas.mobile.zenmanga.utils.SETTINGS
import com.uas.mobile.zenmanga.viewmodel.ReaderViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MangaReader(navController: NavController?, readerViewModel: ReaderViewModel) {

    val isCascade = SETTINGS.getReadingMode() == "cascade"
    val chapterIndex = readerViewModel.chapterIndex.collectAsStateWithLifecycle()
    val isViewingCollectionDetails by readerViewModel.isViewingCollectionDetails.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chapter ${chapterIndex.value + 1}",
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        if (isCascade)
            ReaderByCascade(readerViewModel = readerViewModel, modifier = Modifier.weight(3f))
        else
            ReaderByPage(readerViewModel = readerViewModel, modifier = Modifier.weight(3f))
    }

    BackHandler(true) {
            navController?.popBackStack(
                route = Routes.MANGADETAILS,
                inclusive = false,
                saveState = false
            )

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReaderByPage(readerViewModel: ReaderViewModel, modifier: Modifier) {
    val page = readerViewModel.currentPage.collectAsStateWithLifecycle()
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    if (scale < 1.0f) scale = 1.0f
    if (scale == 1.0f) offset = Offset.Zero
    Column(
        modifier = modifier
    ) {
        GlideImage(
            model = page.value,
            contentDescription = null,
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state),
            contentScale = ContentScale.FillWidth
        )
        ReaderControls(
            readerViewModel = readerViewModel, modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(), null
        )
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReaderByCascade(readerViewModel: ReaderViewModel, modifier: Modifier) {
    val pages = readerViewModel.pageUrls.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    Column(modifier = modifier) {
        BoxWithConstraints(
            Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {

            var scale by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 2) * constraints.maxWidth
                val extraHeight = (scale - 2) * constraints.maxHeight

                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                offset = Offset(
                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = state),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(pages.value) {


                    GlideImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
        ReaderControls(
            readerViewModel = readerViewModel, modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(), lazyListState
        )
    }
}


@Composable
fun ReaderControls(
    readerViewModel: ReaderViewModel,
    modifier: Modifier,
    scrollState: LazyListState?
) {
    val isCascade = SETTINGS.getReadingMode() == "cascade"
    val pageIndex = readerViewModel.pageIndex.collectAsStateWithLifecycle()
    val chapterIndex = readerViewModel.chapterIndex.collectAsStateWithLifecycle()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Dispatchers.Main) {
        scrollState?.scrollToItem(0)
    }
    val onAsyncClick: (index: Int) -> Unit = { index ->
        coroutine.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                readerViewModel.clearCache(context)
            }
            readerViewModel.emptyPages()
            readerViewModel.setChapterIndex(index)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = R.drawable.double_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable {
                    val newIndex = chapterIndex.value - 1
                    onAsyncClick(newIndex)
                })
        if (!isCascade) {
            Icon(painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { readerViewModel.pageBackward() })

            Text(
                text = "${pageIndex.value + 1}",
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { readerViewModel.pageForward() })
        }
        Icon(painter = painterResource(id = R.drawable.double_arrow_right),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable {
                    val newIndex = chapterIndex.value + 1
                    onAsyncClick(newIndex)
                }
        )
    }
}
