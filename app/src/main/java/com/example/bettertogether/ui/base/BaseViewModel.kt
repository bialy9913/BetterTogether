package com.example.bettertogether.ui.base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>: ViewModel() {
    private lateinit var mNavigator: WeakReference<N>

    fun navigator(): N? = mNavigator.get()

    fun setNavigator(n: N) {
        this.mNavigator = WeakReference(n)
    }
}