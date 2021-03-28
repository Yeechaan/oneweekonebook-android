package com.lee.oneweekonebook.ui.suggest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ItemSuggestBinding
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.suggest.model.SuggestBook

class SuggestBookAdapter(private val suggestBookListener: SuggestBookListener) : RecyclerView.Adapter<SuggestBookAdapter.ViewHolder>() {
    var data = listOf<BookInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, suggestBookListener)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(val binding: ItemSuggestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookInfo, suggestBookListener: SuggestBookListener) {
            binding.apply {
                book = item
                clickListener = suggestBookListener

                if (item.link.isNotEmpty()) {
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
                val binding = ItemSuggestBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SuggestBookListener(val clickListener: (book: BookInfo) -> Unit) {
    fun onClick(book: BookInfo) = clickListener(book)
}