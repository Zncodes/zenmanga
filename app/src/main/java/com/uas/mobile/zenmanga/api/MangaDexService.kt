package com.uas.mobile.zenmanga.api

import com.uas.mobile.zenmanga.dto.MDResponse
import com.uas.mobile.zenmanga.dto.chapter.MDChapterResponse
import com.uas.mobile.zenmanga.dto.chapterpages.ChapterPagesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexService {

    @GET("manga")
    suspend fun getMangaByTitle(
        @Query("limit") limit: Int = 20,
        @Query("includedTagsMode") includedTagsMode: String = "AND",
        @Query("excludedTagsMode") excludedTagsMode: String = "OR",
        @Query("contentRating[]") contentRating: List<String> = listOf("safe"),
//      @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive", "erotica"),
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("availableTranslatedLanguage[]") lang: List<String> = listOf("en", "id"),
        @Query("includes[]") includes: List<String> = listOf("cover_art"),
        @Query("title") title: String = ""
    ): MDResponse


    @GET("manga/{mangaId}/feed")
    suspend fun getChaptersFeed(
        @Path("mangaId") mangaId: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("translatedLanguage[]") translatedLanguage: String = "en",
        @Query("contentRating[]") contentRating: List<String> = listOf("safe"),
//      @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive", "erotica"),
        @Query("includeFutureUpdates") includeFutureUpdates: Int = 1,
        @Query("order[chapter]") orderChapter: String = "asc",
        @Query("includes[]") includes: List<String> = listOf("manga"),
        @Query("includeEmptyPages") includeEmptyPages: Int = 0,
        @Query("includeFuturePublishAt") includeFuturePublishAt: Int = 0,
        @Query("includeExternalUrl") includeExternalUrl: Int = 0
    ): MDChapterResponse


    @GET("at-home/server/{chapterId}")
    suspend fun getPagesByChapterId(@Path("chapterId") chapterId: String): ChapterPagesResponse
}