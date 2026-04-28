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
import kotlinx.coroutines.delay
import com.example.szlakigrskieam.R

@Composable
fun AnimationScreen(onAnimationEnd: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }

    // Animator 1 – skala (powiększanie)
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Animator 2 – przezroczystość (fade in)
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200),
        label = "alpha"
    )

    // Animator 3 – lekki „bounce” (zgodny z Material)
    val bounceAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bounce"
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2000)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mountain), // dodaj ikonę gór
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .scale(scaleAnim * bounceAnim)
                .alpha(alphaAnim)
        )
    }
}