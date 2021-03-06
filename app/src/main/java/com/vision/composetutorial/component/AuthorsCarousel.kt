package com.vision.composetutorial.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vision.composetutorial.model.Author
import com.vision.composetutorial.model.FollowableAuthor
import com.vision.composetutorial.R

@Composable
fun AuthorsCarousel(
    authors: List<FollowableAuthor>,
    onAuthorClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(items = authors, key = { item -> item.author.id }) { followableAuthor ->
            AuthorItem(
                author = followableAuthor.author,
                following = followableAuthor.isFollowed,
                onAuthorClick = { following ->
                    onAuthorClick(followableAuthor.author.id, following)
                },
            )
        }
    }
}

@Composable
fun AuthorItem(
    author: Author,
    following: Boolean,
    onAuthorClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val followDescription = if (following) {
        stringResource(id = R.string.following)
    } else {
        stringResource(id = R.string.not_following)
    }

    Column(
        modifier = modifier
            .toggleable(
                value = following,
                enabled = true,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onValueChange = { newFollowing -> onAuthorClick(newFollowing) },
            )
            .sizeIn(maxWidth = 48.dp)
            .semantics(mergeDescendants = true) {
                stateDescription = "$followDescription ${author.name}"
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val authorImageModifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
            if (author.imageUrl.isEmpty()) {
                Icon(
                    modifier = authorImageModifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = null // decorative image
                )
            } else {
                AsyncImage(
                    modifier = authorImageModifier,
                    model = author.imageUrl,
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
            FollowButton(
                following = following,
                backgroundColor = MaterialTheme.colorScheme.surface,
                size = 20.dp,
                iconSize = 14.dp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = author.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AuthorCarouselPreview() {
    MaterialTheme {
        Surface {
            AuthorsCarousel(
                authors = listOf(
                    FollowableAuthor(
                        Author(
                            id = "1",
                            name = "Android Dev",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = true
                    ),
                    FollowableAuthor(
                        Author(
                            id = "3",
                            name = "Android Dev3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        false
                    )
                ),
                onAuthorClick = { _, _ -> },
            )
        }
    }
}

@Preview
@Composable
fun AuthorItemPreview() {
    MaterialTheme {
        Surface {
            AuthorItem(
                author = Author(
                    id = "0",
                    name = "Android Dev",
                    imageUrl = "",
                    twitter = "",
                    mediumPage = "",
                    bio = "",
                ),
                following = true,
                onAuthorClick = { }
            )
        }
    }
}