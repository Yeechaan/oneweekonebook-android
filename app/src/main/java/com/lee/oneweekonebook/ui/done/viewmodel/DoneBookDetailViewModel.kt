package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DoneBookDetailViewModel : ViewModel() {

}

class DoneBookDetailViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoneBookDetailViewModel::class.java)) {
            return DoneBookDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}