package com.uas.mobile.zenmanga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.uas.mobile.zenmanga.nav.Router
import com.uas.mobile.zenmanga.ui.theme.ZenMangaTheme
import com.uas.mobile.zenmanga.utils.SETTINGS
import com.uas.mobile.zenmanga.utils.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenMangaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    SETTINGS = Settings(context)  // Inisiasi Settings dengan context, sehingga nantinya dapat digunakan
                    Router()  // Inisiasi router
                }
            }
        }
    }
}
