package com.loki.ripoti.presentation.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentLandingBinding
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.darkStatusBar
import com.loki.ripoti.util.extensions.navigateSafely
import com.loki.ripoti.util.extensions.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private lateinit var binding: FragmentLandingBinding
    private val landingViewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.colorPrimary))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        darkStatusBar()

        binding.continueBtn.setOnClickListener {
            navigateToLogin()
        }

        lifecycleScope.launchWhenStarted {

            landingViewModel.localUsers.collect { users ->

                navigateToLogin()

                if (users.isNotEmpty()) {
                    if (SharedPreferenceManager.getToken(context) != "") {
                        navigateToHome()
                    }
                }
            }
        }

        if (SharedPreferenceManager.getToken(context) != "") {
            navigateToHome()
        }

    }

    private fun navigateToHome() {
        findNavController().navigateSafely(R.id.action_landingFragment_to_homeFragment)
    }

    private fun navigateToLogin() {
        findNavController().navigateSafely(R.id.action_landingFragment_to_loginFragment)
    }
}