package com.lee.oneweekonebook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemHomeReadingBinding


class HomeReadingBookAdapter(
    private val bookClickListener: HomeReadingListener
) : ListAdapter<Book, RecyclerView.ViewHolder>(DiffUtiImpl()) {

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

        fun bind(item: Book, bookClickListener: HomeReadingListener) = binding.run {
            book = item
            clickListener = bookClickListener

            if (item.coverImage.isNotEmpty()) {
                Glide.with(root.context).load(item.coverImage).into(imageViewBook)
            }
        }
    }

    class DiffUtiImpl : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Book, newItem: Book) =
            oldItem == newItem
    }
}

class HomeReadingListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}