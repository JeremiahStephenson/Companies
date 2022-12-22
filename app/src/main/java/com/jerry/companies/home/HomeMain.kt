package com.jerry.companies.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jerry.companies.R
import com.jerry.companies.destinations.DetailsMainDestination
import com.jerry.companies.repositories.Sort
import com.jerry.companies.ui.common.EmbeddedErrorMessage
import com.jerry.companies.ui.common.FadeAnimatedVisibility
import com.jerry.companies.ui.common.LocalAppBarTitle
import com.jerry.companies.ui.common.theme.CompaniesTheme
import com.jerry.companies.ui.common.theme.ThemePreviews
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

// We're just suppressing the deprecation warnings to the SwipeRefresh composable
// See comments below for more details
@Suppress("Deprecation")
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeMain(
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.app_name))

    val state = getHomeState(viewModel)

    // This is deprecated but the supported version from Google has some bugs
    // The indicator gets stuck sometimes until you scroll the list
    // I'm keeping it in the code commented out to show that I tried to use it
    //    val state = rememberPullRefreshState(
    //        refreshing = loading,
    //        onRefresh = { viewModel.refresh() }
    //    )
    SwipeRefresh(
        state = rememberSwipeRefreshState(state.dataPaging.loadState.refresh is LoadState.Loading),
        onRefresh = { state.dataPaging.refresh() },
    ) {
    //Box(Modifier.pullRefresh(state)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(top = 84.dp)
            ) {
                itemsIndexed(state.dataPaging) { index, item ->
                    if (index > 0) {
                        Divider()
                    }
                    CompanyItem(item?.name.orEmpty()) {
                        item?.id?.let { id ->
                            navController.navigate(DetailsMainDestination(id))
                        }
                    }
                }
            }

            ErrorMessage(state.dataPaging) { state.dataPaging.refresh() }
            ListSorter(state) { viewModel.setSort(it) }
            if (state.errorDialog) {
                SomethingWentWrongDialog(
                    dismiss = { viewModel.dismissErrorDialog() }
                )
            }
        }
        // See comment above about the new pull to refresh from Google
//        PullRefreshIndicator(
//            loading,
//            state,
//            Modifier.align(Alignment.TopCenter)
//        )
    }
}

@Composable
private fun ListSorter(
    state: HomeState,
    onSort: (Sort) -> Unit
) {
    val isSortVisible = remember {
        derivedStateOf {
            state.dataPaging.itemCount > 0
        }
    }
    FadeAnimatedVisibility(visible = isSortVisible.value) {
        SortButton(
            sort = state.sort,
            onSort = onSort
        )
    }
}

@Composable
private fun SortButton(
    sort: Sort,
    onSort: (Sort) -> Unit
) {
    val oppositeSort = when (sort) {
        Sort.ID -> Sort.NAME
        Sort.NAME -> Sort.ID
    }
    OutlinedButton(
        modifier = Modifier
            .padding(top = 20.dp)
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        onClick = { onSort(oppositeSort) }) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.sort_by, oppositeSort.name.lowercase())
        )
    }
}

@Composable
private fun ErrorMessage(
    pagerState: LazyPagingItems<*>,
    onRetry: () -> Unit
) {
    val isErrorVisible = remember {
        derivedStateOf {
            pagerState.itemCount == 0 && pagerState.loadState.refresh is LoadState.Error
        }
    }
    FadeAnimatedVisibility(visible = isErrorVisible.value) {
        EmbeddedErrorMessage(onRetry = onRetry)
    }
}

@Composable
private fun SomethingWentWrongDialog(
    dismiss: () -> Unit
) {
    Dialog(onDismissRequest = dismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.error_message),
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = dismiss
            ) {
                Text(text = stringResource(R.string.ok))
            }
        }
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
            .animateContentSize()
            .clickable { onViewDetails() }
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
        text = companyName
    )
}

@Composable
private fun getHomeState(viewModel: HomeViewModel): HomeState {
    val pagerState = viewModel.companiesPager.collectAsLazyPagingItems()
    val errorDialog = viewModel.errorFlow.collectAsStateWithLifecycle()
    val currentSort = viewModel.currentSortFlow.collectAsStateWithLifecycle()
    return HomeState(pagerState, errorDialog, currentSort)
}

@Preview
@Composable
private fun CompanyItemPreview() {
    CompaniesTheme {
        CompanyItem(companyName = "Company Name") {}
    }
}

@ThemePreviews
@Composable
private fun ErrorDialogPreview() {
    CompaniesTheme {
        SomethingWentWrongDialog {}
    }
}

@ThemePreviews
@Composable
private fun SortButtonPreview() {
    CompaniesTheme {
        SortButton(Sort.NAME) {}
    }
}
