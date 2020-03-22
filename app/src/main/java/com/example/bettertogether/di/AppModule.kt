package com.example.bettertogether.di

import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.auth.sign_in.SignInViewModel
import com.example.bettertogether.ui.auth.sign_up.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val appModule = module {
    single{ FirebaseAuth.getInstance()}
    single{ FirebaseRepository(get()) }
    single { SignInViewModel(get()) }
    single { SignUpViewModel(get()) }
}