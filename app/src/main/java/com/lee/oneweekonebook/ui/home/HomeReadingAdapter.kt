package com.lee.oneweekonebook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemHomeReadingBinding

class HomeReadingAdapter(private val homeReadingListener: HomeReadingListener) : RecyclerView.Adapter<HomeReadingAdapter.ViewHolder>() {

    var data = listOf<Book>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = run {
        ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = data[position]
        holder.bind(item, homeReadingListener)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(val binding: ItemHomeReadingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, homeReadingListener: HomeReadingListener) {
            binding.apply {
                book = item
                clickListener = homeReadingListener

                if (item.coverImage.isNotEmpty()) {
                    Glide.with(root.context).load(item.coverImage).into(imageViewBook)
                } else {
                    Glide.with(root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                }

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHomeReadingBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HomeReadingListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}