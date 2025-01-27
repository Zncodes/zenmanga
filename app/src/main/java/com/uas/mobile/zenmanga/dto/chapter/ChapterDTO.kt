package com.uas.mobile.zenmanga.dto.chapter

import com.google.gson.annotations.SerializedName

data class ChapterDTO(
    @SerializedName("id"            ) var id            : String?                  = null,
    @SerializedName("type"          ) var type          : String?                  = null,
    @SerializedName("attributes"    ) var attributes    : ChapterAttributes?       = ChapterAttributes(),
    @SerializedName("relationships" ) var relationships : ArrayList<ChapterRelationships> = arrayListOf()
)