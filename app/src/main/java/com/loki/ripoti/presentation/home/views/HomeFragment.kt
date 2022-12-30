package com.loki.ripoti.presentation.home.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.databinding.FragmentHomeBinding
import com.loki.ripoti.presentation.home.ReportsViewModel
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.lightStatusBar
import com.loki.ripoti.util.extensions.setStatusBarColor
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val reportsViewModel: ReportsViewModel by viewModels()
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setStatusBarColor(resources.getColor(R.color.white))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lightStatusBar()

        subscribeState()

        binding.apply {

            reportAdapter = ReportAdapter { reports ->
                navigateToReportComments(reports)
            }
            reportRecycler.adapter = reportAdapter
            reportRecycler.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).orientation
                )
            )

            cardUserBg.setOnClickListener {
                val logoutDialog = LogoutDialog()
                logoutDialog.show(parentFragmentManager, LogoutDialog.TAG)
            }

            addReportFab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addReportFragment)
            }

            userInitialsTxt.text = GetUserInitials.initials(requireContext())
        }

    }



    private fun subscribeState() {

        lifecycleScope.launchWhenStarted {

            reportsViewModel.state.collect { state ->

                binding.apply {
                    progressBar.isVisible = state.isLoading
                    retryBtn.isVisible = false

                    if (state.reports.isNotEmpty()){
                        noReportTxt.text = ""
                        reportAdapter.setReportList(state.reports)
                    }
                    else {
                        noReportTxt.text = "No reports available"
                    }

                    if (state.error.isNotBlank()) {
                        showToast(state.error)

                        if (state.error == "check your internet connection") {
                            retryBtn.isVisible = true

                            retryBtn.setOnClickListener {
                                reportsViewModel.getReports()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToReportComments(reports: Reports) {

        val action = HomeFragmentDirections.actionHomeFragmentToReportDetailFragment(
            report = reports
        )
        findNavController().navigate(action)
    }
}