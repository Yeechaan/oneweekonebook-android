package com.lee.oneweekonebook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.databinding.ItemHomeReadingBinding
import com.lee.oneweekonebook.mapper.BookDomain


class HomeReadingBookAdapter(
    private val bookClickListener: HomeReadingListener,
) : ListAdapter<BookDomain, RecyclerView.ViewHolder>(DiffUtiImpl()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeReadingBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position), bookClickListener)
    }

    class ViewHolder(
        val binding: ItemHomeReadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookDomain, bookClickListener: HomeReadingListener) = binding.run {
            book = item
            clickListener = bookClickListener

            if (item.coverImage.isNotEmpty()) {
                Glide.with(root.context).load(item.coverImage).into(imageViewBook)
            }
        }
    }

    class DiffUtiImpl : DiffUtil.ItemCallback<BookDomain>() {
        override fun areItemsTheSame(oldItem: BookDomain, newItem: BookDomain) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookDomain, newItem: BookDomain) =
            oldItem == newItem
    }
}

class HomeReadingListener(val clickListener: (book: BookDomain) -> Unit) {
    fun onClick(book: BookDomain) = clickListener(book)
}