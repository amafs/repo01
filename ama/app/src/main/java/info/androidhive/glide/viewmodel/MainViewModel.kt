package info.androidhive.glide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val position = MutableLiveData<Int>()
    fun changePage(positionIndex: Int) {
        position.postValue(positionIndex)
    }
}