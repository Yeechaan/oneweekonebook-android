package com.lee.oneweekonebook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.oneweekonebook.databinding.ItemCategoryBinding
import com.lee.oneweekonebook.ui.home.model.CategoryBook

class CategoryBookAdapter(private val categoryBookListener: CategoryBookListener) : RecyclerView.Adapter<CategoryBookAdapter.ViewHolder>() {

    var data = listOf<CategoryBook>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, categoryBookListener)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryBook, categoryBookListener: CategoryBookListener) {
            binding.apply {
                category = item
                clickListener = categoryBookListener

                imageViewIcon.setImageResource(item.icon)

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CategoryBookListener(val clickListener: (categoryBook: CategoryBook) -> Unit) {
    fun onClick(categoryBook: CategoryBook) = clickListener(categoryBook)
}