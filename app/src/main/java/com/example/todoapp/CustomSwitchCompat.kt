package com.example.todoapp

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat

class CustomSwitchCompat(context: Context, attrs: AttributeSet) : SwitchCompat(context, attrs) {
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}