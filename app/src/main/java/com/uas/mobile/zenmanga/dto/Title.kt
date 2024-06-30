package com.uas.mobile.zenmanga.dto

import com.google.gson.annotations.SerializedName


data class Title (

  @SerializedName("en" ) var en : String? = null,
  @SerializedName("ja" ) var jp : String? = null,
  @SerializedName("cn" ) var cn : String? = null,
  @SerializedName("id" ) var id : String? = null

)