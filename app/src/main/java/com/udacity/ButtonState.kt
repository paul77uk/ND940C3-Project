package com.udacity


sealed class ButtonState(var text: String) {
    object Clicked : ButtonState("We are loading")
    object Loading : ButtonState("We are loading")
    object Completed : ButtonState("Download")
}