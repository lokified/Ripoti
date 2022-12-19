package com.loki.ripoti.presentation.report_detail.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.loki.ripoti.databinding.FragmentReportDetailBinding
import com.loki.ripoti.presentation.report_detail.CommentViewModel
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportDetailFragment : Fragment() {

    private lateinit var binding: FragmentReportDetailBinding
    private val commentViewModel: CommentViewModel by viewModels()
    private val args: ReportDetailFragmentArgs by navArgs()
    private lateinit var commentsAdapter: CommentsAdapter
    var commentNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentReportDetailBinding.inflate(inflater, container, false)
        setUpObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            progressBar.isVisible = false
            descriptionTxt.text = args.report.description
            reportTimeTxt.text = args.report.created_at
            usernameTxt.text = args.report.username

            commentsNumberTxt.text = "$commentNumber comments"

            commentsAdapter = CommentsAdapter(
                args.report.username,
            "@sheldon"
            )
            commentsRecycler.adapter = commentsAdapter
            commentViewModel.getComments(args.report.id)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun setUpObservers() {

        commentViewModel.state.observe(viewLifecycleOwner) { result ->

            binding.progressBar.isVisible = result.isLoading

            if (result.comment.isNotEmpty()) {
                commentNumber = result.comment.size.toString()
                commentsAdapter.setComment(result.comment)
            }
            if (result.error.isNotBlank()) {
                showToast(result.error)
            }
        }
    }

}