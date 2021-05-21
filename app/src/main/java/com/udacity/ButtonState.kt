package com.udacity


sealed class ButtonState(var text: String, var fieName: String, var status: String) {
    object Clicked : ButtonState("We are loading", "", "")
    object Loading : ButtonState("We are loading", "", "")
    object Completed : ButtonState("Download", "", "")
}