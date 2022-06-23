package com.vision.composetutorial.foryou

import com.vision.composetutorial.model.FollowableAuthor
import com.vision.composetutorial.model.FollowableTopic

sealed interface ForYouInterestsSelectionUiState {

    object Loading: ForYouInterestsSelectionUiState
    object NoInterestsSelection: ForYouInterestsSelectionUiState
    data class WithInterestsSelection(
        val topics: List<FollowableTopic>,
        val authors: List<FollowableAuthor>,
    ): ForYouInterestsSelectionUiState {

        val canSaveInterests: Boolean get() =
            topics.any { it.isFollowed } || authors.any { it.isFollowed }
    }

}