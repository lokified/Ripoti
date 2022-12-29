package com.loki.ripoti.presentation.auth.registration.views

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentRegistrationBinding
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.presentation.auth.registration.RegistrationViewModel
import com.loki.ripoti.util.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel : RegistrationViewModel by viewModels()
    private val firstname = MutableStateFlow("")
    private val lastname = MutableStateFlow("")
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val conPassword = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()
        navigateBack()
        binding.apply {

            progressBar.isVisible = false

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            etFirstName.doOnTextChanged { text, _, _, _ ->
                firstname.value = text.toString()
            }
            etLastName.doOnTextChanged { text, _, _, _ ->
                lastname.value = text.toString()
            }
            etEmail.doOnTextChanged { text, _, _, _ ->
                email.value = text.toString()
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                password.value = text.toString()
            }
            etConPassword.doOnTextChanged { text, _, _, _ ->
                conPassword.value = text.toString()
            }

        }
        subscribeEvents()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.registerState.collect { event ->

                when(event) {

                    is RegistrationViewModel.RegisterEvent.RegisterLoading-> {
                        binding.progressBar.isVisible = true
                    }

                    is RegistrationViewModel.RegisterEvent.RegisterError -> {
                        binding.progressBar.isVisible = false

                        if(event.error.contains("HTTP 400")){
                            showToast("you are already registered")
                        }
                    }

                    is RegistrationViewModel.RegisterEvent.Success -> {
                        binding.progressBar.isVisible = false
                        if (event.message == "registration successful") {
                            showToast(event.message)
                            findNavController().navigateSafely(R.id.action_registrationFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }

    private fun subscribeEvents() {

        lifecycleScope.launch {

            formIsValid.collect { event ->
                binding.registerBtn.setOnClickListener {

                    val user = User(
                        id = 0,
                        name = "${firstname.value} ${lastname.value}",
                        email = email.value,
                        password = password.value
                    )

                    if (event) {
                        viewModel.registerUser(user)
                    }
                }
            }

            delay(3000L)
        }
    }


    private val formIsValid = combine(
        firstname, lastname, email, password, conPassword
    ) {
        firstname, lastname, email, password, conPassword ->

        val firstnameNotEmpty = firstname.isNotEmpty()
        val lastnameNotEmpty = lastname.isNotEmpty()
        val emailNotEmpty = email.isNotEmpty()
        val emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordNotEmpty = password.isNotEmpty()
        val passwordValid = password.length >= 6
        val conPasswordNotEmpty = conPassword.isNotEmpty()
        val passwordMatch = password == conPassword

        binding.apply {

            lFirstName.helperText = null
            lLastName.helperText = null
            lEmail.helperText = null
            lPassword.helperText = null
            lConPassword.helperText = null

            when {
                firstnameNotEmpty.not() -> {
                    lFirstName.helperText = "Please enter first name"
                }

                lastnameNotEmpty.not() -> {
                    lLastName.helperText = "Please enter last name"
                }

                emailNotEmpty.not() -> {
                    lEmail.helperText = "Please enter email"
                }

                emailValid.not() -> {
                    lEmail.helperText = "Please enter a valid email"
                }

                passwordNotEmpty.not() -> {
                    lPassword.helperText = "Please enter password"
                }

                passwordValid.not() -> {
                    lPassword.helperText = "Password must have more than  or 6 characters"
                }

                conPasswordNotEmpty.not() -> {
                    lConPassword.helperText = "Please confirm password"
                }

                passwordMatch.not() -> {
                    lConPassword.helperText = "Passwords do not match"
                }

                else -> {
                    lFirstName.helperText = null
                    lLastName.helperText = null
                    lEmail.helperText = null
                    lPassword.helperText = null
                    lConPassword.helperText = null
                }
            }
        }
        firstnameNotEmpty and lastnameNotEmpty and emailNotEmpty and passwordNotEmpty and conPasswordNotEmpty and passwordMatch

    }
}