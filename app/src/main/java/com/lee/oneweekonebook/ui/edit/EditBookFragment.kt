package com.lee.oneweekonebook.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentEditBookBinding
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.edit.viewmodel.EditBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditBookFragment : NoBottomNavigationToolbarIconFragment() {

    private val editBookViewModel by viewModels<EditBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentEditBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@EditBookFragment
                viewModel = editBookViewModel

                lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        editBookViewModel.uiState.collectLatest { uiState ->
                            val imageUrl = uiState.book?.coverImage
                            if (imageUrl?.isNotEmpty() == true) {
                                Glide.with(root.context).load(imageUrl).into(imageViewBook)
                            } else {
                                Glide.with(root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                            }

                            if (uiState.editSuccess) {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }

                buttonDone.setOnClickListener {
                    editBookViewModel.editBook()
                }
            }

        return binding.root
    }
}