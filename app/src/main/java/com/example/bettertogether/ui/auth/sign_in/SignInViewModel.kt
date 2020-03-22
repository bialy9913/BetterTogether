package com.example.bettertogether.ui.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel<SignInNavigator>() {

    fun fun1() {
        navigator()?.navigateToHome()

        viewModelScope.launch {
            navigator()?.hideProgressBar()
            navigator()?.navigateToHome()
            navigator()?.showToast("asfasf")
        }
    }

}