package com.uas.mobile.zenmanga.dto

import com.google.gson.annotations.SerializedName
import com.uas.mobile.zenmanga.dto.chapter.ChapterDTO

data class MangaDTO(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("description") val description: Map<String, String>,
    @SerializedName("cover") var cover_id: String? = null,
    var cover : String? = null,
    var chapters: MutableList<ChapterDTO> = mutableListOf()
)