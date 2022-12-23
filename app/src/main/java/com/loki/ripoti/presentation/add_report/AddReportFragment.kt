package com.loki.ripoti.presentation.add_report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.loki.ripoti.R
import com.loki.ripoti.databinding.FragmentAddReportBinding
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.hideKeyboard
import com.loki.ripoti.util.extensions.navigateBack
import com.loki.ripoti.util.extensions.showSnackBar
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReportFragment : Fragment() {

    private lateinit var binding: FragmentAddReportBinding
    private val addReportViewModel: AddReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReportBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val token = SharedPreferenceManager.getToken(requireContext())
        val jwt = JWT(token)

        val id = jwt.getClaim("id").asString()!!
        val username = jwt.getClaim("userName").asString()!!

        binding.apply {

            localUserInitialsTxt.text = GetUserInitials.initials(context = requireContext())


            addReportBtn.setOnClickListener {

                if (etAddReport.text.isNotEmpty()) {
                    val description = etAddReport.text.toString()
                    val report = Report(
                        username, description
                    )
                    addReportViewModel.addReport(id.toInt(), report)
                }
                root.hideKeyboard()
            }
        }

        navigateBack()

        setUpState()
    }

    private fun setUpState() {

        addReportViewModel.state.observe(viewLifecycleOwner) { result ->

            binding.progressBar.isVisible = result.isLoading

            if (result.message.isNotEmpty()) {

                if (result.message == "report added") {
                    binding.root.showSnackBar(result.message)
                    findNavController().popBackStack()
                }
            }

            if (result.error.isNotEmpty()) {
                showToast(result.error)
            }
        }
    }
}