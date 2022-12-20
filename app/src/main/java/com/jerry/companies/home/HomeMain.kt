package com.jerry.companies.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jerry.companies.R
import com.jerry.companies.destinations.DetailsMainDestination
import com.jerry.companies.repositories.Filter
import com.jerry.companies.service.DataResource
import com.jerry.companies.ui.common.LocalAppBarTitle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Suppress("Deprecation")
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeMain(
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.app_name))

    val uiState = viewModel.uiFlow.collectAsStateWithLifecycle(DataResource.idle())
    val dataState = viewModel.localCompaniesFlow.collectAsLazyPagingItems()

    val loading by remember { derivedStateOf { uiState.value?.isLoading == true }}

    // This is deprecated but the supported version from Google has some bugs
    // The indicator gets stuck sometimes until you scroll the list
    // I'm keeping it in the code commented out to show that I tried to use it
    //    val state = rememberPullRefreshState(
    //        refreshing = loading,
    //        onRefresh = { viewModel.refresh() }
    //    )
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = { viewModel.refresh() },
    ) {
    //Box(Modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dataState) { item ->
                CompanyItem(item?.company?.name.orEmpty()) {
                    navController.navigate(DetailsMainDestination)
                }
            }
        }
//        PullRefreshIndicator(
//            loading,
//            state,
//            Modifier.align(Alignment.TopCenter)
//        )
    }
}

@Composable
private fun CompanyItem(
    companyName: String,
    onViewDetails: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() }
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
        text = companyName
    )
}
