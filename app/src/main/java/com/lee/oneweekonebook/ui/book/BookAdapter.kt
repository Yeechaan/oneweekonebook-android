package com.lee.oneweekonebook.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemBookBinding
import com.lee.oneweekonebook.utils.visible


class BookAdapter(private val bookClickListener: BookListener, private val bookMoreClickListener: BookMoreListener) : ListAdapter<Book, BookAdapter.ViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, bookClickListener, bookMoreClickListener)
    }

    class ViewHolder private constructor(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, bookClickListener: BookListener, moreClickListener: BookMoreListener) {
            binding.apply {
                book = item
                clickListener = bookClickListener

                if (item.coverImage.isNotEmpty()) {
                    Glide.with(root.context).load(item.coverImage).into(imageViewBook)
                } else {
                    Glide.with(root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                }

                imageViewMore.setOnClickListener {
                    moreClickListener.onClick(it, item.id)
                }

                when (item.type) {
                    BOOK_TYPE_READING -> {
                        imageViewFrom.visible()
                        textViewFrom.visible()
                    }
                    BOOK_TYPE_DONE -> {
                        imageViewFrom.visible()
                        textViewFrom.visible()
                        imageViewTo.visible()
                        textViewTo.visible()
                    }
                }

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}

class BookListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}

class BookMoreListener(val clickListener: (view: View, bookId: Int) -> Unit) {
    fun onClick(view: View, bookId: Int) = clickListener(view, bookId)
}