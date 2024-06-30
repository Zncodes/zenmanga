package com.uas.mobile.zenmanga.dao.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val username: String,
    val password: String,
    val profileImg: String? = null
)
