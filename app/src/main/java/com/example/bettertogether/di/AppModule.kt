package com.example.bettertogether.di

import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.repositories.OfferRepository
import com.example.bettertogether.repositories.UserRepository
import com.example.bettertogether.ui.auth.sign_in.SignInViewModel
import com.example.bettertogether.ui.auth.sign_up.SignUpViewModel
import com.example.bettertogether.ui.history.HistoryViewModel
import com.example.bettertogether.ui.home.HomeViewModel
import com.example.bettertogether.ui.my_account.MyAccountViewModel
import com.example.bettertogether.ui.new_offer.NewOfferViewModel
import com.example.bettertogether.ui.offers.OffersViewModel
import com.example.bettertogether.ui.settings.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single{ FirebaseAuth.getInstance()}
    single{ Firebase.firestore}
    single{ AuthRepository(get(), get()) }
    single{ UserRepository(get(),get()) }
    single{ OfferRepository(get(),get(),get())}
    single{ CurrentOffersRepository(get(),get(),get())}

    //View Models
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { HomeViewModel(get(),get())}
    viewModel { MyAccountViewModel(get(),get())}
    viewModel { SettingsViewModel(get(),get())}
    viewModel { OffersViewModel(get(),get(),get(),get())}
    viewModel { NewOfferViewModel(get(),get(),get())}
    viewModel { HistoryViewModel(get())}
}