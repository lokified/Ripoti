package com.loki.ripoti.presentation.home.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentLogoutDialogBinding
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.navigateSafely

class LogoutDialog: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLogoutDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        const val TAG = "Logout Dialog"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.logoutLayout.setOnClickListener {
            SharedPreferenceManager.saveAccessToken(context, "")
            findNavController().navigateSafely(R.id.action_homeFragment_to_loginFragment)
            dismiss()
        }
    }
}