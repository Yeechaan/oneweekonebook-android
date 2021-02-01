package com.lee.oneweekonebook.ui.wish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemWantBinding

class WishBookAdapter(val bookClickListener: WishBookListener) : ListAdapter<Book, WishBookAdapter.ViewHolder>(WishBookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, bookClickListener)
    }

    class ViewHolder private constructor(val binding: ItemWantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, bookClickListener: WishBookListener) {
            binding.apply {
                book = item
                clickListener = bookClickListener
//                imgPicture.setImageURI(Uri.parse(item.coverImage))

                if (item.coverImage.isNotEmpty()) {
                    Glide.with(binding.root.context).load(item.coverImage).into(binding.imgPicture)
                } else {
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_menu_book).into(binding.imgPicture)
                }

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemWantBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class WishBookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}

class WishBookListener(val clickListener: (book: Book, view: View) -> Unit) {
    fun onClick(book: Book, view: View) = clickListener(book, view)
}