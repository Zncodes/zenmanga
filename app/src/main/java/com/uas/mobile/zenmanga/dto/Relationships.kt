package com.uas.mobile.zenmanga.dto

import com.google.gson.annotations.SerializedName
import dto.SimpleMDAttributes


data class Relationships (

  @SerializedName("id"   ) var id   : String? = null,
  @SerializedName("type" ) var type : String? = null,
  @SerializedName("attributes") var attributes : SimpleMDAttributes? = SimpleMDAttributes()


)