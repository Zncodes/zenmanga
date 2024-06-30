package com.uas.mobile.zenmanga.dto.chapter

import com.google.gson.annotations.SerializedName
import com.uas.mobile.zenmanga.dto.Attributes

data class ChapterRelationships (

    @SerializedName("id"   ) var id   : String? = null,
    @SerializedName("type" ) var type : String? = null,
    @SerializedName("attributes") var attributes : Attributes? = Attributes()


)