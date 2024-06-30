package com.uas.mobile.zenmanga.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.uas.mobile.zenmanga.api.Retrofit
import com.uas.mobile.zenmanga.dto.MangaDTO
import com.uas.mobile.zenmanga.dto.chapter.ChapterDTO
import com.uas.mobile.zenmanga.dto.chapterpages.ChapterPages
import com.uas.mobile.zenmanga.utils.SETTINGS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReaderViewModel : ViewModel(){

    private val service = Retrofit.mangadexService
    private val saveData = true

    private var _manga = MutableStateFlow(null as MangaDTO?)
    private var chapters = MutableStateFlow(mutableListOf<ChapterDTO>())
    private var _baseUrl = MutableStateFlow("")
    private var _chapterPages = MutableStateFlow(ChapterPages())

    private var _pageUrls = MutableStateFlow(listOf<String>())
    val pageUrls = _pageUrls.asStateFlow()

    private var _pageIndex = MutableStateFlow(0)
    val pageIndex = _pageIndex.asStateFlow()

    private var _chapterIndex = MutableStateFlow(-1)
    val chapterIndex = _chapterIndex.asStateFlow()

    private var _currentPage = MutableStateFlow("")
    val currentPage = _currentPage.asStateFlow()

    private val _isViewingCollectionDetails = MutableStateFlow(false)
    val isViewingCollectionDetails: StateFlow<Boolean> = _isViewingCollectionDetails

    fun pageForward(){
        viewModelScope.launch {
            if( _pageIndex.value < _chapterPages.value.data.size - 1){
                _pageIndex.value += 1
                getLink()
            }
        }
    }

    fun pageBackward(){
        viewModelScope.launch {
            if( _pageIndex.value > 0){
                _pageIndex.value -= 1
                getLink()
            }
        }
    }

    fun emptyPages(){
        _pageUrls.update { emptyList() }
    }

    fun setChapterIndex(newValue: Int){
            if(newValue < chapters.value.size && newValue > -1){
                viewModelScope.launch {
                    _chapterIndex.update { newValue }
                    val response = service.getPagesByChapterId(chapters.value[_chapterIndex.value].id!!)
                    _chapterPages.update { response.chapterPages!! }
                    _baseUrl.update { response.baseUrl!! }
                    if(SETTINGS.getReadingMode() == "cascade") {
                        _pageUrls.update { getAllPages() }
                    }
                    else {
                        _pageIndex.update { 0 }
                        getLink()
                    }
                }
            }
    }
    fun setManga(mangaDTO: MangaDTO){
        _manga.value = mangaDTO
        _manga.value?.cover = mangaDTO.cover
        chapters.update { mangaDTO.chapters }
    }

    fun getLink(): String{
        _currentPage.update {
            if(saveData)"${_baseUrl.value}/data-saver/${_chapterPages.value.hash}/${_chapterPages.value.dataSaver[_pageIndex.value]}"
            else "${_baseUrl.value}/data/${_chapterPages.value.hash}/${_chapterPages.value.data[_pageIndex.value]}"
        }
        return _currentPage.value
    }

    fun getAllPages(): List<String> {
        val deferredResult = viewModelScope.async {
            List(_chapterPages.value.data.size) { index ->
                "${_baseUrl.value}/data-saver/${_chapterPages.value.hash}/${_chapterPages.value.dataSaver[index]}"
            }
        }

        var pages: List<String> = emptyList()

        viewModelScope.launch {
            pages = deferredResult.await()
        }
        return pages
    }

    fun clearCache(context: Context){
        viewModelScope.launch {
            Glide.get(context).clearMemory()
            withContext(Dispatchers.IO){
                Glide.get(context).clearDiskCache()

            }
        }
    }
}
