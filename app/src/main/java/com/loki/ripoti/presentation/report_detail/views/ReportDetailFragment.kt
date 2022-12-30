package com.loki.ripoti.presentation.report_detail.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.launch


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
        subscribeState()
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
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

            addCommentBtn.isEnabled = false

            commentEt.doOnTextChanged { text, _, _, _ ->
                addCommentBtn.isEnabled = true

                addCommentBtn.setOnClickListener {
                    val comment = Comment(
                        commentEt.text.toString(),
                        args.report.id
                    )

                    if(text?.isNotEmpty()!!) {
                        commentViewModel.addComment(id.toInt(), comment)
                        root.hideKeyboard()
                    }
                }
            }


            lifecycleScope.launchWhenStarted {

                commentViewModel.addCommentEvent.collect { event ->
                    when(event) {

                        is CommentViewModel.AddCommentEvent.IsLoading -> {
                            binding.addCommentBtn.isEnabled = false
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

    private fun subscribeState() {

        lifecycleScope.launch {

            binding.apply {

                commentViewModel.state.collect { state ->
                    progressBar.isVisible = state.isLoading
                    retryBtn.isVisible = false


                    if (state.comment.isNotEmpty()) {
                        noCommentTxt.isVisible = false
                        commentsAdapter.setComment(state.comment)
                        commentsNumberTxt.text = if (state.comment.size == 1) {
                            state.comment.size.toString() + " comment"
                        } else if(state.comment.size > 1) {
                            state.comment.size.toString() + " comments"
                        } else {
                            "No comments"
                        }
                    }
                    else{
                        if (state.isLoading){
                            noCommentTxt.text = "Loading Comments"
                        }
                        else {
                            showNoComments()
                        }
                    }
                    if (state.error.isNotBlank()) {
                        showToast(state.error)

                        if (state.error == "check your internet connection") {
                            retryBtn.isVisible = true
                            retryBtn.setOnClickListener {
                                commentViewModel.getComments(args.report.id)
                            }
                        }
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