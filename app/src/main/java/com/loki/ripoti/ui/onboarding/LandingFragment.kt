package com.loki.ripoti.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentLandingBinding
import com.loki.ripoti.util.extensions.darkStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor

class LandingFragment : Fragment() {

    private lateinit var binding: FragmentLandingBinding

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
            val action = LandingFragmentDirections.actionLandingFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}