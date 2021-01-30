package com.lee.oneweekonebook.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.ItemReadBinding
import com.lee.oneweekonebook.utils.DateUtils
import com.orhanobut.logger.Logger

class ReadingBookAdapter(val bookClickListener: ReadingBookListener) : ListAdapter<Book, ReadingBookAdapter.ViewHolder>(ReadingBookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, bookClickListener)
    }

    class ViewHolder private constructor(val binding: ItemReadBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, bookClickListener: ReadingBookListener) {
            Logger.d(item)

            binding.apply {
                book = item
                clickListener = bookClickListener
                imgPicture.setImageURI(Uri.parse(item.coverImage))

                txtStDate.text = DateUtils().convertLongToDateString(item.startDate)
                Logger.d(DateUtils().convertLongToDateString(item.startDate))

                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemReadBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ReadingBookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}

class ReadingBookListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}