package com.jerry.companies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.jerry.companies.ui.common.theme.CompaniesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CompaniesTheme {
                MainContent { onBackPressedDispatcher.onBackPressed() }
            }
        }
    }
}