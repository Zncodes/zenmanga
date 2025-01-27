package com.uas.mobile.zenmanga.dto

import com.google.gson.annotations.SerializedName


data class MDResponse (

    @SerializedName("result"   ) var result   : String?         = null,
    @SerializedName("response" ) var response : String?         = null,
    @SerializedName("data"     ) var data     : ArrayList<Data> = arrayListOf(),
    @SerializedName("limit"    ) var limit    : Int?            = null,
    @SerializedName("offset"   ) var offset   : Int?            = null,
    @SerializedName("total"    ) var total    : Int?            = null

){

  fun getManga(data: Data): MangaDTO {
    data.let { dataInfo ->
      val coverId = dataInfo.relationships.singleOrNull { it.type == "cover_art" }?.id
      val fileName = dataInfo.relationships.singleOrNull{ it.type == "cover_art" }?.attributes?.fileName!!
      val id = dataInfo.id!!
      val cover = "https://uploads.mangadex.org/covers/$id/$fileName"
      return MangaDTO(
        id = id,
        title = dataInfo.attributes!!.title!!.en?:dataInfo.attributes!!.title!!.jp?:"[NOT RECOGNISED TITLE]",
        author = "author",
        description = dataInfo.attributes!!.description,
        cover_id = coverId,
        cover = cover
      )
    }
  }


  fun getMangaList(): MutableList<MangaDTO> {
    return data.map { getManga(it) } as MutableList<MangaDTO>
  }


}