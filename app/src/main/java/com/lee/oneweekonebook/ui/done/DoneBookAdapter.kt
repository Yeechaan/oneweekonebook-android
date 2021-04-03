package com.lee.oneweekonebook.ui.done

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemBookBinding

class DoneBookAdapter(val bookClickListener: DoneBookListener) : ListAdapter<Book, DoneBookAdapter.ViewHolder>(DoneBookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, bookClickListener)
    }

    class ViewHolder private constructor(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, bookClickListener: DoneBookListener) {
            binding.apply {
                book = item
                clickListener = bookClickListener

                if (item.coverImage.isNotEmpty()) {
                    Glide.with(binding.root.context).load(item.coverImage).into(imageViewBook)
                } else {
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                }

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemDoneBinding.inflate(layoutInflater, parent, false)
                val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class DoneBookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}

class DoneBookListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}