package com.example.szlakigrskieam.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.szlakigrskieam.R
import kotlinx.coroutines.delay

@Composable
fun AnimationScreen(onAnimationEnd: () -> Unit) {

    var start by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (start) 1f else 0.5f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (start) 1f else 0f,
        animationSpec = tween(1200),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        start = true
        delay(2000)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .scale(scale)
                .alpha(alpha)
        )
    }
}