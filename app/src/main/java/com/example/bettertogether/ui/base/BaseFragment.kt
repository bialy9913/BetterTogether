package com.example.bettertogether.ui.base

import androidx.fragment.app.Fragment
import com.example.bettertogether.utils.showToast

abstract class BaseFragment : Fragment() {

    fun showToast(message: String) {
        context?.showToast(message)
    }

}