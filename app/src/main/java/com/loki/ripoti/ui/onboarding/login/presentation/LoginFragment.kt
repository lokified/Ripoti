package com.loki.ripoti.ui.onboarding.login.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentLoginBinding
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.ui.onboarding.login.LoginViewModel
import com.loki.ripoti.ui.onboarding.registration.presentation.RegistrationFragmentDirections
import com.loki.ripoti.util.extensions.lightStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        setUpObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()
        binding.apply {

            progressBar.isVisible = false

            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            signUpBtn.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                findNavController().navigate(action)
            }

            loginBtn.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                val login = Login(email, password)
                viewModel.loginUser(login)

                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpObservers() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            binding.progressBar.isVisible = state.isLoading

            if (state.message.isNotBlank()) {
                showToast(state.message)

            }
            if (state.error.isNotBlank()) {
                showToast(state.error)
            }
        }
    }
}