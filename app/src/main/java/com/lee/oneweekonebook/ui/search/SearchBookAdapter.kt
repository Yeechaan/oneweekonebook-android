package com.lee.oneweekonebook.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ItemSearchBinding
import com.lee.oneweekonebook.ui.search.model.BookInfo

class SearchBookAdapter(private val searchBookListener: SearchBookListener) :
    RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {
    var data = listOf<BookInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, searchBookListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookInfo, searchBookListener: SearchBookListener) {
            binding.apply {
                searchBook = item
                clickListener = searchBookListener

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
                val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SearchBookListener(val clickListener: (book: BookInfo) -> Unit) {
    fun onClick(book: BookInfo) = clickListener(book)
}
