package com.mohsinsyed.aac_sample.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.databinding.ItemPostBinding

class PostAdapter(
    private val context: Context,
    private var posts: ArrayList<Post>?,
    private val onPostClicked: ((Post?) -> Unit)?,
    private val onEditClicked: ((Post?) -> Unit)?,
    private val onDeleteClicked: ((Int) -> Unit)?,
) : RecyclerView.Adapter<PostAdapter.PostVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        return PostVH(ItemPostBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        val pos = holder.absoluteAdapterPosition
        holder.bind(pos)
    }

    override fun getItemCount() = posts?.size ?: 0

    fun getItem(position: Int) = posts?.get(position)

    fun updateList(list: ArrayList<Post>?) {
        posts = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int, callback: ((Boolean) -> Unit)?) {
        if (position > RecyclerView.NO_POSITION) {
            posts?.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            callback?.invoke(true)
        } else {
            callback?.invoke(false)
        }
    }

    inner class PostVH(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val post = getItem(position)
            binding.root.setOnClickListener {
                onPostClicked?.invoke(post)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClicked?.invoke(position)
            }
            binding.btnEdit.setOnClickListener {
                onEditClicked?.invoke(post)
            }

            binding.tvTitle.text = post?.title
            binding.tvBody.text = post?.body
        }
    }
}