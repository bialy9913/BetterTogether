package com.example.bettertogether.di

import com.example.bettertogether.Constants
import com.example.bettertogether.MainViewModel
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.repositories.auth.user.UserRepository
import com.example.bettertogether.ui.auth.sign_in.SignInViewModel
import com.example.bettertogether.ui.auth.sign_up.SignUpViewModel
import com.example.bettertogether.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val appModule = module {
    single{ FirebaseAuth.getInstance()}
    single{ Firebase.firestore}
    single{ FirebaseRepository(get(),get()) }
    single{ UserRepository(get())}

    //View Models
    single { MainViewModel(get()) }
    single { SignInViewModel(get()) }
    single { SignUpViewModel(get()) }
    single { HomeViewModel()}
}