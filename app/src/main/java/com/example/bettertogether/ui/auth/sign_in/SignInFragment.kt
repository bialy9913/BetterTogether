package com.example.bettertogether.ui.auth.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bettertogether.R
import com.example.bettertogether.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment(), SignInNavigator {

    private val signInViewModel: SignInViewModel by viewModel()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun navigateToHome() {
        println("costam")
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility=View.GONE
        navController.navigateWithoutComingBack(R.id.auth_nav_graph,R.id.main_nav_graph)
    }

    override fun onFailure(message: String) {
        clearFields()
        progressBar.visibility=View.GONE
        showToast(message)
    }

    private fun setOnClickListeners(){ // onClick -> navigator, AndroidDataBinding
        signInBtn.setOnClickListener {
            viewModel.logIn(emailText.text.toString(),passwordText.text.toString())
        }
        signUpText.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun clearFields(){
        emailText.text.clear()
        passwordText.text.clear()
        emailText.error=null
        passwordText.error=null
    }
}
