package com.jerry.companies.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.jerry.companies.R
import com.jerry.companies.destinations.DetailsMainDestination
import com.jerry.companies.ui.common.LocalAppBarTitle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeMain(
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.app_name))

    val uiState = viewModel.flow.collectAsStateWithLifecycle()
    val dataState = viewModel.companiesFlow.collectAsLazyPagingItems()

    val state = rememberPullRefreshState(
        refreshing = uiState.value.isLoading,
        onRefresh = { viewModel.refresh() }
    )
    Box(Modifier.pullRefresh(state)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dataState) { item ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(DetailsMainDestination)
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 44.dp
                        ),
                    text = "${item?.company?.name.orEmpty()} - ${item?.revenue?.joinToString(", ") { it.value.toString() }}"
                )
            }
        }

        PullRefreshIndicator(
            uiState.value.isLoading,
            state,
            Modifier.align(Alignment.TopCenter)
        )
    }
}
