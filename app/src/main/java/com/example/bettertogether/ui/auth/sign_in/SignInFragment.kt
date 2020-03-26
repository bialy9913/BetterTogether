package com.example.bettertogether.ui.auth.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentSignInBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.show
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment(), SignInNavigator {

    private val signInViewModel: SignInViewModel by viewModel()

    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSignInBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_in, container, false
            )
        binding.signInViewModel = signInViewModel
        return binding.root
    }
    override fun signingInStarted() {
        clearFields()
        progressBar.show()
    }
    override fun signingInSuccess() {
        progressBar.hide()
        navController.navigateWithoutComingBack(R.id.auth_nav_graph,R.id.main_nav_graph)
    }

    override fun signingInFailure(message:String) {
        progressBar.hide()
        showToast(message)
    }

    override fun navigateToSignUp() {
        navController.navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun clearFields(){
        email.text.clear()
        password.text.clear()
        email.error=null
        password.error=null
        email.requestFocus()
    }
}
