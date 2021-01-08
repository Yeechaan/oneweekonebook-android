package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DoneBookViewModel : ViewModel() {

}

class DoneBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoneBookViewModel::class.java)) {
            return DoneBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}