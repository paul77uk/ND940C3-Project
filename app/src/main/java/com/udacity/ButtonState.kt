package com.udacity


sealed class ButtonState(var text: String) {
    object Clicked : ButtonState("we are loading")
    object Loading : ButtonState("Loading")
    object Completed : ButtonState("Download")
}