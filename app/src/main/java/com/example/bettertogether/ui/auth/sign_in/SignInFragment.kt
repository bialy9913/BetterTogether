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
import com.example.bettertogether.utils.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment(), SignInNavigator {
    private val signInViewModel: SignInViewModel by viewModel()
    private lateinit var binding : FragmentSignInBinding
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
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_in, container, false
            )
        binding.signInViewModel=signInViewModel
        return binding.root
    }

    override fun signingInStarted() {
        clearFields()
        binding.progressBar.show()
    }
    override fun signingInSuccess() {
        binding.progressBar.hide()
        navController.navigateWithoutComingBack(R.id.main_nav_graph)
    }

    override fun signingInFailure(message:String) {
        binding.progressBar.hide()
        showToast(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun navigateToSignUp() {
        navController.navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun clearFields(){
        email.text?.clear()
        password.text?.clear()
        email.error=null
        password.error=null
        email.requestFocus()
    }
}
