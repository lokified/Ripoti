package com.loki.ripoti.presentation.report_detail.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.auth0.android.jwt.JWT
import com.loki.ripoti.databinding.FragmentReportDetailBinding
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.presentation.report_detail.CommentViewModel
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.hideKeyboard
import com.loki.ripoti.util.extensions.navigateBack
import com.loki.ripoti.util.extensions.showSnackBar
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReportDetailFragment : Fragment() {

    private lateinit var binding: FragmentReportDetailBinding
    private val commentViewModel: CommentViewModel by viewModels()
    private val args: ReportDetailFragmentArgs by navArgs()
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpObservers()

        setUpReport()
        setUpComments()
        navigateBack()
    }

    private fun setUpReport() {
        binding.apply {
            progressBar.isVisible = false
            descriptionTxt.text = args.report.description
            reportTimeTxt.text = args.report.created_at + " . " + args.report.created_on
            usernameTxt.text = args.report.username

            nameTxt.text = "@${args.report.name}"
            userInitialsTxt.text = GetUserInitials.initials(username = args.report.name)
            localUserInitialsTxt.text = GetUserInitials.initials(context = requireContext())
        }
    }

    private fun setUpComments() {
        binding.apply {

            commentsAdapter = CommentsAdapter()
            commentsRecycler.adapter = commentsAdapter
            commentViewModel.getComments(args.report.id)


            val token = SharedPreferenceManager.getToken(requireContext())
            val jwt = JWT(token)

            val id = jwt.getClaim("id").asString()!!

            addCommentBtn.setOnClickListener {
                val comment = Comment(
                    commentEt.text.toString(),
                    args.report.id
                )
                if(commentEt.text.isNotEmpty()) {
                    addCommentBtn.isEnabled = false
                    commentViewModel.addComment(id.toInt(), comment)
                    root.hideKeyboard()
                }
            }

            lifecycleScope.launchWhenStarted {

                commentViewModel.addCommentEvent.collect { event ->
                    when(event) {

                        is CommentViewModel.AddCommentEvent.IsLoading -> {
                            binding.noCommentTxt.text = "Loading."
                        }

                        is CommentViewModel.AddCommentEvent.ErrorAddComment -> {
                            showNoComments()
                            showToast(event.error)
                        }

                        is CommentViewModel.AddCommentEvent.Success -> {
                            showNoComments()
                            binding.commentEt.text = null
                            commentViewModel.getComments(args.report.id)
                            binding.root.showSnackBar(
                                "Comment posted"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setUpObservers() {

        commentViewModel.state.observe(viewLifecycleOwner) { result ->

            binding.progressBar.isVisible = result.isLoading
            binding.retryBtn.isVisible = false


            if (result.comment.isNotEmpty()) {
                binding.addCommentBtn.isEnabled = true
                binding.noCommentTxt.isVisible = false
                commentsAdapter.setComment(result.comment)
                binding.commentsNumberTxt.text = if (result.comment.size == 1) {
                    result.comment.size.toString() + " comment"
                } else if(result.comment.size > 1) {
                    result.comment.size.toString() + " comments"
                } else {
                    "No comments"
                }
            }
            else{
                if (result.isLoading){
                    binding.noCommentTxt.text = "Loading."
                }
                else {
                    binding.addCommentBtn.isEnabled = true
                    showNoComments()
                }
            }
            if (result.error.isNotBlank()) {
                showToast(result.error)

                if (result.error == "check your internet connection") {
                    binding.retryBtn.isVisible = true
                    binding.addCommentBtn.isEnabled = true
                    binding.retryBtn.setOnClickListener {
                        commentViewModel.getComments(args.report.id)
                    }
                }
            }
        }
    }

    private fun showNoComments() {
        binding.noCommentTxt.isVisible = true
        binding.noCommentTxt.text = "No Comments."
    }
}