package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentDoneBookDetailBinding
import com.lee.oneweekonebook.ui.add.AddBookFragmentArgs
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModelFactory
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModelFactory
import com.orhanobut.logger.Logger

class DoneBookDetailFragment : Fragment() {

    private val args by navArgs<DoneBookDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = DoneBookDetailViewModelFactory(bookDao, args.bookId)
        val doneBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(DoneBookDetailViewModel::class.java)

        val binding = FragmentDoneBookDetailBinding.inflate(inflater, container, false).apply {
            viewModel = doneBookDetailViewModel
            lifecycleOwner = this@DoneBookDetailFragment
        }

        return binding.root
    }
}