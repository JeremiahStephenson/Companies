package com.jerry.companies

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.jerry.companies.ui.common.LocalAppBarTitle
import com.jerry.companies.ui.common.unboundClickable
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import androidx.compose.animation.core.Animatable

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalLayoutApi::class,
)
@Composable
fun MainContent(
    onBackPressed: () -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val engine = rememberAnimatedNavHostEngine(
        //rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING
    )
    val navController = engine.rememberNavController()

    val showToolbarAnimator = remember { Animatable(0F) }
    LaunchedEffect(navController.appCurrentDestinationAsState().value) {
        showToolbarAnimator.snapTo(scrollBehavior.state.heightOffset)
        showToolbarAnimator.animateTo(0F) {
            scrollBehavior.state.heightOffset = this.value
        }
    }

    var title by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(
        LocalAppBarTitle provides { title = it },
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                val showBackArrow by remember(navController.appCurrentDestinationAsState().value) {
                    derivedStateOf { navController.previousBackStackEntry != null }
                }
                Toolbar(
                    scrollBehavior = scrollBehavior,
                    showBackArrow = { showBackArrow },
                    onBack = onBackPressed,
                    getTitle = { title.orEmpty() }
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            DestinationsNavHost(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .systemBarsPadding(),
                engine = engine,
                navGraph = NavGraphs.root,
                navController = navController,
                startRoute = NavGraphs.root.startRoute,
            )
        }
    }
}

@Composable
fun Toolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    showBackArrow: () -> Boolean,
    onBack: () -> Unit,
    getTitle: () -> String
) {
    val topAppBarElementColor = MaterialTheme.colorScheme.onPrimary
    val appBarContainerColor = MaterialTheme.colorScheme.primary
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        navigationIcon = {
            AnimatedVisibility(
                visible = showBackArrow(),
                enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                exit = shrinkOut(shrinkTowards = Alignment.Center) + fadeOut()
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .unboundClickable {
                            onBack()
                        }
                        .padding(8.dp),
                    painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.animateContentSize(),
                text = getTitle()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appBarContainerColor,
            scrolledContainerColor = appBarContainerColor,
            navigationIconContentColor = topAppBarElementColor,
            titleContentColor = topAppBarElementColor,
            actionIconContentColor = topAppBarElementColor,
        ),
        scrollBehavior = scrollBehavior
    )
}