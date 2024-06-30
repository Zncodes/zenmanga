package com.uas.mobile.zenmanga.dto.chapterpages

import com.google.gson.annotations.SerializedName


data class ChapterPages (

    @SerializedName("hash"      ) var hash      : String?           = null,
    @SerializedName("data"      ) var data      : ArrayList<String> = arrayListOf(),
    @SerializedName("dataSaver" ) var dataSaver : ArrayList<String> = arrayListOf()

)