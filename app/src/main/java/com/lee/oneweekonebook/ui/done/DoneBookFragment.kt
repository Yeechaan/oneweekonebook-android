package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentDoneBookBinding
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModelFactory
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.orhanobut.logger.Logger

class DoneBookFragment : Fragment() {

    var binding: FragmentDoneBookBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = DoneBookViewModelFactory(bookDao)
        val doneBookViewModel = ViewModelProvider(this, viewModelFactory).get(DoneBookViewModel::class.java)

        binding = FragmentDoneBookBinding.inflate(inflater, container, false)
            .apply {
                viewModel = doneBookViewModel
                lifecycleOwner = this@DoneBookFragment

                val doneBookAdapter = DoneBookAdapter(DoneBookListener { book ->
                    Toast.makeText(requireContext(), book.id.toString(), Toast.LENGTH_SHORT).show()

                    Logger.d(book)
//                findNavController().navigate(DoneBookFragmentDirections.actionDoneBookFragmentToDoneBookDetailFragment(bookId = book.id))
                    findNavController().navigate(HistoryFragmentDirections.actionHistoryDoneFragmentToDoneBookDetailFragment(bookId = book.id))
                })
                recyclerViewDoneBook.apply {
                    adapter = doneBookAdapter
                    addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                }

                doneBookViewModel.books.observe(viewLifecycleOwner, {
                    (recyclerViewDoneBook.adapter as DoneBookAdapter).submitList(it)
                })
            }

        return binding?.root
    }

}