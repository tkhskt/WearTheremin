package com.tkhskt.theremin.feature.tutorial.ui.model

import com.tkhskt.theremin.redux.SideEffect

sealed interface TutorialEffect : SideEffect {
    object TransitToTheremin : TutorialEffect
}
