package com.jerry.companies.ui.common

import androidx.compose.runtime.compositionLocalOf

val LocalAppBarTitle = compositionLocalOf<(String) -> Unit> { {} }