package com.lee.oneweekonebook.ui.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WishBookViewModel : ViewModel() {


}

class WishBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishBookViewModel::class.java)) {
            return WishBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}