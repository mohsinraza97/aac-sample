package com.mohsinsyed.aac_sample.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.databinding.ItemPostBinding

class PostAdapter(
    private val context: Context,
    private val onPostClicked: ((Post?) -> Unit)?,
    private val onEditClicked: ((Post?) -> Unit)?,
    private val onDeleteClicked: ((Post?) -> Unit)?
) : ListAdapter<Post, PostAdapter.PostVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        return PostVH(ItemPostBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostVH(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post?) {
            binding.root.setOnClickListener {
                onPostClicked?.invoke(post)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClicked?.invoke(post)
            }
            binding.btnEdit.setOnClickListener {
                onEditClicked?.invoke(post)
            }

            binding.tvTitle.text = post?.title
            binding.tvBody.text = post?.body
        }
    }
}

private object DIFF : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(old: Post, new: Post) = old.id == new.id
    override fun areContentsTheSame(old: Post, new: Post) = old == new
}