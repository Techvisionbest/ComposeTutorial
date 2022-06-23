package com.vision.composetutorial.foryou

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vision.composetutorial.R
import com.vision.composetutorial.component.*
import com.vision.composetutorial.icon.NiaIcons
import com.vision.composetutorial.ui.theme.NiaTypography
import kotlin.math.floor
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import com.vision.composetutorial.model.previewNewsResources

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForYouRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel(),
){
//    val interestsSelectionState by viewModel.interestsSelectionState.collectAsState()
//    val feedState by viewModel.feedState.collectAsState()
    ForYouScreen(
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
        feedState = ForYouFeedUiState.Loading,
        onTopicCheckedChanged = {string, boolean -> },
        onAuthorCheckedChanged = {string, boolean -> },
        saveFollowedTopics = {},
        onNewsResourcesCheckedChanged = {string, boolean -> }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    windowSizeClass: WindowSizeClass,
    interestsSelectionState: ForYouInterestsSelectionUiState,
    feedState: ForYouFeedUiState,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: ()-> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.app_name,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = stringResource(id = R.string.for_you),
                    actionIcon = Icons.Filled.AccountCircle,
                    actionIconContentDescription = stringResource(id = R.string.for_you),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = modifier
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {
                val numberOfColumns = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 1
                    else -> floor(maxWidth / 300.dp).toInt().coerceAtLeast(1)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    InterestsSelection(
                        interestsSelectionState = interestsSelectionState,
                        showLoadingUIIfLoading = true,
                        onAuthorCheckedChanged = onAuthorCheckedChanged,
                        onTopicCheckedChanged = onTopicCheckedChanged,
                        saveFollowedTopics = saveFollowedTopics
                    )

                    Feed(
                        feedState = feedState,
                        // Avoid showing a second loading wheel if we already are for the interests
                        // selection
                        showLoadingUIIfLoading =
                        interestsSelectionState !is ForYouInterestsSelectionUiState.Loading,
                        numberOfColumns = numberOfColumns,
                        onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged
                    )

                    item {
                        Spacer(
                            // TODO: Replace with windowInsetsBottomHeight after
                            //       https://issuetracker.google.com/issues/230383055
                            Modifier.windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                            )
                        )
                    }
                }
            }

        }
    }
}

private fun LazyListScope.InterestsSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    showLoadingUIIfLoading: Boolean,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit
) {
    when (interestsSelectionState) {
        ForYouInterestsSelectionUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    LoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        ForYouInterestsSelectionUiState.NoInterestsSelection -> Unit
        is ForYouInterestsSelectionUiState.WithInterestsSelection -> {
            item {
                Text(
                    text = stringResource(R.string.onboarding_guidance_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    style = NiaTypography.titleMedium
                )
            }
            item {
                Text(
                    text = stringResource(R.string.onboarding_guidance_subtitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center,
                    style = NiaTypography.bodyMedium
                )
            }
            item {
                AuthorsCarousel(
                    authors = interestsSelectionState.authors,
                    onAuthorClick = onAuthorCheckedChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                TopicSelection(
                    interestsSelectionState,
                    onTopicCheckedChanged,
                    Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                // Done button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NiaFilledButton(
                        onClick = saveFollowedTopics,
                        enabled = interestsSelectionState.canSaveInterests,
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .width(364.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.done)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState.WithInterestsSelection,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(24.dp),
        modifier = modifier
            // LazyHorizontalGrid has to be constrained in height.
            // However, we can't set a fixed height because the horizontal grid contains
            // vertical text that can be rescaled.
            // When the fontScale is at most 1, we know that the horizontal grid will be at most
            // 240dp tall, so this is an upper bound for when the font scale is at most 1.
            // When the fontScale is greater than 1, the height required by the text inside the
            // horizontal grid will increase by at most the same factor, so 240sp is a valid
            // upper bound for how much space we need in that case.
            // The maximum of these two bounds is therefore a valid upper bound in all cases.
            .heightIn(max = max(240.dp, with(LocalDensity.current) { 240.sp.toDp() }))
            .fillMaxWidth()
    ) {
        items(interestsSelectionState.topics) {
            SingleTopicButton(
                name = it.topic.name,
                topicId = it.topic.id,
                imageUrl = it.topic.imageUrl,
                isSelected = it.isFollowed,
                onClick = onTopicCheckedChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleTopicButton(
    name: String,
    topicId: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(312.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surface,
        selected = isSelected,
        onClick = {
            onClick(topicId, !isSelected)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        ) {
            TopicIcon(
                imageUrl = imageUrl
            )
            Text(
                text = name,
                style = NiaTypography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            NiaToggleButton(
                checked = isSelected,
                onCheckedChange = { checked -> onClick(topicId, checked) },
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add, contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check, contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

@Composable
fun TopicIcon(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    AsyncImage(
        // TODO b/228077205, show loading image visual instead of static placeholder
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        model = imageUrl,
        contentDescription = null, // decorative
        modifier = modifier
            .padding(10.dp)
            .size(32.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun LazyListScope.Feed(
    feedState: ForYouFeedUiState,
    showLoadingUIIfLoading: Boolean,
    @IntRange(from = 1) numberOfColumns: Int,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    when (feedState) {
        ForYouFeedUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    LoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        is ForYouFeedUiState.Success -> {
            items(
                feedState.feed.chunked(numberOfColumns)
            ) { saveableNewsResources ->
                Row(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // The last row may not be complete, but for a consistent grid
                    // structure we still want an element taking up the empty space.
                    // Therefore, the last row may have empty boxes.
                    repeat(numberOfColumns) { index ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            val saveableNewsResource =
                                saveableNewsResources.getOrNull(index)

                            if (saveableNewsResource != null) {
                                val launchResourceIntent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(saveableNewsResource.newsResource.url)
                                    )
                                val context = LocalContext.current

                                NewsResourceCardExpanded(
                                    newsResource = saveableNewsResource.newsResource,
                                    isBookmarked = saveableNewsResource.isSaved,
                                    onClick = {
                                        ContextCompat.startActivity(
                                            context,
                                            launchResourceIntent,
                                            null
                                        )
                                    },
                                    onToggleBookmark = {
                                        onNewsResourcesCheckedChanged(
                                            saveableNewsResource.newsResource.id,
                                            !saveableNewsResource.isSaved
                                        )
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