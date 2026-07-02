package nz.eloque.compose_kit.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset

/**
 * Horizontal slide screen-change transitions.
 */
fun <S> AnimatedContentTransitionScope<S>.slideForward(animationSpec: FiniteAnimationSpec<IntOffset> = tween()): ContentTransform =
    slideIntoContainer(SlideDirection.Start, animationSpec) togetherWith
        slideOutOfContainer(SlideDirection.Start, animationSpec)

fun <S> AnimatedContentTransitionScope<S>.slideBackward(animationSpec: FiniteAnimationSpec<IntOffset> = tween()): ContentTransform =
    slideIntoContainer(SlideDirection.End, animationSpec) togetherWith
        slideOutOfContainer(SlideDirection.End, animationSpec)
