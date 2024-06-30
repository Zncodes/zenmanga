package com.uas.mobile.zenmanga.viewmodel

import com.uas.mobile.zenmanga.dto.MangaDTO
import com.uas.mobile.zenmanga.dto.chapter.ChapterDTO
import kotlinx.coroutines.flow.StateFlow


interface MangaViewModel {
    val chapters: StateFlow<List<ChapterDTO>>
    val manga: StateFlow<MangaDTO?>
    val errorMessage: StateFlow<String>
    fun getChapters(mangaId: String)
    fun setSelectedManga(manga: MangaDTO)
    fun resetStatus()
}