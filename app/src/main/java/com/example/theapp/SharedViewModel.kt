package com.example.theapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val iconState = MutableLiveData<Int>()
    var historyID = 0
}