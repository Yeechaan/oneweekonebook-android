package com.lee.oneweekonebook.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentWishBookBinding
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModel
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModelFactory

class WishBookFragment : Fragment() {

//    private val viewModel: WishBookViewModel by WishBookViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = WishBookViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory).get(WishBookViewModel::class.java)

        val binding = FragmentWishBookBinding.inflate(inflater, container, false).apply {

            buttonAddBook.setOnClickListener {
                findNavController().navigate(WishBookFragmentDirections.actionWishBookFragmentToWishBookAddFragment())
            }

            val adapter = WishBookAdapter(WishBookListener { book ->
                Toast.makeText(requireContext(), book.id.toString(), Toast.LENGTH_SHORT).show()
            })
            recyclerViewWishBook.adapter = adapter

            viewModel.books.observe(viewLifecycleOwner, {
                (recyclerViewWishBook.adapter as WishBookAdapter).submitList(it)
            })

            viewModel.setBooks(listOf(Book(id = 1, title = "코딩은 즐거워", publisher = "찬찬")))
        }

        return binding.root
    }
}