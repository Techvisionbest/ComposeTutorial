package com.vision.composetutorial.foryou

import com.vision.composetutorial.model.SaveableNewResource

sealed interface ForYouFeedUiState {

    object Loading: ForYouFeedUiState

    data class Success(
        val feed: List<SaveableNewResource>
    ): ForYouFeedUiState
}