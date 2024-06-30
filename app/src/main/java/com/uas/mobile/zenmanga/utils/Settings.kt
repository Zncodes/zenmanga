package com.uas.mobile.zenmanga.utils

import android.content.Context

class Settings(context: Context){

    private val storage = context.getSharedPreferences("UserPrefs",0)

    fun getReadingMode(): String{
        return storage.getString("reading_mode","page").toString()
    }

    fun getMangaLang(): String{
        return storage.getString("manga_lang","en").toString()
    }

    fun setReadingMode(mode: String){
        storage.edit().putString("reading_mode",mode).apply()
    }

    fun setMangaLang(lang: String){
        storage.edit().putString("manga_lang",lang).apply()
    }

}