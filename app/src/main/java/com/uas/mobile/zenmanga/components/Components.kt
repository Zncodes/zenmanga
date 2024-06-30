package com.uas.mobile.zenmanga.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uas.mobile.zenmanga.R
import com.uas.mobile.zenmanga.dto.MangaDTO
import com.uas.mobile.zenmanga.nav.Routes

typealias LambdaString = (String) -> Unit
typealias LambdaManga = (MangaDTO) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    initialQuery: String,
    onSearch: LambdaString = {},
    label: String = "Search manga title..."
) {
    var search by rememberSaveable { mutableStateOf(initialQuery) }

    LaunchedEffect(key1 = null) {
        search = initialQuery
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = search,
            onValueChange = { search = it },
            trailingIcon = {
                Icon(
                    Icons.Filled.Search,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    contentDescription = "",
                    modifier = Modifier.clickable { onSearch(search) }
                )
            },
            placeholder = {
                Text(text = label, color = Color.Gray, fontSize = 16.sp)
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.surfaceTint,
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Blue,
            ),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSearch(search) }),
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@Composable
fun SearchList(onClick: LambdaManga, mangaList: List<MangaDTO>){
    LazyColumn{
        itemsIndexed(mangaList){ _, manga ->
            SearchItem(manga, onClick)

        }
    }
}

@Composable
fun SearchItem(manga: MangaDTO, onClick: LambdaManga){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier.padding(10.dp)
    )
    {
        Row(
            modifier = Modifier
                .clickable { onClick(manga) }
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = manga.cover,
                contentDescription = "",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = manga.title,
                modifier = Modifier.weight(2f).padding(5.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DisplayButton(title: String = "Sample Text",
                  @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
                      .fillMaxWidth()
                      .padding(10.dp), onDisplay: @Composable () -> Unit){
    val display = remember { mutableStateOf(false) }
    val toggleDisplay: () -> Unit = {display.value = !display.value }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ), modifier = modifier
    ) {
        if(display.value){ OnDisplay(onDisplay, toggleDisplay) } else { OnClosed(title = title, toggleDisplay) }
    }
}

@Composable
fun OnDisplay(content: @Composable () -> Unit, onClick: () -> Unit){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f)){
            content()
        }
        Icon(painter = painterResource(id = R.drawable.baseline_arrow_drop_up_24), contentDescription = "", modifier = Modifier
            .weight(0.15f)
            .fillMaxWidth()
            .clickable { onClick() })
    }

}

@Composable
fun OnClosed(title: String, onClick: () -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable { onClick() }, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title)
        Icon(painter = painterResource(id = R.drawable.arrow_down), contentDescription = "")
    }
}

@Composable
fun BottomBar(onScreenChange: LambdaString){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.06f)
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {

        Icon(Icons.Filled.Search, contentDescription = "",modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .weight(1f)
            .clickable { onScreenChange(Routes.HOMESCREEN) })
        Icon(Icons.Filled.AccountCircle, contentDescription = "",modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .weight(1f)
            .clickable { onScreenChange(Routes.SETTINGS) })
    }
}