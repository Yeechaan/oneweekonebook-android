package com.lee.oneweekonebook.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.oneweekonebook.databinding.ItemSearchBinding
import com.lee.oneweekonebook.ui.search.model.SearchBook

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

    class ViewHolder private constructor(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchBook) {
            binding.searchBook = item
            binding.executePendingBindings()
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
