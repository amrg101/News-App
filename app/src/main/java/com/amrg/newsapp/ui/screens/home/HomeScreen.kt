package com.amrg.newsapp.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import coil.annotation.ExperimentalCoilApi
import com.amrg.newsapp.R
import com.amrg.newsapp.shared.NetworkObserver
import com.amrg.newsapp.shared.navigateToCustomTab
import com.amrg.newsapp.ui.common.ArticleItemCard
import com.amrg.newsapp.ui.common.NetworkError
import com.amrg.newsapp.ui.common.ProgressDots
import com.amrg.newsapp.ui.theme.figeronaFont
import com.amrg.newsapp.ui.theme.pacificoFont
import kotlinx.coroutines.CoroutineScope


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalCoilApi::class
)
@Composable
fun HomeScreen(navController: NavController, networkStatus: NetworkObserver.Status) {

    val viewModel: HomeViewModel = hiltViewModel()

    /*
     Block back button press if search bar is visible to avoid
     app from closing immediately, instead disable search bar
     on first back press, and close app on second.
     */
    val sysBackButtonState = remember { mutableStateOf(false) }
    BackHandler(enabled = sysBackButtonState.value) {
        if (viewModel.topBarState.isSearchBarVisible) {
            if (viewModel.topBarState.searchText.isNotEmpty()) {
                viewModel.onAction(UserAction.TextFieldInput("", networkStatus))
            } else {
                viewModel.onAction(UserAction.CloseIconClicked)
            }
        }
    }

    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    HomeScreenScaffold(
        viewModel = viewModel,
        networkStatus = networkStatus,
        navController = navController,
        coroutineScope = coroutineScope,
        sysBackButtonState = sysBackButtonState,
    )
}


@ExperimentalCoilApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun HomeScreenScaffold(
    viewModel: HomeViewModel,
    networkStatus: NetworkObserver.Status,
    navController: NavController,
    coroutineScope: CoroutineScope,
    sysBackButtonState: MutableState<Boolean>,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val allArticlesState = viewModel.allArticlesState
    val topBarState = viewModel.topBarState

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 8.dp)
            ) {
                Crossfade(
                    targetState = topBarState.isSearchBarVisible,
                    animationSpec = tween(durationMillis = 200), label = "search cross fade"
                ) {
                    if (it) {
                        SearchAppBar(onCloseIconClicked = {
                            viewModel.onAction(UserAction.CloseIconClicked)
                        }, onInputValueChange = { newText ->
                            viewModel.onAction(
                                UserAction.TextFieldInput(newText, networkStatus)
                            )
                        }, text = topBarState.searchText, onSearchClicked = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                        sysBackButtonState.value = true
                    } else {
                        HomeTopAppBar(
                            onSearchIconClicked = {
                                viewModel.onAction(UserAction.SearchIconClicked)
                            })
                        sysBackButtonState.value = false
                    }
                }
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 70.dp)
            ) {

                // If search text is empty show list of all articles.
                if (topBarState.searchText.isBlank()) {
                    // show fullscreen progress indicator when loading the first page.
                    if (allArticlesState.page == 1 && allArticlesState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (!allArticlesState.isLoading && allArticlesState.error != null) {
                        NetworkError(onRetryClicked = { viewModel.reloadItems() })
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(start = 8.dp, end = 8.dp),
                            columns = GridCells.Adaptive(295.dp)
                        ) {
                            items(allArticlesState.items.size) { i ->
                                val item = allArticlesState.items[i]
                                if (networkStatus == NetworkObserver.Status.Available
                                    && i >= allArticlesState.items.size - 1
                                    && !allArticlesState.endReached
                                    && !allArticlesState.isLoading
                                ) {
                                    viewModel.loadNextItems()
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ArticleItemCard(
                                        title = item.title ?: "",
                                        author = item.author ?: "",
                                        date = item.publishedAt ?: "",
                                        content = item.description ?: "",
                                        coverImageUrl = item.urlToImage ?: "",
                                        isFav = viewModel.isArticleFav(item),
                                        onClick = {
                                            item.url?.let { url ->
                                                navigateToCustomTab(
                                                    context,
                                                    url
                                                )
                                            }
                                        },
                                        onFavCheckedChange = { checked ->
                                            if (checked)
                                                viewModel.onAction(UserAction.FavIconAdd(item))
                                            else
                                                viewModel.onAction(UserAction.FavIconDelete(item))
                                        }
                                    )
                                }
                            }
                            item {
                                if (allArticlesState.isLoading) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        ProgressDots()
                                    }
                                }
                            }
                        }
                    }

                    // Else show the search results.
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(start = 8.dp, end = 8.dp),
                        columns = GridCells.Adaptive(295.dp)
                    ) {
                        if (topBarState.isSearching) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ProgressDots()
                                }
                            }
                        }

                        items(topBarState.searchResults.size) { i ->
                            val item = topBarState.searchResults[i]
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                ArticleItemCard(
                                    title = item.title ?: "",
                                    author = item.author ?: "",
                                    date = item.publishedAt ?: "",
                                    content = item.description ?: "",
                                    coverImageUrl = item.urlToImage,
                                    isFav = viewModel.isArticleFav(item),
                                    onClick = {
                                        item.url?.let { url ->
                                            navigateToCustomTab(
                                                context,
                                                url
                                            )
                                        }
                                    },
                                    onFavCheckedChange = { checked ->
                                        if (checked)
                                            viewModel.onAction(UserAction.FavIconAdd(item))
                                        else
                                            viewModel.onAction(UserAction.FavIconDelete(item))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopAppBar(
    onSearchIconClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(id = R.string.home_header),
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = pacificoFont
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onSearchIconClicked) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.home_search_icon_desc),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SearchAppBar(
    onCloseIconClicked: () -> Unit,
    onInputValueChange: (String) -> Unit,
    text: String,
    onSearchClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = text,
        onValueChange = {
            onInputValueChange(it)
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp
        ),
        placeholder = {
            Text(
                text = "Search...",
                fontFamily = figeronaFont,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = ContentAlpha.medium
                )
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    onInputValueChange("")
                } else {
                    onCloseIconClicked()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = ContentAlpha.medium
            ),
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        shape = RoundedCornerShape(16.dp)
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), NetworkObserver.Status.Unavailable)
}