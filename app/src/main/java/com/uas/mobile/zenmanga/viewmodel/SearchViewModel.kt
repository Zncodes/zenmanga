package com.uas.mobile.zenmanga.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.mobile.zenmanga.api.Retrofit
import com.uas.mobile.zenmanga.dto.MangaDTO
import com.uas.mobile.zenmanga.dto.chapter.ChapterDTO
import com.uas.mobile.zenmanga.utils.SETTINGS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel(), MangaViewModel {

    private val service = Retrofit.mangadexService

    private val _chapters = MutableStateFlow(emptyList<ChapterDTO>())
    override val chapters: StateFlow<List<ChapterDTO>> = _chapters.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    override val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private var _manga = MutableStateFlow(null as MangaDTO?)
    override val manga: StateFlow<MangaDTO?> = _manga.asStateFlow()

    private var _searchMangas = MutableStateFlow(mutableListOf<MangaDTO>())
    val searchMangas: StateFlow<List<MangaDTO>> = _searchMangas.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    override fun resetStatus() {}

    override fun setSelectedManga(manga: MangaDTO) {
        _manga.update { manga }
        _manga.value?.cover = manga.cover
    }

    override fun getChapters(mangaId: String) {
        viewModelScope.launch {
            try {

                val response = service.getChaptersFeed(
                    mangaId = mangaId,
                    translatedLanguage = SETTINGS.getMangaLang(),
                    limit = 10
                )

                val chaptersList = mutableListOf<ChapterDTO>()
                val total = response.total!!
                var limit = response.limit!!
                val difference = (total - limit) / 2
                response.data.forEach { chaptersList.add(it) }
                if (difference > 0) {
                    for (i in 0..2) {
                        val secondResponse = service.getChaptersFeed(
                            mangaId = mangaId,
                            offset = limit,
                            translatedLanguage = SETTINGS.getMangaLang(),
                            limit = difference
                        )
                        secondResponse.data.forEach { chaptersList.add(it) }
                        limit += difference
                    }
                }

                _chapters.update { chaptersList }
                _manga.value?.chapters = _chapters.value.toMutableList()
            } catch (ex: Exception) {
                _errorMessage.update { "An error has occurred while obtaining chapters" }
            }
        }
    }

    fun searchMangas(title: String) {
        viewModelScope.launch {
            try {
                val response = service.getMangaByTitle(title = title, lang = listOf(SETTINGS.getMangaLang()))
                _searchMangas.update { response.getMangaList() }
                _currentQuery.value = title
            } catch (ex: Exception) {
                ex.printStackTrace()
                _errorMessage.update { "An error occurred while searching manga" }
            }
        }
    }
}
