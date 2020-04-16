package com.example.bettertogether.ui.my_account

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import kotlinx.coroutines.launch

class MyAccountViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<MyAccountNavigator>() {

    fun signOut(){
        viewModelScope.launch {
            val isDone=authRepository.logOut()
            info("done: $isDone")
            navigator()?.navigateToAuth()
        }
    }
}