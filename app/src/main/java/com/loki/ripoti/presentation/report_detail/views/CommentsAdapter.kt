package com.loki.ripoti.presentation.report_detail.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loki.ripoti.data.remote.response.Comments
import com.loki.ripoti.databinding.CommentItemLayoutBinding
import com.loki.ripoti.util.GetUserInitials

class CommentsAdapter(
): RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    private var commentList = mutableListOf<Comments>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentItemLayoutBinding.inflate(inflater, parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        holder.bind(commentList[position])
    }

    fun setComment(comment: List<Comments>) {
        this.commentList = comment.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = commentList.size

    inner class CommentHolder(val binding: CommentItemLayoutBinding):  RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(comments: Comments) {

            binding.apply {
                usernameTxt.text = comments.username
                nameTxt.text = "@${comments.name}"
                commentTxt.text = comments.comment
                userInitialsTxt.text = GetUserInitials.initials(username = comments.name)
            }
        }
    }
}