package com.loki.ripoti.ui.onboarding.registration.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentRegistrationBinding
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.ui.onboarding.registration.RegistrationViewModel
import com.loki.ripoti.util.extensions.lightStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel : RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
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

            registerBtn.setOnClickListener {
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val user = User(
                    name = "$firstName $lastName",
                    email,
                    password
                )
                viewModel.registerUser(user)
            }
        }
    }

    private fun setUpObservers() {

        viewModel.state.observe(viewLifecycleOwner) { state ->

            binding.progressBar.isVisible = state.isLoading

            if (state.message.isNotBlank()) {
                showToast(state.message)

                val action = RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
                findNavController().navigate(action)
            }
            if (state.error.isNotBlank()) {
                showToast(state.error)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.state.value?.message = ""
    }
}