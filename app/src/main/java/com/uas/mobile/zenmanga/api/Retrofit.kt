package com.uas.mobile.zenmanga.api

object Retrofit {
    private const val BASE_URL_MANGADEX = "https://api.mangadex.org/"
    val mangadexService: MangaDexService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL_MANGADEX)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(MangaDexService::class.java)
    }
}
