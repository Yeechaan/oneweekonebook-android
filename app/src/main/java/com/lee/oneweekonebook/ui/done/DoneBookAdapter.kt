package com.lee.oneweekonebook.ui.done

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemDoneBinding

class DoneBookAdapter(val bookClickListener: DoneBookListener) : ListAdapter<Book, DoneBookAdapter.ViewHolder>(DoneBookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, bookClickListener)
    }

    class ViewHolder private constructor(val binding: ItemDoneBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, bookClickListener: DoneBookListener) {
            binding.apply {
                book = item
                clickListener = bookClickListener
                imgPicture.setImageURI(Uri.parse(item.coverImage))
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDoneBinding.inflate(layoutInflater, parent, false)
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