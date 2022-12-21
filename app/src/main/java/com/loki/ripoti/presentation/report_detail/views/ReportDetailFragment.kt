package com.loki.ripoti.presentation.report_detail.views

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
import androidx.navigation.fragment.navArgs
import com.auth0.android.jwt.JWT
import com.loki.ripoti.databinding.FragmentReportDetailBinding
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.presentation.report_detail.CommentViewModel
import com.loki.ripoti.util.GetUserInitials
import com.loki.ripoti.util.SharedPreferenceManager
import com.loki.ripoti.util.extensions.navigateBack
import com.loki.ripoti.util.extensions.showSnackBar
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
            descriptionTxt.text = args.report.report.description
            reportTimeTxt.text = args.report.report.created_at
            usernameTxt.text = args.report.report.username

            nameTxt.text = "@${args.report.name}"
            userInitialsTxt.text = args.report.report.username[0].toString()
            localUserInitialsTxt.text = GetUserInitials.initials(requireContext())
        }
    }

    private fun setUpComments() {
        binding.apply {

            commentsAdapter = CommentsAdapter(
                args.report.report.username,
                args.report.name
            )
            commentsRecycler.adapter = commentsAdapter
            commentViewModel.getComments(args.report.report.id)



            val token = SharedPreferenceManager.getToken(requireContext())
            val jwt = JWT(token)

            val id = jwt.getClaim("id").asString()!!

            addCommentBtn.setOnClickListener {
                val comment = Comment(
                    commentEt.text.toString(),
                    args.report.report.id
                )
                commentViewModel.addComment(id.toInt(), comment)
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
                            commentViewModel.getComments(args.report.report.id)
                            showSnackBar(
                                "Comment posted",
                                binding.root
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


            if (result.comment.isNotEmpty()) {
                binding.noCommentTxt.isVisible = false
                commentsAdapter.setComment(result.comment)
            }
            else{
                if (result.isLoading){
                    binding.noCommentTxt.text = "Loading."
                }
                else {
                    showNoComments()
                }
            }
            if (result.error.isNotBlank()) {
                showToast(result.error)
            }
        }
    }

    private fun showNoComments() {
        binding.noCommentTxt.isVisible = true
        binding.noCommentTxt.text = "No Comments."
    }
}