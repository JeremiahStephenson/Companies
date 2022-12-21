package com.jerry.companies.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerry.companies.R
import com.jerry.companies.ui.common.theme.CompaniesTheme
import com.jerry.companies.ui.common.theme.ThemePreviews

@Composable
fun EmbeddedErrorMessage(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Icon(
            modifier = Modifier.size(44.dp),
            painter = painterResource(R.drawable.ic_baseline_error_outline_24),
            contentDescription = null
        )
        Text(text = stringResource(R.string.error_message))
        Button(
            onClick = onRetry
        ) {
            Text(text = stringResource(R.string.retry))
        }
        Spacer(modifier = Modifier.weight(1F))
    }
}

@ThemePreviews
@Composable
private fun ErrorPreview() {
    CompaniesTheme {
        EmbeddedErrorMessage {}
    }
}