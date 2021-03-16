package com.lee.oneweekonebook.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ItemBookBinding
import com.lee.oneweekonebook.databinding.ItemSearchBinding
import com.lee.oneweekonebook.ui.search.model.SearchBook
import com.lee.oneweekonebook.ui.search.model.asBook
import com.orhanobut.logger.Logger

class SearchBookAdapter : RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {
    var data = listOf<SearchBook>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchBook) {
            binding.apply {

                if (item.coverImage.isNotEmpty()) {
                    Glide.with(binding.root.context).load(item.coverImage).into(imageViewBook)
                } else {
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                }

                book = item.asBook()

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)
                val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
