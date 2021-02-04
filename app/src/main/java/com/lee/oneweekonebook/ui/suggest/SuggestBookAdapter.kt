package com.lee.oneweekonebook.ui.suggest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ItemSuggestBinding
import com.lee.oneweekonebook.ui.suggest.model.SuggestBook

class SuggestBookAdapter : RecyclerView.Adapter<SuggestBookAdapter.ViewHolder>() {
    var data = listOf<SuggestBook>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(val binding: ItemSuggestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SuggestBook) {
            binding.book = item

            if (item.link.isNotEmpty()) {
                Glide.with(binding.root.context).load(item.link).into(binding.imageViewBook)
            } else {
                Glide.with(binding.root.context).load(R.drawable.ic_baseline_menu_book).into(binding.imageViewBook)
            }

            binding.executePendingBindings()
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